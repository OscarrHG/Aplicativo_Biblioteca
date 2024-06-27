package com.arzendev.libapp;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    @GET("get_libros.php")
    Call<List<Libro>> getLibros();

    @GET("buscar_libros.php")
    Call<List<Libro>> buscarLibros(@Query("query") String query);

    @GET("libros_favoritos.php")
    Call<List<Libro>> librosFavoritos(@Query("query") String query);
}
