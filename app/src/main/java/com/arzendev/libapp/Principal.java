package com.arzendev.libapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Principal extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LibrosAdapter librosAdapter;
    private List<Libro> libros;
    EditText txtBuscar;
    TextView txtSaludar;
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_principal);

        recyclerView = findViewById(R.id.listRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        libros = new ArrayList<>();
        librosAdapter = new LibrosAdapter(libros, this);
        recyclerView.setAdapter(librosAdapter);

        txtBuscar = findViewById(R.id.txtBuscar);
        txtBuscar.setText("");

        Button btnBuscar = findViewById(R.id.btnBuscarLibro);

        txtSaludar = findViewById(R.id.textTituloCatalogo);
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        String correo = sharedPreferences.getString("array", "");

        try {
            JSONObject jsonObject = new JSONObject(correo);
            String nombres = jsonObject.getString("nombres");

            // Asignar los valores a los TextViews
            txtSaludar.setText("Hola, "+ nombres + "!");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        gson = new GsonBuilder()
                .setLenient()
                .create();

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = txtBuscar.getText().toString();
                busquedaLibros(query);
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

        Button irPerfil = findViewById(R.id.btnPerfil);
        irPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Perfil.class);
                startActivity(intent);
                finish();
            }
        });

        // Cargar los libros desde la API
        cargarLibros();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void updateRecyclerView(List<Libro> nuevosLibros) {
        libros.clear();
        libros.addAll(nuevosLibros);
        librosAdapter.notifyDataSetChanged();
    }

    private void cargarLibros() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2/crud/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        Call<List<Libro>> call = apiService.getLibros();

        call.enqueue(new Callback<List<Libro>>() {
            @Override
            public void onResponse(Call<List<Libro>> call, retrofit2.Response<List<Libro>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    libros.addAll(response.body());
                    librosAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(Principal.this, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Libro>> call, Throwable t) {
                Toast.makeText(Principal.this, "Error en la conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void busquedaLibros(String query){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2/crud/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        Call<List<Libro>> call = apiService.buscarLibros(query);

        call.enqueue(new Callback<List<Libro>>() {
            @Override
            public void onResponse(Call<List<Libro>> call, retrofit2.Response<List<Libro>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    libros = response.body();
                    librosAdapter.updateBooks(libros);
                } else {
                    Toast.makeText(Principal.this, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<List<Libro>> call, Throwable t) {
                Toast.makeText(Principal.this, "Error en la conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                txtBuscar.setText(t.getMessage());
            }
        });
    }
}