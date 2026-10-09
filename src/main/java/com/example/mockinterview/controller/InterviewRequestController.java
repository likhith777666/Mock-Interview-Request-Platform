package com.example.mockinterview.controller;

import com.example.mockinterview.dto.ConfirmRequestForm;
import com.example.mockinterview.dto.CreateRequestForm;
import com.example.mockinterview.model.InterviewRequest;
import com.example.mockinterview.service.InterviewerMatchingService;
import com.example.mockinterview.service.InterviewRequestService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/requests")
public class InterviewRequestController {

    private final InterviewRequestService requestService;
    private final InterviewerMatchingService matchingService;

    public InterviewRequestController(InterviewRequestService requestService,
                                      InterviewerMatchingService matchingService) {
        this.requestService = requestService;
        this.matchingService = matchingService;
    }

    @GetMapping("/new")
    public String newRequest(Model model) {
        model.addAttribute("form", new CreateRequestForm());
        return "request-form";
    }

    @PostMapping
    public String createRequest(@Valid @ModelAttribute("form") CreateRequestForm form,
                                BindingResult bindingResult,
                                @RequestParam("resume") MultipartFile resume,
                                RedirectAttributes redirectAttributes) {
        if (resume == null || resume.isEmpty()) {
            bindingResult.reject("resume", "Resume is required.");
        }

        if (bindingResult.hasErrors()) {
            return "request-form";
        }

        try {
            InterviewRequest request = requestService.create(form, resume);
            redirectAttributes.addFlashAttribute("success",
                    "Request submitted successfully. Request ID: " + request.getRequestId());
            return "redirect:/requests/" + request.getRequestId();
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return "redirect:/requests/new";
        }
    }

    @GetMapping("/{requestId}")
    public String details(@PathVariable String requestId, Model model) {
        InterviewRequest request = requestService.getByRequestId(requestId);
        model.addAttribute("request", request);
        model.addAttribute("suitableInterviewers", matchingService.findSuitableInterviewers(request));
        return "request-details";
    }

    @GetMapping("/{requestId}/select-interviewer")
    public String selectInterviewerPage(@PathVariable String requestId, Model model) {
        InterviewRequest request = requestService.getByRequestId(requestId);
        model.addAttribute("request", request);
        model.addAttribute("interviewers", matchingService.findSuitableInterviewers(request));
        return "select-interviewer";
    }

    @PostMapping("/{requestId}/select-interviewer")
    public String selectInterviewer(@PathVariable String requestId,
                                    @RequestParam Long interviewerId,
                                    RedirectAttributes redirectAttributes) {
        InterviewRequest request = requestService.getByRequestId(requestId);
        requestService.selectInterviewer(request.getId(), interviewerId);
        redirectAttributes.addFlashAttribute("success", "Interviewer selected.");
        return "redirect:/requests/" + requestId;
    }

    @GetMapping("/{requestId}/confirm")
    public String confirmPage(@PathVariable String requestId, Model model) {
        InterviewRequest request = requestService.getByRequestId(requestId);
        model.addAttribute("request", request);
        model.addAttribute("form", new ConfirmRequestForm());
        return "confirm-request";
    }

    @PostMapping("/{requestId}/confirm")
    public String confirm(@PathVariable String requestId,
                          @Valid @ModelAttribute("form") ConfirmRequestForm form,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes,
                          Model model) {
        if (bindingResult.hasErrors()) {
            InterviewRequest request = requestService.getByRequestId(requestId);
            model.addAttribute("request", request);
            return "confirm-request";
        }

        try {
            InterviewRequest request = requestService.getByRequestId(requestId);
            requestService.confirm(request.getId(), form);
            redirectAttributes.addFlashAttribute("success", "Interview confirmed.");
            return "redirect:/requests/" + requestId;
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return "redirect:/requests/" + requestId + "/confirm";
        }
    }

    @PostMapping("/{requestId}/complete")
    public String complete(@PathVariable String requestId, RedirectAttributes redirectAttributes) {
        InterviewRequest request = requestService.getByRequestId(requestId);
        requestService.complete(request.getId());
        redirectAttributes.addFlashAttribute("success", "Interview marked completed.");
        return "redirect:/requests/" + requestId;
    }
}
