package com.example.mockinterview.repository;

import com.example.mockinterview.model.InterviewRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InterviewRequestRepository extends JpaRepository<InterviewRequest, Long> {
    Optional<InterviewRequest> findByRequestId(String requestId);
}
