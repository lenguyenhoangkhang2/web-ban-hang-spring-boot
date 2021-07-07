package com.congnghejava.webbanhang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.congnghejava.webbanhang.config.AppProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
@EnableJpaAuditing
public class WebBanHangApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebBanHangApplication.class, args);
	}
}
