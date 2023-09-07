package com.example.salecar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    EditText etEmail, etPassword;
    Button btnLogin;

    Util util;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        btnLogin = findViewById(R.id.btnLogin);

        util = new Util(Login.this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (util.connectionTest()){
                    if (etEmail.getText().toString().trim().equals("") || etPassword.getText().toString().trim().equals("")) {
                        util.completeData();
                    } else {
                        if (util.validateEmail(etEmail.getText().toString())){
                            try {
                                JSONObject sendData = new JSONObject();
                                sendData.put("email", etEmail.getText().toString().trim());
                                sendData.put("password", etPassword.getText().toString().trim());
                                Log.w("json", "" + sendData);
                                petition("login", sendData, etEmail.getText().toString().trim());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            util.completeData();
                        }
                    }
                } else {
                    Toast.makeText(Login.this, "Debe Conectarse a internet", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void petition(String direction, JSONObject data, String email) {

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

        jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, data, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                Log.w("response", "" + response);
                try {
                    if (response.getBoolean("is_success")) {
                        JSONObject data = response.getJSONObject("data");
                        Integer user_id = data.getInt("user_id");
                        String token = data.getString("token");
                        util.session(user_id, email, token);
                        Toast.makeText(Login.this, "Bienvenido", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Login.this, Advertisement.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(Login.this, "Usuario o Clave Incorrecto", Toast.LENGTH_LONG).show();
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
                //params.put("Authorization", "Bearer " + Utils.readSharedSetting(context, "access_token", ""));
                return params;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }
}