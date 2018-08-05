# pip install paho-mqtt

import json
import paho.mqtt.client as mqtt
import random
import time
import threading
import sys

mqttc = mqtt.Client("raja_client1082", clean_session=False)

# mqttc.username_pw_set("username", "password")
# mqttc.connect("m12.cloudmqtt.com", 11111, 60)

mqttc.connect("broker.hivemq.com", 1883, 60)


print ("Publishing data to broker.hivemq.com:1883 Topic: raja/sensor/temp") 
print ("Press ^C to quit..")
try:
    while (True):
        mqttc.publish("raja/sensor/temp", payload=round(random.normalvariate(20,1),2), qos=0)
        time.sleep(5)
except KeyboardInterrupt:
    print ('Bye!')

 
