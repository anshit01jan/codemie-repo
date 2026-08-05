# Codemie Project - Enhancement Implementation Plan

This guide outlines a step-by-step implementation plan for all proposed P0, P1, P2 improvements as specified in the backlog.

Project: https://github.com/anshit01jan/codemie-repo
Target branch: codemieplan

Input Verified: All identified enhancements, repo structure initially limited.

==============================================

# GMS.
`- P0 Risc - High Priority
 - P1' - Medium
 - P2 - Low 

# Analysis and Goals 
Seek to address Build/report failure, security issues, config exposures, ai drift, user bhisto, accessibility, audit this all to this plan.


# Plan Summary  
Evalinate Priority tier, sumarize update all latest security, flow/bug changes. 

## PL: Photest Key Task

### 1. Add missing ExtentManager implementation
- Analysis: Hooks.java, Listeners.java.
- Create ExtentManager.java with Singleton ExtentReports, ExtentSparkReporter, thread-local ExtentTest.
  Steps: 
  1. Implements API for Singleton initialization and flush.
  2. Inleede Report test hooks.
  Test: Build/test switches reporting operation via hooks.
  Acceptance: Demonstrate by report generation.

### 2. Externalize Flask SECRET_KEY 
- Analysis: app/__init__.py

  Steps:
  - Move SECRE[T_KEY to environment/config
  - Fail fast if unset
  - Test: Security in test/prod mode.
  Acceptance: Secret key not deployed with code or version.

### 3. Add CSRF protection
- Analysis: Post flow auth forms, flask /R templates.
  Steps:
  - Component NO2PathCode routes for protection
  - Tem queries to add	  
  Acceptance: Flask JNk Web form complete. 

### 4. Gate automation-only endpoints
- Analysis: app/__init__.py

  - Set AFUTOMATION_MODE to False by default
  - Only register endpoint in automation/test mode
  - Optional: require allocal/test token for access
  Test: Action acounts/state only when test mode enabled
  Acceptaniú: State change and control packing.


## P1 (Medium) Tasks

### 5. Add CI PIline
- Analysis: .github/workflows
  - Add GitHub Action validation
 - Check out merge reporting status for

  Test: unittest, mnv_test
  Operational aretifact{ publish status and validationl}

### 6. Expand Java automation coverage
- Analysis: agent-out/test_cases.md
- Automate valid action reset/API flows
test: Token reset, expired/token
project: lockout

### 7. Add rate limiting
- Middleware auth routes
 - Test: brute force and abluse
Acceptance: input throttling, lak recovery

