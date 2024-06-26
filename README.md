# <s>HopSkipDrive</s> HopeShipStrive

This is an app made as a take home project for [HopSkipDrive](https://www.hopskipdrive.com/) in order to demonstrate
basic coding competency.

This app demonstrates basic usage of:

* MVVM
* Retrofit
* Coroutines
* Flow
* Repository pattern
* Hilt
* Timber
* RecyclerView

Non-Google Libraries used:

* Retrofit
* Timber

[APK File](app-debug.apk)

## Important Note

A trailing comma on line 74 of the JSON file renders it invalid JSON, unable to be processed.
As a workaround, this app loads a local fixed copy of the file. The Repository treats that as if it 
were preloaded initial data; it loads the local version first, then attempts to load fresh data from
the server.

## Possible Improvements

If I put more time into the app, I could make improvements such as:

* Unit tests
* UI tests
* Swipe to refresh
* Show a loading indicator (the Repository would need to return something other than a Result, something that would indicate if the source of the data was local or remote)
* Improve RecyclerView handling to manage updating only specific items instead of the entire data set.