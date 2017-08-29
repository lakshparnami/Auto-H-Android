package com.lucifer.auto_h.tabs;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.lucifer.auto_h.R;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import static com.lucifer.auto_h.Constants.PiIP;
import static com.lucifer.auto_h.Constants.Pin;


/**
 * A simple {@link Fragment} subclass.
 */
public class ControlFragment extends ParentFragment {

    Socket s1;
    OutputStream Sout;
    DataOutputStream dos;

    public ControlFragment() {
        // Required empty public constructor
    }

    void UpdateDetails(int light1, int light2, int light3, int fan1, int fan2, int door, int garage) {

        if (light1 > 0)
            ((Switch) findViewById(R.id.light_1_switch)).setChecked(true);
        else
            ((Switch) findViewById(R.id.light_1_switch)).setChecked(false);
        if (light2 > 0)
            ((Switch) findViewById(R.id.light_2_switch)).setChecked(true);
        else
            ((Switch) findViewById(R.id.light_2_switch)).setChecked(false);
        if (light3 > 0)
            ((Switch) findViewById(R.id.light_3_switch)).setChecked(true);
        else
            ((Switch) findViewById(R.id.light_3_switch)).setChecked(false);
        if (fan1 > 0)
            ((Switch) findViewById(R.id.fan_1_switch)).setChecked(true);
        else
            ((Switch) findViewById(R.id.fan_1_switch)).setChecked(false);
        if (fan2 > 0)
            ((Switch) findViewById(R.id.fan_2_switch)).setChecked(true);
        else
            ((Switch) findViewById(R.id.fan_2_switch)).setChecked(false);
        if (door > 0)
            ((Switch) findViewById(R.id.myDoorSwitch)).setChecked(true);
        else
            ((Switch) findViewById(R.id.myDoorSwitch)).setChecked(false);
        if (garage > 0)
            ((Switch) findViewById(R.id.mygarageDoorSwitch)).setChecked(true);
        else
            ((Switch) findViewById(R.id.mygarageDoorSwitch)).setChecked(false);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_control, container, false);
        getDetails();
        Switch l1 = (Switch) v.findViewById(R.id.light_1_switch);
        Switch l2 = (Switch) v.findViewById(R.id.light_2_switch);
        Switch l3 = (Switch) v.findViewById(R.id.light_3_switch);
        Switch f1 = (Switch) v.findViewById(R.id.fan_1_switch);
        Switch f2 = (Switch) v.findViewById(R.id.fan_2_switch);
        Switch door = (Switch) v.findViewById(R.id.myDoorSwitch);
        Switch garage = (Switch) v.findViewById(R.id.mygarageDoorSwitch);
        Pin = "6002";
        l1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                execute("l1" + (isChecked ? "1" : "0") + Pin);
            }
        });
        l2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                execute("l2" + (isChecked ? "1" : "0") + Pin);
            }
        });
        l3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                execute("l3" + (isChecked ? "1" : "0") + Pin);
            }
        });
        f1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                execute("f1" + (isChecked ? "1" : "0") + Pin);
            }
        });
        f2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                execute("f2" + (isChecked ? "1" : "0") + Pin);
            }
        });
        door.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                execute("d1" + (isChecked ? "1" : "0") + Pin);
            }
        });
        garage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                execute("d2" + (isChecked ? "1" : "0") + Pin);
            }
        });

//        v.findViewById(R.id.myLightSwitch);

        return v;
    }

    private void execute(String s) {
        try {
            dos.write('b');
            for (int i = 0; i < s.length(); i++)
                dos.write(s.charAt(i));
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            setupSocketConnection();
        }
    }

    void setupSocketConnection() {
        try {
            s1 = new Socket(PiIP, 1234);
            Sout = s1.getOutputStream();
            dos = new DataOutputStream(Sout);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            if (dos != null && Sout != null && s1 != null) {
                dos.close();
                Sout.close();
                s1.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
