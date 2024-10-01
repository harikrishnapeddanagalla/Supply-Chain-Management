package com.hari.scm.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product {

	private Long id;
	private String productCode;
	private String productName;
	private int stockQuantity;
	private double price;
}
