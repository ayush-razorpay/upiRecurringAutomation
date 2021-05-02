package com.upi.automation.service;

import com.upi.automation.dao.Result;
import com.upi.automation.dao.TestCase;
import org.springframework.stereotype.Component;

@Component
public interface UpiRecurringAutomationService {


    String createAuthorization(String vpa, String customerId, Result resultModel,
                               TestCase.SubscriptionFrequency frequency);

    public String createSubsequentDebit(Result result) throws Exception;


}
