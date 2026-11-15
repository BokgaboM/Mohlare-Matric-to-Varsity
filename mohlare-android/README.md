# Mohlare

Play Store title: **Mohlare: Matric to Varsity**. Mohlare is Sepedi for "tree": one root, then branches, leaves and flowers. The logo is a new shoot growing from a tree stump.

Android app that helps Grade 11 and 12 learners plan their way to university: APS, courses, bursaries and deadlines in one place.

Built from scratch by Bokgabo Mohlala. Started 15 November 2026.

## Stack

- Kotlin + Jetpack Compose (Material 3)
- Navigation Compose, DataStore (saved settings)
- Firebase (Auth, Firestore, Messaging) to be added. See "Next steps".
- minSdk 26, targetSdk 36, package `za.co.mohlare.app`

## What's in this starter

- App icon and logo from the Mohlare mark (`res/drawable/`, `res/mipmap-anydpi-v26/`)
- Two themes, Heritage and Litema, switchable in Me → Appearance and remembered on the phone (`ui/theme/`)
- Pattern art drawn in code: triangles (Heritage) and litema arcs (Litema) (`ui/theme/Patterns.kt`)
- Welcome screen, then four tabs: Path, Discover (courses and bursaries), Plan, Me
- Sample data only (`data/SampleData.kt`). Every name, date and APS number is a placeholder.

## Open it

1. Unzip, then in Android Studio choose **File → Open** and pick the `mohlare-android` folder.
2. Let Gradle sync finish (first sync downloads Gradle 8.13 and the libraries).
3. If Studio offers to update the Android Gradle Plugin or library versions, accept. The versions in `gradle/libs.versions.toml` are a safe baseline.

## Run it on your phone (no emulator)

1. **Build → Build App Bundle(s) / APK(s) → Build APK(s)**.
2. Find the APK in `app/build/outputs/apk/debug/`.
3. Send it to your phone (Google Drive or email), open it, and allow "Install unknown apps" for that one app.

## Next steps

1. **Fonts:** download Bricolage Grotesque, Fraunces and Inter from Google Fonts (all free, OFL licence). Put the `.ttf` files in `app/src/main/res/font/` (lowercase names, e.g. `fraunces_bold.ttf`) and swap `FontFamily.SansSerif` / `FontFamily.Serif` in `ui/theme/Themes.kt`.
2. **Firebase:** create the `mohlare-app` project, add `app/google-services.json`, then add the `com.google.gms.google-services` plugin and the Firebase BoM.
3. **Sign-in:** replace the Welcome buttons with Firebase Authentication (Google + email).
4. **Real data:** load universities, courses and bursaries from Firestore instead of `SampleData`.
5. **Dark mode:** add a dark colour set to each theme.
6. **Remote themes:** load extra themes from the Firestore `themes` collection.
