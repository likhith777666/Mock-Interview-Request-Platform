package com.example.mockinterview.service;

import com.example.mockinterview.model.Interviewer;
import com.example.mockinterview.model.InterviewerDecline;
import com.example.mockinterview.model.InterviewRequest;
import com.example.mockinterview.repository.InterviewerDeclineRepository;
import com.example.mockinterview.repository.InterviewerRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class InterviewerMatchingService {

    private final InterviewerRepository interviewerRepository;
    private final InterviewerDeclineRepository declineRepository;

    public InterviewerMatchingService(InterviewerRepository interviewerRepository,
                                       InterviewerDeclineRepository declineRepository) {
        this.interviewerRepository = interviewerRepository;
        this.declineRepository = declineRepository;
    }

    public List<Interviewer> findSuitableInterviewers(InterviewRequest request) {
        Set<Long> declinedIds = declineRepository.findByRequestId(request.getId())
                .stream()
                .map(d -> d.getInterviewer().getId())
                .collect(Collectors.toSet());

        String targetRole = request.getTargetRole().toLowerCase(Locale.ROOT);
        String requirementText = Optional.ofNullable(request.getInterviewRequirements()).orElse("").toLowerCase(Locale.ROOT);

        return interviewerRepository.findAll().stream()
                .filter(i -> !declinedIds.contains(i.getId()))
                .sorted(Comparator.comparingInt((Interviewer i) -> score(i, targetRole, requirementText)).reversed())
                .toList();
    }

    private int score(Interviewer interviewer, String targetRole, String requirementText) {
        int score = 0;

        if (interviewer.getRole() != null &&
                (targetRole.contains(interviewer.getRole().toLowerCase(Locale.ROOT))
                        || interviewer.getRole().toLowerCase(Locale.ROOT).contains(targetRole))) {
            score += 10;
        }

        for (String skill : interviewer.getSkills()) {
            String s = skill.toLowerCase(Locale.ROOT);
            if (targetRole.contains(s) || requirementText.contains(s)) {
                score += 2;
            }
        }
        return score;
    }
}
