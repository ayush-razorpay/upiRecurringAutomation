package com.upi.automation.service;

import com.upi.automation.dao.TestCase;

public interface CaseExecutor {

    public void requestAuthorizations(String runId) throws Exception;

    public void checkSubscriptionAuhStatus(String runId) throws Exception;


    public void createSubsequentDebits(String runId, TestCase.SubscriptionFrequency frequency) throws Exception;


}
