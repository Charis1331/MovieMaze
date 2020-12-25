<h1 align="center">MovieMaze</h1>

<p align="center">
MovieMaze is a demo app built on modern Android framework tech-stacks and MVVM architecture.
<br>Its main purpose is to investigate the new Paging3 library and combine multiple data sources using Flow
<br>Data are fetched via the TMDB API and stored locally in a database, enabling offline usage of the app.
</p>
</br>

<p align="center">
<img src="/media/preview.png"/>
</p>

## Tech Stack

* Minimum SDK version 26
* [Kotlin](https://kotlinlang.org/) based, [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) & [Flow](https://kotlinlang.org/docs/reference/coroutines/flow.html) for asynchronous operations and multiple data streams filtering [(MutableStateFlow)](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/-mutable-state-flow/).
* [Koin](https://github.com/InsertKoinIO/koin) for DI.
* Jetpack
    * [Paging3](https://developer.android.com/jetpack/androidx/releases/paging) - take advantage of TMDB API's paging capabilities
    * [Room](https://developer.android.com/jetpack/androidx/releases/room) - pair it with paging for offline support
    * [Lifecycle](https://developer.android.com/jetpack/androidx/releases/lifecycle) - persist core data during configuration changes
* [Retrofit](https://github.com/square/retrofit) - for all the API requests
* [Glide](https://github.com/bumptech/glide) - for image loading
* [MDC](https://github.com/material-components/material-components-android) - use of shared element transitions
* [Gson](https://github.com/google/gson) - for parsing JSON data
* [Truth](https://truth.dev/) - assertion library for Tests
* [MockWebServer](https://github.com/square/okhttp/tree/master/mockwebserver) - to test the endpoint validity of each request
* [Barista](https://github.com/AdevintaSpain/Barista) - a wrapper around Espresso, to make UI testing easier
* [Gradle Kotlin DSL](https://docs.gradle.org/current/userguide/kotlin_dsl.html) - get all the goodies of Kotlin in Gradle scripts

## MAD Score

![Summary] (/mad_scorecard/summary.png)
![Kotlin] (/mad_scorecard/kotlin.png)

# License
```xml
   Copyright 2020 Charis1331 (Charalampos Choulis)

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```