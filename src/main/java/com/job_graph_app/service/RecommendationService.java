package com.job_graph_app.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.job_graph_app.repository.JobGraphRepository;

@Service
public class RecommendationService {

    private final JobGraphRepository repository;

    public RecommendationService(JobGraphRepository repository) {
        this.repository = repository;
    }

    public List<Map<String, Object>> getRecommendations(String userId) {
        return repository.recommendJobs(userId);
    }

    public List<Map<String, Object>> getRecommendationsBySkills(
            List<String> skills) {

        return repository.recommendJobsBySkills(skills);
    }
}