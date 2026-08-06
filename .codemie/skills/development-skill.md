## Purpose

Develop enterprise-grade applications from requirements while following secure development practices, clean architecture, documentation and maintainability standards.

Provide end-to-end application development capabilities including:

Complete Full stack application that runs on localhost
Frontend Development
Backend Development
Database Design
Git Management

## Rules
- Read all the enhancements first.
- Create working end to end Application with Frontend, Backend and Database, that can run on localhost
- All enhancements (P0,P1,P2) must be included while developing end to end application
- Use below only Mandatory Technology Stack.
- Follow below Standard Repository Structure for full working application.
- Don't generate automation test cases(using playwright inside automation folder). Generate only folders
- Use Provided GitHub Repo and Branch name for committing entire code.
- Before Commiting, always take approval from user.
  -`Do you want to commit the entire code to provided Repo and branch?`
   If user replies `yes`,`approve` then commit the entire code to same repo and branch and provide commit id to user in output.
   If user replies `no` then dont commit any file or folder to the repo.
- Once user provide approval, then commit all the files, dont leave any file for commit, except target folder.

## Mandatory Technology Stack
 # Frontend
  - Bootstrap 5.x
 # Backend
  - Python 3.12+
  - Flask
 # Database
  - SQLite
 # Version Control
  - Git

## Standard Repository Structure
repository-root
│
├── app
│   ├── api
│   ├── services
│   ├── repositories
│   ├── models
│   ├── validators
│   ├── middleware
│
├── database
│   ├── schema
│   ├── migrations
│   └── seed
│
├── tests
│   ├── unit
│   ├── integration
│   ├── api
│
├── src
│   ├── main
│       ├── java (for test automation framework)
├── src
│   ├── test
│       ├── java (for automation test scripts)
│
├── docs
├── scripts
├── .gitignore
├── requirements.txt
├── README.md
└── run.py

## Architecture Principles
Mandatory
- Separation of Concerns
- SOLID Principles
- Repository Pattern
- Service Pattern
``
Prohibited
- Business Logic in Controllers
- Direct UI to Database Communication
- Hardcoded Configuration
- Global Mutable State

## Coding Standards
Mandatory
- Input Validation
- Output Encoding
- Exception Handling
- Rate Limiting
- CSRF Protection
- Secure Session Management

Prohibited
- Hardcoded Passwords
- Hardcoded API Keys
- Hardcoded Secrets
- Sensitive Data Logging

## Mandatory Enhancement Implementation

Whenever generating or modifying an application, automatically implement the following improvements.
Implement all the P0, P1 and P2 features listed in enhancements, while creating or updating application

## P0 Critical Engineering Backlog

### Secrets

- Remove hardcoded Flask SECRET_KEY.
- Load from environment.
- Fail fast outside test environments when missing.

### CSRF

- Protect all POST forms.
- Include CSRF tokens in templates.
- Reject invalid requests.

## P1 (Medium Priority)

Automatically implement:

- Rate limiting for authentication endpoints.
- Structured audit logging.
- Secrets from environment variables.
- Centralize environment URL configuration.
- Secure session cookie configuration.

## P2 (Low Priority)

Automatically implement:

- Accessibility improvements.
- Bootstrap SRI or local fallback.
- Remove repository noise.
- Preserve password whitespace.
- Remove unused dependencies.

# Completion Criteria

The implementation is complete only when:

- Application builds successfully.
- Frontend is functional.
- Backend is functional.
- Documentation is complete.
- Git commits are generated.