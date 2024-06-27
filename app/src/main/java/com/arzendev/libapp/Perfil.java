package com.arzendev.libapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONException;
import org.json.JSONObject;

public class Perfil extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_perfil);

        TextView txtnombrePerfil = findViewById(R.id.txtnombrePerfil);
        TextView txtapellidosPerfil = findViewById(R.id.txtapellidosPerfil);
        TextView txtcorreoPerfil = findViewById(R.id.txtcorreoPerfil);

        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        String correo = sharedPreferences.getString("array", "");

        try {
            JSONObject jsonObject = new JSONObject(correo);
            String idUs = jsonObject.getString("id");
            String nombres = jsonObject.getString("nombres");
            String apellidos = jsonObject.getString("apellidos");
            String usu_usuario = jsonObject.getString("usu_usuario");

            txtnombrePerfil.setText(nombres);
            txtapellidosPerfil.setText(apellidos);
            txtcorreoPerfil.setText(usu_usuario);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Button btnCerrarSesion = findViewById(R.id.btnCerrarCesion);
        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Limpiar SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();

                // Redirigir al MainActivity
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Button irCatalogo = findViewById(R.id.btnCatalogo);
        irCatalogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Principal.class);
                startActivity(intent);
                finish();
            }
        });

        Button irFavoritos = findViewById(R.id.btnFavoritos);
        irFavoritos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Favs.class);
                startActivity(intent);
                finish();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}