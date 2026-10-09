package com.example.mockinterview.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "interview_requests")
public class InterviewRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String requestId;

    @Column(nullable = false)
    private String candidateName;

    @Column(nullable = false)
    private String candidateEmail;

    @Column(nullable = false)
    private String targetRole;

    @Column(nullable = false)
    private String experience;

    @Column(length = 3000)
    private String interviewRequirements;

    @Column(nullable = false)
    private String resumeFileName;

    @Column(nullable = false)
    private String resumeStoredPath;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "preferred_slots", joinColumns = @JoinColumn(name = "request_id"))
    @Column(name = "slot")
    private List<LocalDateTime> preferredSlots = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status = RequestStatus.PENDING;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "interviewer_id")
    private Interviewer interviewer;

    private LocalDateTime confirmedSlot;

    @Column(length = 1000)
    private String meetingLink;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public InterviewRequest() {}

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getRequestId() { return requestId; }
    public String getCandidateName() { return candidateName; }
    public String getCandidateEmail() { return candidateEmail; }
    public String getTargetRole() { return targetRole; }
    public String getExperience() { return experience; }
    public String getInterviewRequirements() { return interviewRequirements; }
    public String getResumeFileName() { return resumeFileName; }
    public String getResumeStoredPath() { return resumeStoredPath; }
    public List<LocalDateTime> getPreferredSlots() { return preferredSlots; }
    public RequestStatus getStatus() { return status; }
    public Interviewer getInterviewer() { return interviewer; }
    public LocalDateTime getConfirmedSlot() { return confirmedSlot; }
    public String getMeetingLink() { return meetingLink; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public void setId(Long id) { this.id = id; }
    public void setRequestId(String requestId) { this.requestId = requestId; }
    public void setCandidateName(String candidateName) { this.candidateName = candidateName; }
    public void setCandidateEmail(String candidateEmail) { this.candidateEmail = candidateEmail; }
    public void setTargetRole(String targetRole) { this.targetRole = targetRole; }
    public void setExperience(String experience) { this.experience = experience; }
    public void setInterviewRequirements(String interviewRequirements) { this.interviewRequirements = interviewRequirements; }
    public void setResumeFileName(String resumeFileName) { this.resumeFileName = resumeFileName; }
    public void setResumeStoredPath(String resumeStoredPath) { this.resumeStoredPath = resumeStoredPath; }
    public void setPreferredSlots(List<LocalDateTime> preferredSlots) { this.preferredSlots = preferredSlots; }
    public void setStatus(RequestStatus status) { this.status = status; }
    public void setInterviewer(Interviewer interviewer) { this.interviewer = interviewer; }
    public void setConfirmedSlot(LocalDateTime confirmedSlot) { this.confirmedSlot = confirmedSlot; }
    public void setMeetingLink(String meetingLink) { this.meetingLink = meetingLink; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
