# Mobile App Development - Assignment 2 (FreeCycleV2)  
# Project Name: FreeCycleV2
## Project Description:
FreeCycle is a web application that allows users to offer unwanted reusable items to others which helps in contributing to a circular economy. A user signs up and can then view/add/edit/delete listings. Listings include contact details and location via map for collection of the item/s.

### The following technologies have been used in the making of the FreeCycle web application:
* Kotlin
* Android Studio
* Git/GitHub
* Picasso
* Firebase Realtime Database
* Firebase Storage
* Firebase Authentication
* Google Authentication
* Google Maps API


## Features
* CRUD functionality for listings, including images, and location. When created/updated/deleted listings are persisted to Firebase Realtime Database.
* Use of date picker to set date item is available for collection.
* Use of toggle button to set status of listing active/inactive.
* User can use toggle switch to view all user listings, or just their own listings.
* User authentication using Firebase Authentication and Google Authentication. A user can choose whether to sign up/log in with email and password or to authenticate using their google account.
* Image upload functionality. User profile picture is persisted to Firebase Storage. This functionality for listing images has been started but not completed.
* App navigation via Nav Drawer.
* Use of Navigation Architecture Component.
* Use of date picker to set the date that the item is available for collection on.
* Use of toggle button to set status of listing active/inactive.
* Map functionality to allow the user to set their location in order to allow for collection of an item. On first attempt at adding a listing location permissions are
requested, this sets the users current location to the location object. This location can also be updated/edited by the user. The map functionality makes use of the 
Google Maps API.
* Swipe left to delete support.
* Swipe right to edit support.
* Night mode functionality has been started but not completed.

## Visuals

### Sign Up & Login Page:
![image](https://user-images.githubusercontent.com/76408967/209324829-1df6f7b8-ad8b-420a-9c4f-aad4c1c57057.png)
### Sign in with Google
![image](https://user-images.githubusercontent.com/76408967/209324908-c4887bc1-5ade-41ad-a131-bb3df806d620.png)

### Nav Drawer:
![image](https://user-images.githubusercontent.com/76408967/209324966-6301e02d-4afd-4efe-aa8d-d35116fc716c.png)

### Listings:
![image](https://user-images.githubusercontent.com/76408967/209325019-2579f9aa-dd7a-4a03-9415-b822b784eee2.png)


### Add Listing:
![image](https://user-images.githubusercontent.com/76408967/209325198-fa88727a-2364-46bd-89a2-9a020b602ad9.png)

### View Listing:
![image](https://user-images.githubusercontent.com/76408967/209325284-df2bbaea-56e5-450a-a2d1-e2115cb13884.png)


## UX/DX Approach Adopted
### UX
I wanted the app to be accessible and easy to use so went with simple colours and a clutter free, and straightforward user interface. The Material Design Colour Picker was used in order to choose colours that make the application accessible. See references below for link to this resource. 
A nav drawer was implemented to allow for easy navigation between screens. I included clear descriptions of each field required to be filled bt the user. A date picker was used in order to easily choose the date available.
Buttons and listing active toggle are clearly marked and recognisable.
### DX
I followed The MVVM Design Pattern, separating the program logic from the UI functionality.
I also made sure that the code is neat, with an easy to understand naming convention and correctly formatted. 


## Git Approach Adopted
My git approach involved having 2 branches - Main and Develop. While working on functionality I committed to the Develop branch and when ready to create a release, merged this into the Main branch. The releases were then created from the Main branch.
I have a clear commit history, a develop and main/release branch, and tagged releases.

## Personal Statement
I have spent a lot of time working on this app. I found it interesting and challenging to convert FreeCycle (Assignment 1) to the MVVM Design Pattern. It is fascinating how there can be so many different ways of doing something and the levels of complexity can vary wildly. Perhaps starting from scratch with a new app and implementing the MVVM pattern may have been less challenging, and perhaps less time consuming also. I am happy with what I have accomplished here but with more time could have accomplised a lot more. 
Close to the end, I started to implement night mode functionality as well as persisting listing images to Firebase storage. Unfortunately I ran out of time and didn't get to complete these.

## References
Helpful online resources used in this project:

Material Design Color Tool
* https://m2.material.io/resources/color/#!/?view.left=0&view.right=0&primary.color=42A5F5&secondary.color=9FA8DA

Fragments and The Navigation Component
* https://developer.android.com/codelabs/basic-android-kotlin-training-fragments-navigation-component#0

Live Data
* https://developer.android.com/topic/libraries/architecture/livedata

Nav Drawer Separator
* https://stackoverflow.com/questions/30243365/how-to-add-one-section-separator-for-navigation-drawer-in-android

Converting Date when sending to/from Firebase Realtime Database
* https://stackoverflow.com/questions/35183146/how-can-i-create-a-java-8-localdate-from-a-long-epoch-time-in-milliseconds
* https://www.concretepage.com/java/java-8/convert-between-java-localdate-epoch
* https://www.geeksforgeeks.org/localdate-from-method-in-java/#:~:text=The%20from()%20method%20of,LocalDate%20from%20a%20temporal%20object.&text=Parameter%3A%20This%20method%20accepts%20a,local%20date%20and%20not%20null
* https://stackoverflow.com/questions/43584244/how-to-save-the-current-date-time-when-i-add-new-value-to-firebase-realtime-data

Type Casting
* https://www.geeksforgeeks.org/kotlin-explicit-type-casting/

Location Permissions
* https://developer.android.com/training/location/permissions
* https://github.com/googlemaps/android-samples/blob/main/tutorials/kotlin/CurrentPlaceDetailsOnMap/app/src/main/java/com/example/currentplacedetailsonmap/MapsActivityCurrentPlace.kt
* https://codelabs.developers.google.com/codelabs/advanced-android-kotlin-training-maps#7
* http://tools.android.com/tips/lint/suppressing-lint-warnings

Using requireActivity()
* https://medium.com/android-news/the-requireactivity-and-requirecontext-example-1c089ce11a3a

Night Mode/Dark Theme
* https://www.geeksforgeeks.org/how-to-create-a-dark-mode-for-a-custom-android-app-in-kotlin/
* https://developer.android.com/codelabs/basic-android-kotlin-training-change-app-theme#0

Location permission bug fix
* https://stackoverflow.com/questions/40760625/how-to-check-permission-in-fragment 







