package com.upi.automation.dao;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Entity(name = "TestCase")
@NoArgsConstructor
public class TestCase {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String id;

    private String bank;
    private String vpa;
    private String psp;
    @Enumerated(EnumType.STRING)
    private SubscriptionFrequency frequency;
    private String isEnabled;


    public enum SubscriptionFrequency {
        daily,
        monthly
    }


}
