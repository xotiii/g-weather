# GWeather

GWeather is a modern weather application built with Kotlin and Jetpack Compose. It provides real-time weather information and maintains a history of weather fetches for logged-in users.

## 🚀 Getting Started

### Prerequisites

Before running the application, you must provide your own OpenWeather API key.

1.  Open the `app/build.gradle.kts` file.
2.  Locate the `OPEN_WEATHER_API_KEY` field in the `defaultConfig` block:
    ```kotlin
    buildConfigField("String", "OPEN_WEATHER_API_KEY", "\"YOUR_API_KEY_HERE\"")
    ```
3.  Replace `YOUR_API_KEY_HERE` with your valid API key.

## 🏗 Architecture & Principles

The project is architected with scalability, maintainability, and testability in mind, following:

*   **MVVM (Model-View-ViewModel)**: Separation of UI logic from business logic.
*   **Clean Architecture**: Implementation of Domain, Data, and Presentation layers to ensure a decoupled and independent code base.
*   **SOLID Principles**: Adherence to object-oriented design principles for robust software development.

## 🛠 Tech Stack

*   **Language**: [Kotlin](https://kotlinlang.org/)
*   **UI**: [Jetpack Compose](https://developer.android.com/jetpack/compose)
*   **Networking**:
    *   [Retrofit](https://square.github.io/retrofit/): Type-safe HTTP client.
    *   [OkHttp](https://square.github.io/okhttp/): HTTP client for efficient networking.
*   **Persistence**:
    *   [Room](https://developer.android.com/training/data-storage/room): SQLite abstraction for local database storage (Weather History, User Accounts).
    *   [SharedPreferences](https://developer.android.com/training/data-storage/shared-preferences): For managing user session data.
*   **Dependency Injection**: [Dagger Hilt](https://dagger.dev/hilt/)
*   **Asynchronous Programming**: [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) & [Flow](https://kotlinlang.org/docs/flow.html)

## 🧪 Testing

The project includes comprehensive unit tests to ensure reliability:

*   **Framework**: [JUnit 5](https://junit.org/junit5/)
*   **Mocking**: [MockK](https://mockk.io/)
*   **Coroutine Testing**: `kotlinx-coroutines-test`

## 📦 Features

*   **User Authentication**: Register and Login functionality with local persistence.
*   **Real-time Weather**: Fetches current weather data based on device location.
*   **Weather History**: Automatically saves weather snapshots for the logged-in user.
*   **Responsive UI**: Dark and Light mode support with adaptive layouts.
