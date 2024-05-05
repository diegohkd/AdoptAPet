# Adopt a Pet
Adopt a Pet is an Android app for pet adoption powered by [Petfinder](https://www.petfinder.com/), that I have mostly as a sample project.

# WIP 
This is currently a WIP, but the architecture and some basic functionality are mostly done, such as searching for Pets and opening the details screen of a pet. The most noticeable pending implementation should be the UI. 

# Technologies and other highlights
- MVVM, [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html) and Android's [Guide to app architecture](https://developer.android.com/topic/architecture).
- Jetpack.
	- Compose.
	- Navigation with Compose.
	- DI with Hilt.
	- Paging.
	- Room.
- Kotlin Flow.
- Kotlin Coroutines.
- Multi-modules.
- Design system.
	- Extension theme of Material Theme.
	- Different color schema per animal type.
- Gradle
	- Kotlin DSL.
	- Versions catalog.
	- Common config with Precompiled script plugins and constants.
- Code analysis with detekt.
- Unit tests with JUnit4 and Mockk.
- CI - Simple Github Actions workflow that runs on pull requests to prevent code with issues getting merged. It validates:
	- Code quality using detekt.
	- Unit tests.
	- And that the project builds.

# Architecture
![Architecture](assets/app_architecture.webp?raw=true "App Architecture")

The architecture is based on the [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html) and Android's [Guide to app architecture](https://developer.android.com/topic/architecture). The goal is to be as close to a pure Clean Architecture as possible, but note that this is not an ideal architecture for such small and simple project as this one. I used it mostly for study purposes.