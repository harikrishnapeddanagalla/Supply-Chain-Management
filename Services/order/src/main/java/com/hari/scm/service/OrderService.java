package com.hari.scm.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.hari.scm.entity.Order;
import com.hari.scm.entity.OrderItem;
import com.hari.scm.exception.InsufficientStockException;
import com.hari.scm.feign.ProductClient;
import com.hari.scm.model.Product;
import com.hari.scm.repository.OrderRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

	private final OrderRepository orderRepo;
	private final ProductClient productClient;
	private final JavaMailSender mailSender;
	private final TemplateEngine templateEngine;
	
	@Transactional
	public Order createOrder(Order request) throws Exception {
		
		Order order = new Order();
		List<OrderItem> lineItems = new ArrayList<>();
		
		if(request==null) {
			throw new Exception("Order object is null");
		}
		double totalOrderPrice=0.0;
		
		List<OrderItem> items = request.getItems();
		
		for (OrderItem orderItem : items) {
			
			String productCode = orderItem.getProductCode();
			Product product = productClient.getProductByCode(productCode);
			
			int currentStockQuantity = product.getStockQuantity();
			
			int orderedQuantity = orderItem.getQuantity();
			
			if(validateStock(currentStockQuantity,orderedQuantity)) {
				throw new InsufficientStockException("Stock is less for the product :"+productCode);
			}
			
			double totalPrice = orderedQuantity * product.getPrice();
			
		   orderItem.setPrice(totalPrice);
		   orderItem.setProductName(product.getProductName());
		   orderItem.setProductCode(product.getProductCode());
		   
		   orderItem.setOrder(order);
		   
		   totalOrderPrice=totalOrderPrice+totalPrice;
		   lineItems.add(orderItem);

		   productClient.updateProductQuantity(productCode, orderedQuantity);
		   
		}
		
		   order.setOrderNumber(request.getOrderNumber());
		   order.setOrderDate(LocalDate.now());
		   order.setOrderStatus("Created");
		   order.setTotalPrice(totalOrderPrice);
		   order.setPaymentType(request.getPaymentType());
		   order.setItems(lineItems);
        
        return orderRepo.save(order);
		
	}
	

	public boolean validateStock(int currentStockQuantity,int orderedQuantity) {
		
		return currentStockQuantity<orderedQuantity;
	}


	public void sendOrderConfirmationEmail() {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo("jagan.t@ubq.in");
            message.setSubject("Order Confirmation");
            message.setText("Order created successfully");

            mailSender.send(message);
            System.out.println("Mail sent successfully");

        } catch (MailException e) {
            // This will give you a more detailed error
            System.err.println("Error sending email: " + e.getMessage());
            e.printStackTrace();
        }
    }
	
	public void sendOrderEmailConfirmationWithAttachment(Order order) {
		 
		 try {
			 
			MimeMessage message = mailSender.createMimeMessage();
		    MimeMessageHelper helper = new MimeMessageHelper(message,true);
			 
			helper.setTo("peddanagallaharikrishna@gmail.com");
			helper.setSubject("Order confirmation!");
			helper.setFrom("peddanagallaharikrishna@gmail.com");
			
			Context context = new Context();
			
			context.setVariable("customerName", "Harikrishna");
			context.setVariable("orderNumber", order.getOrderNumber());
			context.setVariable("orderDate", order.getOrderDate());
			context.setVariable("totalAmount", order.getTotalPrice());
			context.setVariable("orderItems", order.getItems());
			
			String htmlContent = templateEngine.process("order-confirmation", context);
			helper.setText(htmlContent,true);
			
			mailSender.send(message);
			System.out.println("Order confirmation email sent successfully");
			
		} catch (MessagingException e) {
			
			e.printStackTrace();
		}
		 
		
		
	}
}
