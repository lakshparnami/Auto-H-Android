package com.lucifer.auto_h.tabs;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lucifer.auto_h.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;

import static com.lucifer.auto_h.Constants.PiIP;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class ParentFragment extends Fragment {

    View v;

    public ParentFragment() {
        // Required empty public constructor
    }

    public View findViewById(int id) {
        return v.findViewById(id);
    }

    public String php() throws UnsupportedEncodingException {/*
        private EditText mEmailView;
        private EditText mNameView;
        private EditText mPhoneView;
        private EditText mConfEmailView;
        private EditText mPasswordView;
        private EditText mConfPasswordView;
      */  // Get user defined values

        // Create data variable for sent values to server
        String data = "";
        String text = "";
        BufferedReader reader = null;

        // Send data
        try {

            // Defined URL  where to send data
            URL url = new URL("http://" + PiIP + "/current_status.php");

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
                ex.printStackTrace();
            }
        }

        // Show response on activity
//        Toast.makeText(getApplicationContext(),text,Toast.LENGTH_LONG).show();

        return text;
    }

    public void getDetails() {
        try {
            JSONArray jsString = new JSONArray(php());
            int light1 = Integer.parseInt(jsString.getJSONObject(0).getString("l1"));
            int light2 = Integer.parseInt(jsString.getJSONObject(0).getString("l2"));
            int light3 = Integer.parseInt(jsString.getJSONObject(0).getString("l3"));
            int fan1 = Integer.parseInt(jsString.getJSONObject(0).getString("f1"));
            int fan2 = Integer.parseInt(jsString.getJSONObject(0).getString("f2"));
            int door = Integer.parseInt(jsString.getJSONObject(0).getString("door"));
            int garage = Integer.parseInt(jsString.getJSONObject(0).getString("garage"));
            UpdateDetails(light1, light2, light3, fan1, fan2, door, garage);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.e("Light 1", "NOT DEFINED1");
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Light 1", "NOT DEFINED2");
        }
    }

    abstract void UpdateDetails(int light1, int light2, int light3, int fan1, int fan2, int door, int garage);

    public Context getApplicationContext() {
        return getActivity().getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        textView.setText(R.string.hello_blank_fragment);
        return textView;
    }

}
