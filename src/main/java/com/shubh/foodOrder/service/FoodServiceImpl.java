package com.shubh.foodOrder.service;

import com.shubh.foodOrder.Repository.FoodRepository;
import com.shubh.foodOrder.entity.FoodEntity;
import com.shubh.foodOrder.io.FoodRequest;
import com.shubh.foodOrder.io.FoodResponse;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
//import software.amazon.awssdk.core.sync.RequestBody;
//import software.amazon.awssdk.services.s3.S3Client;
//import software.amazon.awssdk.services.s3.model.PutObjectRequest;
//import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FoodServiceImpl implements FoodService{

//    @Autowired
//    private S3Client s3Client;
    @Autowired
    private FoodRepository foodRepository;
    @Autowired
    private GridFsTemplate gridFsTemplate;

//    @Value("${aws.s3.bucketname}")
    private String bucketName;

//    public String uploadFile(MultipartFile file) {
//        String filenameExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
//        String key = UUID.randomUUID().toString()+"."+filenameExtension;
//        try {
//            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
//                    .bucket(bucketName)
//                    .key(key)
//                    .acl("public-read")
//                    .contentType(file.getContentType())
//                    .build();
//            PutObjectResponse response = s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));
//
//            if (response.sdkHttpResponse().isSuccessful()) {
//                return "https://"+bucketName+".s3.amazonaws.com/"+key;
//            } else {
//                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "File upload failed");
//            }
//        }catch (IOException ex) {
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occured while uploading the file");
//        }
//    }

//editing code
//    public FoodResponse addFood(FoodRequest request /*, MultipartFile file*/) {
//        FoodEntity newFoodEntity = convertToEntity(request);
////        String imageUrl = uploadFile(file);
////        newFoodEntity.setImageUrl(imageUrl);
//        newFoodEntity = foodRepository.save(newFoodEntity);
//        System.out.println("addFood run hua");
//        return convertToResponse(newFoodEntity);
//    }

//8888888888888888888888888888888888888888888888888888888888888888888888
    public FoodResponse addFood(FoodRequest request, MultipartFile file) {
        // Convert request DTO to entity
        FoodEntity newFoodEntity = convertToEntity(request);

        // Save image to MongoDB using GridFS
        if (file != null && !file.isEmpty()) {
            try {
                ObjectId imageId = gridFsTemplate.store(file.getInputStream(), file.getOriginalFilename(), file.getContentType());
                newFoodEntity.setImageId(imageId.toHexString()); // <-- set image ID instead of URL
            } catch (IOException e) {
                throw new RuntimeException("Image upload failed", e);
            }
        }

        // Save food entity to MongoDB
        newFoodEntity = foodRepository.save(newFoodEntity);
        System.out.println("addFood run hua");

        return convertToResponse(newFoodEntity);
    }

//8888888888888888888888888888888888888888888888888888888888888888888888


    private FoodEntity convertToEntity(FoodRequest request) {
        return FoodEntity.builder()
                .name(request.getName())
                .description(request.getDescription())
                .category(request.getCategory())
                .price(request.getPrice())
                .build();

    }

    private FoodResponse convertToResponse(FoodEntity entity) {

        String imageUrl = (entity.getImageId()!=null)?"/api/foods/image/"+entity.getImageId():null;

        return FoodResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .category(entity.getCategory())
                .price(entity.getPrice())
                .imageUrl(imageUrl)
                .build();
    }



    @Override
    public List<FoodResponse> readFoods() {
        List<FoodEntity> databaseEntries = foodRepository.findAll();
        return databaseEntries.stream().map(object -> convertToResponse(object)).collect(Collectors.toList());
    }

    @Override
    public FoodResponse readFood(String id) {
        FoodEntity existingFood = foodRepository.findById(id).orElseThrow(() -> new RuntimeException("Food not found for the id:"+id));
        return convertToResponse(existingFood);
    }

    @Override
    public void deleteFood(String id) {
        FoodResponse response = readFood(id);
//      String imageUrl = response.getImageUrl();
//      String filename = imageUrl.substring(imageUrl.lastIndexOf("/")+1);
//      boolean isFileDelete = deleteFile(filename);
        if (!foodRepository.existsById(id)) {
            throw new NoSuchElementException("Food item with ID " + id + " not found.");
        }
//        foodRepository.deleteById(id);
        foodRepository.deleteById(response.getId());

    }

//    @Override
//    public boolean deleteFile(String filename) {
//        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
//                .bucket(bucketName)
//                .key(filename)
//                .build();
//        s3Client.deleteObject(deleteObjectRequest);
//        return true;
//    }






}
