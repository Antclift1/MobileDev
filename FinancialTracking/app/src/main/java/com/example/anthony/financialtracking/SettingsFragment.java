package com.example.anthony.financialtracking;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import static android.app.Activity.RESULT_OK;
//import static com.example.anthony.financialtracking.SignupActivity.getUser;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
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
public class SettingsFragment extends Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;
    ImageView profilePicture;
    TextView name;
    Button budget;
    String username;
    JSONObject userdata;
    private RecordViewModel mRecordViewModel;
    Button populate, clear;

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        profilePicture = (ImageView)view.findViewById(R.id.profilePicture);
        mRecordViewModel = ViewModelProviders.of(this).get(RecordViewModel.class);
        /*name = (TextView)view.findViewById(R.id.name);
        username = ((MainActivity)getActivity()).getLogin();

        try {
            name.setText(userdata.getString("Lastname") + ", " + userdata.getString("Firstname"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(savedInstanceState!=null){
            profileImageBitmap = savedInstanceState.getParcelable("BitmapImage");
            profilePicture.setImageBitmap(profileImageBitmap);
        }
        */
        populate=view.findViewById(R.id.populate);
        populate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                populate();
            }
        });
        clear=view.findViewById(R.id.clear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clear();
            }
        });

        profilePicture.setOnClickListener(this);
        budget=view.findViewById(R.id.personalBudget);
        budget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startBudget();
            }
        });
        return view;
    }
    static final int REQUEST_IMAGE_CAPTURE = 1;
    protected void onTakePicture(View view){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,REQUEST_IMAGE_CAPTURE);
    }
    Bitmap profileImageBitmap = null;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_IMAGE_CAPTURE&&
                resultCode==RESULT_OK){
            Bundle extras = data.getExtras();
            profileImageBitmap=(Bitmap)extras.get("data");
            profilePicture.setImageBitmap(profileImageBitmap);
        }
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void startBudget(){
        Intent intent = new Intent(this.getContext(), SetBudgetActivity.class);
        startActivity(intent);
    }
    @Override
    public void onAttach(Context context) {
        GetProfile getProfile = new GetProfile();
        getProfile.execute(username);
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

    @Override
    public void onClick(View view) {
        if(view.getId() == profilePicture.getId()){
            onTakePicture(view);
        }
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

    public void populate(){
        mRecordViewModel.populateDatabase();
    }
    public void clear(){
        mRecordViewModel.clearDatabase();
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
                name.setText(obj.getString("Lastname") + ", " + obj.getString("Firstname"));

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
