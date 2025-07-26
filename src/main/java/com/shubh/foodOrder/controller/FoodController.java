package com.shubh.foodOrder.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.shubh.foodOrder.io.FoodRequest;
import com.shubh.foodOrder.io.FoodResponse;
import com.shubh.foodOrder.service.FoodService;
import com.shubh.foodOrder.service.FoodServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/foods")
@AllArgsConstructor
@CrossOrigin("*")
public class FoodController {

    private final FoodServiceImpl foodService;

    @Autowired
    private GridFsTemplate gridFsTemplate;

//image adding editing
//    @PostMapping
//    public FoodResponse addFood(@RequestPart("food") String foodString/*,@RequestPart("file") MultipartFile file*/) {
//        ObjectMapper objectMapper = new ObjectMapper();
//        FoodRequest request = null;
//        try {
//            request = objectMapper.readValue(foodString, FoodRequest.class);
//        }catch(JsonProcessingException ex) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid JSON format");
//        }
//        FoodResponse response = foodService.addFood(request/*, file*/);
//        return response;
//    }

//88888888888888888888888888888888888888888888888888888888888888888
    @PostMapping
    public FoodResponse addFood(@RequestPart("food") String foodString,
                                @RequestPart("file") MultipartFile file) {
        ObjectMapper objectMapper = new ObjectMapper();
        FoodRequest request;
        try {
            request = objectMapper.readValue(foodString, FoodRequest.class);
        } catch (JsonProcessingException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid JSON format");
        }

        // Pass both request and image file to service
        return foodService.addFood(request, file);
    }
//88888888888888888888888888888888888888888888888888888888888888888

    @GetMapping
    public List<FoodResponse> readFoods() {
        return foodService.readFoods();
    }
    @GetMapping("/{id}")
    public FoodResponse readFood(@PathVariable String id) {
        return foodService.readFood(id);
    }

//888888888888888888888888888888888888888888888888888888888888888888888888888
@GetMapping("/image/{id}")
@CrossOrigin("*")
public ResponseEntity<?> getImage(@PathVariable String id) {
    try {
        GridFSFile file = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(id)));
        if (file == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Image not found");
        }

        GridFsResource resource = gridFsTemplate.getResource(file);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(resource.getContentType()))
                .body(new InputStreamResource(resource.getInputStream()));
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving image");
    }
}

//888888888888888888888888888888888888888888888888888888888888888888888888888


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFood(@PathVariable String id) {
        foodService.deleteFood(id);
    }
}
