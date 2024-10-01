package com.hari.scm.product;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class productService {

	private final ProductRepository productRepo;
	private final ProductCategoryRepository productCategory;
	private final ProductMapper mapper;

	public Long addProduct(ProductRequest request) {

		Product product = mapper.toProduct(request);

		Product savedProduct = productRepo.save(product);

		return savedProduct.getId();
	}

	public List<Product> getAllProducts() {

		List<Product> products = productRepo.findAll();

		return products;
	}

	public Product getProductById(Long id) {

		Product product = productRepo.findById(id).orElse(null);

		return product;
	}

	public Product getProductByCode(String productCode) {
		
		Product product = productRepo.findByProductCode(productCode);
		
		return product;
	}

	public Product updateProduct(Long id, Product request) {
		
		Product product = productRepo.findById(id).orElse(null);
		
		product.setProductCode(request.getProductCode());
		product.setProductName(request.getProductName());
		product.setProductType(request.getProductType());
		product.setStockQuantity(request.getStockQuantity());
		product.setPrice(request.getPrice());
		
		return productRepo.save(product);
	}

	public void deleteProductById(Long id) {
		
		productRepo.deleteById(id);
		
	}

	public void updateStock(String productCode, int quantity) throws Exception {

		Product product = productRepo.findByProductCode(productCode);

		if (product == null) {
			throw new Exception("Product not found with code : " + productCode);
		}

		Long currentStock = product.getStockQuantity();

		if (currentStock < quantity) {
			throw new Exception("Insufficient stock for the product" + productCode);
		}

		product.setStockQuantity(currentStock - quantity);

		productRepo.save(product);
	}

	public Integer addProductCategory(ProductCategory request) {
		// TODO Auto-generated method stub
		productCategory.save(request);
		
		return productCategory.save(request).getProductId();
	}

	public List<ProductCategory> getProductTypes() {
		
		List<ProductCategory> productTypes = productCategory.findAll();
		
		return productTypes;
	}

	public void uploadProductsUsingExcel(MultipartFile file) throws Exception {
		
		List<Product> products = new ArrayList<>();
		
	 try (InputStream inputStream = file.getInputStream();
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream)) {
		 
		 XSSFSheet sheet = workbook.getSheetAt(0);
		 
		 for (int i = 1; i <=sheet.getLastRowNum(); i++) {
			
			 XSSFRow row = sheet.getRow(i);
			 
			 String productCode = row.getCell(0).getStringCellValue();
			 String productName = row.getCell(1).getStringCellValue();
			 String productType = row.getCell(2).getStringCellValue();
			 Long quantity = (long) row.getCell(3).getNumericCellValue();
		     double price = row.getCell(4).getNumericCellValue();
		     
		     //need to validate the data before processing.. will add validations later
		     
		     Product product = new Product();
		     
		     product.setProductCode(productCode);
		     product.setProductName(productName);
		     product.setProductType(productType);
		     product.setStockQuantity(quantity);
		     product.setPrice(price);
		     
		     products.add(product);
		     
		}
		 
		 productRepo.saveAll(products);
		
	};
		
	}

//	private boolean isRowDataValid(String productCode, String productName, 
//			String productType, Long quantity, double price) {
//		
//		
//		boolean result = productCode.isBlank() || productCode.isEmpty();
//		
//		if(productCode.isBlank() || productCode.isEmpty()) {
//			
//			return false;
//		}
//		
//		return true;
//		
//	}
	
	

}
