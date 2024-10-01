package com.hari.scm.product;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long>{

	Product findByProductCode(String productCode);
}
