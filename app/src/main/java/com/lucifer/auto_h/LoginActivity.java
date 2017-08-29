package com.lucifer.auto_h;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.lucifer.auto_h.tabs.MainActivity;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import static com.lucifer.auto_h.Constants.ADMIN;
import static com.lucifer.auto_h.Constants.GUEST;
import static com.lucifer.auto_h.Constants.LOGGED_OUT;
import static com.lucifer.auto_h.Constants.PiIP;
import static com.lucifer.auto_h.Constants.Pin;
import static com.lucifer.auto_h.Constants.USER;

public class LoginActivity extends AppCompatActivity {
    static int i = 0;
    static EditText[] pin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        View guest = findViewById(R.id.guest_btn);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(getString(R.string.logged_in_as), LOGGED_OUT);
        editor.apply();
        pin = new EditText[4];
        i = 0;
        StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(threadPolicy);
        pin[0] = (EditText) findViewById(R.id.pin0);
        pin[1] = (EditText) findViewById(R.id.pin1);
        pin[2] = (EditText) findViewById(R.id.pin2);
        pin[3] = (EditText) findViewById(R.id.pin3);
        doAnimations();
        ToggleButton show_pwd = (ToggleButton) findViewById(R.id.showpwd);

        show_pwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    for (int x = 0; x < 4; x++)
                        pin[x].setTransformationMethod(new HideReturnsTransformationMethod());
                } else {
                    for (int x = 0; x < 4; x++)
                        pin[x].setTransformationMethod(new PasswordTransformationMethod());
                }
            }
        });
        guest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt(getString(R.string.logged_in_as), GUEST);
                editor.apply();
                startActivity(new Intent(LoginActivity.this, GuestActivity.class));
                finish();
            }
        });

        setupSocket();
    }

    private void doAnimations() {

        View guest = findViewById(R.id.guest_layout);
        guest.setVisibility(View.VISIBLE);
        Animation animation2 = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation2.setDuration(500);
        animation2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Animation animation3 = new AlphaAnimation(0.0f, 1.0f);
                animation3.setDuration(500);
                findViewById(R.id.pin_layout).setVisibility(View.VISIBLE);
                findViewById(R.id.pin_layout).startAnimation(animation3);
                findViewById(R.id.enter_pin).setVisibility(View.VISIBLE);
                findViewById(R.id.enter_pin).startAnimation(animation3);
                findViewById(R.id.forgot_password).setVisibility(View.VISIBLE);
                findViewById(R.id.forgot_password).startAnimation(animation3);
                findViewById(R.id.title).setVisibility(View.VISIBLE);
                findViewById(R.id.title).startAnimation(animation3);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        guest.startAnimation(animation2);

    }

    private void setupSocket() {
        try {
            new GetViaSocket(1234).execute();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void CheckPin() {
        String pin1 = "";
        pin1 = pin[0].getText().toString()
                + pin[1].getText().toString()
                + pin[2].getText().toString()
                + pin[3].getText().toString();
        try {
            String result = php(pin1);
            /*
            Toast.makeText(getApplicationContext(), result + pin[0].getText().toString()
                    + pin[1].getText().toString()
                    + pin[2].getText().toString()
                    + pin[3].getText().toString(), Toast.LENGTH_LONG).show();*/
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = preferences.edit();
            if (result.contains("Welcome")) {
                Pin = pin1;
                editor.putInt(getString(R.string.logged_in_as), USER);
                editor.apply();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            } else if (result.contains("Admin")) {
                editor.putInt(getString(R.string.logged_in_as), ADMIN);
                editor.apply();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
                Pin = pin1;
            } else {
                pin[1].setText("");
                pin[2].setText("");
                pin[3].setText("");
                pin[0].setText("");
//                i = 0;
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public String php(String pin) throws UnsupportedEncodingException {/*
        private EditText mEmailView;
        private EditText mNameView;
        private EditText mPhoneView;
        private EditText mConfEmailView;
        private EditText mPasswordView;
        private EditText mConfPasswordView;
      */  // Get user defined values

        // Create data variable for sent values to server
        String data = "";
        data += "&" + URLEncoder.encode("pin", "UTF-8")
                + "=" + URLEncoder.encode(pin, "UTF-8");

        String text = "";
        BufferedReader reader = null;

        // Send data
        try {

            // Defined URL  where to send data
            URL url = new URL("http://" + PiIP + "/check_pin.php");

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.nothing, R.anim.nothing);
    }

    private class GetViaSocket extends AsyncTask<Void, Character, String> {

        int x;
        char c;
        Socket s1;
        InputStream Sin;
        DataInputStream dos;
        private int port = 1234;

        public GetViaSocket() {
            super();
        }

        GetViaSocket(int port) {
            super();
            this.port = port;
        }

        @Override
        protected String doInBackground(Void... params) {
            getS();
            return null;
        }

        @Override
        protected void onProgressUpdate(Character... values) {
            x = i;
            if (x < 4) {
                pin[x].setTransformationMethod(new HideReturnsTransformationMethod());
                pin[x].setText(c + "");
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (x < 4)
                        pin[x].setTransformationMethod(new PasswordTransformationMethod());
                }
            }, 500);
            i++;
            if (i == 4) {
                System.out.println("HERE LUCY");
                try {
                    CheckPin();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        protected void onPostExecute(String s) {
            x = i = 0;
            new GetViaSocket(1234).execute();
        }

        private void getS() {
            try {   //ServerSocket s=new ServerSocket(1234);

                s1 = new Socket(PiIP, port);
                Sin = s1.getInputStream();
                dos = new DataInputStream(Sin);
                c = '\b';
                while (i < 4) {
                    if ((c = (char) dos.read()) > 0) {
                        publishProgress(c);
                    }
                }

                dos.close();
                Sin.close();
                s1.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    }
}
