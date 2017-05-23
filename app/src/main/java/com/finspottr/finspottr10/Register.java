package com.finspottr.finspottr10;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class Register extends AppCompatActivity {
    EditText firstname, lastname, username, password, email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firstname = (EditText) findViewById(R.id.et_firstname);
        lastname = (EditText) findViewById(R.id.et_lastname);
        username = (EditText) findViewById(R.id.et_username);
        password = (EditText) findViewById(R.id.et_password);
        email = (EditText) findViewById(R.id.et_email);
    }
    public void OnReg (View view) {
        String str_firstname = firstname.getText().toString();
        String str_lastname = lastname.getText().toString();
        String str_username = username.getText().toString();
        String str_password = password.getText().toString();
        String str_email = email.getText().toString();
        String type = "register";
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(type, str_firstname, str_lastname, str_username, str_password, str_email);

    }

    //  Function added for main menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        return true;
    }
    //  below functions for menu icons ie after Home: LogAircraft: Record: Add action here
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.Home:
                //home button activity through "intent"
                Intent intent=new Intent(this, MainActivity.class);
                startActivity(intent);
                //home button activity through "intent"
                return true;
            case R.id.LogAircraft:
                Intent intent2=new Intent(this,AddAircraft.class);
                startActivity(intent2);
                return true;
           /* case R.id.Record:
                //change below line for record navigation
                //Toast.makeText(this, "Record Coming Soon", Toast.LENGTH_LONG).show();
                //return true;
                Intent intent3=new Intent(this,Allrecords.class);
                startActivity(intent3);
                return true;*/
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
