package com.example.datalogger;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class Orientation {
    private SensorManager sensorManager;
    public Sensor rotationVectorSensor;
    private SensorEventListener listener;
    float[] rotationMatrix = new float[9];
    long lastTimeStamp = 0;

    public Orientation(SensorManager sensorManager, Activity listener){
        this.sensorManager = sensorManager;
        this.listener = (SensorEventListener) listener;
        this.rotationVectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
    }

    public void start() {
        sensorManager.registerListener(listener, rotationVectorSensor, SensorManager.SENSOR_DELAY_GAME); //SensorManager.SENSOR_DELAY_NORMAL
        reset();
    }

    public void stop(){
        sensorManager.unregisterListener(listener,rotationVectorSensor);
    }

    public void reset(){
        rotationMatrix = new float[9];
    }

    public float[] fillRotationMatrix(SensorEvent event){
        sensorManager.getRotationMatrixFromVector(rotationMatrix, event.values); //preencher matriz de rotação
        lastTimeStamp = event.timestamp;
        return rotationMatrix;
    }
    public float[] getRotationMatrix(){
        return rotationMatrix;
    }

    public long getLastTimeStamp() {
        return lastTimeStamp;
    }

    public float[] rotateVector(float[] vector, float[] r){
        float[] rotatedVector = new float[3];

        for (int i = 0; i < r.length;i+=3 ){
            rotatedVector[i] = r[i]*vector[0] + r[i+1]*vector[1] + r[i+2]*vector[2];
        }
        return rotatedVector;
    }
}
