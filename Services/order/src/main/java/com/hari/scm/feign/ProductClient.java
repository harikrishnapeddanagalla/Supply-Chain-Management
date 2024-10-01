package com.hari.scm.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hari.scm.model.Product;

@FeignClient(name = "product-service",url = "${product-service.url}")
public interface ProductClient {

	@GetMapping("/getProductByCode/{productCode}")
	Product getProductByCode(@PathVariable("productCode") String productCode);
	
	@PutMapping("/updateProductQuantity/{productCode}")
	void updateProductQuantity(@PathVariable("productCode") String productCode,@RequestParam("quantity") int quantity);
}
