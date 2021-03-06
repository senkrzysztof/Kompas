package com.example.xenon.kompas;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    private TextView tw;

    private Sensor mSensor;
    private ImageView tarcza;
    private ImageView igla;
    private SensorManager manager;
    private static final int FROM_RADS_TO_DEGS = -57;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tw=(TextView)findViewById(R.id.textView);
        igla=(ImageView)findViewById(R.id.imageView2);
        tarcza=(ImageView)findViewById(R.id.imageView);


        manager=(SensorManager)getSystemService(SENSOR_SERVICE);

        List<Sensor> lista = manager.getSensorList(Sensor.TYPE_ALL);

        String temp="";
        for(Sensor el : lista){
            temp+=el.getName();
            temp+=" ";
        }
        tw.setText(temp);


        mSensor = manager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        manager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);

        //tarcza.setRotation(40);




    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor == mSensor) {
            if (event.values.length > 4) {
                float[] truncatedRotationVector = new float[4];
                System.arraycopy(event.values, 0, truncatedRotationVector, 0, 4);
                update(truncatedRotationVector);
            } else {
                update(event.values);
            }
        }
    }

    private void update(float[] vectors) {
        float[] rotationMatrix = new float[9];
        SensorManager.getRotationMatrixFromVector(rotationMatrix, vectors);
        int worldAxisX = SensorManager.AXIS_X;
        int worldAxisZ = SensorManager.AXIS_Z;
        float[] adjustedRotationMatrix = new float[9];
        SensorManager.remapCoordinateSystem(rotationMatrix, worldAxisX, worldAxisZ, adjustedRotationMatrix);
        float[] orientation = new float[3];
        SensorManager.getOrientation(adjustedRotationMatrix, orientation);
        float pitch = orientation[1] * FROM_RADS_TO_DEGS;
        float roll = orientation[2] * FROM_RADS_TO_DEGS;
        //((TextView)findViewById(R.id.textView)).setText("Pitch: "+pitch);
        //((TextView)findViewById(R.id.textView)).setText("Roll: "+roll);

        tarcza.setRotation(roll);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
