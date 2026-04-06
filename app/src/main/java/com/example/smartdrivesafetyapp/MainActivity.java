package com.example.smartdrivesafetyapp;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private TextView statusText;
    private Button startButton;

    private SensorManager sensorManager;
    private Sensor accelerometer;

    private boolean monitoringEnabled = false;
    private boolean isDriving = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        statusText = findViewById(R.id.statusText);
        startButton = findViewById(R.id.startButton);

        // Initialize sensor
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        startButton.setOnClickListener(v -> {

            monitoringEnabled = !monitoringEnabled;

            if (monitoringEnabled) {

                startButton.setText("Stop Monitoring");
                statusText.setText("Monitoring Motion...");

            } else {

                startButton.setText("Start Driving Mode");
                statusText.setText("Status: Not Driving");

                isDriving = false;
            }

        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (accelerometer != null) {
            sensorManager.registerListener(
                    this,
                    accelerometer,
                    SensorManager.SENSOR_DELAY_NORMAL
            );
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (!monitoringEnabled)
            return;

        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        double acceleration =
                Math.sqrt(x*x + y*y + z*z);

        if (acceleration > 12) {

            if (!isDriving) {

                isDriving = true;
                statusText.setText("Driving Detected 🚗");

            }

        } else {

            if (isDriving) {

                isDriving = false;
                statusText.setText("No Driving Detected");

            }

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}