Crash reporting integration

Recommended: Firebase Crashlytics or Sentry.

Steps (Firebase Crashlytics):
1. Add Firebase to your project and download google-services.json (DO NOT commit it).
2. Add the Crashlytics Gradle plugin and dependency as documented by Firebase.
3. Upload mapping.txt from the release build to Crashlytics during CI to symbolicate stack traces.

Store service account or API keys in CI secrets and do not commit them to the repo.
