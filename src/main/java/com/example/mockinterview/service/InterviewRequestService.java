package com.example.mockinterview.service;

import com.example.mockinterview.dto.ConfirmRequestForm;
import com.example.mockinterview.dto.CreateRequestForm;
import com.example.mockinterview.model.*;
import com.example.mockinterview.repository.InterviewerDeclineRepository;
import com.example.mockinterview.repository.InterviewerRepository;
import com.example.mockinterview.repository.InterviewRequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class InterviewRequestService {

    private static final DateTimeFormatter SLOT_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    private final InterviewRequestRepository requestRepository;
    private final InterviewerRepository interviewerRepository;
    private final InterviewerDeclineRepository declineRepository;
    private final FileStorageService fileStorageService;

    public InterviewRequestService(InterviewRequestRepository requestRepository,
                                   InterviewerRepository interviewerRepository,
                                   InterviewerDeclineRepository declineRepository,
                                   FileStorageService fileStorageService) {
        this.requestRepository = requestRepository;
        this.interviewerRepository = interviewerRepository;
        this.declineRepository = declineRepository;
        this.fileStorageService = fileStorageService;
    }

    @Transactional
    public InterviewRequest create(CreateRequestForm form, MultipartFile resume) throws Exception {
        FileStorageService.StoredFile stored = fileStorageService.store(resume);

        InterviewRequest request = new InterviewRequest();
        request.setRequestId("MIR-" + System.currentTimeMillis());
        request.setCandidateName(form.getCandidateName());
        request.setCandidateEmail(form.getCandidateEmail());
        request.setTargetRole(form.getTargetRole());
        request.setExperience(form.getExperience());
        request.setInterviewRequirements(form.getInterviewRequirements());
        request.setResumeFileName(stored.originalName());
        request.setResumeStoredPath(stored.storedPath());
        request.setStatus(RequestStatus.PENDING);

        List<LocalDateTime> slots = new ArrayList<>();
        slots.add(parseSlot(form.getSlot1()));
        addIfPresent(slots, form.getSlot2());
        addIfPresent(slots, form.getSlot3());
        request.setPreferredSlots(slots);

        return requestRepository.save(request);
    }

    private LocalDateTime parseSlot(String value) {
        return LocalDateTime.parse(value, SLOT_FORMAT);
    }

    private void addIfPresent(List<LocalDateTime> slots, String value) {
        if (value != null && !value.isBlank()) {
            slots.add(parseSlot(value));
        }
    }

    public List<InterviewRequest> allRequests() {
        return requestRepository.findAll();
    }

    public InterviewRequest getByRequestId(String requestId) {
        return requestRepository.findByRequestId(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found: " + requestId));
    }

    public InterviewRequest getById(Long id) {
        return requestRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Request not found: " + id));
    }

    @Transactional
    public void accept(Long requestId, Long interviewerId) {
        InterviewRequest request = getById(requestId);
        Interviewer interviewer = interviewerRepository.findById(interviewerId)
                .orElseThrow(() -> new IllegalArgumentException("Interviewer not found"));

        request.setInterviewer(interviewer);
        request.setStatus(RequestStatus.PENDING);
        requestRepository.save(request);
    }

    @Transactional
    public void decline(Long requestId, Long interviewerId) {
        InterviewRequest request = getById(requestId);
        Interviewer interviewer = interviewerRepository.findById(interviewerId)
                .orElseThrow(() -> new IllegalArgumentException("Interviewer not found"));

        if (!declineRepository.existsByRequestIdAndInterviewerId(requestId, interviewerId)) {
            declineRepository.save(new InterviewerDecline(request, interviewer));
        }

        if (request.getInterviewer() != null && request.getInterviewer().getId().equals(interviewerId)) {
            request.setInterviewer(null);
        }

        request.setStatus(RequestStatus.INTERVIEWER_DECLINED);
        requestRepository.save(request);
    }

    @Transactional
    public void selectInterviewer(Long requestId, Long interviewerId) {
        InterviewRequest request = getById(requestId);
        Interviewer interviewer = interviewerRepository.findById(interviewerId)
                .orElseThrow(() -> new IllegalArgumentException("Interviewer not found"));

        if (declineRepository.existsByRequestIdAndInterviewerId(requestId, interviewerId)) {
            throw new IllegalArgumentException("This interviewer is not available for this request.");
        }

        request.setInterviewer(interviewer);
        request.setStatus(RequestStatus.PENDING);
        requestRepository.save(request);
    }

    @Transactional
    public void confirm(Long requestId, ConfirmRequestForm form) {
        InterviewRequest request = getById(requestId);

        LocalDateTime slot = parseSlot(form.getConfirmedSlot());
        if (!request.getPreferredSlots().contains(slot)) {
            throw new IllegalArgumentException("Confirmed slot must be one of the candidate's preferred slots.");
        }

        if (request.getInterviewer() == null) {
            throw new IllegalArgumentException("Select an interviewer before confirming.");
        }

        request.setConfirmedSlot(slot);
        request.setMeetingLink(form.getMeetingLink());
        request.setStatus(RequestStatus.CONFIRMED);
        requestRepository.save(request);
    }

    @Transactional
    public void complete(Long requestId) {
        InterviewRequest request = getById(requestId);
        request.setStatus(RequestStatus.COMPLETED);
        requestRepository.save(request);
    }
}
