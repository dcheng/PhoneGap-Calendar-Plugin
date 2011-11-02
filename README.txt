Both application and application-it's Maven builds will try to deploy the apk into an Android device. Best to have the emulator running or you will see an error: device not found.

Also, the main Maven Build file (.pom) requires to manually set the location of the Android SDK. I didn't get time to find a better way to do this.

As of Android 4.0, it is possible to access the device Calendar via Calendar Provider. This was released after this project was completed and therefore this solution only access the Google Calendar in the cloud via the new Google APIs infrastructure, which is one way to go about it for devices with previous releases of Android. 

Credits for this project go to: Sergio Martinez and Juan Miguel Muñoz. A description of the motivation for this project can be found here: http://developer.vodafone.com/phonegap-calendar-api-plugin/
