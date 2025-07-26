package com.shubh.foodOrder.service;

import com.shubh.foodOrder.io.UserRequest;
import com.shubh.foodOrder.io.UserResponse;

public interface UserService {

    UserResponse registerUser(UserRequest request);

    String findByUserId();
}