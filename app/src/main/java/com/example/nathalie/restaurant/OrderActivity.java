package com.example.nathalie.restaurant;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static android.widget.Toast.LENGTH_SHORT;

public class OrderActivity extends AppCompatActivity {
    SharedPreferences saveOrder;
    int price;
    String name, activity;
    public List<String> orderList = new ArrayList<String>();
    RequestQueue requestQueue;
    String wait;
    String url = "https://resto.mprog.nl/order";

    TextView zero, one, two, three, four, five, six;
    Context mContext;
    Activity mActivity;
    String order;

    ListAdapter theAdapter;
    ListView theListView;
    String deleteItem;

    public List<String> priceList = new ArrayList<String>();
    public List<String> amountList = new ArrayList<String>();
    int i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        mContext = getApplicationContext();
        mActivity = OrderActivity.this;

        Intent intent = getIntent();

        // check which activity started this
        activity = intent.getStringExtra("receivedActivity");

        saveOrder = getSharedPreferences("App_settings", MODE_PRIVATE);

        if (activity.equals("fromMain") || activity.equals("fromMenu")) {
            retreiveSharedValue();

        }
        else {

            // get chosen order
            order = intent.getStringExtra("receivedItem");
//            price = intent.getIntExtra("receivedPrive", 0);
            orderList.add(order);
            retreiveSharedValue();
            packagesharedPreferences();

            Log.d("hallo_o", order);
            Log.d("hallo,", String.valueOf(orderList.size()) );

//            for(i=0; i<orderList.size(); i++) {
//                Log.d("hallo,", String.valueOf(orderList.get(1)));
//                Log.d("hallo,", String.valueOf(orderList));
//
//                if(order.equals(orderList.get(i))) {
//                    Log.d("hallo_same", "");
//
//                    int totalAmount = Integer.valueOf(amountList.get(i));
//                    int totalPrice = Integer.valueOf(priceList.get(i));
//
//                    totalAmount += 1;
//                    totalPrice += price;
//
//                    amountList.set(i, String.valueOf(totalAmount));
//                    priceList.set(i, String.valueOf(totalPrice));
//                } else {
//
//                    orderList.add(order);
//                    amountList.add("1");
//                    priceList.add(String.valueOf(price));
//
//                }
//
//            }




//            retreiveSharedValue();
//            packagesharedPreferences();
        }
        fillListView ();


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //do whatever you need for the hardware 'back' button

            Intent intent = new Intent(OrderActivity.this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void packagesharedPreferences() {
        SharedPreferences.Editor editor = saveOrder.edit();
        Set<String> orderSet = new HashSet<String>();
        orderSet.addAll(orderList);
        editor.putStringSet("LIST", orderSet);
        editor.apply();
        Log.d("hallo_storesharedPreferences",""+orderSet);
    }

    private void retreiveSharedValue() {
        Set<String> set = saveOrder.getStringSet("LIST", null);
        orderList.addAll(set);
        Log.d("retreivesharedPreferences",""+set);
    }


    public void fillListView () {

        theAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                orderList);

        theListView = (ListView) findViewById(R.id.listview_order);

        theListView.setAdapter(theAdapter);

        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                deleteItem = String.valueOf(adapterView.getItemAtPosition(i));

                new AlertDialog.Builder(OrderActivity.this)
                        .setTitle("Delete item?")
                        .setMessage("Are you sure?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //the user wants to leave - so dismiss the dialog and exit
                                dialog.dismiss();
                                deleteOrder(deleteItem);
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

    public void deleteOrder(String deleteItem) {

//        orderList.clear();
        orderList.remove(deleteItem);
        packagesharedPreferences();

        // update listview
        theListView.invalidateViews();
    }

    public void showWait(View view) {

//        orderList.clear();
//        packagesharedPreferences();
//
//        // update listview
//        theListView.invalidateViews();


        // Initialize a new RequestQueue instance
        requestQueue = Volley.newRequestQueue(mContext);

        // Initialize a new JsonObjectRequest instance
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        // Process the JSON
                        // Get the JSON array
                        try {
                            Log.d("hallo_resp", response.getString("preparation_time"));
                            wait = response.getString("preparation_time");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        };

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

        String waitingTime = "Your waiting time is " + String.valueOf(wait) + " minutes";

        Toast.makeText(mContext, waitingTime, LENGTH_SHORT).show();

        orderList.clear();
        packagesharedPreferences();

        // update listview
        theListView.invalidateViews();



    }
}




