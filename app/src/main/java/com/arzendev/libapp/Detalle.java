package com.arzendev.libapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Detalle extends AppCompatActivity {
    String libroID, usuarioID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);

        Button btnFav = findViewById(R.id.btnFav);

        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        usuarioID = getUsuarioIDFromArray(sharedPreferences.getString("array", ""));

        TextView idDetalle = findViewById(R.id.idDetalle);
        TextView tituloDetalle = findViewById(R.id.tituloDetalle);
        TextView autorDetalle = findViewById(R.id.autorDetalle);
        TextView generoDetalle = findViewById(R.id.generoDetalle);
        ImageView portadaDetalle = findViewById(R.id.detalleImage);
        TextView sinopsisDetalle = findViewById(R.id.sinopsisDetalle);

        // Obtener los datos del Intent
        Intent intent = getIntent();
        libroID = intent.getStringExtra("id");
        String titulo = intent.getStringExtra("titulo");
        String autor = intent.getStringExtra("autor");
        String genero = intent.getStringExtra("genero");
        String portada_url = intent.getStringExtra("portada_url");
        String sinopsis = intent.getStringExtra("sinopsis");

        // Asignar los datos a las vistas
        idDetalle.setText(libroID);
        tituloDetalle.setText(titulo);
        autorDetalle.setText(autor);
        generoDetalle.setText(genero);
        sinopsisDetalle.setText(sinopsis);
        Glide.with(this).load(portada_url).into(portadaDetalle);

        btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarFavorito();
            }
        });
    }

    private String getUsuarioIDFromArray(String array) {
        try {
            JSONObject jsonObject = new JSONObject(array);
            return jsonObject.getString("id");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void volver(){
        Intent intent = new Intent(getApplicationContext(), Principal.class);
        startActivity(intent);
        finish();
    }

    public void validarFavorito(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://10.0.2.2/crud/insertar_favorito.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    String message = jsonResponse.getString("message");
                    Toast.makeText(Detalle.this, message, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Detalle.this, "Error de JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(Detalle.this, "Error de red: " + volleyError.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("id_libro", libroID);
                parametros.put("id_usuario", usuarioID);
                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
