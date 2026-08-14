const welcomeSection =
    document.getElementById("welcomeSection");

const detailsSection =
    document.getElementById("detailsSection");

const loadingSection =
    document.getElementById("loadingSection");

const resultsSection =
    document.getElementById("resultsSection");

const emptySection =
    document.getElementById("emptySection");

const errorSection =
    document.getElementById("errorSection");

const userNameInput =
    document.getElementById("userName");

const skillInput =
    document.getElementById("skillInput");

const skillList =
    document.getElementById("skillList");

const resultSkills =
    document.getElementById("resultSkills");

const resultUserName =
    document.getElementById("resultUserName");

const nameError =
    document.getElementById("nameError");

const skillError =
    document.getElementById("skillError");

const recommendationsContainer =
    document.getElementById("recommendationsContainer");

let skills = [];


function showScreen(screen) {

    document.querySelectorAll(".screen").forEach(section => {
        section.classList.remove("active");
    });

    screen.classList.add("active");
}


// Get Started
document
    .getElementById("getStartedButton")
    .addEventListener("click", () => {

        showScreen(detailsSection);

    });


// Add skill
function addSkill() {

    const skill = skillInput.value.trim();

    if (!skill) {
        return;
    }

    const exists = skills.some(
        existing =>
            existing.toLowerCase() === skill.toLowerCase()
    );

    if (exists) {

        skillInput.value = "";

        return;
    }

    skills.push(skill);

    skillInput.value = "";

    renderSkills();
}


document
    .getElementById("addSkillButton")
    .addEventListener("click", addSkill);


// Allow Enter to add skill
skillInput.addEventListener("keydown", event => {

    if (event.key === "Enter") {

        event.preventDefault();

        addSkill();
    }

});


// Display selected skills
function renderSkills() {

    skillList.innerHTML = "";

    skills.forEach((skill, index) => {

        const tag = document.createElement("div");

        tag.className = "skill-tag";

        tag.innerHTML = `
            ${escapeHtml(skill)}
            <button
                class="skill-remove"
                onclick="removeSkill(${index})"
            >
                ×
            </button>
        `;

        skillList.appendChild(tag);
    });
}


// Remove skill
function removeSkill(index) {

    skills.splice(index, 1);

    renderSkills();
}


// Validate form
function validateForm() {

    let valid = true;

    nameError.textContent = "";
    skillError.textContent = "";

    if (!userNameInput.value.trim()) {

        nameError.textContent =
            "Please enter your name.";

        valid = false;
    }

    if (skills.length === 0) {

        skillError.textContent =
            "Please add at least one skill.";

        valid = false;
    }

    return valid;
}


// Find jobs
document
    .getElementById("findJobsButton")
    .addEventListener("click", findJobs);


async function findJobs() {

    if (!validateForm()) {
        return;
    }

    showScreen(loadingSection);

    const requestData = {

        name: userNameInput.value.trim(),

        skills: skills

    };


    try {

        const response = await fetch(
            "/api/recommendations",
            {
                method: "POST",

                headers: {
                    "Content-Type": "application/json"
                },

                body: JSON.stringify(requestData)
            }
        );


        if (!response.ok) {

            throw new Error(
                "Server returned " + response.status
            );

        }


        const recommendations =
            await response.json();


        resultUserName.textContent =
            userNameInput.value.trim();


        renderResultSkills();


        if (
            !recommendations ||
            recommendations.length === 0
        ) {

            showScreen(emptySection);

            return;
        }


        renderRecommendations(
            recommendations
        );

        showScreen(resultsSection);

    } catch (error) {

        console.error(
            "Recommendation error:",
            error
        );

        const errorText =
            document.getElementById("errorText");

        errorText.textContent =
            "We couldn't connect to the job recommendation service. Please make sure the Spring Boot application is running.";

        showScreen(errorSection);
    }
}


// Display user's skills on results page
function renderResultSkills() {

    resultSkills.innerHTML = "";

    skills.forEach(skill => {

        const tag = document.createElement("div");

        tag.className = "skill-tag";

        tag.textContent = skill;

        resultSkills.appendChild(tag);
    });
}


// Display REAL recommendations from backend
function renderRecommendations(jobs) {

    recommendationsContainer.innerHTML = "";


    jobs.forEach(job => {

        const card =
            document.createElement("div");

        card.className = "job-card";


        const percentage =
            Number(job.matchPercentage || 0);


        card.innerHTML = `

            <div class="job-top">

                <div>

                    <div class="job-title">
                        ${escapeHtml(
                            job.job || "Job"
                        )}
                    </div>

                    <div class="job-company">
                        ${escapeHtml(
                            job.company || "Company"
                        )}
                    </div>

                </div>

                <div class="match-badge">
                    ${percentage}%
                </div>

            </div>


            <div class="job-info">

                📍 ${escapeHtml(
                    job.location || "Location not specified"
                )}

            </div>


            <div class="job-info">

                ✓ ${job.matchingSkills}
                of
                ${job.totalRequiredSkills}
                required skills matched

            </div>

        `;


        recommendationsContainer.appendChild(card);

    });
}


// Change skills
document
    .getElementById("changeSkillsButton")
    .addEventListener("click", () => {

        showScreen(detailsSection);

    });


// Try again
const tryAgainButton =
    document.getElementById("tryAgainButton");

if (tryAgainButton) {

    tryAgainButton.addEventListener(
        "click",
        () => {

            showScreen(detailsSection);

        }
    );
}


// Retry after error
const retryButton =
    document.getElementById("retryButton");

if (retryButton) {

    retryButton.addEventListener(
        "click",
        () => {

            showScreen(detailsSection);

        }
    );
}


// Prevent HTML injection
function escapeHtml(value) {

    const div =
        document.createElement("div");

    div.textContent = value;

    return div.innerHTML;
}