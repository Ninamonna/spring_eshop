package com.komarova.spring_eshop.dao;

import com.komarova.spring_eshop.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepo extends JpaRepository<Product, Long> {
}
