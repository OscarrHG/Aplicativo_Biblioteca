package com.arzendev.libapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText txtCorreo, txtPswd;
    Button btnIngresar, btnGoRegistro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        // Verificar si el usuario ya está logueado
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

        if (isLoggedIn) {
            Intent intent = new Intent(getApplicationContext(), Principal.class);
            startActivity(intent);
            finish();
        } else {
            setContentView(R.layout.activity_main);

            txtCorreo = findViewById(R.id.txtCorreo);
            txtPswd = findViewById(R.id.txtPswd);
            btnIngresar = findViewById(R.id.btnIngresar);
            btnGoRegistro = findViewById(R.id.btnGoRegistro);

            btnIngresar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    validarUsuario("http://10.0.2.2/crud/validar_usuario.php");
                }
            });

            btnGoRegistro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), Regisuser.class);
                    startActivity(intent);
                }
            });
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void validarUsuario(String URL){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (!s.isEmpty()){
                    // Guardar estado de usuario en SharedPreferences
                    SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isLoggedIn", true);
                    editor.putString("usu_usuario", txtCorreo.getText().toString().trim());
                    editor.putString("array",s);
                    editor.apply();

                    Intent intent = new Intent(getApplicationContext(), Principal.class);
                    startActivity(intent);
                    finish();
                } else{
                    Toast.makeText(MainActivity.this, "Usuario o Contraseña invalida", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(MainActivity.this, volleyError.toString(), Toast.LENGTH_SHORT).show();
                txtCorreo.setText(volleyError.toString());
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("usu_usuario", txtCorreo.getText().toString().trim());
                parametros.put("usu_password", txtPswd.getText().toString().trim());
                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}