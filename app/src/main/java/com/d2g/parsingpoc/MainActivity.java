package com.d2g.parsingpoc;



import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends Activity {

    private String TAG = MainActivity.class.getSimpleName();
    private ListView lv;

    ArrayList<HashMap<String, String>> liveChannelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        liveChannelList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list);

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

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
//                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONArray jsonArray = new JSONArray(jsonStr);

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
                    R.layout.list_item, new String[]{"channel_name", "channel_url"},
                    new int[]{R.id.name, R.id.source});
            lv.setAdapter(adapter);
        }
    }
}