Dependency update process

- Use Gradle's dependencyUpdates task (from the Gradle Versions Plugin) to find updates.
- Schedule periodic dependency checks (dependabot or a GitHub Action) and review changelogs before upgrading.
- Keep a compatibility test matrix for major upgrades.
