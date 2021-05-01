package com.upi.automation.api;

import com.upi.automation.service.CaseExecutor;
import com.upi.automation.service.UpiRecurringAutomation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

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
    public ResponseEntity authorize(@RequestParam("runid") String runid) {
//        this.upiRecurringAutomation.createAuthorization(vpa, this.customerId,null);
        caseExecutor.requestAuthorizations(runid);
        return ResponseEntity.ok("up and running");

    }

    //
    @PostMapping("/update-auth-status")
    public ResponseEntity updateAuthStatus(@RequestParam("runid") String runid) throws IOException {
        caseExecutor.checkSubscriptionAuhStatus(runid);
        return ResponseEntity.ok("up and running");

    }

}
