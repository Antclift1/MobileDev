package com.example.anthony.financialtracking;

import android.content.Intent;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity{

    String username = "";
    static final int REGISTER_REQUEST_CODE = 1;
    TextView status;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        status = (TextView) findViewById(R.id.textview);
        button = (Button) findViewById(R.id.testbutton);


    }


    public void startlogin(View view) {

            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivityForResult(loginIntent, REGISTER_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REGISTER_REQUEST_CODE && resultCode == RESULT_OK) {
            username = data.getExtras().getString("Username");

            Toast.makeText(this, "Welcome " + username, Toast.LENGTH_SHORT).show();
            status.setText(username);
        }
    }
}
