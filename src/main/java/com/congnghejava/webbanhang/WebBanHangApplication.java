package com.congnghejava.webbanhang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.congnghejava.webbanhang.config.AppProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class WebBanHangApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebBanHangApplication.class, args);
	}
}
