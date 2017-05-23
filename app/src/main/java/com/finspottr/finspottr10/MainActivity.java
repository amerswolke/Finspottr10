package com.finspottr.finspottr10;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    EditText UsernameEt, PasswordEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UsernameEt = (EditText)findViewById(R.id.et_username);
        PasswordEt = (EditText)findViewById(R.id.et_password);
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
                Intent intent=new Intent(this,MainActivity.class);
                startActivity(intent);
                //home button activity through "intent"
                return true;

            case R.id.LogAircraft:
                Intent intent2=new Intent(this,AddAircraft.class);
                startActivity(intent2);
                return true;

            case R.id.Record:
                //change below line for record navigation
                //Toast.makeText(this, "Record Coming Soon", Toast.LENGTH_LONG).show();
                //return true;
                Intent intent3=new Intent(this,Allrecords.class);
                startActivity(intent3);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //  public void onLogin need to add something for AircraftUp on AddAircraft activity
    public void OnLogin(View view) {
        String username = UsernameEt.getText().toString();
        String password = PasswordEt.getText().toString();
        String type = "login";
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(type, username, password);
    }


    public void OpenReg (View view) {
        startActivity(new Intent(this, Register.class));
    }

}
