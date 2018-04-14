package com.zumepizza.interview;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.zumepizza.interview.data.Datum;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/*
 Exercise instructions

 1) Fetch menu data using the URL given in API.java.
 2) Use fetched data to instantiate product model objects.
 2) Using product models, populate a list view using designs found in mocks/menu-mock-up.png
 3) Implement cart functionality, to allow users to add/remove items to their cart.
 4) Add cart activity to display items added to cart. Use designs found in mocks/cart-mock-up.png

 */


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    final int ANIMATION_DURATION = 650;
    private TableLayout tableLayout;
    Datum[] datumArray = null;
    private API api;
    private AlphaAnimation buttonClick;
    private AnimationSet animationSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        api = new API(this);
        initViews();
        getData();
    }

    private void initViews() {
        buttonClick = new AlphaAnimation(1F, 0.5F);
        animationSet = new AnimationSet(true);
        animationSet.addAnimation(buttonClick);
        animationSet.setDuration(ANIMATION_DURATION);
        tableLayout = (TableLayout) findViewById(R.id.main_table);
    }
    
    private void getData (){
        API.ResponseHandler responseHandler = new API.ResponseHandler() {
            @Override
            public void completion(JSONArray response) {
                Log.d(TAG, response.toString());
                try {
                    datumArray = Utils.decodeResp(Datum[].class, response.toString());
                    populateTable();
                }
                catch (Exception e) {
                    e.getStackTrace();
                }
            }
        };
        api.fetchMenu(responseHandler);
    }

    private void populateTable() {
        TableRow row = null;
        for(Datum datum: datumArray) {
            row = new TableRow(this);
            row.setGravity(Gravity.CENTER);
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layoutView = inflater.inflate(R.layout.item_layout, null);

            ImageView mainImage = layoutView.findViewById(R.id.main_image);
            ImageButton addButton = layoutView.findViewById(R.id.add_button);
            TextView title = layoutView.findViewById(R.id.title);
            TextView toppings = layoutView.findViewById(R.id.toppings);
            TextView price = layoutView.findViewById(R.id.price);

            Picasso.get().load(datum.getMenuAsset().getUrl()).into(mainImage);
            title.setText(datum.getName());
            toppings.setText(Utils.getToppings(datum.getToppings()));
            price.setText(Utils.getFormsttedPrice(datum.getPrice()));

            addButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    v.startAnimation(animationSet);
                    Toast.makeText(getApplicationContext(), "PRESSED: " + v.getTransitionName(), Toast.LENGTH_SHORT).show();
                }
            });

            row.addView(layoutView);
            tableLayout.addView(row);
        }
    }
}
