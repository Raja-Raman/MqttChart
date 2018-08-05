package com.raja.mqttchart2;

import helpers.ChartHelper;
import helpers.MqttHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MainActivity extends AppCompatActivity {
    String TAG = "Raja";
    MqttHelper mqttHelper;
    ChartHelper mChart;
    LineChart chart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "OnCreate...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mqttHelper = new MqttHelper(getApplicationContext());

        chart = (LineChart) findViewById(R.id.chart);
        mChart = new ChartHelper(chart);
        //chart.getDescription().setEnabled(false);
        String desc =  mqttHelper.serverUri +"/" +mqttHelper.subscriptionTopic;
        Log.d(TAG, desc);
        chart.getDescription().setText(desc);

        //initMqtt();
    }

    private void initMqtt(){
        Log.w(TAG,"Initializing MQTT..");
        mqttHelper.connect();
        mqttHelper.mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {
                Log.w(TAG,"Connected to MQTT broker");
            }
            @Override
            public void connectionLost(Throwable throwable) {
                Log.w(TAG,"MQTT connection lost");
            }
            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                Log.w(TAG, "Msg: " +mqttMessage.toString());
                mChart.addEntry(Float.valueOf(mqttMessage.toString()));
            }
            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
            }
        });
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "OnDestroy...");
        mqttHelper.disconnect();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "OnPause...");
        mqttHelper.unsubscribe();
        super.onPause();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "OnResume...");
        super.onResume();
        //mqttHelper.subscribe();
        initMqtt();
    }
}
