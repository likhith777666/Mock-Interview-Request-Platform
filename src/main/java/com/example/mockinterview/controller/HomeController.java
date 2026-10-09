package com.example.mockinterview.controller;

import com.example.mockinterview.service.InterviewRequestService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final InterviewRequestService requestService;

    public HomeController(InterviewRequestService requestService) {
        this.requestService = requestService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("requests", requestService.allRequests());
        return "index";
    }
}
