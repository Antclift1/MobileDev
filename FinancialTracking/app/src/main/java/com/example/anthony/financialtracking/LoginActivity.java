package com.example.anthony.financialtracking;

import android.content.Intent;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class LoginActivity extends Activity implements LoginResponse{

    static final int REGISTER_REQUEST_CODE = 1;
    private EditText usernameField,passwordField;
    private String user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameField = (EditText)findViewById(R.id.UserField);
        passwordField = (EditText)findViewById(R.id.PassField);



    }

    @Override
    public void finish() {
        //Toast.makeText(this, user, Toast.LENGTH_SHORT).show();
        Intent data = new Intent(this, MainActivity.class);
        data.putExtra("Username", user);
        setResult(RESULT_OK, data);
        super.finish();
    }

    public void login(View view){
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();

        new Login(this).execute(username, password);

    }


    public void gotosignup(View view){
        Intent signupIntent = new Intent(this, SignupActivity.class);
        startActivityForResult(signupIntent, REGISTER_REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REGISTER_REQUEST_CODE && resultCode == RESULT_OK) {
            user = data.getExtras().getString("Username");
            finish();
        }
    }

    @Override
    public void processFinish(String result) {
        user = result;
        //Toast.makeText(this, user, Toast.LENGTH_SHORT).show();
        if(user.equals("Login Failed")){
            Toast.makeText(this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
        }
        else{
            finish();

        }
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Please login or create an account.", Toast.LENGTH_SHORT).show();
    }
}
