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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Favs extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LibrosAdapter librosAdapter;
    private List<Libro> libros;
    Gson gson;
    TextView txtSaludar;
    String idUs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_favs);

        recyclerView = findViewById(R.id.listRecyclerViewFavs);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        libros = new ArrayList<>();
        librosAdapter = new LibrosAdapter(libros, this);
        recyclerView.setAdapter(librosAdapter);

        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        String correo = sharedPreferences.getString("array", "");

        try {
            JSONObject jsonObject = new JSONObject(correo);
            idUs = jsonObject.getString("id");
            String nombres = jsonObject.getString("nombres");

            // Asignar los valores a los TextViews
            txtSaludar = findViewById(R.id.textTituloCatalogo);
            txtSaludar.setText("Libros favoritos de "+ nombres + "!");

            librosFavoritos(idUs);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        gson = new GsonBuilder()
                .setLenient()
                .create();

        Button irCatalogo = findViewById(R.id.btnCatalogo);
        irCatalogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Principal.class);
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

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void librosFavoritos(String query){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2/crud/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        Call<List<Libro>> call = apiService.librosFavoritos(query);

        call.enqueue(new Callback<List<Libro>>() {
            @Override
            public void onResponse(Call<List<Libro>> call, retrofit2.Response<List<Libro>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    libros = response.body();
                    librosAdapter.updateBooks(libros);
                } else {
                    Toast.makeText(Favs.this, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
                    TextView txt5 = findViewById(R.id.textView5);
                    txt5.setText(response.toString());
                }
            }

            @Override
            public void onFailure(Call<List<Libro>> call, Throwable t) {
                Toast.makeText(Favs.this, "Error en la conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                TextView txt5 = findViewById(R.id.textView5);
                txt5.setText(t.getMessage());
            }
        });
    }
}