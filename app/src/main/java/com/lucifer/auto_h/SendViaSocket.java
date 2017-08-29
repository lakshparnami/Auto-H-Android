package com.lucifer.auto_h;

import android.os.AsyncTask;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import static com.lucifer.auto_h.Constants.PiIP;

public class SendViaSocket extends AsyncTask<String, Void, Void> {

    private int port = 1234;

    public SendViaSocket() {
        super();
    }

    public SendViaSocket(int port) {
        super();
        this.port = port;
    }

    private void load(final String s) throws IOException {
        try {   //ServerSocket s=new ServerSocket(1234);

            Socket s1 = new Socket(PiIP, port);
            OutputStream Sout = s1.getOutputStream();
            DataOutputStream dos = new DataOutputStream(Sout);
            /*for(int i=0;i<s.length()-1;i++)
                dos.write(s.charAt(i));
           */
            dos.write(s.charAt(0));
            /*dos.close();
            Sout.close();
            s1.close();*/
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    protected Void doInBackground(String... params) {
        try {
            load(params[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}