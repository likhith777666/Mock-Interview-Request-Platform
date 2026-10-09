package com.example.mockinterview.controller;

import com.example.mockinterview.model.InterviewRequest;
import com.example.mockinterview.model.Interviewer;
import com.example.mockinterview.repository.InterviewerRepository;
import com.example.mockinterview.service.InterviewerMatchingService;
import com.example.mockinterview.service.InterviewRequestService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/interviewer")
public class InterviewerController {

    private final InterviewRequestService requestService;
    private final InterviewerRepository interviewerRepository;
    private final InterviewerMatchingService matchingService;

    public InterviewerController(InterviewRequestService requestService,
                                  InterviewerRepository interviewerRepository,
                                  InterviewerMatchingService matchingService) {
        this.requestService = requestService;
        this.interviewerRepository = interviewerRepository;
        this.matchingService = matchingService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("requests", requestService.allRequests());
        model.addAttribute("interviewers", interviewerRepository.findAll());
        return "interviewer-dashboard";
    }

    @PostMapping("/requests/{requestId}/accept")
    public String accept(@PathVariable Long requestId,
                          @RequestParam Long interviewerId,
                          RedirectAttributes redirectAttributes) {
        requestService.accept(requestId, interviewerId);
        redirectAttributes.addFlashAttribute("success", "Request accepted. Select a slot and meeting link to confirm.");
        return "redirect:/interviewer/dashboard";
    }

    @PostMapping("/requests/{requestId}/decline")
    public String decline(@PathVariable Long requestId,
                           @RequestParam Long interviewerId,
                           RedirectAttributes redirectAttributes) {
        requestService.decline(requestId, interviewerId);
        redirectAttributes.addFlashAttribute("success", "Request declined. Another interviewer can be selected.");
        return "redirect:/interviewer/dashboard";
    }

    @ModelAttribute("allInterviewers")
    public Iterable<Interviewer> allInterviewers() {
        return interviewerRepository.findAll();
    }
}
