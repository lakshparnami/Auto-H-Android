package com.lucifer.auto_h.admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
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

import static android.view.View.GONE;
import static com.lucifer.auto_h.Constants.PiIP;

public class Preferences extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final SeekBar light_1_seekbar = (SeekBar) findViewById(R.id.light_1_seekbar);
        final SeekBar light_2_seekbar = (SeekBar) findViewById(R.id.light_2_seekbar);
        final SeekBar light_3_seekbar = (SeekBar) findViewById(R.id.light_3_seekbar);
        final SeekBar light_4_seekbar = (SeekBar) findViewById(R.id.light_4_seekbar);
        final SeekBar fan_1_seekbar = (SeekBar) findViewById(R.id.fan_1_seekbar);
        final SeekBar fan_2_seekbar = (SeekBar) findViewById(R.id.fan_2_seekbar);
        final Switch voiceS = (Switch) findViewById(R.id.voiceSwitch);
        findViewById(R.id.save).setVisibility(GONE);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
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
                    String s = GetText(l1, l2, l3, l4, f1, f2, getIntent().getStringExtra("Pin"), voice);
                    Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                    startActivity(new Intent(Preferences.this, AdminPanel.class));
                    finish();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();

                }
            }
        });
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
            URL url = new URL("http://" + PiIP + "/insertpref.php");

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

    @Override
    public void onBackPressed() {

    }
}
