package com.example.salecar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

public class Advertisement extends AppCompatActivity {
    ListView lvAdvertisement;
    ImageView ivPublicity;

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
        setContentView(R.layout.activity_advertisement);

        lvAdvertisement = findViewById(R.id.lvAdvertisement);
        ivPublicity = findViewById(R.id.ivPublicity);

        util = new Util(Advertisement.this);
        Cursor cursor = util.getSession();
        user_id = cursor.getInt(1);
        email = cursor.getString(2);
        token = cursor.getString(3);

        petition("advertisement");

        ivPublicity.setVisibility(View.VISIBLE);
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
                            JSONObject form = data.getJSONObject(i);
                            Integer id = form.getInt("id");
                            String name = form.getString("name");
                            String brand = form.getString("brand");
                            String model = form.getString("model");
                            String manufactured = form.getString("manufactured");
                            String year = form.getString("year");
                            String plate = form.getString("plate");
                            String mileage = form.getString("mileage");
                            Integer functioning = form.getInt("functioning");
                            Integer esthetic = form.getInt("esthetic");
                            String image1 = form.getString("image1");
                            String image2 = form.getString("image2");
                            String image3 = form.getString("image3");
                            String image4 = form.getString("image4");

                            items.add(new AdvertisementObject(id, name, brand,model, manufactured, year, plate, mileage, functioning, esthetic, image1));
                        }
                        AdvertisementAdapter advertisementAdapter = new AdvertisementAdapter(Advertisement.this, items);
                        lvAdvertisement.setAdapter(advertisementAdapter);
                    } else {
                        Toast.makeText(Advertisement.this, "", Toast.LENGTH_LONG).show();
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