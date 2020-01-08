package com.example.ifood.com.listtodossensores;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener, AdapterView.OnItemSelectedListener {

    SensorManager mSensorManager;
    List<Sensor> mSensores;
    private TextView txtTemperatura;
    int mSensorSelecionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        txtTemperatura = findViewById(R.id.txtValores);

        mSensorManager  = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensores = mSensorManager.getSensorList(Sensor.TYPE_ALL);

        List<String> nomeSensores = new ArrayList<String>();
        for (Sensor sensor :mSensores){
            nomeSensores.add(sensor.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,android.R.layout.simple_spinner_item, nomeSensores);
        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        Spinner spn = (Spinner) findViewById(R.id.spnSensor);
        spn.setAdapter(adapter);
        spn.setOnItemSelectedListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this,mSensores.get(mSensorSelecionado),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        Toast.makeText(this,"precisao mudou" + i,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        String valores = "Valores do sensor \n";

        for (int i = 0; i <sensorEvent.values.length; i++){
            valores += "values["+i+"] = "+sensorEvent.values[i]+"\n";
        }
        txtTemperatura.setText(valores);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        mSensorSelecionado = position;
        mSensorManager.unregisterListener(this);
        mSensorManager.registerListener(
                this,
                mSensores.get(mSensorSelecionado),
                SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
