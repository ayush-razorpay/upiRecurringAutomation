package com.upi.automation.dao.repository;

import com.upi.automation.dao.Result;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestResultRepository extends JpaRepository<Result, String> {
}
