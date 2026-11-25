
---
name: android-feature-development
description: Use this skill whenever modifying an existing Android application or implementing a new feature in an Android project. This includes adding Jetpack Compose screens, implementing ViewModel and repository logic, integrating Room database functionality, changing existing app behaviour, and extending an existing Android app while preserving its architecture and coding conventions.
---

# Android Feature Development

## 1. Understand the project

Before implementing any feature:

- Explore the existing project structure.
- Identify the architecture currently used.
- Locate relevant UI, business logic and data layers.
- Inspect existing implementations of similar functionality.
- Identify reusable components, utilities and dependencies.
- Understand how the application handles state and navigation.

Do not assume package names, file locations, class names or architectural patterns.

Always discover them from the current source code.

## 2. Follow existing conventions

- Follow the architecture already established in the project.
- Reuse existing components and abstractions.
- Follow existing naming and package conventions.
- Use existing libraries and dependency versions.
- Avoid introducing unnecessary dependencies.
- Avoid unnecessary refactoring.

If the project architecture has changed, follow the current implementation rather than outdated assumptions.

## 3. Plan before implementation

Before modifying code:

1. Understand the feature requirements.
2. Identify relevant existing implementations.
3. Explain the proposed approach.
4. Identify files that need modification or creation.
5. Identify potential compatibility and data migration concerns.

## 4. Implementation guidelines

- Keep UI, business logic and data access appropriately separated.
- Follow existing state management conventions.
- Reuse existing data models and repositories where appropriate.
- Handle loading, empty and error states.
- Preserve existing user data.
- Avoid duplicate functionality.
- Do not introduce architectural complexity without justification.

## 5. Database changes

When a feature requires database modifications:

- Inspect the existing database schema.
- Understand the current migration strategy.
- Implement required schema changes.
- Preserve existing user data.
- Avoid destructive migrations.
- Verify migration compatibility.

## 6. Verification

After implementation:

- Review all modified files.
- Run relevant tests.
- Run the appropriate application build.
- Fix errors introduced by the changes.
- Report which checks actually passed.

Never claim successful verification without executing it.

## 7. Final summary

Explain:

- What was implemented.
- Important architectural decisions.
- Files created and modified.
- Database changes, if any.
- Tests and build checks performed.
- Remaining limitations.