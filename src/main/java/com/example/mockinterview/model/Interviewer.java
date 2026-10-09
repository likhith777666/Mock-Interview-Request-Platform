package com.example.mockinterview.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "interviewers")
public class Interviewer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    private String role;
    private String experienceYears;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "interviewer_skills", joinColumns = @JoinColumn(name = "interviewer_id"))
    @Column(name = "skill")
    private List<String> skills = new ArrayList<>();

    public Interviewer() {}

    public Interviewer(String name, String email, String role, String experienceYears, List<String> skills) {
        this.name = name;
        this.email = email;
        this.role = role;
        this.experienceYears = experienceYears;
        this.skills = skills;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public String getExperienceYears() { return experienceYears; }
    public List<String> getSkills() { return skills; }

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setRole(String role) { this.role = role; }
    public void setExperienceYears(String experienceYears) { this.experienceYears = experienceYears; }
    public void setSkills(List<String> skills) { this.skills = skills; }
}
