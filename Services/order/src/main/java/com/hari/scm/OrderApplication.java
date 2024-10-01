package com.hari.scm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.hari.scm.service.OrderService;

@SpringBootApplication
@EnableFeignClients
@EnableTransactionManagement
@EnableDiscoveryClient
public class OrderApplication {

	@Autowired
	private OrderService service;
	
	public static void main(String[] args) {
		SpringApplication.run(OrderApplication.class, args);
	}

//	@EventListener(ApplicationReadyEvent.class)
//	public void triggerMail() {
//		service.sendOrderConfirmationEmail();
//	}
	
}
