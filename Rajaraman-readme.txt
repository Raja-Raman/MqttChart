MQTT Android tutorial with dynamic charts:
https://github.com/PhilJay/MPAndroidChart

https://wildanmsyah.wordpress.com/2017/05/11/mqtt-android-client-tutorial/
https://github.com/wildan2711/mqtt-android-tutorial
https://customer.cloudmqtt.com/instance

simulator.py is a python publisher to test this app.

To compile the Android app:
1. Create a new Android studio project with an empty activity.

In the project level build.gradle under allprojects/repositories:  
(NOTE: there is another buildscript/repositories section, but that will not work)
    maven {  url "https://repo.eclipse.org/content/repositories/paho-snapshots/" }
    maven { url "https://jitpack.io" }

2. in app level build.gradle:       
    Add the paho client and service (note the version number carefully) under dependencies:
        implementation 'org.eclipse.paho:org.eclipse.paho.client.mqttv3:1.1.0'
        implementation 'org.eclipse.paho:org.eclipse.paho.android.service:1.1.1' 
        
    Add the charts jar file:
        implementation 'com.github.PhilJay:MPAndroidChart:v3.0.3'
        
    click 'sync now'.
    
3. in Android manifest:
<activity android:name=".MainActivity" android:screenOrientation="landscape">

   add permissions:
        <uses-permission android:name="android.permission.INTERNET" />
        <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
        <uses-permission android:name="android.permission.WAKE_LOCK" />    
   add the mqtt service under the application node:
        <service android:name="org.eclipse.paho.android.service.MqttService" />   
        
4. Copy the follwoing two files under <root>\app\src\main\java\helpers        
MqttHelper.java
ChartHelper.java

5. Run the python program simulator.py
