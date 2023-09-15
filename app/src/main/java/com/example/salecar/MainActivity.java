package com.example.salecar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button btnLogin, btnRegister;
    Util util;

    private final static int applicationPermissions = 1;
    private static String[] permissions = {
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.RECORD_AUDIO
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        validatePermission();

        util = new Util(MainActivity.this);

        try {
            if (util.verifySession()) {
                Intent intent = new Intent(MainActivity.this, Advertisements.class);
                startActivity(intent);
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
                finish();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Register.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void validatePermission() {
        if ((checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) &&
                (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) &&
                (checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) &&
                (checkSelfPermission(android.Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED) &&
                (checkSelfPermission(android.Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)) {
            Log.w("AppPermission", "OK");
        } else {
            if ((shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)) ||
                    (shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_FINE_LOCATION)) ||
                    (shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_COARSE_LOCATION)) ||
                    (shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_NETWORK_STATE)) ||
                    (shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO))) {
                Log.w("AppPermission", "Denied");
                permissionDialog();
            } else {
                Log.w("AppPermission", "Denied");
                requestPermissions(permissions, applicationPermissions);
            }
        }
    }

    private void permissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Permisos Desactivados");
        builder.setMessage("Debe aceptar los permisos para el correcto funcionamiento de la App");

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                requestPermissions(permissions, applicationPermissions);
            }
        });
        builder.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == applicationPermissions)
        {
            int validator = 0;
            Log.w("grantResults", "" +grantResults.length);
            if (grantResults.length == 5)
            {
                for (int i = 0; i < grantResults.length; i++)
                {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED)
                    {
                        Log.w("idPermission", "" + grantResults[i]);
                        validator++;
                    }
                }
            }
            if (validator == 0)
            {
                Log.w("AppPermission", "OK");
            } else {
                Log.w("AppPermission", "Denied");
                manualPermissionDialog();
            }
        }
    }

    private void manualPermissionDialog() {
        final CharSequence[] options = {"si", "no"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Desea configurar los permisos manualmente?");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (options[which].equals("si")) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                } else {
                    Toast msj = Toast.makeText(MainActivity.this, "Debe aceptar los permisos para el correcto funcionamiento de la App", Toast.LENGTH_LONG);
                    msj.show();
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
}