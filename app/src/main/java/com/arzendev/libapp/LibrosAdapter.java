package com.arzendev.libapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class LibrosAdapter extends RecyclerView.Adapter<LibrosAdapter.LibroViewHolder> {
    private List<Libro> libros;
    private Context context;

    public LibrosAdapter(List<Libro> libros, Context context) {
        this.libros = libros;
        this.context = context;
    }

    @NonNull
    @Override
    public LibroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false); //cardview_libro
        return new LibroViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LibroViewHolder holder, int position) {
        Libro libro = libros.get(position);
        holder.titulo.setText(libro.getTitulo().trim());
        holder.autor.setText(libro.getAutor().trim());
        holder.genero.setText(libro.getGenero().trim());
        holder.disponibilidad.setText(libro.getDisponibilidad().trim());
        // Aquí puedes cargar la imagen de portada usando una biblioteca como Glide
        Glide.with(context).load(libro.getPortada_url()).into(holder.portada);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, Detalle.class);
            intent.putExtra("id",libro.getId());
            intent.putExtra("titulo", libro.getTitulo());
            intent.putExtra("autor", libro.getAutor());
            intent.putExtra("genero", libro.getGenero());
            intent.putExtra("portada_url", libro.getPortada_url());
            intent.putExtra("sinopsis", libro.getSinopsis()); // Asegúrate de que el modelo Libro tenga un campo descripción
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return libros.size();
    }

    public void updateBooks(List<Libro> libros) {
        this.libros = libros;
        notifyDataSetChanged();
    }

    class LibroViewHolder extends RecyclerView.ViewHolder {
        TextView titulo, autor, genero, disponibilidad;
        ImageView portada;

        public LibroViewHolder(@NonNull View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.tituloTextView);
            autor = itemView.findViewById(R.id.autorTextView);
            genero = itemView.findViewById(R.id.generoTextView);
            portada = itemView.findViewById(R.id.idItemImageView);
            disponibilidad = itemView.findViewById(R.id.disponibilidadTextView);
        }
    }
}
