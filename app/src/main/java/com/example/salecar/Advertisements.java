package com.example.salecar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
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
import java.util.HashMap;
import java.util.Map;

public class Advertisements extends AppCompatActivity {
    ListView lvAdvertisement;
    ImageButton ibHome, ibMessage, ibProfile;

    ArrayList<AdvertisementObject> items = new ArrayList<>();

    Util util;
    Integer user_id;
    String email, token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_advertisements);

        lvAdvertisement = findViewById(R.id.lvAdvertisement);

        ibHome = findViewById(R.id.ibHome);
        ibMessage = findViewById(R.id.ibMessage);
        ibProfile = findViewById(R.id.ibProfile);

        util = new Util(Advertisements.this);
        Cursor cursor = util.getSession();
        user_id = cursor.getInt(1);
        email = cursor.getString(2);
        token = cursor.getString(3);

        petition("advertisement");

        lvAdvertisement.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AdvertisementObject selected = (AdvertisementObject) parent.getItemAtPosition(position);

                int advertisement_id = selected.getId();

                Intent intent = new Intent(Advertisements.this, Advertisement.class);
                intent.putExtra("id", advertisement_id);
                startActivity(intent);
            }
        });

        ibHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        ibMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        ibProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Advertisements.this, Profile.class);
                startActivity(intent);
            }
        });
    }

    private void petition(String direction) {

        String url = getString(R.string.serve) + "/" + direction;

        ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(this);
        //progressDialog.setMax(100);
        progressDialog.setMessage("Cargando...");
        progressDialog.setTitle("Revisando la Informacion");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);

        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest;

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                Log.w("response", "" + response);
                try {
                    if (response.getBoolean("is_success")) {
                        JSONArray data = response.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject jsonObject = data.getJSONObject(i);
                            Integer id = jsonObject.getInt("id");
                            Integer user_id = jsonObject.getInt("user_id");
                            String brand = jsonObject.getString("brand");
                            String model = jsonObject.getString("model");
                            String manufactured = jsonObject.getString("manufactured");
                            String year = jsonObject.getString("year");
                            String plate = jsonObject.getString("plate");
                            String mileage = jsonObject.getString("mileage");
                            Integer functioning = jsonObject.getInt("functioning");
                            Integer esthetic = jsonObject.getInt("esthetic");
                            String image1 = jsonObject.getString("image1");
                            String image2 = jsonObject.getString("image2");
                            String image3 = jsonObject.getString("image3");
                            String image4 = jsonObject.getString("image4");
                            String price = jsonObject.getString("price");

                            items.add(new AdvertisementObject(id, user_id, brand,model, manufactured, year, plate, mileage, functioning, esthetic, image1, price));
                        }
                        AdvertisementAdapter advertisementAdapter = new AdvertisementAdapter(Advertisements.this, items);
                        lvAdvertisement.setAdapter(advertisementAdapter);
                    } else {
                        Toast.makeText(Advertisements.this, "", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.w("error", "" + error);
                progressDialog.dismiss();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "Bearer " + token);
                return params;
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(jsonObjectRequest);
    }
}