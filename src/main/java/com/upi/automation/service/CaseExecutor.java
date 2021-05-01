package com.upi.automation.service;

import java.io.IOException;

public interface CaseExecutor {

    public void requestAuthorizations(String runId);

    public void checkSubscriptionAuhStatus(String runId) throws IOException;
}
