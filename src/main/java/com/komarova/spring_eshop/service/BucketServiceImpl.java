package com.komarova.spring_eshop.service;

import com.komarova.spring_eshop.dao.BucketRepo;
import com.komarova.spring_eshop.dao.ProductRepo;
import com.komarova.spring_eshop.domain.Bucket;
import com.komarova.spring_eshop.domain.Product;
import com.komarova.spring_eshop.domain.User;
import com.komarova.spring_eshop.dto.BucketDTO;
import com.komarova.spring_eshop.dto.BucketDetailDTO;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BucketServiceImpl implements BucketService{
    private final BucketRepo bucketRepo;
    private final ProductRepo productRepo;
    private final UserService userService;

    public BucketServiceImpl(BucketRepo bucketRepo, ProductRepo productRepo, UserService userService) {
        this.bucketRepo = bucketRepo;
        this.productRepo = productRepo;
        this.userService = userService;
    }

    @Override
    @Transactional
    public Bucket createBucket(User user, List<Long> productsIds) {
        Bucket bucket = new Bucket();
        bucket.setUser(user);
        List<Product> productList = getCollectRefProductsByIds(productsIds);
        bucket.setProducts(productList);
        return bucketRepo.save(bucket);
    }

    private List<Product> getCollectRefProductsByIds(List<Long> productsIds) {
        return productsIds.stream()
                .map(productRepo::getOne)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void addProducts(Bucket bucket, List<Long> productsIds) {
        List<Product> products = bucket.getProducts();
        List<Product> newProductsList = products == null ? new ArrayList<>() : new ArrayList<>(products);
        newProductsList.addAll(getCollectRefProductsByIds(productsIds));
        bucket.setProducts(newProductsList);
        bucketRepo.save(bucket);

    }

    @Override
    public BucketDTO getBucketByUser(String name) {
        User user = userService.findByName(name);
        if(user == null || user.getBucket() == null) {
            return new BucketDTO();
        }
        BucketDTO bucketDTO = new BucketDTO();
        Map<Long, BucketDetailDTO> mapByProductId = new HashMap<>();

        List<Product> products = user.getBucket().getProducts();
        for (Product product : products) {
            BucketDetailDTO detail = mapByProductId.get(product.getId());
            if(detail == null) {
                mapByProductId.put(product.getId(), new BucketDetailDTO(product));
            } else {
                detail.setAmount(detail.getAmount() + 1.0);
                detail.setSum(detail.getSum() + product.getPrice());
            }
        }
        bucketDTO.setBucketDetails(new ArrayList<>(mapByProductId.values()));
        bucketDTO.aggregate();

        return bucketDTO;
    }
}
