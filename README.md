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

There is a lot of good material about the Clean Architecture, but often they have conflicting ideas. The Clean Architecture book is also not always very straight to the point. So after some studying and research, I'd like to highlight some details about each layer that might be interesting and not always easy to find in those material.
- UI layer
  - MVVM is used.
  - ViewModel fetches through use-cases only what's needed. 
    - Usually, there's no multi-purpose use-case, a screen is expected to fetch only what it needs, therefore, use-cases are usually driven by the screens' needs.
    - That's also one reason why it is better to pass the ID of a model from one screen to another, instead of an entire model. Then the destination screen loads only what it needs with the given ID.
    - But this shouldn't be a strict rule. Having multiple use-cases can lead to unnecessary complexity and overhead.
  - Models returned from use-cases are not passed to the View, and instead, the ViewModel uses them to prepare the data needed by the View, and passes that data through another model that is available only in the `ui` module.
  - Since Compose is used in the View, not passing to the View the models coming from the use-cases (domain module) also helps with the stability of Composables, as classes from modules where Compose compiler is not run are determined unstable.
- Domain layer
  - Where business logic resides, driven by the use-cases and entities.
  - Entities are not visible to the UI layer. They are used internally in the domain layer.
- Data layer
  - Responsible for handling data-related operations such as fetching data from external sources.
  - It is driven by the domain layer needs and it should hide its internal details from the domain. Therefore, using Dependency Inversion Principle, the domain defines interfaces, and the data layer contains the implementation for those interfaces, meeting the Dependency Rule of the Clean Architecture.
  - Its main entry points are Repositories and Services. Repositories are usually recommended to be used for handling domain models, so Services are used for anything not specific to domain models, such as analytics.
  - Data Sources wrap external data providers and hide the details of things like database entities from the Repositories and Services, thus, they live in their own modules. Since this architecture is expected to be used in a big project, this helps making Repositories and Services simpler and cleaner.

One interesting module is the `di-core`. Since DI is used, we usually expect the module where the launcher Activity resides to be the root module where all dependency graphs from other modules are connected into a single graph. That is achieved by adding those modules as dependencies. In this case, that would be done in the `ui` module. However, that would break the architecture, as the `ui` module would have visibility of layers that it shouldn't, such as the data layer. So the purpose of the `di-core` is exactly to prevent that. It doesn't contain any class or Kotlin code, except for the build Gradle where it has all other modules as dependency.
 
Again, I just want to emphasize that this is a complex architecture with tons of boilerplate. Most of the time, the simpler the better, but this architecture is not simple. Adding a single field to one model might require changes in multiple classes, for example:
- In the response model parsed from a network request response.
- In the database model.
- In the domain Entity.
- In the domain model passed from the use-case to the ViewModel.
- In the model passed from the ViewModel to the View.
- In the mapper classes.
- Etc.