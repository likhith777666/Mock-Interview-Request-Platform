package com.example.mockinterview.repository;

import com.example.mockinterview.model.InterviewerDecline;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterviewerDeclineRepository extends JpaRepository<InterviewerDecline, Long> {
    boolean existsByRequestIdAndInterviewerId(Long requestId, Long interviewerId);
    List<InterviewerDecline> findByRequestId(Long requestId);
}
