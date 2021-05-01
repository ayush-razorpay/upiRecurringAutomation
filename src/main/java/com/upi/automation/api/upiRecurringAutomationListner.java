package com.upi.automation.api;

import com.upi.automation.service.CaseExecutor;
import com.upi.automation.service.UpiRecurringAutomation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class upiRecurringAutomationListner {

    @Autowired
    UpiRecurringAutomation upiRecurringAutomation;

    @Autowired
    CaseExecutor caseExecutor;

    String customerId = "cust_H56On0j1H53Ioj";

    @GetMapping("/health")
    public ResponseEntity greeting() {
        return ResponseEntity.ok("up and running");
    }

    @PostMapping("/authorize")
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
                                                @RequestParam("frequency") String frequency) throws Exception {
        caseExecutor.checkSubscriptionAuhStatus(runid);
        return ResponseEntity.ok("Request is complete");

    }

}
