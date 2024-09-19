# GitHub User Search Android App (playground)

## Overview

This is a simple Android app designed for searching GitHub users. It provides a straightforward interface to interact with GitHub's user search functionality.

## Authentication

To avoid hitting API rate limits, please include your GitHub authentication token in the build configuration. Add the following line to your `build.gradle` file:

```kotlin
buildConfigField("String", "BASIC_AUTH_TOKEN", "\"your_github_token\"")
```

## User Interface
The app's user interface is designed for supprot both dark and light mode
<video src="docs/evidence.mp4"></video>

## Todo

- Handle exceptions based on their type
- Implement event logging
- Improve build plugins
- Update unit test coverage (currently limited to ViewModel)
