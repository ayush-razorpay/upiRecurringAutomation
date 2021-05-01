package com.upi.automation.dao.repository;

import com.upi.automation.dao.TestCase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestCaseRepository extends JpaRepository<TestCase, String> {


}
