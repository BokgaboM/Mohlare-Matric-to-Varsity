# Mohlare: Matric to Varsity

![Android CI](https://github.com/BokgaboM/mohlare-android/actions/workflows/android-ci.yml/badge.svg)

**Mohlare** (Sepedi for "tree") is an Android app that helps South African Grade 11 and 12 learners plan their way to university. Enter your marks once, and Mohlare shows your APS, the courses you qualify for, the bursaries open to you, and every deadline in one plan.

> Every tree starts with one root. Mohlare is built on the idea that one learner who follows their calling makes a way for the people who come after them.

## Status

Early development. Sign-in, profile setup, marks entry with live APS, themes and navigation work. Courses, bursaries and deadlines still use sample data.

## Features

| Tab | What it does |
| --- | --- |
| **Path** | Your APS, your next deadlines, and saved courses and bursaries |
| **Discover** | Search courses by university and see if your APS qualifies; browse bursaries by field and closing date |
| **Plan** | Track applications (planning, submitted, accepted) and a document checklist |
| **Me** | Your marks, reminders, appearance and privacy settings |

**Themes:** Mohlare ships with two looks, **Heritage** (warm earth tones with Ndebele-inspired patterns) and **Litema** (clay tones with curved mural lines). The theme system is built so new looks can be added over time.

**Languages (planned):** English, Sepedi, isiZulu and Afrikaans.

## Tech stack

- Kotlin, Jetpack Compose, Material 3
- Navigation Compose
- DataStore for on-device settings
- Firebase Authentication (Google via Credential Manager, email) and Cloud Firestore
- minSdk 26, targetSdk 36

## Project structure

```
app/src/main/java/za/co/mohlare/app/
├── MainActivity.kt          App entry point
├── MohlareApp.kt            Navigation and bottom tabs
├── AppViewModel.kt          Sign-in state: signed out, profile setup or ready
├── data/                    Firebase repositories, sample data, saved settings
└── ui/
    ├── auth/                Google sign-in through Credential Manager
    ├── components/          Shared cards, buttons, pills, date blocks
    ├── screens/             Welcome, email sign-in, profile setup, Path, Discover, Plan, Me, Appearance
    └── theme/               Themes, colours and pattern drawing
```

## Getting started

1. Clone the repo and open it in Android Studio (**File → Open**).
2. Add your own `app/google-services.json` from the Firebase console (it is not committed). See **Firebase setup** below.
3. Let Gradle sync finish.
4. Run it on a phone or emulator, or build an APK with **Build → Build App Bundle(s) / APK(s) → Build APK(s)**.

## Firebase setup

1. In the Firebase console, open the `mohlare-app` project.
2. **Authentication → Sign-in method:** turn on **Google** and **Email/Password**.
3. **Project settings → Your apps → Android app** (`za.co.mohlare.app`): add your debug **SHA-1** fingerprint. Get it in Android Studio from the Gradle panel: **app → Tasks → android → signingReport**.
4. Download a fresh `google-services.json` (after steps 2 and 3) and put it in `app/`.
5. **Firestore Database:** create the database, then paste the contents of `firestore.rules` into the **Rules** tab and publish.

## Continuous integration

Every push to `main` runs **Android CI** (`.github/workflows/android-ci.yml`):

| Job | What it does | Output |
| --- | --- | --- |
| Unit tests | Runs `testDebugUnitTest` (APS rules and more) | Test report |
| Lint | Runs Android lint; warnings don't fail the run yet | Lint report |
| Build debug APK | Builds the app once tests pass | `mohlare-debug-apk` to download and install |

**One-time setup:** the workflow needs `google-services.json`, which isn't in the repo. In GitHub, open **Settings → Secrets and variables → Actions → New repository secret**, name it `GOOGLE_SERVICES_JSON`, and paste the whole contents of your `app/google-services.json` file.

## Roadmap

- [x] Screens, navigation and theme system
- [x] App icon and logo
- [ ] Brand fonts (Bricolage Grotesque, Fraunces, Inter)
- [x] Firebase sign-in (Google and email)
- [x] Profile setup with privacy consent, sign out and account deletion
- [x] Marks entry and standard APS (best six subjects, Life Orientation not counted)
- [x] GitHub Actions pipeline: tests, lint, APK build
- [ ] University-specific scores (Wits, UCT and others)
- [ ] Universities, courses and bursaries from Firestore
- [ ] Deadline reminders
- [ ] Sepedi, isiZulu and Afrikaans translations
- [ ] Dark mode
- [ ] Hosted privacy policy and data download
- [ ] Closed testing and Google Play release

## Licence

© 2026 Bokgabo Mohlala. All rights reserved.

This code is not open source. No part of it may be copied, modified or distributed without written permission from the author.
