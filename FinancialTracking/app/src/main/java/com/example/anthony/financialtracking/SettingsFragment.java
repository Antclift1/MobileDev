package com.example.anthony.financialtracking;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import static android.app.Activity.RESULT_OK;
import static com.example.anthony.financialtracking.SignupActivity.getUser;
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
    String username;
    JSONObject userdata;

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        profilePicture = (ImageView)view.findViewById(R.id.profilePicture);
        name = (TextView)view.findViewById(R.id.name);
        userdata = ((MainActivity)getActivity()).profileData;

        try {
            name.setText(userdata.getString("Lastname") + ", " + userdata.getString("Firstname"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(savedInstanceState!=null){
            profileImageBitmap = savedInstanceState.getParcelable("BitmapImage");
            profilePicture.setImageBitmap(profileImageBitmap);
        }
        profilePicture.setOnClickListener(this);
        return view;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState!=null) {
            profileImageBitmap = savedInstanceState.getParcelable("BitmapImage");
            profilePicture.setImageBitmap(profileImageBitmap);
        }

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
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelable("BitmapImage", profileImageBitmap);

    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState!=null) {
            profileImageBitmap = savedInstanceState.getParcelable("BitmapImage");
            profilePicture.setImageBitmap(profileImageBitmap);
        }
    }

    @Override
    public void onAttach(Context context) {
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



}
