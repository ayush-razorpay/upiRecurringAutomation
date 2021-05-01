package com.upi.automation.service.impl;

import com.upi.automation.dao.Result;
import com.upi.automation.dao.TestCase;
import com.upi.automation.dao.repository.TestCaseRepository;
import com.upi.automation.dao.repository.TestResultRepository;
import com.upi.automation.service.CaseExecutor;
import com.upi.automation.service.UpiRecurringAutomation;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.upi.automation.config.AutomationConfig.*;

@Slf4j
@Component
public class CaseExecutorImpl implements CaseExecutor {

    @Autowired
    TestCaseRepository testCaseRepository;
    @Autowired
    TestResultRepository resultRepository;
    @Autowired
    UpiRecurringAutomation upiRecurringAutomation;


    @Override
    public void requestAuthorizations(String runId) {
        //fetch the data from table
        List<TestCase> cases = this.testCaseRepository.findAll().stream()
                .filter(x -> x.getIsEnabled().equalsIgnoreCase("y"))
                .collect(Collectors.toList());

        log.info("total cases fetach for execution  : {}", cases.size());
        //diplay count and then execute one by one

        for (TestCase testCase : cases) {
            Result r = new Result();
            try {
                r.setBank(testCase.getBank());
                r.setCustomerId(customerId);
                r.setPsp(testCase.getPsp());
                r.setRunId(runId);
                r.setVpa(testCase.getVpa());

                this.upiRecurringAutomation.createAuthorization(testCase.getVpa(), customerId, r, testCase.getFrequency());
            } catch (Exception e) {
                log.error("test case execution failed for cas : {} and error {}", testCase, e);
            } finally {
                this.resultRepository.save(r);
            }

        }


    }

    @Override
    public void checkSubscriptionAuhStatus(String runId) throws IOException {

        //fetch all the tokens for a customer id and the check status and update
        OkHttpClient client = okHttpClient;
        Request request = new Request.Builder()
                .url(HTTPS_API_RAZORPAY_COM_V_1 + "v1/customers/" + customerId + "/tokens")
                .method("GET", null)
                .addHeader(AUTHORIZATION, basicAuthBase64Encode)
                .build();
        Response response = client.newCall(request).execute();

        Map t = objectMapper.readValue(response.body().string(), Map.class);

        List<Map> validTokens = (List<Map>) t.get("items");

        List<Result> dat = this.resultRepository.findAll().stream()
                .filter(x -> x.getRunId().equalsIgnoreCase(runId)).collect(Collectors.toList());

        for (Result r : dat) {
            var list = validTokens.stream().filter(x -> x.get("id").equals(r.getTokenId())).collect(Collectors.toList());
            if (list.size() > 0) {
                r.setMandateCreationStatus("success");
                resultRepository.save(r);
            }

        }
    }
}
