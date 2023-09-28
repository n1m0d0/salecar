package com.example.salecar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
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

import java.util.HashMap;
import java.util.Map;

public class Profile extends AppCompatActivity {
    TextView tvName, tvEmail;
    ImageButton ibHome, ibMessage, ibProfile;
    LinearLayout llBody;

    String name;
    String email;

    Util util;
    Integer userId;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_profile);

        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);

        llBody = findViewById(R.id.llBody);

        ibHome = findViewById(R.id.ibHome);
        ibMessage = findViewById(R.id.ibMessage);
        ibProfile = findViewById(R.id.ibProfile);

        util = new Util(Profile.this);
        Cursor cursor = util.getSession();
        userId = cursor.getInt(1);
        token = cursor.getString(3);

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

        ibProfile.setEnabled(false);

        petition("user");
    }

    private void petition(String direction) {

        String url = getString(R.string.serve) + "/" + direction + "/" + userId + "?included=phones";

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
                        String name =  data.getString("name");
                        String email = data.getString("email");
                        JSONArray phones = data.getJSONArray("phones");

                        tvName.setText(name.toUpperCase());
                        tvEmail.setText(email);

                        ComponentGenerator component = new ComponentGenerator(Profile.this, llBody);

                        for (int i = 0; i < phones.length(); i++) {
                            JSONObject jsonObject =  phones.getJSONObject(i);
                            Integer idPhone = jsonObject.getInt("id");
                            String number = jsonObject.getString("number");
                            component.customItemList(idPhone, number);
                        }
                    } else {
                        Toast.makeText(Profile.this, "", Toast.LENGTH_LONG).show();
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