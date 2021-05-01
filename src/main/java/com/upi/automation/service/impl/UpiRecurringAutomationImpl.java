package com.upi.automation.service.impl;

import com.upi.automation.dao.Result;
import com.upi.automation.dao.TestCase;
import com.upi.automation.dao.repository.TestCaseRepository;
import com.upi.automation.service.UpiRecurringAutomation;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

import static com.upi.automation.config.AutomationConfig.*;

@Slf4j
@Service
public class UpiRecurringAutomationImpl implements UpiRecurringAutomation {


    @Autowired
    TestCaseRepository repository;


    @Override
    public String checkAuthStatus(String customerId, String tokenId) throws Exception {
        return null;
    }

    @Override
    public String createAuthorization(String vpa, String customerId, Result resultModel, TestCase.SubscriptionFrequency frequency) {
        try {
            resultModel.setFrequency(frequency.toString());
            resultModel.setTimeStamp(new Date());


            String orderId = createOrderId(customerId, frequency);
            resultModel.setOrderId(orderId);

            String paymentId = createPayment(orderId, customerId, vpa);
            resultModel.setPaymentId(paymentId);

            String token = getTokenIdIfValid(paymentId);
            resultModel.setTokenId(token);
        } catch (Exception e) {
            log.error("exception while creating authorization error : {} ", e);
        }
        return null;
    }


    @Override
    public String createSubsequentDebit() {
        return null;
    }

    private String createPayment(String orderId, String customerId, String vpa) throws Exception {
        OkHttpClient client = okHttpClient;
        MediaType mediaType = MediaType.parse(APPLICATION_JSON);

        RequestBody body = RequestBody.create(mediaType, "{\n    \"amount\": 200,\n  " +
                "  \"currency\": \"INR\",\n  " +
                "  \"contact\": 8888888888,\n " +
                "   \"email\": \"gaurav@gmail.com\",\n " +
                "   \"order_id\": \"" + orderId + "\",\n   " +
                " \"customer_id\": \"" + customerId + "\",\n   " +
                " \"method\": \"upi\",\n  " +
                "  \"recurring\": \"1\",\n  " +
                "  \"upi\": " +
                "{\n        \"flow\": \"collect\",\n  " +
                "      \"type\": \"recurring\",\n      " +
                "  \"vpa\": \"" + vpa + "\"\n  " +
                "  }\n}");

        Request request = new Request.Builder()
                .url(HTTPS_API_RAZORPAY_COM_V_1 + "/payments/create/redirect")
                .method("POST", body)
                .addHeader(AUTHORIZATION, basicAuthBase64Encode)
                .addHeader("Content-Type", APPLICATION_JSON)
                .build();
        Response response = client.newCall(request).execute();

        if (response.code() != 200)
            throw new Exception("unable to create payment :", new Exception(response.body().string()));


        String v1 = response.body().string();

        String paymentId = v1.substring(v1.indexOf("pay_"), v1.indexOf("\",\"gateway\":"));
        System.out.println("paymentId-" + paymentId);


        if (paymentId == null || (paymentId.trim().isEmpty())) {
            throw new Exception("Payment Id null");
        }

        return paymentId;


    }

    private String getTokenIdIfValid(String paymentId) throws Exception {

        Request request = new Request.Builder()
                .url(HTTPS_API_RAZORPAY_COM_V_1 + "/payments/" + paymentId)
                .method("GET", null)
                .addHeader(AUTHORIZATION, basicAuthBase64Encode)
                .build();
        Response response = okHttpClient.newCall(request).execute();

        if (response.code() != 200)
            throw new Exception("unable to fetch details of payId :", new Exception(response.body().string()));
        Map t = objectMapper.readValue(response.body().string(), Map.class);

        String v = (String) t.get("token_id");
        if (v == null || v.trim().isEmpty())
            throw new Exception("unable to find tokenID :" + v, new Exception(response.body().string()));
        return v;

    }

    private String createOrderId(String customerId, TestCase.SubscriptionFrequency frequency) throws Exception {
        OkHttpClient client = okHttpClient;
        MediaType mediaType = MediaType.parse(APPLICATION_JSON);

        String b = "{\"amount\":200,\"currency\":\"INR\",\"customer_id\":\""
                + customerId +
                "\",\"method\":\"upi\",\"token\":{\"max_amount\":200,\"frequency\":\""
                + frequency.toString() +
                "\",\"expire_at\":\"1965631950\"}}";

        RequestBody body = RequestBody.create(mediaType, b);

        Request request = new Request.Builder()
                .url(HTTPS_API_RAZORPAY_COM_V_1 + "/orders")
                .method("POST", body)
                .addHeader(AUTHORIZATION, basicAuthBase64Encode)
                .addHeader("Content-Type", APPLICATION_JSON)
                .build();
        Response response = client.newCall(request).execute();

        if (response.code() != 200)
            throw new Exception("unable to generate orderId :", new Exception(response.body().string()));
        Map t = objectMapper.readValue(response.body().string(), Map.class);

        String v = (String) t.get("id");

        return v;
    }


}


