package com.example.datalogger;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class Magnetometer {
    private final SensorManager sensorManager;
    private final SensorEventListener listener;
    public Sensor magnetometerSensor;
    public float[] magValues;
    public long lastTimeStamp = 0;

    public Magnetometer(SensorManager sensorManager, Activity listener){
        this.sensorManager = sensorManager;
        this.listener = (SensorEventListener) listener;
        this.magnetometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    public void start() {
        sensorManager.registerListener(listener, magnetometerSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    public void stop(){
        sensorManager.unregisterListener(listener,magnetometerSensor);
    }

    public void fillMagValues(SensorEvent event){
        magValues = event.values;
        lastTimeStamp = event.timestamp;
    }

    public long getLastTimeStamp() {
        return lastTimeStamp;
    }

    public float[] getMagValues() {
        return magValues;
    }
}
