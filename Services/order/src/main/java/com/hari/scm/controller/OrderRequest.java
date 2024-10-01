package com.hari.scm.controller;

import java.time.LocalDate;
import java.util.List;

import com.hari.scm.entity.OrderItem;

public record OrderRequest
(
		String orderNumber,
		String paymentType,
		List<OrderItem> items
		
		) {

}
