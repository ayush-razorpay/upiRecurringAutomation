package com.upi.automation.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AutomationConfig {

    public static final String HTTPS_API_RAZORPAY_COM_V_1 = "https://api.razorpay.com/v1";
    public static final String basicAuthBase64Encode = "Basic cnpwX2xpdmVfNVc4b1pQQk5RUGJaREU6d2RETERqSmRaWHg3WWpFcklFNFJZU0h0";

    public static final String APPLICATION_JSON = "application/json";
    public static final String AUTHORIZATION = "Authorization";

    public static final HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
        @Override
        public void log(String message) {
            log.info("OkHttp: ");
            log.info(message);
            log.info("\n");
        }
    }).setLevel(HttpLoggingInterceptor.Level.BODY);

    public static final ObjectMapper objectMapper = new ObjectMapper();
    public static final OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
            .addInterceptor(httpLoggingInterceptor)
            .build();

    public static final String customerId = "cust_H56On0j1H53Ioj";

}
