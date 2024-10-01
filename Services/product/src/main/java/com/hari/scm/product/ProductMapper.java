package com.hari.scm.product;

import org.springframework.stereotype.Service;

@Service
public class ProductMapper {

	public Product toProduct(ProductRequest request) {
		
		Product product = Product.builder()
		.productCode(request.productCode())
		.productName(request.productName())
		.productType(request.productType())
		.stockQuantity(request.stockQuantity())
		.price(request.price()).build();
		
		return product;
	}

}
