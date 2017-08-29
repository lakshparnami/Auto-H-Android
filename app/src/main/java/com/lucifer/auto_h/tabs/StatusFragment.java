package com.lucifer.auto_h.tabs;


import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.lucifer.auto_h.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class StatusFragment extends ParentFragment {


    public StatusFragment() {
        // Required empty public constructor
    }


    void UpdateDetails(int light1, int light2, int light3, int fan1, int fan2, int door, int garage) {

        if (light1 > 0)
            ((ToggleButton) findViewById(R.id.light_1_switch)).setChecked(true);
        else
            ((ToggleButton) findViewById(R.id.light_1_switch)).setChecked(false);
        if (light2 > 0)
            ((ToggleButton) findViewById(R.id.light_2_switch)).setChecked(true);
        else
            ((ToggleButton) findViewById(R.id.light_2_switch)).setChecked(false);
        if (light3 > 0)
            ((ToggleButton) findViewById(R.id.light_3_switch)).setChecked(true);
        else
            ((ToggleButton) findViewById(R.id.light_3_switch)).setChecked(false);
        if (fan1 > 0)
            ((ToggleButton) findViewById(R.id.fan_1_switch)).setChecked(true);
        else
            ((ToggleButton) findViewById(R.id.fan_1_switch)).setChecked(false);
        if (fan2 > 0)
            ((ToggleButton) findViewById(R.id.fan_2_switch)).setChecked(true);
        else
            ((ToggleButton) findViewById(R.id.fan_2_switch)).setChecked(false);
        if (door > 0)
            ((ToggleButton) findViewById(R.id.myDoorSwitch)).setChecked(true);
        else
            ((ToggleButton) findViewById(R.id.myDoorSwitch)).setChecked(false);
        if (garage > 0)
            ((ToggleButton) findViewById(R.id.mygarageDoorSwitch)).setChecked(true);
        else
            ((ToggleButton) findViewById(R.id.mygarageDoorSwitch)).setChecked(false);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_status, container, false);
        StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(threadPolicy);

        getDetails();

        v.findViewById(R.id.fan_1_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "trying", Toast.LENGTH_SHORT).show();
                getDetails();
                Toast.makeText(getApplicationContext(), "trying2222", Toast.LENGTH_SHORT).show();
            }
        });
        return v;
    }

}
