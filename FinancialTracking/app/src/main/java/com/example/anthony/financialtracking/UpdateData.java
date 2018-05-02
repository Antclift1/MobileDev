package com.example.anthony.financialtracking;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by Anthony on 5/2/2018.
 */

public class UpdateData extends AsyncTask<double[], String, String>{


    private LoginResponse callback;

    String type;
    String username;
    String link;
    String data;

    public UpdateData(Context context, String username, String type) {
        this.type = type;
        this.username = username;
        this.callback = (LoginResponse) context;
    }

    protected void onPreExecute(){
    }

    @Override
    protected String doInBackground(double[]... arg0) {
        try{
            int[] budgetdata = new int[10];
            for(int i = 0; i < 10; i++){
                if(i < arg0[0].length)
                    budgetdata[i] = (int)arg0[0][i];
                else
                    budgetdata[i] = 0;
            }


            int i;
            //if(type == "budget") {
                link = "http://ec2-18-216-10-60.us-east-2.compute.amazonaws.com/MobileDev/setBudget.php";
                data = URLEncoder.encode("username", "UTF-8") + "=" +
                        URLEncoder.encode(username, "UTF-8");
                data += "&" + URLEncoder.encode("bud1", "UTF-8") + "=" +
                        URLEncoder.encode(Integer.toString(budgetdata[0]), "UTF-8");
                data += "&" + URLEncoder.encode("bud2", "UTF-8") + "=" +
                        URLEncoder.encode(Integer.toString(budgetdata[1]), "UTF-8");
                data += "&" + URLEncoder.encode("bud3", "UTF-8") + "=" +
                        URLEncoder.encode(Integer.toString(budgetdata[2]), "UTF-8");
                data += "&" + URLEncoder.encode("bud4", "UTF-8") + "=" +
                        URLEncoder.encode(Integer.toString(budgetdata[3]), "UTF-8");
                data += "&" + URLEncoder.encode("bud5", "UTF-8") + "=" +
                        URLEncoder.encode(Integer.toString(budgetdata[4]), "UTF-8");
                data += "&" + URLEncoder.encode("bud6", "UTF-8") + "=" +
                        URLEncoder.encode(Integer.toString(budgetdata[5]), "UTF-8");
                data += "&" + URLEncoder.encode("bud7", "UTF-8") + "=" +
                        URLEncoder.encode(Integer.toString(budgetdata[6]), "UTF-8");
                data += "&" + URLEncoder.encode("bud8", "UTF-8") + "=" +
                        URLEncoder.encode(Integer.toString(budgetdata[7]), "UTF-8");
                data += "&" + URLEncoder.encode("bud9", "UTF-8") + "=" +
                        URLEncoder.encode(Integer.toString(budgetdata[8]), "UTF-8");
                data += "&" + URLEncoder.encode("bud10", "UTF-8") + "=" +
                        URLEncoder.encode(Integer.toString(budgetdata[9]), "UTF-8");


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
            return data;
        }
    }


    @Override
    protected void onPostExecute(String result){
        callback.processFinish(result);
    }

}

