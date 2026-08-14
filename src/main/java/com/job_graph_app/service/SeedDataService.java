package com.job_graph_app.service;

import com.job_graph_app.repository.JobGraphRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class SeedDataService implements CommandLineRunner {

    private final JobGraphRepository repository;

    public SeedDataService(JobGraphRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {

        repository.createSkill(
            "skill-001",
            "Java",
            "Programming"
        );

        repository.createSkill(
            "skill-002",
            "Spring Boot",
            "Framework"
        );

        repository.createSkill(
            "skill-003",
            "SQL",
            "Database"
        );

        repository.createSkill(
            "skill-004",
            "Docker",
            "DevOps"
        );

        repository.createSkill(
            "skill-005",
            "AWS",
            "Cloud"
        );
        repository.createJob(
    "job-001",
    "Java Backend Developer",
    "Develop backend applications using Java and Spring Boot.",
    "Mid Level"
);

repository.createJob(
    "job-002",
    "Full Stack Developer",
    "Build web applications using Java, Spring Boot and frontend technologies.",
    "Mid Level"
);

repository.createJob(
    "job-003",
    "DevOps Engineer",
    "Manage cloud infrastructure, deployment and containerized applications.",
    "Mid Level"
);
repository.addRequiredSkill("job-001", "skill-001");
repository.addRequiredSkill("job-001", "skill-002");
repository.addRequiredSkill("job-001", "skill-003");
repository.addRequiredSkill("job-001", "skill-004");
repository.addRequiredSkill("job-002", "skill-001");
repository.addRequiredSkill("job-002", "skill-002");
repository.addRequiredSkill("job-002", "skill-003");
repository.addRequiredSkill("job-003", "skill-004");
repository.addRequiredSkill("job-003", "skill-005");
repository.createCompany(
    "company-001",
    "Tech Solutions"
);

repository.createCompany(
    "company-002",
    "Cloud Systems"
);
repository.createLocation(
    "location-001",
    "Bangalore",
    "India"
);

repository.createLocation(
    "location-002",
    "Hyderabad",
    "India"
);
repository.addJobCompany("job-001", "company-001");
repository.addJobCompany("job-002", "company-001");
repository.addJobCompany("job-003", "company-002");

repository.addJobLocation("job-001", "location-001");
repository.addJobLocation("job-002", "location-002");
repository.addJobLocation("job-003", "location-001");
repository.createUser(
    "user-001",
    "Neha"
);

repository.addUserSkill("user-001", "skill-001");
repository.addUserSkill("user-001", "skill-002");
repository.addUserSkill("user-001", "skill-003");
        System.out.println("=================================");
        System.out.println("Skill seed data created!");
        System.out.println("=================================");
    }
}
