package com.example.anthony.financialtracking;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SettingsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class SettingsFragment extends Fragment{

    private OnFragmentInteractionListener mListener;
    TextView nameField;
    String username;
    String[] userdata;

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        username = ((MainActivity)this.getActivity()).username;

        nameField = (TextView) view.findViewById(R.id.ProfileName);

        return view;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        GetProfile getProfile = new GetProfile();
        getProfile.execute();
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void logout(View view){
        ((MainActivity)getActivity()).logout(view);
    }

    public void updateBudget(View view){
        ((MainActivity)getActivity()).updateBudget(view);
    }


    public class GetProfile extends AsyncTask<String, String, Void> {

        @Override
        protected Void doInBackground(String... arg0) {
            try {
                username = arg0[0];
                String link = "http://ec2-18-216-10-60.us-east-2.compute.amazonaws.com/MobileDev/getProfile.php";
                String data = URLEncoder.encode("username", "UTF-8") + "=" +
                        URLEncoder.encode(username, "UTF-8");

                URL url = new URL(link);
                URLConnection conn = url.openConnection();

                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                wr.write(data);
                wr.flush();

                BufferedReader reader = new BufferedReader(new
                        InputStreamReader(conn.getInputStream()));

                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                    break;
                }

                JSONObject obj = new JSONObject(sb.toString());
                userdata = new String[10];
                JSONArray jsonArray = obj.getJSONArray("userdata");
                if (jsonArray != null) {
                    int len = jsonArray.length();
                    for (int i = 0; i < len; i++) {
                        userdata[i] = (jsonArray.get(i).toString());
                    }
                }
                nameField.setText(userdata[5] + ", " + userdata[4]);

            }catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid){
            super.onPostExecute(aVoid);
            //nameField.setText("PostExecute");
        }
    }
}
