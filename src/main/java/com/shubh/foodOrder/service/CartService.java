package com.shubh.foodOrder.service;

import com.shubh.foodOrder.io.CartRequest;
import com.shubh.foodOrder.io.CartResponse;

public interface CartService {

    CartResponse addToCart(CartRequest request);

    CartResponse getCart();

    void clearCart();

    CartResponse removeFromCart(CartRequest cartRequest);
}