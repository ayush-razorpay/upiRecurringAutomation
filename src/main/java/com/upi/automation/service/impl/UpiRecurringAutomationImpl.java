package com.upi.automation.service.impl;

import com.upi.automation.dao.Result;
import com.upi.automation.dao.TestCase;
import com.upi.automation.service.UpiRecurringAutomation;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

import static com.upi.automation.config.AutomationConfig.*;

@Slf4j
@Service
public class UpiRecurringAutomationImpl implements UpiRecurringAutomation {


    @Override
    public String createAuthorization(String vpa, String customerId, Result resultModel, TestCase.SubscriptionFrequency frequency) {
        try {
            resultModel.setFrequency(frequency.toString());
            resultModel.setTimeStamp(new Date());


            String orderId = createRecurringPaymentOrderId(customerId, frequency);
            resultModel.setOrderId(orderId);
            resultModel.addComments("orderId Generated : " + orderId);


            String paymentId = createPayment(orderId, customerId, vpa);
            resultModel.setPaymentId(paymentId);
            resultModel.addComments("Payment Created : " + paymentId);

            String token = getTokenIdIfValid(paymentId);

            resultModel.addComments("Token Created : " + token);

            resultModel.setTokenId(token);
        } catch (Exception e) {
            log.error("exception while creating authorization error : {} ", e);
            resultModel.addComments("exception while creating authorization error stackTrace :" + e.getStackTrace()
                    + "error :" + e.getMessage());
        }
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

    private String createRecurringPaymentOrderId(String customerId, TestCase.SubscriptionFrequency frequency) throws Exception {
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
            throw new Exception("unable to generate orderId for Authorization Debit:", new Exception(response.body().string()));
        Map t = objectMapper.readValue(response.body().string(), Map.class);

        String v = (String) t.get("id");

        return v;
    }


    private String createSubsequentDebitOrderId() throws Exception {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse(APPLICATION_JSON);
        RequestBody body = RequestBody.create(mediaType,
                "{\n  \"amount\":200,\n  \"currency\":\"INR\"\n}\n");
        Request request = new Request.Builder()
                .url(HTTPS_API_RAZORPAY_COM_V_1 + "/orders")
                .method("POST", body)
                .addHeader(AUTHORIZATION, basicAuthBase64Encode)
                .addHeader("Content-Type", APPLICATION_JSON)
                .build();
        Response response = client.newCall(request).execute();
        if (response.code() != 200)
            throw new Exception("unable to generate orderId for subsequentDebit:", new Exception(response.body().string()));
        Map t = objectMapper.readValue(response.body().string(), Map.class);

        String v = (String) t.get("id");

        return v;
    }


    //todo:this still doent work. have to complete this
    @Override
    public String createSubsequentDebit(Result result) throws Exception {
        String orderId = this.createSubsequentDebitOrderId();

        OkHttpClient client = okHttpClient;
        MediaType mediaType = MediaType.parse(APPLICATION_JSON);
        RequestBody body = RequestBody.create(mediaType,
                "{\n    \"amount\": 200,\n " +
                        "   \"email\": \"abc@xyz.com\",\n " +
                        "   \"currency\": \"INR\",\n   " +
                        " \"order_id\": \"" + orderId + "\",\n  " +
                        "  \"customer_id\": \"" + result.getCustomerId() + "\",\n   " +
                        " \"token\": \"" + result.getTokenId() + "\",\n  " +
                        "  \"contact\": \"9896148608\",\n   " +
                        " \"recurring\": \"1\",\n   " +
                        " \"description\": \"Automation Testing\"\n}");
        Request request = new Request.Builder()
                .url(HTTPS_API_RAZORPAY_COM_V_1 + "/payments/create/recurring")
                .method("POST", body)
                .addHeader(AUTHORIZATION, basicAuthBase64Encode)
                .addHeader("Content-Type", APPLICATION_JSON)
                .build();
        Response response = client.newCall(request).execute();

        return null;
    }

}


