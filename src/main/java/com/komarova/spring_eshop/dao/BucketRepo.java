package com.komarova.spring_eshop.dao;

import com.komarova.spring_eshop.domain.Bucket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BucketRepo extends JpaRepository<Bucket, Long> {
}
