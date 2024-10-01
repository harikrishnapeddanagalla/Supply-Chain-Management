package com.hari.scm.product;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ProductController {

	private final productService service;

	@PostMapping("/addProduct")
	public ResponseEntity<Long> addProduct(@RequestBody @Validated ProductRequest request) {

		return ResponseEntity.ok(service.addProduct(request));
	}

	@GetMapping("/getAllProducts")
	public ResponseEntity<List<Product>> getAllProducts() {

		return ResponseEntity.ok(service.getAllProducts());
	}

	@GetMapping("/getProductById/{id}")
	public ResponseEntity<Product> getProductById(@PathVariable("id") Long id) {

		return ResponseEntity.ok(service.getProductById(id));
	}

	@GetMapping("/getProductByCode/{productCode}")
	public ResponseEntity<Product> getProductByCode(@PathVariable("productCode") String productCode) {

		return ResponseEntity.ok(service.getProductByCode(productCode));
	}

	@PutMapping("/updateProduct/{id}")
	public ResponseEntity<Product> updateProduct(@PathVariable("id") Long id, @RequestBody Product product) {

		return ResponseEntity.ok(service.updateProduct(id, product));

	}

	@DeleteMapping("/deleteProduct/{id}")
	public ResponseEntity<Void> deleteProduct(@PathVariable("id") Long id) {

		service.deleteProductById(id);

		return ResponseEntity.noContent().build();

	}
	
	@PutMapping("/updateProductQuantity/{productCode}")
	public ResponseEntity<Void> updateProductQuantity(
			@PathVariable("productCode") String productCode,
			@RequestParam int quantity) {

		try {
			service.updateStock(productCode, quantity);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(null);
		}
	}
	
	@PostMapping("/addProductCategory")
	public ResponseEntity<Integer> addProductCategory(@RequestBody ProductCategory request) {

		return ResponseEntity.ok(service.addProductCategory(request));
	}
	
	@GetMapping("/getProductTypes")
	public ResponseEntity<List<ProductCategory>> getProductTypes() {

		return ResponseEntity.ok(service.getProductTypes());
	}
	
	@PostMapping(value = "/upload",consumes = {"multipart/form-data"})
	public ResponseEntity<String> uploadProductsUsingExcel(@RequestPart("file") MultipartFile file){
		
		try {
			service.uploadProductsUsingExcel(file);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResponseEntity.ok("Error while uploading file : "+e.getMessage());
		}
		
		
		return ResponseEntity.ok("File uploaded successfully");
	}
}
