package com.shubh.foodOrder.Repository;

import com.shubh.foodOrder.entity.FoodEntity;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.MongoRepository;

@Repository
public interface FoodRepository extends MongoRepository<FoodEntity, String> {
}