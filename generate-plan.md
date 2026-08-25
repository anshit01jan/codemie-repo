# generate-plan.md

## 1. Project Goals

- **Security:** Address hardcoded credentials, unprotected endpoints, and implement best practices (secret management, CSRF, and rate-limiting).
- **Reliability:** Ensure missing or duplicate classes, critical automation or reporting code, and dependency management issues are resolved for reproducible builds.
- **Maintainability:** Standardize class naming, repository hygiene, and align README and docs with actual repo structure.
- **CI/CD Readiness:** Integrate robust testing‐Python unit tests, Java UI automation—and add CI workflows for automatic verification.
 - **Test Coverage:** Ensure key user stories (especially reset password) are covered in both unit and end-to-end tests, for both success and negative cases.

## 2. Enhancement Plan and Steps

Below is a breakdown of each enhancement by priority, with summary, files likely impacted, implementation steps, acceptance criteria, and testing approach.

---

### **P0 — High Priority**

#### 1. Add Missing `ExtentManager` Implementation ([SCRUM-113](https://anshit01jan.atlassian.net/browse/SCRUM-113))
- **Summary:** Add a singleton Extent report manager to support test reporting.
 - **Files Impacted:**  
  - `src/main/java/framework/reports/ExtentManager.java`   
  - `src/test/java/hooks/Hooks.java`  
  - `src/main/java/framework/listeners/Listeners.java`
- **Steps:**  
  1. Create `ExtentManager.java` as a singleton with thread-local ExtentTest capability and standard methods (`initReport`, `startTest`, `getTest`, `flush`).
  2. Verify correct imports/usages in Hooks and Listeners.
 - **Testing:**   
  - Run `mvn test` and ensure compilation and report generation.
 - **Acceptance Criteria:**   
  - Code compiles; test suite runs; no missing class errors.

#### 2. Remove Hardcoded Flask Secret Key ([SCRUM-114](https://anshit01jan.atlassian.net/browse/SCRUM-114))
- **Summary:** Eliminate hardcoded Flask secret; use environment variable.
 - **Files Impacted:** `app/__init__.py`, README/config docs may be updated.
- **Steps:**  
  1. Refactor app config to source `SECRET_KEY` from env (with sensible local fallback for dev/test only).
  2. Fail safely if missing, except under tests.
  3. Update docs for local development.
 - **Testing:**   
  - Unit tests; manual startup with/without env var.
 - **Acceptance Criteria:**   
  - No secrets hardcoded; failure if unset; tests all pass.


#### 3. Protect Automation Endpoints ([SCRUM-115](https://anshit01jan.atlassian.net/browse/SCRUM-115))
- **Summary:** Restrict `/__automation__/*` endpoints to test mode and protect with header/token.
 - **Files Impacted:** `app/__init__.py`, automation test configs, `app/testing.py`
- **Steps:**   
  1. Add config/env var into `AUTOMATION_MODE `.
  2. Only register endpoints when mode enabled.
  3. Require/check a secret header/token for access.
  4. Update tests to use token.
 - **Testing:**   
  - E2E: try accessing endpoints with/without token.
  - Ensure automation (Java, Python) still works.
- **Acceptance Criteria:**  
  - Endpoints off by default; protected with header if enabled.


---

### **P1 — Medium Priority**

#### 4. Add CSRF Protection ([SCRUM-116](https://anshit01jan.atlassian.net/browse/SCRUM-116))
- **Summary:** Protect all POST forms using Flask-WTF (or similar).
- **Files Impacted:** `requirements.txt`, `app/routes.py`, `app/templates/*.html`
- **Steps:**   
  1. Add `Flask-WTF` and initialize CSRF protection.
  2. Update all forms to render CSRF field.
  3. Update tests to submit CSRF token.
 - **Testing:**   
  - Submit form with and without token; verify 400.
  - All unit/UI tests pass.
 - **Acceptance Criteria:**   
  - CSRF enabled; forms protected.


#### 5. Add Rate Limiting to Auth Endpoints ([SCRUM-117](https://anshit01jan.atlassian.net/browse/SCRUM-117))
- **Summary:** Use Flask-Limiter to throttle login/forgot/reset endpoints.
- **Files Impacted:** `requirements.txt`, `app/routes.py`
- **Steps:**  
  1. Install and configure `Flask-Limiter`.
  2. Apply reasonable limits per IP to auth endpoints.
  3. Add tests to verify 429 after threshold.
 - **Testing:**   
  - Repeated requests in test; expect 429 after threshold.
 - **Acceptance Criteria:**   
  - Throttling enforced; configurable limits.



#### 6. Add GitHub Actions CI Workflow ([SCRUM-118](https://anshit01jan.atlassian.net/browse/SCRUM-118))
- **Summary:** Enable auto-checks on PRS/push.
- **Files Impacted:** `.github/workflows/ci.yml`
- **Steps:**  
  1. Add workflow for Python (unit) and Java (automation) tests.
  2. Include caching (Maven), artifact upload, and badge/docs updates if desired.
