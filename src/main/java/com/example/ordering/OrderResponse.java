package com.example.ordering;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderResponse {

    // 주문의 고유 아이디
    @SerializedName("uuid")
    private String uuid;

    // 주문 종류
    @SerializedName("side")
    private String side;

    // 주문 방식
    @SerializedName("ord_type")
    private String ordType;

    // 주문 상태
    @SerializedName("state")
    private String state;

    // 마켓의 유일 키
    @SerializedName("market")
    private String market;

    // 주문 생성 시간
    @SerializedName("created_at")
    private String createAt;
    @SerializedName("volume")
    private String volume;

    // 주문 당시 화폐 가격
    @SerializedName("price")
    private String price;

    // 체결 후 남은 주문 양
    @SerializedName("remaining_volume")
    private String remainingVolume;

    // 수수료로 예약된 비용
    @SerializedName("reserved_fee")
    private String reservedFee;

    // 남은 수수료
    @SerializedName("remaining_fee")
    private String remainingFee;

    // 사용된 수수료
    @SerializedName("paid_fee")
    private String paidFee;

    // 거래에 사용중인 비용
    @SerializedName("locked")
    private String locked;

    // 체결된 양
    @SerializedName("executed_volume")
    private String executedVolume;

    // 해당 주문에 걸린 체결 수
    @SerializedName("trades_count")
    private int tradesCount;

}
