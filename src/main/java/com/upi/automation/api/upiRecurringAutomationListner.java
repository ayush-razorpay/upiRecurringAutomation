package com.upi.automation.api;

import com.upi.automation.dao.TestCase;
import com.upi.automation.service.CaseExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class upiRecurringAutomationListner {


    @Autowired
    CaseExecutor caseExecutor;

    String customerId = "cust_H56On0j1H53Ioj";


    @PostMapping("/execute-all-cases")
    public ResponseEntity authorize(@RequestParam("runid") String runid) throws Exception {
        caseExecutor.requestAuthorizations(runid);
        return ResponseEntity.ok("Request is complete");

    }

    @PostMapping("/update-auth-status")
    public ResponseEntity updateAuthStatus(@RequestParam("runid") String runid) throws Exception {
        caseExecutor.checkSubscriptionAuhStatus(runid);
        return ResponseEntity.ok("Request is complete");

    }

    @PostMapping("/create-subsequent-debit")
    public ResponseEntity createSubsequentDebit(@RequestParam("runid") String runid,
                                                @RequestParam("frequency") TestCase.SubscriptionFrequency frequency) throws Exception {
        caseExecutor.createSubsequentDebits(runid, frequency);
        return ResponseEntity.ok("Request is complete");

    }

}
