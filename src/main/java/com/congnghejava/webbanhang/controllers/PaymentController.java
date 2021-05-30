package com.congnghejava.webbanhang.controllers;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.congnghejava.webbanhang.config.AppProperties;
import com.congnghejava.webbanhang.exception.BadRequestException;
import com.congnghejava.webbanhang.models.Cart;
import com.congnghejava.webbanhang.models.EPaymentMethod;
import com.congnghejava.webbanhang.models.Order;
import com.congnghejava.webbanhang.models.Product;
import com.congnghejava.webbanhang.models.User;
import com.congnghejava.webbanhang.repository.CartService;
import com.congnghejava.webbanhang.repository.UserCredentialRepository;
import com.congnghejava.webbanhang.security.CurrentUser;
import com.congnghejava.webbanhang.security.UserPrincipal;
import com.congnghejava.webbanhang.services.OrderService;
import com.congnghejava.webbanhang.services.UserService;
import com.google.gson.JsonSyntaxException;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.param.checkout.SessionCreateParams.LineItem;

@RestController
@RequestMapping("/payment")
public class PaymentController {

	@Autowired
	AppProperties appProperties;

	@Autowired
	UserService userService;

	@Autowired
	CartService cartService;

	@Autowired
	UserCredentialRepository userCredentialRepository;

	@Autowired
	OrderService orderService;

	@PostConstruct
	public void init() {
		Stripe.apiKey = appProperties.getStripe().getTokenSecret();
	}

	@PostMapping("/stripe")
	public String paymentWithCheckoutPage(@CurrentUser UserPrincipal userPrincipal) throws StripeException {

		// @formatter:off
		User user = userService.getCurrentUser(userPrincipal);
		if(!user.getUserInfo().isFullfill()) {
			throw new BadRequestException("Thông tin giao hàng còn thiếu!");
		}
		
		List<Cart> carts = cartService.findByUserAndEnable(user, true);
		
		
		SessionCreateParams params = SessionCreateParams.builder()		
				.addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
				.setCustomerEmail(user.getUserCredential().getEmail())
				.setMode(SessionCreateParams.Mode.PAYMENT)		
				.addAllLineItem(buildItem(carts))
				.setSuccessUrl("http://localhost:3000/checkout/success")
				.setCancelUrl("http://localhost:3000/checkout/cancel")
				.build();
		Session session = Session.create(params);
	
		return session.getId();
		// @formatter:on
	}

	@PostMapping("/stripe/webhooks")
	public void postStripeEventWebhook(@RequestBody String body, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String sigHeader = request.getHeader("Stripe-Signature");
		Event event = null;

		try {
			event = Webhook.constructEvent(body, sigHeader, appProperties.getStripe().getEndpointSecret());
		} catch (JsonSyntaxException e) {
			response.setStatus(400);
			return;
		} catch (SignatureVerificationException e) {
			response.setStatus(400);
			return;
		}

		if ("checkout.session.completed".equals(event.getType())) {
			Session session = (Session) event.getDataObjectDeserializer().getObject().orElseGet(null);
			User user = userCredentialRepository.findByEmail(session.getCustomerEmail()).get().getUser();
			List<Cart> carts = cartService.findByUserAndEnable(user, true);

			PaymentIntent paymentIntent = PaymentIntent.retrieve(session.getPaymentIntent());

			String chargeSuccessId = paymentIntent.getCharges().getData().stream()
					.filter(charge -> charge.getStatus().equals("succeeded")).findAny().get().getId();
			Order order = new Order(user, carts);
			order.setSettled(true);
			order.setPaymentMethod(EPaymentMethod.Stripe);
			order.setStripeChagreId(chargeSuccessId);

			orderService.save(order);

			carts.forEach(cart -> cartService.remove(cart.getId()));
		}

		response.setStatus(200);
	}

	public List<LineItem> buildItem(List<Cart> listItem) throws StripeException {
		// @formatter:off
		System.out.println("chay toi day");
				List<LineItem> listLineItem = listItem.stream().map(item -> 
					SessionCreateParams.LineItem.builder()
						.setQuantity(Long.valueOf(item.getQuantity()))
						.setPriceData(SessionCreateParams.LineItem.PriceData.builder()
							.setCurrency("VND")
							.setUnitAmount(calPrice(item.getProduct()))
							.setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
								.setName(item.getProduct().getName())
//								.addImage(item.getProduct().getUrlOfficalImage())
							.build())
						.build())
					.build())
					.collect(Collectors.toList());
		// @formatter:on
		return listLineItem;
	}

	public Long calPrice(Product product) {
		Long price = Math.round(Double.valueOf(product.getPrice() * (100 - product.getDiscount()) / 100) / 10000)
				* 10000;
		return price;
	}

}
