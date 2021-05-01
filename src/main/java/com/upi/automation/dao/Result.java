package com.upi.automation.dao;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity(name = "ReportData")
@NoArgsConstructor
public class Result {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String id;
    private String frequency;
    private Date timeStamp;
    private String bank;
    private String vpa;
    private String paymentId;
    private String mandateCreationStatus;
    private String firstDebitStatus;
    private String customerId;
    private String tokenId;
    private String orderId;
    private String umn;
    private String psp;
    private String comments;

    private String runId;
}
