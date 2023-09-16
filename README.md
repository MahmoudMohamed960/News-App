# News App

simple project to show news and it`s details
## Table of Contents

1. [Overview](#overview)
2. [Architecture](#architecture)
3. [Dependencies](#dependencies)

## Overview

News app provide many features that you can list ,filter , save favourite news 

## Architecture

This project follows the **MVVM (Model-View-ViewModel)** architecture pattern with a **Clean Architecture** approach. Here's a high-level overview of the architecture:

- **Presentation Layer (UI)**: This layer contains the user interface components, such as Activities, Fragments, and Views. It interacts with the ViewModel to display data and handle user input.

- **ViewModel**: ViewModel acts as an intermediary between the UI and the underlying data. It fetches data from the use cases in the Domain layer and provides it to the UI components.

- **Domain Layer**: The domain layer contains the business logic of the application. It defines use cases that interact with repositories to fetch and manipulate data.

- **Data Layer**: The data layer consists of data sources, repositories, and remote data sources. It manages data retrieval and storage. We use Retrofit for network requests and Room for local database storage.

- **Dependency Injection**: Dagger Hilt is used for dependency injection, making it easier to manage and provide dependencies throughout the application.

## Dependencies

List the main dependencies and libraries used in your project:

- **Retrofit**: For making network requests.
- **Room**: For local database storage.
- **Dagger Hilt**: For dependency injection.
- **ViewModel**: Part of Android Architecture Components.
- **Glide**: For image loading and caching.
- **Paging 3**: For pagination of data.
- **SwipeRefreshLayout**: For pull-to-refresh functionality.
- **Mockito**: For mocking in unit tests.
- **JUnit**: For unit testing.
- **Robolectric**: For Android unit testing on the JVM.
