package com.programming.techie.productservice.infrastructure.dataaccess.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.programming.techie.productservice.infrastructure.dataaccess.entity.ProductEntity;

@Repository
public interface ProductMongoRepository extends MongoRepository<ProductEntity, String> {

}
