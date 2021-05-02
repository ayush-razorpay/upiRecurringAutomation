package com.upi.automation.api;

import com.upi.automation.dao.TestCase;
import com.upi.automation.service.CaseExecutor;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class upiRecurringAutomationListner {


    @Autowired
    CaseExecutor caseExecutor;

    String customerId = "cust_H56On0j1H53Ioj";


    @PostMapping("/execute-all-cases")
    public ResponseEntity authorize(@RequestParam("runid") String runid) throws Exception {
       val list = caseExecutor.requestAuthorizations(runid);
        val toReturnMap =   createResponseMap(list," test cases executed for");
        return ResponseEntity.ok(toReturnMap);

    }




    @PostMapping("/update-auth-status")
    public ResponseEntity updateAuthStatus(@RequestParam("runid") String runid) throws Exception {
        val list = caseExecutor.checkSubscriptionAuhStatus(runid);
        val toReturnMap =  createResponseMap(list,"authorization status updated for ");
        return ResponseEntity.ok(toReturnMap);

    }

    @PostMapping("/create-subsequent-debit")
    public ResponseEntity createSubsequentDebit(@RequestParam("runid") String runid,
                                                @RequestParam("frequency") TestCase.SubscriptionFrequency frequency) throws Exception {
        val list =  caseExecutor.createSubsequentDebits(runid, frequency);
        val toReturnMap =   createResponseMap(list," Subsequent Debit request created for");
        return ResponseEntity.ok(toReturnMap);

    }

    private static Map createResponseMap(List toReturn,String key) {
        Map<String, List> toReturnMap = new HashMap<>();
        toReturnMap.put(key, toReturn);
        return toReturnMap;
    }

}
