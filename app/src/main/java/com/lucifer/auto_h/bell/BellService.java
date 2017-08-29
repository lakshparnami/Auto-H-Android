package com.lucifer.auto_h.bell;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import static com.lucifer.auto_h.Constants.PiIP;

public class BellService extends Service {
    public BellService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        new GetViaSocket(1235).execute();
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private class GetViaSocket extends AsyncTask<Void, String, String> {

        char c;
        Socket s1;
        String s;
        InputStream Sin;
        DataInputStream dos;
        private int port = 1235;

        public GetViaSocket() {
            super();
        }

        GetViaSocket(int port) {
            super();
            this.port = port;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                getS();
            } catch (IOException e) {
                //e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            if (values[0].contains("bell") || values[0].contains("Bell")) {
                startActivity(new Intent(getApplicationContext(), BellActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                s = "";
                values[0] = "";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                dos.close();
                Sin.close();
                s1.close();
            } catch (NullPointerException e) {
                ////e.printStackTrace();
            } catch (IOException e) {
                //e.printStackTrace();
            }
            new GetViaSocket(1235).execute();
        }

        private void getS() throws IOException {
            try {   //ServerSocket s=new ServerSocket(1234);

                s1 = new Socket(PiIP, port);
                Sin = s1.getInputStream();
                dos = new DataInputStream(Sin);
                c = '\b';
                while (s1.isConnected()) {
                    if ((c = (char) dos.read()) > 0) {
                        s = s + c;
                        publishProgress(s);
                    }
                }

            } catch (IOException e) {
                //e.printStackTrace();
            }


        }

    }

}
