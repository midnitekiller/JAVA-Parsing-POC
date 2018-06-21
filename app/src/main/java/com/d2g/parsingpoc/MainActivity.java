package com.d2g.parsingpoc;


import com.d2g.parsingpoc.ChannelListAdapter;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends Activity {

    private String TAG = MainActivity.class.getSimpleName();
    private ListView lv;
    private ImageView chanlogo;
    private RelativeLayout chanLayout;
    int row_layout;
    Context context;
    ArrayList<JSONObject> channel_list;
    int pos;
    String serveruri;
    String serverss_url = "http://33.33.33.33/";



    ArrayList<HashMap<String, String>> liveChannelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        liveChannelList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list);
        chanlogo = (ImageView) findViewById(R.id.channelLogo);
        chanLayout = (RelativeLayout) findViewById(R.id.channelLayout);




        new GetChannelList().execute();
    }

    private class GetChannelList extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MainActivity.this, "Json Data is downloading", Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = "http://33.33.33.33/api/channels.php?hotel_id=1";
            String jsonStr = sh.makeServiceCall(url);
            String serverss_url = "http://33.33.33.33/";
//            ArrayList<JSONObject> channel_list = getArrayListFromJSONArray();
//            final ChannelListAdapter channel_adapter = new ChannelListAdapter(getApplicationContext(), R.layout.channel_item_list,channel_list,serverss_url);


            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
//                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONArray jsonArray = new JSONArray(jsonStr);


//                    ArrayList<JSONObject> channel_list = getArrayListFromJSONArray(jsonArray);
//                    final ChannelListAdapter channel_adapter = new ChannelListAdapter(getApplicationContext(), R.layout.channel_item_list,channel_list,serverss_url);



                    // Getting JSON Array node
//                    JSONArray liveChannels = jsonArray.getJSONArray("liveChannelList");

                    // looping through All Channels
//                    for (int i = 0; i < liveChannelList.length(); i++) {
                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject c = liveChannels.getJSONObject(i);
                        JSONObject c = jsonArray.getJSONObject(i);

                        String channel_name = c.getString("channel_name");
                        String channel_url = c.getString("channel_url");
                        String channel_logo = c.getString("channel_logo");
                        String img_path = c.getString("img_path");

                        // tmp hash map for single contact
                        HashMap<String, String> channel = new HashMap<>();

                        // adding each child node to HashMap key => value
                        channel.put("channel_name", channel_name);
                        channel.put("channel_url", channel_url);
                        channel.put("channel_logo", channel_logo);
                        channel.put("img_path", img_path);



//                        Picasso.with(MainActivity.this).load(serverss_url + "img_path").resize(120, 120).into(chanlogo);

                        // adding contact to contact list
                        liveChannelList.add(channel);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }





        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            ListAdapter adapter = new SimpleAdapter(MainActivity.this, liveChannelList,
                    R.layout.list_item, new String []{"img_path", "channel_name", "channel_url"},
                    new int[]{R.id.channelLogo, R.id.name, R.id.source});
            lv.setAdapter(adapter);
        }


    }
    private ArrayList<JSONObject> getArrayListFromJSONArray(JSONArray jsonArray){

        ArrayList<JSONObject> aList=new ArrayList<JSONObject>();
        try {
            if (jsonArray != null) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    aList.add(jsonArray.getJSONObject(i));
                }
            }
        }catch (JSONException je){je.printStackTrace();}
        return  aList;
    }

}