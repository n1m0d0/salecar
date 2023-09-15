package com.example.salecar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
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

import java.util.HashMap;
import java.util.Map;

public class Advertisement extends AppCompatActivity {
    TextView tvBrand, tvModel, tvPrice, tvManufactured, tvYear, tvMileage;
    RatingBar rbFunctioning, rbEsthetic;

    ImageView ivImage1, ivImage2, ivImage3, ivImage4;
    Util util;
    Integer user_id;
    String email, token;

    Integer id;
    String name;
    String brand;
    String model;
    String manufactured;
    String year;
    String plate;
    String mileage;
    Integer functioning;
    Integer esthetic;
    String image1;
    String image2;
    String image3;
    String image4;
    String price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_advertisement);

        tvBrand = findViewById(R.id.tvBrand);
        tvModel = findViewById(R.id.tvModel);
        tvPrice = findViewById(R.id.tvPrice);
        tvManufactured = findViewById(R.id.tvManufactured);
        tvYear = findViewById(R.id.tvYear);
        tvMileage = findViewById(R.id.tvMileage);

        rbFunctioning = findViewById(R.id.rbFunctioning);
        rbEsthetic = findViewById(R.id.rbEsthetic);

        ivImage1 = findViewById(R.id.ivImage1);
        ivImage2 = findViewById(R.id.ivImage2);
        ivImage3 = findViewById(R.id.ivImage3);
        ivImage4 = findViewById(R.id.ivImage4);

        Bundle params = this.getIntent().getExtras();
        id = params.getInt("id");

        util = new Util(Advertisement.this);
        Cursor cursor = util.getSession();
        user_id = cursor.getInt(1);
        email = cursor.getString(2);
        token = cursor.getString(3);

        petition("advertisement");
    }

    private void petition(String direction) {

        String url = getString(R.string.serve) + "/" + direction + "/" + id;

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
                        JSONObject data = response.getJSONObject("data");
                        name = data.getString("name");
                        brand = data.getString("brand");
                        model = data.getString("model");
                        manufactured = data.getString("manufactured");
                        year = data.getString("year");
                        plate = data.getString("plate");
                        mileage = data.getString("mileage");
                        functioning = data.getInt("functioning");
                        esthetic = data.getInt("esthetic");
                        image1 = data.getString("image1");
                        image2 = data.getString("image2");
                        image3 = data.getString("image3");
                        image4 = data.getString("image4");
                        price = data.getString("price");

                        tvBrand.setText(brand);
                        tvModel.setText(model);
                        tvManufactured.setText(manufactured);
                        tvYear.setText(year);
                        tvMileage.setText(mileage);
                        rbFunctioning.setRating(functioning);
                        rbEsthetic.setRating(esthetic);
                        tvPrice.setText(price);

                        String[] separated1 = image1.split("/");
                        Picasso.get().load("https://carsale.ajatic.com/" + "storage/" + separated1[1]).error(R.drawable.sale_car).into(ivImage1);

                        String[] separated2 = image2.split("/");
                        Picasso.get().load("https://carsale.ajatic.com/" + "storage/" + separated2[1]).error(R.drawable.sale_car).into(ivImage2);

                        String[] separated3 = image3.split("/");
                        Picasso.get().load("https://carsale.ajatic.com/" + "storage/" + separated3[1]).error(R.drawable.sale_car).into(ivImage3);

                        String[] separated4 = image4.split("/");
                        Picasso.get().load("https://carsale.ajatic.com/" + "storage/" + separated4[1]).error(R.drawable.sale_car).into(ivImage4);
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