package com.shubh.foodOrder.service;




import com.shubh.foodOrder.io.FoodResponse;
import com.shubh.foodOrder.io.FoodRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
public interface FoodService {

//    String uploadFile(MultipartFile file);

//    FoodResponse addFood(FoodRequest request/*,MultipartFile file*/);


    FoodResponse addFood(FoodRequest request, MultipartFile file); //image uploading new method

    List<FoodResponse> readFoods();

    FoodResponse readFood(String id);

//    boolean deleteFile(String filename);

    void deleteFood(String id);

}
