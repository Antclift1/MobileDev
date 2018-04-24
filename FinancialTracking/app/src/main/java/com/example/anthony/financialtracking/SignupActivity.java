package com.example.anthony.financialtracking;

import android.content.Intent;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SignupActivity extends Activity implements LoginResponse{


    static final int REGISTER_REQUEST_CODE = 1;
    private EditText usernameField,passwordField,passwordField2;
    private String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        usernameField = (EditText)findViewById(R.id.SignupUser);
        passwordField = (EditText)findViewById(R.id.SignupPass);
        passwordField2 = (EditText)findViewById(R.id.SignupConfirm);


    }

    @Override
    public void finish() {
        //Toast.makeText(this, user, Toast.LENGTH_SHORT).show();
        Intent data = new Intent(this, LoginActivity.class);
        data.putExtra("Username", user);
        setResult(RESULT_OK, data);
        super.finish();
    }

    //submit the signup information
    public void submit(View view){
        String username = usernameField.getText().toString();
        if(passwordField.getText().toString() == passwordField2.getText().toString()) {
            String password = passwordField.getText().toString();

            new Signup(this).execute(username, password);
        }
        else {
            Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
        }

    }

    //function for moving back to the login screen with the button
    public void backtologin(View view){
        super.finish();
    }

    @Override
    public void processFinish(String result) {
        user = result;
        finish();
    }
}
