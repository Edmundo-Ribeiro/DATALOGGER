package com.example.datalogger;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class Accelerometer {
    private SensorManager sensorManager;
    private SensorEventListener listener;
    //sensor acelerometro
    public Sensor accelerometerSensor;
    public float[] acceleration;
    public long lastTimeStamp;

    public Accelerometer(SensorManager sensorManager, Activity listener){
        this.sensorManager = sensorManager;
        this.listener = (SensorEventListener) listener;
        this.accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
    }

    public void start() {
        sensorManager.registerListener(listener, accelerometerSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    public void stop(){
        sensorManager.unregisterListener(listener,accelerometerSensor);
    }

    public void fillAccValues(SensorEvent event){
        lastTimeStamp = event.timestamp;
        acceleration = event.values;
    }

    public long getLastTimeStamp() {
        return lastTimeStamp;
    }

    public float[] getAcceleration() {
        return acceleration;
    }
}
