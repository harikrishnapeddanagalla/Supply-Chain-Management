package com.hari.scm.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hari.scm.entity.Order;
import com.hari.scm.service.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;
	
	@PostMapping("/createOrder")
	public ResponseEntity<String> createOrder(@RequestBody Order order){
		
		try {
			
			Order savedOrder = orderService.createOrder(order);
//			orderService.sendOrderConfirmationEmail();
			orderService.sendOrderEmailConfirmationWithAttachment(savedOrder);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResponseEntity.ok("Error while creating order");
		}
		return ResponseEntity.ok("Order created successfully");
	}
}
