package com.komarova.spring_eshop.service;

import com.komarova.spring_eshop.dao.ProductRepo;
import com.komarova.spring_eshop.domain.Bucket;
import com.komarova.spring_eshop.domain.User;
import com.komarova.spring_eshop.dto.ProductDTO;
import com.komarova.spring_eshop.mapper.ProductMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductMapper mapper = ProductMapper.MAPPER;

    private final ProductRepo productRepo;
    private final BucketService bucketService;
    private final UserService userService;

    public ProductServiceImpl(ProductRepo productRepo, BucketService bucketService, UserService userService) {
        this.productRepo = productRepo;
        this.bucketService = bucketService;
        this.userService = userService;
    }

    @Override
    public List<ProductDTO> getAll() {
        return mapper.fromProductList(productRepo.findAll());
    }

    @Override
    @Transactional
    public void addToUserBucket(Long productId, String username) {
        User user = userService.findByName(username);
        if (user == null) {
            throw new RuntimeException("User not found " + username);
        }

        Bucket bucket = user.getBucket();
        if (bucket == null) {
            Bucket newBucket = bucketService.createBucket(user, Collections.singletonList(productId));
            user.setBucket(newBucket);
            userService.save(user);
        } else {
            bucketService.addProducts(bucket, Collections.singletonList(productId));
        }

    }
}
