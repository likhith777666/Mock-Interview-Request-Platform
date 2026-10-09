package com.example.mockinterview.model;

import jakarta.persistence.*;

@Entity
@Table(name = "interviewer_declines",
       uniqueConstraints = @UniqueConstraint(columnNames = {"request_id", "interviewer_id"}))
public class InterviewerDecline {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private InterviewRequest request;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Interviewer interviewer;

    public InterviewerDecline() {}

    public InterviewerDecline(InterviewRequest request, Interviewer interviewer) {
        this.request = request;
        this.interviewer = interviewer;
    }

    public Long getId() { return id; }
    public InterviewRequest getRequest() { return request; }
    public Interviewer getInterviewer() { return interviewer; }
}
