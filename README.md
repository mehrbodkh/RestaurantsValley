# RestaurantsValley
This project uses FourSquare API to show restaurants near you. You can move the map and search areas for restaurants. Then you can see their locations on map and you can see restaurants info such as address and contacts.

## Technologies
This project has been fully written in Kotlin with the use of following libraries:
- Architecture Components
- Hilt
- Navigation Component
- Flow
- Coroutines
- MockK
- Retrofit
- Mapbox
- Gson

## Architecture
This project is built using MVVM architecture pattern in context of Clean Architecture. So, there are three layers in the project:
- Domain: Which is responsible for basic logics and provides use cases and interfaces for repositories.
- Data: Which provides data needed for the app to function.
- Presentation: Which handles UI and presentation logic of the app.

## Map Module
There is a module here called Map Module which is written to make it easier to user different maps in the future.
