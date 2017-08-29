package com.lucifer.auto_h.tabs;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import static com.lucifer.auto_h.Constants.PiIP;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReportFragment extends Fragment {


    double graph2LastXValue = 0;
    LineGraphSeries<DataPoint> series, series2;
    double mLastRandom = 0;
    Random mRand = new Random();
    String time;
    int l1, l2, l3, l4, f1, f2, pump;
    GraphView graph, graph2;
    JSONArray jsString;

    public ReportFragment() {
        // Required empty public constructor
    }

    public static String getDate(long milliSeconds) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
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
        data += "&" + URLEncoder.encode("oldpin", "UTF-8")
                + "=" + URLEncoder.encode("1234", "UTF-8");

        String text = "";
        BufferedReader reader = null;

        // Send data
        try {

            // Defined URL  where to send data
            URL url = new URL("http://" + PiIP + "/log.php");

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
        jsString = new JSONArray();
        try {
            jsString = new JSONArray(php());
//            Toast.makeText(getApplicationContext(),jsString.toString(),Toast.LENGTH_LONG).show();


            for (int i = 0; i < jsString.length(); i++) {
                l1 = Integer.parseInt(jsString.getJSONObject(i).getString("l1"));
                l2 = Integer.parseInt(jsString.getJSONObject(i).getString("l2"));
                l3 = Integer.parseInt(jsString.getJSONObject(i).getString("l3"));
                l4 = Integer.parseInt(jsString.getJSONObject(i).getString("l4"));
                f1 = Integer.parseInt(jsString.getJSONObject(i).getString("f1"));
                f2 = Integer.parseInt(jsString.getJSONObject(i).getString("f2"));
                pump = Integer.parseInt(jsString.getJSONObject(i).getString("pump"));
                time = jsString.getJSONObject(i).getString("timestarted");

                int light = l1 + l2 + l3 + l4;
                int fan = f1 + f2;
                Date d = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    d = sdf.parse(time);
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
                series.appendData(new DataPoint(d, light), true, 100);
                series2.appendData(new DataPoint(d, fan), true, 100);
               /* if (i == 0) {
                    graph.getViewport().setMinX(d.getTime());
                }
                if (i == jsString.length() - 1) {
                    graph.getViewport().setMaxX(d.getTime());
                }
*/
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_report, container, false);
        graph = (GraphView) v.findViewById(R.id.graph);
        graph2 = (GraphView) v.findViewById(R.id.graph2);
        graph.getGridLabelRenderer().setLabelFormatter(
                new DefaultLabelFormatter() {
                    @Override
                    public String formatLabel(double value, boolean isValueX) {
                        if (isValueX) {

                            return getDate((long) value);
                        } else
                            return super.formatLabel(value, false);
                    }
                });
        graph2.getGridLabelRenderer().setLabelFormatter(
                new DefaultLabelFormatter() {
                    @Override
                    public String formatLabel(double value, boolean isValueX) {
                        if (isValueX) {

                            return getDate((long) value);
                        } else
                            return super.formatLabel(value, false);
                    }
                });
        series = new LineGraphSeries<>();
        series2 = new LineGraphSeries<>();
        getDetails();
        series.setColor(Color.parseColor("#006644"));
        series2.setColor(Color.parseColor("#0440ff"));
        graph.addSeries(series);
        graph2.addSeries(series2);
        graph.getGridLabelRenderer().setHumanRounding(false);
        graph.getViewport().setScrollable(true);
        graph.getViewport().setScalable(true);
        graph2.getGridLabelRenderer().setHumanRounding(false);
        graph2.getViewport().setScrollable(true);
        graph2.getViewport().setScalable(true);
        graph.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE && v.getParent() != null) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                }

                return false;
            }
        });
        graph2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE && v.getParent() != null) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                }

                return false;
            }
        });
/*
        Runnable mTimer = new Runnable() {
            @Override
            public void run() {
                if (graph2LastXValue > 20)
                    graph.getViewport().setMinX(graph2LastXValue - 20);
                else
                    graph.getViewport().setMinX(0);
                graph.getViewport().setMaxXAxisSize(200d);
                graph2LastXValue += 1d;
                series.appendData(new DataPoint(graph2LastXValue, getRandom()), true, 100);
                series2.appendData(new DataPoint(graph2LastXValue, getRandom()), true, 100);
                new Handler().postDelayed(this, 1000);
            }
        };
        nw Handler().postDelayed(mTimer, 1000);
        graph.getViewport().setScrollable(true);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);*/
        return v;
    }

}
