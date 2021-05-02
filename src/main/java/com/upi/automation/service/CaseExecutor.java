package com.upi.automation.service;

import com.upi.automation.dao.TestCase;

import java.util.List;

public interface CaseExecutor {

    public List requestAuthorizations(String runId) throws Exception;

    public List checkSubscriptionAuhStatus(String runId) throws Exception;


    public List createSubsequentDebits(String runId, TestCase.SubscriptionFrequency frequency) throws Exception;


}
