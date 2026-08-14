package com.job_graph_app.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.job_graph_app.service.RecommendationService;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(
            RecommendationService recommendationService) {

        this.recommendationService = recommendationService;
    }

    @GetMapping("/{userId}")
    public List<Map<String, Object>> getRecommendations(
            @PathVariable String userId) {

        return recommendationService.getRecommendations(userId);
    }

    @PostMapping
    public List<Map<String, Object>> getRecommendationsBySkills(
            @RequestBody RecommendationRequest request) {

        return recommendationService.getRecommendationsBySkills(
                request.getSkills()
        );
    }

    public static class RecommendationRequest {

        private String name;
        private List<String> skills;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<String> getSkills() {
            return skills;
        }

        public void setSkills(List<String> skills) {
            this.skills = skills;
        }
    }
}
