package com.example.mockinterview.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class CreateRequestForm {

    @NotBlank
    private String candidateName;

    @NotBlank
    @Email
    private String candidateEmail;

    @NotBlank
    private String targetRole;

    @NotBlank
    private String experience;

    private String interviewRequirements;

    @NotBlank
    private String slot1;

    private String slot2;
    private String slot3;

    public String getCandidateName() { return candidateName; }
    public String getCandidateEmail() { return candidateEmail; }
    public String getTargetRole() { return targetRole; }
    public String getExperience() { return experience; }
    public String getInterviewRequirements() { return interviewRequirements; }
    public String getSlot1() { return slot1; }
    public String getSlot2() { return slot2; }
    public String getSlot3() { return slot3; }

    public void setCandidateName(String candidateName) { this.candidateName = candidateName; }
    public void setCandidateEmail(String candidateEmail) { this.candidateEmail = candidateEmail; }
    public void setTargetRole(String targetRole) { this.targetRole = targetRole; }
    public void setExperience(String experience) { this.experience = experience; }
    public void setInterviewRequirements(String interviewRequirements) { this.interviewRequirements = interviewRequirements; }
    public void setSlot1(String slot1) { this.slot1 = slot1; }
    public void setSlot2(String slot2) { this.slot2 = slot2; }
    public void setSlot3(String slot3) { this.slot3 = slot3; }
}
