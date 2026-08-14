package com.job_graph_app.repository;

import java.util.List;
import java.util.Map;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.springframework.stereotype.Repository;

@Repository
public class JobGraphRepository {

    private final Driver driver;

    public JobGraphRepository(Driver driver) {
        this.driver = driver;
    }
public void addRequiredSkill(String jobId, String skillId) {

    String cypher = """
        MATCH (j:Job {id: $jobId})
        MATCH (s:Skill {id: $skillId})
        MERGE (j)-[:REQUIRES_SKILL]->(s)
        """;

    try (Session session = driver.session()) {
        session.run(
            cypher,
            java.util.Map.of(
                "jobId", jobId,
                "skillId", skillId
            )
        ).consume();
    }
}
    public void createSkill(String id, String name, String category) {

        String cypher = """
            MERGE (s:Skill {id: $id})
            SET s.name = $name,
                s.category = $category
            """;

        try (Session session = driver.session()) {
            session.run(
                cypher,
                java.util.Map.of(
                    "id", id,
                    "name", name,
                    "category", category
                )
            ).consume();
        }
    }
    public void createJob(
        String id,
        String title,
        String description,
        String experienceLevel) {

    String cypher = """
        MERGE (j:Job {id: $id})
        SET j.title = $title,
            j.description = $description,
            j.experienceLevel = $experienceLevel
        """;

    try (Session session = driver.session()) {
        session.run(
            cypher,
            java.util.Map.of(
                "id", id,
                "title", title,
                "description", description,
                "experienceLevel", experienceLevel
            )
        ).consume();
    }
}
public void createCompany(
        String id,
        String name) {

    String cypher = """
        MERGE (c:Company {id: $id})
        SET c.name = $name
        """;

    try (Session session = driver.session()) {
        session.run(
            cypher,
            java.util.Map.of(
                "id", id,
                "name", name
            )
        ).consume();
    }
}
public void createLocation(
        String id,
        String city,
        String country) {

    String cypher = """
        MERGE (l:Location {id: $id})
        SET l.city = $city,
            l.country = $country
        """;

    try (Session session = driver.session()) {
        session.run(
            cypher,
            java.util.Map.of(
                "id", id,
                "city", city,
                "country", country
            )
        ).consume();
    }
}
public void addJobCompany(String jobId, String companyId) {

    String cypher = """
        MATCH (j:Job {id: $jobId})
        MATCH (c:Company {id: $companyId})
        MERGE (j)-[:OFFERED_BY]->(c)
        """;

    try (Session session = driver.session()) {
        session.run(
            cypher,
            java.util.Map.of(
                "jobId", jobId,
                "companyId", companyId
            )
        ).consume();
    }
}
public void addJobLocation(String jobId, String locationId) {

    String cypher = """
        MATCH (j:Job {id: $jobId})
        MATCH (l:Location {id: $locationId})
        MERGE (j)-[:LOCATED_IN]->(l)
        """;

    try (Session session = driver.session()) {
        session.run(
            cypher,
            java.util.Map.of(
                "jobId", jobId,
                "locationId", locationId
            )
        ).consume();
    }
}
public void createUser(
        String id,
        String name) {

    String cypher = """
        MERGE (u:User {id: $id})
        SET u.name = $name
        """;

    try (Session session = driver.session()) {
        session.run(
            cypher,
            java.util.Map.of(
                "id", id,
                "name", name
            )
        ).consume();
    }
}
public void addUserSkill(String userId, String skillId) {

    String cypher = """
        MATCH (u:User {id: $userId})
        MATCH (s:Skill {id: $skillId})
        MERGE (u)-[:HAS_SKILL]->(s)
        """;

    try (Session session = driver.session()) {
        session.run(
            cypher,
            java.util.Map.of(
                "userId", userId,
                "skillId", skillId
            )
        ).consume();
    }
}
public List<Map<String, Object>> recommendJobs(String userId) {

    String cypher = """
        MATCH (u:User {id: $userId})-[:HAS_SKILL]->(s:Skill)
              <-[:REQUIRES_SKILL]-(j:Job)

        WITH j, count(DISTINCT s) AS matchingSkills

        MATCH (j)-[:REQUIRES_SKILL]->(requiredSkill:Skill)

        WITH j,
             matchingSkills,
             count(DISTINCT requiredSkill) AS totalRequiredSkills

        OPTIONAL MATCH (j)-[:OFFERED_BY]->(c:Company)
        OPTIONAL MATCH (j)-[:LOCATED_IN]->(l:Location)

        RETURN
            j.title AS job,
            c.name AS company,
            l.city AS location,
            matchingSkills,
            totalRequiredSkills,
            round(
                100.0 * matchingSkills / totalRequiredSkills
            ) AS matchPercentage

        ORDER BY matchPercentage DESC
        """;

    try (Session session = driver.session()) {

        return session.run(
            cypher,
            Map.of("userId", userId)
        ).list(record -> Map.of(
            "job", record.get("job").asString(),
            "company", record.get("company").asString(),
            "location", record.get("location").asString(),
            "matchingSkills", record.get("matchingSkills").asInt(),
            "totalRequiredSkills", record.get("totalRequiredSkills").asInt(),
            "matchPercentage", record.get("matchPercentage").asDouble()
        ));
    }
}
public List<Map<String, Object>> recommendJobsBySkills(List<String> skills) {

    String cypher = """
        UNWIND $skills AS skillName

        MATCH (s:Skill)
        WHERE toLower(trim(s.name)) = toLower(trim(skillName))

        MATCH (j:Job)-[:REQUIRES_SKILL]->(s)

        WITH j, count(DISTINCT s) AS matchingSkills

        MATCH (j)-[:REQUIRES_SKILL]->(requiredSkill:Skill)

        WITH j,
             matchingSkills,
             count(DISTINCT requiredSkill) AS totalRequiredSkills

        OPTIONAL MATCH (j)-[:OFFERED_BY]->(c:Company)
        OPTIONAL MATCH (j)-[:LOCATED_IN]->(l:Location)

        RETURN
            j.title AS job,
            c.name AS company,
            l.city AS location,
            matchingSkills,
            totalRequiredSkills,
            round(
                100.0 * matchingSkills / totalRequiredSkills
            ) AS matchPercentage

        ORDER BY matchPercentage DESC
        """;

    try (Session session = driver.session()) {

        return session.run(
            cypher,
            Map.of("skills", skills)
        ).list(record -> Map.of(
            "job", record.get("job").asString(),
            "company", record.get("company").asString(""),
            "location", record.get("location").asString(""),
            "matchingSkills", record.get("matchingSkills").asInt(),
            "totalRequiredSkills", record.get("totalRequiredSkills").asInt(),
            "matchPercentage", record.get("matchPercentage").asDouble()
        ));
    }
}
}