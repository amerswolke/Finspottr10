package com.finspottr.finspottr10;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by amers on 3/15/2017.
 */

public class AddAircraft extends AppCompatActivity implements View.OnClickListener{

    //Declaring views
    public EditText registrationnum;  //aircraft registration
    public EditText manufacturer;
    public EditText model;
    public EditText airline;
    public EditText city;
    public EditText province;
    public EditText country;
    public EditText date;
    private Button buttonChoose;
    private Button buttonUpload;
    private ImageView imageView;
    private EditText editTextName;   //Aircraft file name

    //Image request code
    private int PICK_IMAGE_REQUEST = 1;

    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;

    //Bitmap to get image from gallery
    private Bitmap bitmap;

    //Uri to store the image uri
    private Uri filePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_aircraft);

        //Requesting storage permission
        requestStoragePermission();

        //Initializing views
        registrationnum = (EditText) findViewById(R.id.registrationnum);
        manufacturer = (EditText) findViewById(R.id.manufacturer);
        model = (EditText) findViewById(R.id.model);
        airline = (EditText) findViewById(R.id.airline);
        city = (EditText) findViewById(R.id.city);
        province = (EditText) findViewById(R.id.province);
        country = (EditText) findViewById(R.id.country);
        date = (EditText) findViewById(R.id.date);
        buttonChoose = (Button) findViewById(R.id.buttonChoose);
        buttonUpload = (Button) findViewById(R.id.buttonUpload);
        imageView = (ImageView) findViewById(R.id.imageView);
        editTextName = (EditText) findViewById(R.id.editTextName);

        //Setting clicklistener
        buttonChoose.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);
    }

    /*
      * This is the method responsible for image upload
      * We need the full image path and the name for the image in this method
      * */
    public void uploadMultipart() {
        //getting name for the image



        //getting the actual path of the image

        String registrationnumText = registrationnum.getText().toString().trim();
        String manufacturerText = manufacturer.getText().toString().trim();
        String modelText = model.getText().toString().trim();
        String airlineText = airline.getText().toString().trim();
        String cityText = city.getText().toString().trim();
        String provinceText = province.getText().toString().trim();
        String countryText = country.getText().toString().trim();
        String dateText = date.getText().toString().trim();
        String path = getPath(filePath);
        String name = editTextName.getText().toString().trim();


        //Uploading code
        try {
            String uploadId = UUID.randomUUID().toString();

            //Creating a multi part request
            new MultipartUploadRequest(this, uploadId, Constants.UPLOAD_URL)
                    .addParameter("registrationnum", registrationnumText)
                    .addParameter("manufacturer", manufacturerText)
                    .addParameter("model", modelText)
                    .addParameter("airline", airlineText)
                    .addParameter("city", cityText)
                    .addParameter("province", provinceText)
                    .addParameter("country", countryText)
                    .addParameter("date", dateText)
                    .addFileToUpload(path, "image") //Adding file
                    .addParameter("name", name) //Adding text parameter to the request
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload(); //Starting the upload

        } catch (Exception exc) {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    //method to show file chooser
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    //handling the image chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //method to get the file path from uri
    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }


    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }
    @Override
    public void onClick(View v) {
        if(v == buttonChoose){
            showFileChooser();
        }
        if (v == buttonUpload) {
            uploadMultipart();
        }
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
                Intent intent3=new Intent(this,Allrecords.class);
                startActivity(intent3);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}