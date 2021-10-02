package com.example.datalogger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements SensorEventListener {


    Button btnStartStop;
    TextView textSample;
    TextView textRealSteps;
    Context context;

    Boolean started = false;
    int sampleCount = 0;

    SensorManager sensorManager;
    Orientation orientation;
    Accelerometer accelerometer;
    Magnetometer magnetometer;

    //files
    String accMagOrientationFile = "sensors_acc_accR_mag_rvector";
    String androidStepCounterFile = "android_steps";
    String accStepCounterFile = "acc_steps";
    String accRStepCounterFile = "acc_rotated_steps";
    String fileNamePrefix;
    String fileNameSuffix;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        orientation = new Orientation(sensorManager,this);
        accelerometer = new Accelerometer(sensorManager,this);
        magnetometer = new Magnetometer(sensorManager,this);

        btnStartStop = findViewById(R.id.btnStartStop);
        textSample =  findViewById(R.id.textSampleNumber);
        textRealSteps = findViewById(R.id.textRealSteps);

        context = getApplicationContext();

        btnStartStop.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!started){
                    btnStartStop.setText("STOP");

                    fileNamePrefix = getFilePrefix();
                    fileNameSuffix = getFileSuffix();

                    //iniciar acc
                    //iniciar acc z
                    //iniciar mag
                    //iniciar orientação


                }
                else{
                    btnStartStop.setText("START");
                }

                started = !started;
            }
        });
    }


    public void saveIntoFile(Context context,String filename, String content) {

        File file = new File(context.getFilesDir(), filename);
        String contentToSave = content.toLowerCase();

        try (FileOutputStream fos = context.openFileOutput(file.getName(), Context.MODE_APPEND)) {

            fos.write(contentToSave.getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getFilePrefix(){
        String sampleNumber = textSample.getText().toString();
        String realSteps = textRealSteps.getText().toString();
        return sampleNumber + "_" + realSteps +"_";
    }

    public String getFileSuffix(){
        String nowTime = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss").format(Calendar.getInstance().getTime());
        return nowTime + ".txt";
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()){
            case Sensor.TYPE_LINEAR_ACCELERATION:
                accelerometer.fillAccValues(event);
                String fileContentData = joinSensorsData();
                saveIntoFile(context,fileNamePrefix + accMagOrientationFile + fileNameSuffix ,fileContentData);
                break;

            case Sensor.TYPE_ROTATION_VECTOR:
                orientation.fillRotationMatrix(event);
                break;

            case Sensor.TYPE_MAGNETIC_FIELD:
                magnetometer.fillMagValues(event);
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public String joinSensorsData(){
        float [] accValues = accelerometer.getAcceleration();
        String accString = floatArrayAsString(accValues) + "," + accelerometer.getLastTimeStamp();

        float[] rotationMatrixValues = orientation.getRotationMatrix();
        String rotationMatrixString = floatArrayAsString(rotationMatrixValues) + "," + orientation.getLastTimeStamp();

        float[] rotatedAccValues = orientation.rotateVector(accValues,rotationMatrixValues);
        String rotatedAccString = floatArrayAsString(rotatedAccValues);

        float[] magValues = magnetometer.getMagValues();
        String magString = floatArrayAsString(magValues) + "," + magnetometer.getLastTimeStamp();

        String[] sList = {accString,rotationMatrixString,rotatedAccString,magString};
        return TextUtils.join(",",sList) + "\n";
    }

    public String floatArrayAsString(float[] array){
        String arrayString = array.toString();
        return arrayString.substring(1,arrayString.length()-1); //remover "[" e "]"
    }
}