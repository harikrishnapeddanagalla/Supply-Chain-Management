package com.hari.scm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hari.scm.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long>{

}
