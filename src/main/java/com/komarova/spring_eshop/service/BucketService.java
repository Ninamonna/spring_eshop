package com.komarova.spring_eshop.service;

import com.komarova.spring_eshop.domain.Bucket;
import com.komarova.spring_eshop.domain.User;
import com.komarova.spring_eshop.dto.BucketDTO;

import java.util.List;

public interface BucketService {

    Bucket createBucket(User user, List<Long> productsIds);
    void addProducts(Bucket bucket, List<Long> productsIds);

    BucketDTO getBucketByUser(String name);
}
