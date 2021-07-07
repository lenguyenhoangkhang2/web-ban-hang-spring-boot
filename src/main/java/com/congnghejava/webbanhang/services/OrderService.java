package com.congnghejava.webbanhang.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.congnghejava.webbanhang.exception.BadRequestException;
import com.congnghejava.webbanhang.models.EOrderStatus;
import com.congnghejava.webbanhang.models.EPaymentMethod;
import com.congnghejava.webbanhang.models.Mail;
import com.congnghejava.webbanhang.models.Order;
import com.congnghejava.webbanhang.models.Product;
import com.congnghejava.webbanhang.models.User;
import com.congnghejava.webbanhang.payload.response.OrderResponse;
import com.congnghejava.webbanhang.repository.OrderRepository;
import com.congnghejava.webbanhang.repository.criteria.OrderSearchCriteria;
import com.congnghejava.webbanhang.repository.specification.OrderSpecification;
import com.stripe.exception.StripeException;
import com.stripe.model.Refund;
import com.stripe.param.RefundCreateParams;

@Service
public class OrderService {
	@Autowired
	OrderRepository orderRepository;

	@Autowired
	ProductService productService;

	@Autowired
	EmailSenderService emailSenderService;

	@Value("${spring.mail.username}")
	private String adminEmail;

	public List<Order> findAll() {
		return orderRepository.findAll();
	}

	public Page<Order> findWithFilter(OrderSearchCriteria searchCriteria, Pageable page) {
		Specification<Order> conditions = Specification.where(OrderSpecification.hasId(searchCriteria.getId())
				.and(OrderSpecification.hasStatus(searchCriteria.getStatus())
						.and(OrderSpecification.hasUserName(searchCriteria.getUsername()))));
		return orderRepository.findAll(conditions, page);
	}

	public Order findById(String id) {
		return orderRepository.findById(id).get();
	}

	public Page<Order> findAllByUser(User user, Pageable page) {
		return orderRepository.findByUser(user, page);
	}

	public void updateOrder(Order order) {
		if (EOrderStatus.Open.equals(order.getStatus())) {
			order.setStatus(EOrderStatus.Confirmed);
			return;
		}
		if (EOrderStatus.Confirmed.equals(order.getStatus()) || EOrderStatus.Returned.equals(order.getStatus())) {
			order.setStatus(EOrderStatus.Shipping);
			return;
		}
		if (EOrderStatus.Shipping.equals(order.getStatus())) {
			order.setStatus(EOrderStatus.Collected);
			order.setSettled(true);
			return;
		}
		if (EOrderStatus.Collected.equals(order.getStatus())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Đơn hàng đã được nhận vào ngày " + order.getLastModifiedDate());
		}
		if (EOrderStatus.Canceled.equals(order.getStatus())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không thể cập nhật đơn hàng đã hủy");
		}
	}

	public void returnedOrder(Order order) {
		if (order.getStatus().equals(EOrderStatus.Shipping)) {
			order.setStatus(EOrderStatus.Returned);
		} else {
			throw new BadRequestException("Đơn hàng vẫn chưa được gửi. Không thể thực hiện thao tác này!");
		}
	}

	public void canceledOrder(Order order) throws StripeException {
		if (order.getStatus() != EOrderStatus.Canceled && order.getStatus() != EOrderStatus.Collected) {
			order.setStatus(EOrderStatus.Canceled);

			order.getOrderItems().stream().forEach(item -> {
				Product product = productService.findById(item.getProductId());
				product.setQuantity(product.getQuantity() + item.getQuantity());
				productService.save(product);
			});

			orderRepository.save(order);
			if (order.getPaymentMethod() == EPaymentMethod.Stripe) {
				Refund.create(RefundCreateParams.builder().setCharge(order.getStripeChagreId()).build());
			}

		} else {
			String message = order.getStatus() == EOrderStatus.Canceled ? "Đơn hàng đã được hủy trước đó"
					: "Không thể hủy đơn hàng đã nhận";
			throw new BadRequestException(message);
		}
	}

	public void sendOrderStatusMail(Order order) throws MailException, MessagingException {

		OrderResponse orderResponse = new OrderResponse(order);
		Mail mail = new Mail();

		mail.setFrom(adminEmail);
		mail.setTo(order.getUser().getUserCredential().getEmail());
		mail.setSubject("Trạng thái đơn hàng được cập nhật");

		Map<String, Object> model = new HashMap<String, Object>();
		model.put("order", orderResponse);

		mail.setProps(model);

		emailSenderService.sendEmail(mail, "order-status");
	}

	public Order save(Order order) {
		return orderRepository.save(order);
	}

	public void remove(Order order) {
		orderRepository.deleteById(order.getId());
	}
}
