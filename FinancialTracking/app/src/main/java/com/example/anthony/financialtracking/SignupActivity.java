package com.example.anthony.financialtracking;

import android.content.Intent;
import android.app.Activity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.regex.Pattern;

public class SignupActivity extends Activity implements LoginResponse{


    static final int REGISTER_REQUEST_CODE = 1;
    private EditText emailField,usernameField,passwordField,passwordField2,budgetField;
    private EditText firstField, lastField, dobField;
    private RadioGroup genderGroup;
    private String user;


    RadioButton radioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        emailField = (EditText)findViewById(R.id.SignupEmail);
        usernameField = (EditText)findViewById(R.id.SignupUser);
        passwordField = (EditText)findViewById(R.id.SignupPass);
        passwordField2 = (EditText)findViewById(R.id.SignupConfirm);

        firstField = (EditText)findViewById(R.id.SignupFirst);
        lastField = (EditText)findViewById(R.id.SignupLast);
        dobField = (EditText)findViewById(R.id.SignupDOB);
        budgetField = (EditText)findViewById(R.id.SignupBudget);

        genderGroup = (RadioGroup)findViewById(R.id.GenderGroup);

        new DateInputMask(dobField);


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
        String email = emailField.getText().toString();
        int selectedGender = genderGroup.getCheckedRadioButtonId();
        radioButton = (RadioButton) findViewById(selectedGender);



        Pattern p = Pattern.compile("[^a-zA-Z0-9]");
        boolean usernameSpecialChar = p.matcher(username).find();

        boolean validEmail = Patterns.EMAIL_ADDRESS.matcher(email).matches();

        if(!validEmail){
            Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
        }
        else if(usernameSpecialChar){
            Toast.makeText(this, "Username must be alphanumeric.", Toast.LENGTH_SHORT).show();


            String dob = dobField.getText().toString();

            Toast.makeText(this, dob, Toast.LENGTH_SHORT).show();
        }
        else if (!passwordField.getText().toString().equals(passwordField2.getText().toString())){
                Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
        }
        else{
            String gender = radioButton.getText().toString();
            String password = passwordField.getText().toString();
            String first = firstField.getText().toString();
            String last = lastField.getText().toString();
            String dob = dobField.getText().toString();
            String budget = budgetField.getText().toString();
            new Signup(this).execute(email, username, password, first, last, gender, dob, budget);

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
