package com.example.mockinterview.dto;

import jakarta.validation.constraints.NotBlank;

public class ConfirmRequestForm {

    @NotBlank
    private String confirmedSlot;

    @NotBlank
    private String meetingLink;

    public String getConfirmedSlot() { return confirmedSlot; }
    public String getMeetingLink() { return meetingLink; }

    public void setConfirmedSlot(String confirmedSlot) { this.confirmedSlot = confirmedSlot; }
    public void setMeetingLink(String meetingLink) { this.meetingLink = meetingLink; }
}
