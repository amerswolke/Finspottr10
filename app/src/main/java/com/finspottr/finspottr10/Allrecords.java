package com.finspottr.finspottr10;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Allrecords extends AppCompatActivity {
    //adapter class
    ArrayList<AdapterItems> listAircraftData = new ArrayList<AdapterItems>();
    MyCustomAdapter myadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allrecords);

        //add data and view it
        myadapter = new MyCustomAdapter(listAircraftData);
        ListView lvlist = (ListView) findViewById(R.id.lvlist);
        lvlist.setAdapter(myadapter);

        new MyAsyncTaskgetImages().execute();
    }

    //display aircraft list
    private class MyCustomAdapter extends BaseAdapter {
        public ArrayList<AdapterItems> listaircraftDataAdpater;

        public MyCustomAdapter(ArrayList<AdapterItems> listaircraftDataAdpater) {
            this.listaircraftDataAdpater = listaircraftDataAdpater;
        }


        @Override
        public int getCount() {
            return listaircraftDataAdpater.size();
        }

        @Override
        public String getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater mInflater = getLayoutInflater();
            View myView = mInflater.inflate(R.layout.layout_ticket, null);

            final AdapterItems s = listaircraftDataAdpater.get(position);

            TextView etID=( TextView)myView.findViewById(R.id.etid);
            etID.setText("" + s.id);

            TextView tv_RegistrationNum = (TextView) myView.findViewById(R.id.tv_registrationnum);
            tv_RegistrationNum.setText(s.registrationnum);

            TextView tv_Manufacturer = (TextView) myView.findViewById(R.id.tv_manufacturer);
            tv_Manufacturer.setText(s.manufacturer);

            TextView tv_Model = (TextView) myView.findViewById(R.id.tv_model);
            tv_Model.setText(s.model);

            ImageView ivImage = (ImageView) myView.findViewById(R.id.ivImage);
            Picasso.with(parent.getContext()).load(Uri.parse(s.ivImage)).into(ivImage);

            return myView;
        }

    }

    public class MyAsyncTaskgetImages extends AsyncTask<Void, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(Void... params) {
            String IMAGES_URL = "http://finspottr.com/getImages.php";
            // TODO Auto-generated method stub
            try {
                String AircraftData;
                //define the url we have to connect with
                URL url = new URL(IMAGES_URL);
                //URL url = new URL(params[0]);
                //make connect with url and send request
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                //waiting for 7000ms for response
                urlConnection.setConnectTimeout(7000);//set timeout to 5 seconds

                try {
                    //getting the response data
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    String str = inputStreamToString(in);
                    return new JSONObject(str);
                } finally {
                    //end connection
                    urlConnection.disconnect();
                }

            }catch (Exception ex){}
            return null;
        }

        // fix later
        protected void onProgressUpdate(String... progress) {
            try {
                JSONArray json = new JSONArray(progress[0]);
                for (int i=0;i<json.length();i++){
                    JSONObject log = json.getJSONObject(i);
                    //listAircraftData.add(new AdapterItems(log.getInt("id"), log.getString("registrationnum"), log.getString("manufacturer"), log.getString("model"), log.getString("airline"), log.getString("city"), log.getString("province"), log.getString("country"), log.getString("date"), log.getInt("url"), log.getString("name")));
                }
                myadapter.notifyDataSetChanged();
                //display response data
                Toast.makeText(getApplicationContext(), progress[0], Toast.LENGTH_LONG).show();

            } catch (Exception ex) {
            }
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            JSONArray images = jsonObject.optJSONArray("images");

            listAircraftData.clear();
            for (int i = 0; i < images.length(); ++i) {
                JSONObject entry = images.optJSONObject(i);

                AdapterItems item = new AdapterItems(entry.optInt("id"), entry.optString("registrationnum"), entry.optString("manufacturer"), entry.optString("model"), entry.optString("airline"), entry.optString("city"), entry.optString("province"), entry.optString("country"), entry.optString("date"), entry.optString("url"), entry.optString("name"));
                listAircraftData.add(item);
            }
            myadapter.notifyDataSetChanged();
            Toast.makeText(getApplicationContext(), "Done Downloading", Toast.LENGTH_LONG).show();
        }
    }


    public static String inputStreamToString(InputStream stream) {
        try {
            InputStreamReader reader = new InputStreamReader(stream, "UTF8");
            StringWriter writer = new StringWriter();
            char buffer[] = new char[8192];

            while (true) {
                int n = reader.read(buffer);
                if (n == -1) {
                    break;
                }

                writer.write(buffer);
            }

            return writer.toString();
        } catch (UnsupportedEncodingException e) {
            Log.e("Allrecords", "expected UTF8 response", e);
        } catch (IOException e) {
            Log.e("Allrecords", "could not read response", e);
        }

        return null;
    }

    // this method convert any stream to string
    public static String ConvertInputToStringNoChange(InputStream inputStream) {

        BufferedReader bureader=new BufferedReader( new InputStreamReader(inputStream));
        String line ;
        String linereultcal="";

        try{
            while((line=bureader.readLine())!=null) {

                linereultcal+=line;

            }
            inputStream.close();


        }catch (Exception ex){}

        return linereultcal;
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

}
