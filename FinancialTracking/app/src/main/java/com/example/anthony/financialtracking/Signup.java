package com.example.anthony.financialtracking;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by Anthony on 4/24/2018.
 */

public class Signup extends AsyncTask<String, String, String> {
    private Context context;

    private LoginResponse callback;

    //flag 0 means get and 1 means post.(By default it is get.)
    public Signup(Context context) {
        this.callback = (LoginResponse) context;
    }

    protected void onPreExecute(){
    }

    @Override
    protected String doInBackground(String... arg0) {
        try{
            String email = (String)arg0[0];
            String username = (String)arg0[1];
            String password = (String)arg0[2];
            String budget = (String)arg0[3];
            String first = (String)arg0[4];
            String last = (String)arg0[5];
            String gender = (String)arg0[6];
            String dob = (String)arg0[7];

            String link="http://ec2-18-216-10-60.us-east-2.compute.amazonaws.com/MobileDev/signup.php";
            String data  = URLEncoder.encode("email", "UTF-8") + "=" +
                    URLEncoder.encode(email, "UTF-8");
            data += "&" + URLEncoder.encode("username", "UTF-8") + "=" +
                    URLEncoder.encode(username, "UTF-8");
            data += "&" + URLEncoder.encode("password", "UTF-8") + "=" +
                    URLEncoder.encode(password, "UTF-8");
            data += "&" + URLEncoder.encode("budget", "UTF-8") + "=" +
                    URLEncoder.encode(budget, "UTF-8");
            data += "&" + URLEncoder.encode("first", "UTF-8") + "=" +
                    URLEncoder.encode(first, "UTF-8");
            data += "&" + URLEncoder.encode("last", "UTF-8") + "=" +
                    URLEncoder.encode(last, "UTF-8");
            data += "&" + URLEncoder.encode("gender", "UTF-8") + "=" +
                    URLEncoder.encode(gender, "UTF-8");
            data += "&" + URLEncoder.encode("dob", "UTF-8") + "=" +
                    URLEncoder.encode(dob, "UTF-8");


            URL url = new URL(link);
            URLConnection conn = url.openConnection();

            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write( data );
            wr.flush();

            BufferedReader reader = new BufferedReader(new
                    InputStreamReader(conn.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while((line = reader.readLine()) != null) {
                sb.append(line);
                break;
            }

            return sb.toString();
        } catch(Exception e){
            return new String("Exception: " + e.getMessage());
        }
    }

    @Override
    protected void onPostExecute(String result){
        callback.processFinish(result);
    }
}
