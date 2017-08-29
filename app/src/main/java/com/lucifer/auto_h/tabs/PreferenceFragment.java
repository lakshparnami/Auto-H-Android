package com.lucifer.auto_h.tabs;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;

import com.lucifer.auto_h.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import static com.lucifer.auto_h.Constants.PiIP;
import static com.lucifer.auto_h.Constants.Pin;


/**
 * A simple {@link Fragment} subclass.
 */
public class PreferenceFragment extends ParentFragment {


    public PreferenceFragment() {
        // Required empty public constructor
    }

    @Override
    void UpdateDetails(int light1, int light2, int light3, int fan1, int fan2, int door, int garage) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_preference, container, false);
        final SeekBar light_1_seekbar = (SeekBar) findViewById(R.id.light_1_seekbar);
        final SeekBar light_2_seekbar = (SeekBar) findViewById(R.id.light_2_seekbar);
        final SeekBar light_3_seekbar = (SeekBar) findViewById(R.id.light_3_seekbar);
        final SeekBar light_4_seekbar = (SeekBar) findViewById(R.id.light_4_seekbar);
        final SeekBar fan_1_seekbar = (SeekBar) findViewById(R.id.fan_1_seekbar);
        final SeekBar fan_2_seekbar = (SeekBar) findViewById(R.id.fan_2_seekbar);
        final Switch voiceS = (Switch) findViewById(R.id.voiceSwitch);
        CardView fab = (CardView) findViewById(R.id.save);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String l1 = String.valueOf(light_1_seekbar.getProgress());
                String l2 = String.valueOf(light_2_seekbar.getProgress());
                String l3 = String.valueOf(light_3_seekbar.getProgress());
                String l4 = String.valueOf(light_4_seekbar.getProgress());
                String f1 = String.valueOf(fan_1_seekbar.getProgress());
                String f2 = String.valueOf(fan_2_seekbar.getProgress());
                String voice = (voiceS.isChecked()) ? "1" : "0";
                try {
                    String s = GetText(l1, l2, l3, l4, f1, f2, Pin, voice);
                    Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();

                }
            }
        });
        return v;
    }

    public String GetText(String l1, String l2, String l3, String l4, String f1, String f2, String pin, String voice) throws UnsupportedEncodingException {

        // Create data variable for sent values to server

        String data = URLEncoder.encode("l1", "UTF-8")
                + "=" + URLEncoder.encode(l1, "UTF-8");

        data += "&" + URLEncoder.encode("l2", "UTF-8") + "="
                + URLEncoder.encode(l2, "UTF-8");

        data += "&" + URLEncoder.encode("l3", "UTF-8")
                + "=" + URLEncoder.encode(l3, "UTF-8");

        data += "&" + URLEncoder.encode("l4", "UTF-8")
                + "=" + URLEncoder.encode(l4, "UTF-8");

        data += "&" + URLEncoder.encode("f1", "UTF-8")
                + "=" + URLEncoder.encode(f1, "UTF-8");

        data += "&" + URLEncoder.encode("f2", "UTF-8")
                + "=" + URLEncoder.encode(f2, "UTF-8");

        data += "&" + URLEncoder.encode("pin", "UTF-8")
                + "=" + URLEncoder.encode(pin, "UTF-8");
        data += "&" + URLEncoder.encode("voice", "UTF-8")
                + "=" + URLEncoder.encode(voice, "UTF-8");

        String text = "";
        BufferedReader reader = null;

        // Send data
        try {

            // Defined URL  where to send data
            URL url = new URL("http://" + PiIP + "/updatepref.php");

            // Send POST data request

            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();

            // Get the server response

            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line).append("\n");
            }


            text = sb.toString();
        } catch (Exception ex) {
            Log.e("Send", ex.toString());
            //Toast.makeText(getApplicationContext(),""+ex,Toast.LENGTH_SHORT).show();
            ex.printStackTrace();
        } finally {
            try {

                if (reader != null) {
                    reader.close();
                }
            } catch (Exception ex) {

            }
        }

        // Show response on activity
//        Toast.makeText(getApplicationContext(),text,Toast.LENGTH_LONG).show();

        return text;
    }

/*
    void showView(View view) {
       Animation animation = new  ScaleAnimation(1.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.0f);
       animation.setDuration(300);
       view.setVisibility(View.VISIBLE);
       view.startAnimation(animation);
    }

    void hideView(View view) {
       Animation animation = new  ScaleAnimation(1.0f, 1.0f, 1.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.0f);
       animation.setDuration(300);
       view.setVisibility(View.GONE);
       view.startAnimation(animation);
    }*/
}
