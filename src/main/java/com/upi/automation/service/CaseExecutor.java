package com.upi.automation.service;

public interface CaseExecutor {

    public void requestAuthorizations(String runId) throws Exception;

    public void checkSubscriptionAuhStatus(String runId) throws Exception;


}
