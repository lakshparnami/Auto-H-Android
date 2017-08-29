package com.lucifer.auto_h.admin;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.lucifer.auto_h.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;

import static com.lucifer.auto_h.Constants.PiIP;

public class AdminPanel extends AppCompatActivity {
    public static TableRecyclerAdapter mAdapter;
    ArrayList<TableRecyclerAdapter.TableLogData> itemsData = new ArrayList<>();
    String email, name, pin, phone;
    boolean admin;
    RecyclerView recyclerView;

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
        data += "&" + URLEncoder.encode("oldpin", "UTF-8")
                + "=" + URLEncoder.encode("1234", "UTF-8");

        String text = "";
        BufferedReader reader = null;

        // Send data
        try {

            // Defined URL  where to send data
            URL url = new URL("http://" + PiIP + "/userdata.php");

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

    private void getDetails() {
        JSONArray jsString = new JSONArray();
        try {
            jsString = new JSONArray(php());
//            Toast.makeText(getApplicationContext(),jsString.toString(),Toast.LENGTH_LONG).show();
            itemsData.clear();

            for (int i = 0; i < jsString.length(); i++) {
                email = jsString.getJSONObject(i).getString("email");
                name = jsString.getJSONObject(i).getString("name");
                phone = jsString.getJSONObject(i).getString("phone");
                pin = jsString.getJSONObject(i).getString("pin");
                admin = jsString.getJSONObject(i).getString("admin").equalsIgnoreCase("1");
                itemsData.add(new TableRecyclerAdapter.TableLogData(name, email, pin, phone, admin));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_admin_panel);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(threadPolicy);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminPanel.this, SignUpActivity.class));
                finish();
            }
        });
      /* Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
               .setAction("Action", null).show();*/
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        getDetails();
        mAdapter = new TableRecyclerAdapter(itemsData);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(mAdapter);
        Collections.reverse(itemsData);
    }

}
