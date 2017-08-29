package com.lucifer.auto_h.admin;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.lucifer.auto_h.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

import static com.lucifer.auto_h.Constants.PiIP;


class TableRecyclerAdapter extends RecyclerView.Adapter<TableRecyclerAdapter.ViewHolder> {
    private static boolean edited = false;
    private static String oldPin = "0000";
    private final ArrayList<TableLogData> itemsData;
    // Replace the contents of a view (invoked by the layout_recycler manager)
    String name, email, pin, phone;
    boolean admin = false;

    TableRecyclerAdapter(ArrayList<TableLogData> itemsData) {
        this.itemsData = itemsData;
    }

    // Create new views (invoked by the layout_recycler manager)
    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        // create a new view
        final View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_users, parent, false);
        return new ViewHolder(itemLayoutView);
    }

    private void setEditable(ViewHolder viewHolder) {
        viewHolder.email.setFocusableInTouchMode(true);
        viewHolder.name.setFocusableInTouchMode(true);
        viewHolder.pin.setFocusableInTouchMode(true);
        viewHolder.phone.setFocusableInTouchMode(true);
        viewHolder.edit.setVisibility(View.GONE);
        viewHolder.save.setVisibility(View.VISIBLE);
        viewHolder.undo.setVisibility(View.VISIBLE);
        viewHolder.adminCheckbox.setVisibility(View.VISIBLE);
        name = viewHolder.name.getText().toString();
        email = viewHolder.email.getText().toString();
        pin = viewHolder.pin.getText().toString();
        phone = viewHolder.phone.getText().toString();
        admin = viewHolder.adminCheckbox.isChecked();
    }

    private void setNonEditable(ViewHolder viewHolder) {

        viewHolder.email.setFocusable(false);
        viewHolder.name.setFocusable(false);
        viewHolder.pin.setFocusable(false);
        viewHolder.phone.setFocusable(false);
        viewHolder.edit.setVisibility(View.VISIBLE);
        viewHolder.save.setVisibility(View.GONE);
        viewHolder.undo.setVisibility(View.GONE);
        viewHolder.adminCheckbox.setVisibility(View.GONE);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {


        // - get data from your itemsData at this position
        // - replace the contents of the view with that itemsData
        viewHolder.email.setText(itemsData.get(position).getEmail());
        viewHolder.name.setText(itemsData.get(position).getName());
        viewHolder.pin.setText(itemsData.get(position).getPin());
        viewHolder.phone.setText(itemsData.get(position).getPhone());
        viewHolder.adminCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    viewHolder.admin.setVisibility(View.VISIBLE);
                else
                    viewHolder.admin.setVisibility(View.GONE);
            }
        });
        if (itemsData.get(position).isAdmin()) {
            viewHolder.admin.setVisibility(View.VISIBLE);
            viewHolder.adminCheckbox.setChecked(true);
        }
        viewHolder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEditable(viewHolder);

            }
        });
        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    php(String.valueOf(viewHolder.pin.getText()));
                    itemsData.remove(position);
                    AdminPanel.mAdapter.notifyItemRemoved(position);
                    notifyItemRangeChanged(position, itemsData.size());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
        viewHolder.undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNonEditable(viewHolder);
                viewHolder.name.setText(name);
                viewHolder.email.setText(email);
                viewHolder.phone.setText(phone);
                viewHolder.pin.setText(pin);
                viewHolder.adminCheckbox.setChecked(admin);
            }
        });
        viewHolder.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNonEditable(viewHolder);
                int i = 0;

                name = viewHolder.name.getText().toString();
                email = viewHolder.email.getText().toString();
                String newpin = viewHolder.pin.getText().toString();
                phone = viewHolder.phone.getText().toString();
                String auth = "0";
                if (viewHolder.adminCheckbox.isChecked()) {
                    auth = "1";
                }

                try {
                    php(pin, newpin, email, name, phone, auth);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public String php(String oldPin, String newPin, String email, String name, String phno, String auth) throws UnsupportedEncodingException {/*
        private EditText mEmailView;
        private EditText mNameView;
        private EditText mPhoneView;
        private EditText mConfEmailView;
        private EditText mPasswordView;
        private EditText mConfPasswordView;
      */  // Get user defined values

        // Create data variable for sent values to server
        String data = URLEncoder.encode("oldpin", "UTF-8")
                + "=" + URLEncoder.encode(oldPin, "UTF-8");
        data += "&" + URLEncoder.encode("newpin", "UTF-8")
                + "=" + URLEncoder.encode(newPin, "UTF-8");
        data += "&" + URLEncoder.encode("phno", "UTF-8")
                + "=" + URLEncoder.encode(phno, "UTF-8");
        data += "&" + URLEncoder.encode("auth", "UTF-8")
                + "=" + URLEncoder.encode(auth, "UTF-8");
        data += "&" + URLEncoder.encode("name", "UTF-8")
                + "=" + URLEncoder.encode(name, "UTF-8");
        data += "&" + URLEncoder.encode("email", "UTF-8")
                + "=" + URLEncoder.encode(email, "UTF-8");
        Log.e("MYSTUFF", data);
        String text = "";
        BufferedReader reader = null;

        // Send data
        try {

            // Defined URL  where to send data
            URL url = new URL("http://" + PiIP + "/updateuser.php");

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
        Log.e("MYSTUFF", text);
        return text;
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
        String data = URLEncoder.encode("pin", "UTF-8")
                + "=" + URLEncoder.encode(pin, "UTF-8");
        Log.e("MYSTUFF", data);
        String text = "";
        BufferedReader reader = null;

        // Send data
        try {

            // Defined URL  where to send data
            URL url = new URL("http://" + PiIP + "/deleteuser.php");

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
        Log.e("MYSTUFF", text);
        return text;
    }


    // Return the size of your itemsData (invoked by the layout_recycler manager)
    @Override
    public int getItemCount() {
        return itemsData.size();
    }
    // inner class to hold a reference to each item of RecyclerView

    static class ViewHolder extends RecyclerView.ViewHolder {
        EditText name, email, pin, phone;
        ImageView admin;
        ImageButton edit, delete, save, undo;
        CheckBox adminCheckbox;
//        public final RelativeLayout listItemLayout;

        ViewHolder(final View itemLayoutView) {
            super(itemLayoutView);
            name = (EditText) itemLayoutView.findViewById(R.id.name);
            pin = (EditText) itemLayoutView.findViewById(R.id.pin);
            phone = (EditText) itemLayoutView.findViewById(R.id.phone);
            email = (EditText) itemLayoutView.findViewById(R.id.email);
            admin = (ImageView) itemLayoutView.findViewById(R.id.shield);
            edit = (ImageButton) itemLayoutView.findViewById(R.id.edit);
            delete = (ImageButton) itemLayoutView.findViewById(R.id.delete);
            save = (ImageButton) itemLayoutView.findViewById(R.id.save);
            adminCheckbox = (CheckBox) itemLayoutView.findViewById(R.id.adminCheckbox);
            undo = (ImageButton) itemLayoutView.findViewById(R.id.undo);
        }


    }

    static class TableLogData {


        String name, email, pin, phone;
        boolean admin;

        TableLogData(String name, String email, String pin, String phone, boolean admin) {
            this.name = name;
            this.email = email;
            this.pin = pin;
            this.phone = phone;
            this.admin = admin;
        }

        public String getName() {
            return name;
        }

        String getEmail() {
            return email;
        }

        String getPin() {
            return pin;
        }

        String getPhone() {
            return phone;
        }

        public boolean isAdmin() {
            return admin;
        }

    }
}