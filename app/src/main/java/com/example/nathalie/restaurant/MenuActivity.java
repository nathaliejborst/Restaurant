package com.example.nathalie.restaurant;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity {
    public ArrayList<String> orderList = new ArrayList<String>();
    public ArrayList<String> stringPrice = new ArrayList<String>();
    public ArrayList<String> stringAmount = new ArrayList<String>();

    private ListView lvProduct;
    private ProductListAdapter adapter;
    private List<Product> mProductlist = new ArrayList<>();
    String chosenCategory, chosenItem, itemClicked;
    int itemPrice;
    private Context mContext;
    private Activity mActivity;
    JSONObject item;
    RequestQueue requestQueue;
    String url = "https://resto.mprog.nl/menu";
    Intent intentOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // get intent and choice of category
        Intent intent = getIntent();
        chosenCategory = intent.getStringExtra("receivedCategory");

        // Get the application context
        mContext = getApplicationContext();
        mActivity = MenuActivity.this;

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
                            JSONArray array = response.getJSONArray("items");

                            // Loops trough every item of the JSON array
                            for (int j = 0; j < array.length(); j++) {

                                // Get the j'th JSON object
                                item = array.getJSONObject(j);

                                // Get the category of j'th array-item
                                String checkCategory = item.getString("category");

                                // adds items of chosen category to lists
                                if (checkCategory.equals(chosenCategory)) {
                                    int count = 0;

                                    int id = item.getInt("id");
                                    String name = item.getString("name");
                                    String description = item.getString("description");
                                    int price = item.getInt("price");
                                    String image = item.getString("image_url");

                                    fillListView(id, name, description, price, image);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Do something when error occurred
                        Log.d("JSONERROR", "something wrong with JSON-request, check code");
                    }
                }
        );

        Log.d("hallo_check", "inlistview?");

        // Add JsonObjectRequest to the RequestQueue
        requestQueue.add(jsonObjectRequest);

    }

    //
    public void fillListView(int id, String name, String description, int price, String image) {

        chosenItem = name;

        // Get listview from XML
        lvProduct = (ListView) findViewById(R.id.listview_product);

        // Fill productlist
        mProductlist.add(new Product(id, name, price, description, image));
        adapter = new ProductListAdapter(getApplicationContext(), mProductlist);
        lvProduct.setAdapter(adapter);


        lvProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                // Initialize intent
                intentOrder = new Intent(mActivity, OrderActivity.class);

                itemPrice = Integer.valueOf(String.valueOf(view.getTag()).substring(0, 1));
                itemClicked = String.valueOf(view.getTag()).substring(1);

                // Margerita Pizza is the only one with a 2-digit price (looks ugly)
                if (itemPrice == 1) {
                    itemPrice = 10;
                    itemClicked = String.valueOf(itemClicked).substring(1);
                }

                new AlertDialog.Builder(MenuActivity.this)
                        .setTitle(itemClicked)
                        .setMessage("Add item?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                                // Give price and item as intent
                                intentOrder.putExtra("receivedPrice", itemPrice);
                                intentOrder.putExtra("receivedItem", itemClicked);
                                intentOrder.putExtra("receivedActivity", "fromClick");
                                startActivity(intentOrder);

                                //the user wants to leave - so dismiss the dialog and exit
                                dialog.dismiss();

                            }
                        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // the user is not sure? So you can exit or just stay
                        dialog.dismiss();
                    }
                }).show();


            }
        });

    }


    public void menuToOrder(View view) {

        intentOrder.putExtra("receivedActivity", "fromMenu");
        startActivity(intentOrder);
    }

}
