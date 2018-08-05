# MqttChart
Android chart updated through MQTT messages.

Based on a project by Wildan:
https://wildanmsyah.wordpress.com/2017/05/11/mqtt-android-client-tutorial/ 
https://github.com/wildan2711/mqtt-android-tutorial

The original project had the problem of multiple subscriptions to the same topic, and duplicte messages were received.
This modification fixes the issue with life cycle event calls.

The MPAndroid chart is from:
https://github.com/PhilJay/MPAndroidChart
