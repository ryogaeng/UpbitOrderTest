package com.example.ordering;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
    @Setter
    @NoArgsConstructor
    @ToString
    public class OrderRequest{

        // 마켓 id
        private String market;

        // 주문 종류 (매수(bid), 매도(ask))
        private String side;

        // 주문량
        private String volume;

        // 주문 가격
        private String price;

        // 주문 타입
        private String ord_type;
 }
