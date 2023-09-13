package com.example.ordering;

import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.auth0.jwt.JWT;
import retrofit2.Response;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.UUID;

@SpringBootApplication
public class OrderingApplication {

	private static OrderRequest order(String side, String price, String volume) {

		OrderRequest orderRequest = new OrderRequest();

		orderRequest.setMarket("KRW-BTC");    // 마켓은 무조건 KRW-BTC
		orderRequest.setPrice(price);    // price set
		orderRequest.setVolume(volume);    // volume set
		orderRequest.setSide(side);        // side set

		switch (orderRequest.getSide()) {
			case "bid":    // 매수
				if (orderRequest.getVolume() == null) {    // 시장가 매수
					orderRequest.setOrd_type("price");

				} else     // 지정가 매수
					orderRequest.setOrd_type("limit");
				break;
			case "ask":    // 매도
				if (orderRequest.getPrice() == null)    // 시장가 매도
					orderRequest.setOrd_type("market");
				else     // 지정가 매도
					orderRequest.setOrd_type("limit");
			default:
				return null;
		}

		return orderRequest;
	}

	public static void main(String[] args) throws
			NoSuchAlgorithmException, UnsupportedEncodingException {

		SpringApplication.run(OrderingApplication.class, args);

		// 업비트에서 부여받은 accessKey와 secretKey
		String accessKey = "본인의 access key 입력";
		String secretKey = "본인의 secret key 입력";

		// order 메소드를 이용하여 내가 원하는 세팅으로 주문 요청
		OrderRequest orderRequest = order("bid", "1000000", "0.005");

		// query 문자열의 각 요소를 저장하기 위한 queryElements
		ArrayList<String> queryElements = new ArrayList<>();

		/*
			for문으로 orderRequest 객체의 필드와 해당 필드의 값을 순회하며
			Map.Entry<String, String> 타입의 elements 변수에 할당하고,
			elements의 키-값 쌍을 key=value 형식의 문자열로 변환하여 queryElements list에 add
		 */

		for(Field field : orderRequest.getClass().getDeclaredFields()){
			field.setAccessible(true);	// private 필드에 접근 가능하도록 설정
			String key = field.getName();
			Object valueObj;
			try{
				valueObj = field.get(orderRequest);
				if (valueObj != null){	// 필드 값이 null이 아닐 경우에만 추가
					String value = valueObj.toString();
					queryElements.add(key + '=' + value);
				}
			}	catch (IllegalAccessException e){
				e.printStackTrace();
			}
		}

		/*
			upbit 가이드를 준수하기위해 파라미터의 자료형 중 배열은
			key1=value1&key2=value2&key3=value3... 꼴로 만들어야한다.
		 */
		String queryString = String.join("&", queryElements.toArray(new String[0]));

		System.out.println("orderRequest = " + orderRequest);
		System.out.println("queryString = " + queryString);

		/*
		1. (side) 	매수(bid) or 매도(ask) 선택
		2. (price)	주문 가격 선택
		3. (volume) 주문량 선택
		- market은 default로 KRW-BTC
		- 1,2,3에 따라서 ord_type(limit, price, market)이 결정
		*/

		// SHA-512 해싱을 사용하여 queryString 해싱
		MessageDigest md = MessageDigest.getInstance("SHA-512");
		md.update(queryString.getBytes("utf8"));

		String queryHash = String.format("%0128x", new BigInteger(1, md.digest()));

		// JWT 토큰 생성
		Algorithm algorithm = Algorithm.HMAC256(secretKey);
		String jwtToken = JWT.create()
				.withClaim("access_key", accessKey)	// 부여받은 accessKey
				.withClaim("nonce", UUID.randomUUID().toString())	// 랜덤한 UUID 적용
				.withClaim("query_hash", queryHash)	// queryString에 SHA-512를 적용한 queryHash
				.withClaim("query_hash_alg", "SHA512")	// 해싱 알고리즘은 SHA-512를 사용하겠다!
				.sign(algorithm);

		String authenticationToken = "Bearer " + jwtToken;

		UpbitAPI api = OrderClient.getApi();

		try{
			Response<OrderResponse> response = api.placeOrder(authenticationToken, orderRequest).execute();

			if (response.isSuccessful()){
				OrderResponse orderResponse = response.body();
				System.out.println("주문 UUID: " + orderResponse.getUuid());
				System.out.println("주문 상태: " + orderResponse.getState());
			}
			else{
				System.out.println("주문 실패: " + response.errorBody().string());
			}
		}	catch (IOException e){
			e.printStackTrace();
		}
	}
}