- **Testing:**  
  - Validate CI pipeline by push to a test branch.
- **Acceptance Criteria:**   
  - CI joks run and pass on clean repo state (or issues documented).

#### 7. Move Credentials Out of `env.properties` ([SCRUM-119](https://anshit01jan.atlassian.net/browse/SCRUM-119))
- **Summary:** Remove hardcoded test creds; use env/system props.
 - **Files Impacted:** `src/test/resources/env.properties`, test config reader
- ***Steps:**  
  1. Refactor to load creds from env vars or system props.
  2. Remove cleartext defaults from VCS.
  3. Update docs for local setup.
- **Testing:**   
  - Tests fail clearly if creds are unset.
- **Acceptance Criteria:**  
  - No real creds in VCS; env/system props supported.

#### 8. Register TestNG Listener in `testng.xml` ([SCRUM-120](https://anshit01jan.atlassian.net/browse/SCRUM-120))
- **Summary:** Ensure Listeners run in automation suite.
 - **Files Impacted:** `testng.xml`
- **Steps:**   
  1. Add `<listeners>` block for `framework.listeners.Listeners`.
  2. Validate logs/artifacts indicate listener execution.
- **Testing:**  
  - Run `mvn test` and confirm listener effects.
- **Acceptance Criteria:**  
  - Listener executes; no regression. 


#### 9. Expand Cucumber Reset Password Coverage ([SCRUM-121](https://anshit01jan.atlassian.net/browse/SCRUM-121))
- **Summary:** Add E2E secnarios for success, expiry, weak passwords, and post-reset login.
- **Files Impacted:**  
  - `src/test/resources/features/reset_password.feature`
  - Page objects and step definitions
- ***Steps:**  
  1. Add features for valid reset, expired reset, and weak password validation.
  2. Update StepDefs and Pages for new flows.
- **Testing:**  
  - `mvn test` ensures new and existing scenarios pass.
 - **Acceptance Criteria:**   
  - Browser coverage of real world reset flows.


#### 10. Add Reproducible Dependency Locking for Python ([SCRUM-122](https://anshit01jan.atlassian.net/browse/SCRUM-122))
- **Summary:** Pin all Python deps via a lock file.
 - **Files Impacted:** `requirements.txt`, `best-practice lock file` (e.g. `requirements.lock`)
- **Steps:**   
  1. Generate lock file via pip-tools/Poetry/uv.
  2. Document how to regenerate.
- **Testing:**  
  - Clean install from lock file; tests pass.
- **Acceptance Criteria:**  
  - Lock file exists; reproducible installs.

---

### **P2  — Low Priority**

#### 11. Rename Java Utility Classes to Standard Naming ([SCRUM-123](https://anshit01jan.atlassian.net/browse/SCRUM-123))
- **Summary:** Rename lowercase classes to conventional Java class names.
 - **Files Impacted:**  
  - `src/main/java/framework/utils/config.java`
  - `src/main/java/framework/utils/environment/env.java`
- ***Steps:**  
  1. Rename to `Config.java` and `EnvironmentConfig.java` (to agreed name).
  2. Update all imports/references.
 - **Testing:**   
  - Compile/run tests after rename.
- **Acceptance Criteria:**   
  - Class names match file names; tests pass.

#### 12. Consolidate Duplicate Logger Utilities ([SCRUM-124](https://anshit01jan.atlassian.net/browse/SCRUM-124))
- **Summary:** Remove redundant logging wrappers.
- **Files Impacted:** `Logger.java`, `LoggerFactory.java`
- **Steps:**   
  1. Analyze usages and select single approach (wrapper or direct Log4j).
  2. Refactor and remove redundant class.
 - **Testing:**   
  - Verify logs and tests pass.
 - **Acceptance Criteria:**   
  - Single logging approach; no broken references.

#### 13. Clean Generated Artifacts from Repository ([SCRUM-125](https://anshit01jan.atlassian.net/browse/SCRUM-125))
- **Summary:** Ignore/remove transient agent outputs and Playwright snapshots.
 - **Files Impacted:** `.gitignore`, `.playwright-mcp/*`, `agent-output/*`
- **Steps:**   
  1. Add gitignore entries for transient outputs.
  2. Remove stale files from VCS (as agreed).
- **Testing:**   
  - Confirm generated artifacts aren’t tracked after clean/commit.
- **Acceptance Criteria:**   
  - Only source is tracked; docs clarify intent if needed.
 

#### 14. Fix README Drift to Match Repository Structure ([SCRUM-126](https://anshit01jan.atlassian.net/browse/SCRUM-126))
- **Summary:** Update docs to reflect actual tree and run instructions.
- **Files Impacted:** `README.md`
- **Steps:**   
  1. Remove references to non-existent paths/files.
  2. Update setup/test/run/CI instructions to match repo. 
- **Testing:**   
  - Reviewer verifies docs works.
- **Acceptance Criteria:**  
  - Docs are clear, correct, and match the repo.

## 3. PR Strategy

- **Proposed:** One PR for the entire plan, covering P0, P1, and P2 backlog as a single logical package.
