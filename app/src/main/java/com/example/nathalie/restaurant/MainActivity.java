package com.example.nathalie.restaurant;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Context mContext;
    private Activity mActivity;
    RequestQueue requestQueue;
    String url = "https://resto.mprog.nl/categories";
    List<String> categoriesList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the application context
        mContext = getApplicationContext();
        mActivity = MainActivity.this;

        // Initialize a new RequestQueue instance
        requestQueue = Volley.newRequestQueue(mContext);

        // Initialize a new JsonObjectRequest instance
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        // Process the JSON
                        try {
                            // Get the JSON array
                            JSONArray array = response.getJSONArray("categories");
                            Log.d("hallo_array", String.valueOf(array));

                            // Fill list with categories from JSON
                            for (int i = 0; i < array.length(); i++) {
                                categoriesList.add(array.getString(i));
                            }

                            // Show categories in list view
                            fillSimpleListView();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("JSON ERROR", "Error when requesting a response from JSON");
                    }
                }
        );

        // Add JsonObjectRequest to the RequestQueue
        requestQueue.add(jsonObjectRequest);

    }

    public void fillSimpleListView() {

        ListAdapter theAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                categoriesList);

        // Get the XML listview
        ListView theListView = (ListView) findViewById(R.id.categoryListView);


        theListView.setAdapter(theAdapter);

        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                // Initialize intent
                Intent intent = new Intent(mActivity, MenuActivity.class);

                // Get chosen option from user
                String chosenCategory = String.valueOf(adapterView.getItemAtPosition(i));
                Log.d("hallo_choice", chosenCategory);

                // show menu list for correct category
                intent.putExtra("receivedCategory", chosenCategory);
                startActivity(intent);
            }
        });

    }

    public void toOrder(View view) {

        Intent intent = new Intent(mActivity, OrderActivity.class);
        intent.putExtra("receivedActivity", "fromMain");
        startActivity(intent);
    }
}