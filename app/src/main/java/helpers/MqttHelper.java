package helpers;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.raja.mqttchart2.MainActivity;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Created by wildan on 3/19/2017.
 */

public class MqttHelper {
    public String TAG = "Raja";
    public MqttAndroidClient mqttAndroidClient;
    //public String serverUri = "tcp://broker.hivemq.com:1883/";   // do not have a trailing slash
    public String serverUri = "tcp://broker.hivemq.com:1883";
    public  String clientId = "Rajas_Client";
    public  String subscriptionTopic = "raja/sensor/+";

    public String username = "xxxxxxx";
    public String password = "yyyyyyy";

    public MqttHelper(Context context){
        Log.w(TAG, "MqttHelper: constructor");
        mqttAndroidClient = new MqttAndroidClient(context, serverUri, clientId);
        mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {
                Log.w(TAG, "[Default] Connected to MQTT server: " +s);
            }
            @Override
            public void connectionLost(Throwable throwable) {
                Log.w(TAG, "[Default] MQTT Connection lost");
            }
            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                Log.w(TAG, mqttMessage.toString());
            }
            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
            }
        });
        //connect();
    }

    public void setCallback(MqttCallbackExtended callback) {
        Log.w(TAG,"MqttHelper: setting callback..");
        mqttAndroidClient.setCallback(callback);
    }

    public void connect(){
        Log.w(TAG,"MqttHelper: connecting to server..");
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(false);
        //mqttConnectOptions.setUserName(username);
        //mqttConnectOptions.setPassword(password.toCharArray());
        try {
            mqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                    disconnectedBufferOptions.setBufferEnabled(true);
                    disconnectedBufferOptions.setBufferSize(100);
                    disconnectedBufferOptions.setPersistBuffer(false);
                    disconnectedBufferOptions.setDeleteOldestMessages(false);
                    mqttAndroidClient.setBufferOpts(disconnectedBufferOptions);
                    subscribe();
                }
                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.w(TAG, "Failed to connect to: " + serverUri + exception.toString());
                }
            });
        } catch (MqttException ex){
            ex.printStackTrace();
        }
    }

    public void subscribe() {
        Log.w(TAG,"MqttHelper: subscribing to topic..");
        try {
            mqttAndroidClient.subscribe(subscriptionTopic, 0, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.w(TAG,"Subscribed to topic.");
                }
                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.w(TAG, "Subscription failed!");
                }
            });
        } catch (MqttException ex) {
            System.err.println("Exception while subscribing:");
            ex.printStackTrace();
        }
    }

    public void unsubscribe() {
        Log.w(TAG,"MqttHelper: Unsubscribing to topic..");
        try {
            mqttAndroidClient.unsubscribe(subscriptionTopic);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        Log.w(TAG,"MqttHelper: disconnecting..");
        if (!mqttAndroidClient.isConnected())
            return;
        try {
            IMqttToken token = mqttAndroidClient.disconnect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Log.d(TAG, "Disconnected from MQTT server");
                    mqttAndroidClient.unregisterResources();
                }
                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Log.d(TAG, "Could not disconnect from server");
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
