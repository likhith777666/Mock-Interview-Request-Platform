package com.example.mockinterview.config;

import com.example.mockinterview.model.Interviewer;
import com.example.mockinterview.repository.InterviewerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seedInterviewers(InterviewerRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                repository.saveAll(List.of(
                    new Interviewer("Anil Kumar", "anil@example.com", "DevOps Engineer", "6+ years",
                            List.of("AWS", "Kubernetes", "Terraform", "Jenkins", "Docker")),
                    new Interviewer("Priya Sharma", "priya@example.com", "Software Engineer", "5+ years",
                            List.of("Java", "Spring Boot", "REST API", "Microservices", "SQL")),
                    new Interviewer("Rahul Reddy", "rahul@example.com", "Cloud Engineer", "7+ years",
                            List.of("AWS", "Azure", "Linux", "Terraform", "Networking")),
                    new Interviewer("Sneha Rao", "sneha@example.com", "SRE Engineer", "6+ years",
                            List.of("Kubernetes", "AWS", "Prometheus", "Grafana", "Incident Response"))
                ));
            }
        };
    }
}
