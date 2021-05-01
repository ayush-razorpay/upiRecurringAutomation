package com.upi.automation.service;

import com.upi.automation.dao.Result;
import com.upi.automation.dao.TestCase;
import org.springframework.stereotype.Component;

@Component
public interface UpiRecurringAutomation {

    String checkAuthStatus(String customerId, String tokenId) throws Exception;

    String createAuthorization(String vpa, String customerId, Result resultModel,
                               TestCase.SubscriptionFrequency frequency);

    String createSubsequentDebit();


}
