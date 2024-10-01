package com.hari.scm.product;

public record ProductResponse(
		String productCode, 
		String productName, 
		String productType, 
		Long stockQuantity,
		double price
		) {

}
