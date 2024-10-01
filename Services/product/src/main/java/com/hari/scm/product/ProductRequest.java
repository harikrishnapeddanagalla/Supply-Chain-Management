package com.hari.scm.product;

public record ProductRequest(
		String productCode, 
		String productName, 
		String productType, 
		Long stockQuantity,
		double price) {

}
