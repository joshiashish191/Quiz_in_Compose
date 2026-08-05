# Quiz_in_Compose

A Jetpack Compose-based Android quiz application.

[![CI](https://github.com/<your-org>/<your-repo>/actions/workflows/android-ci.yml/badge.svg)](https://github.com/<your-org>/<your-repo>/actions)

## Overview

This app provides a simple multiple-choice quiz interface built with Jetpack Compose.

## Building

Requirements:
- Android Studio
- Android SDK

To build and run locally:
1. Open the project in Android Studio.
2. Let Gradle sync and install required SDK components.
3. Run the app on an emulator or device.

## Release signing (local and CI)

Do NOT commit keystores or secret files to the repository.

Local testing (use a local keystore stored outside the repo):
1. Create a keystore file (e.g. release.keystore) and keep it outside the repository.
2. Set environment variables before running Gradle, for example on macOS/Linux:

   export KEYSTORE_PATH=/path/to/release.keystore
   export KEYSTORE_PASSWORD=your_store_password
   export KEY_ALIAS=your_key_alias
   export KEY_PASSWORD=your_key_password

3. Run: ./gradlew assembleRelease

CI (GitHub Actions):
- Store the base64-encoded keystore in a secret (KEYSTORE_BASE64) and other secrets: KEYSTORE_PASSWORD, KEY_ALIAS, KEY_PASSWORD.
- The provided CI workflow (./github/workflows/android-ci.yml) decodes the keystore and sets environment variables for Gradle.

## Contributing

Feel free to open PRs for bug fixes or features. Keep UI and logic modular. See CONTRIBUTING.md for guidelines.

## License

See LICENSE file for license details.

