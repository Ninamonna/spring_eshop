package com.komarova.spring_eshop.dto;

import com.komarova.spring_eshop.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class BucketDetailDTO {
    private String title;
    private Long productId;
    private Double price;
    private  Double amount;
    private Double sum;

    public BucketDetailDTO(Product product) {
        this.title = product.getTitle();
        this.productId = product.getId();
        this.price = product.getPrice();
        this.amount = 1.0;
        this.sum = product.getPrice();
    }

}
