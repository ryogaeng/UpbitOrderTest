package com.example.ordering;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

import java.util.Map;

public interface UpbitAPI {
    @POST("/v1/orders")
    @Headers("Content-Type: application/json; charset=utf-8")
    Call<OrderResponse> placeOrder(
            @Header("Authorization") String authHeader,
            @Body OrderRequest orderRequest
    );
}
