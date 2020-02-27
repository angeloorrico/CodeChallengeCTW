# Code Challenge CTW 

App developed as part of the Android CTW Code Challenge.

- The app retrieves and shows a list of locations gathered from Here Geocoder Autocomplete API.

- It is possible to sort the list by distance (default sorting) or by name.

- Selecting a location from the list shows its details on another screen.

- A location can be favorited from the details screen.
  - A heart shape with animation was implemented to save/delete the locations.

- The list of favorites locations can be accessed from the main (list) screen.

- The app supports portrait and landscape orientations on all screens.

- Some unit and instrumented tests are provided in the project.

- Realm Database is used to save the favorites locations.

- Patterns like Factory, Observer, Singleton and Builder were implemented.

- MVVM was used as the architectural pattern of the application.


# PS:

- The app needs to be tested with an emulator with Google Play Services available, or in a real device.
  - Google Play Services is necessary because of the Google Maps component.
- The device location is necessary and requested from the user in order of get locations with distance from Here Suggestions API.
- Please insert your own Google Maps API Key in order the map component to function.

Screenshots:
<br><img src="/locations_list.png" width="320">
<br><br><img src="/sort_options.png" width="320">
<br><br><img src="/location_details.png" width="320">
<br><br><img src="/location_details_land.png" width="480">
<br><br><img src="/favorited_locations.png" width="320">
<br><br><img src="/favorited_locations_land.png" width="480">
