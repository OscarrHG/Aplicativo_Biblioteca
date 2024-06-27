package com.arzendev.libapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
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

public class Regisuser extends AppCompatActivity {

    EditText txtnombres, txtapellidos, txtcorreocrear, txtpswdcrear, txtpswdcrearconf;
    Button btnBack, btnCrearUsuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_regisuser);

        btnBack = findViewById(R.id.btnBack);
        btnCrearUsuario = findViewById(R.id.btnCrearUsuario);
        txtnombres = findViewById(R.id.txtNombres);
        txtapellidos = findViewById(R.id.txtApellidos);
        txtcorreocrear = findViewById(R.id.txtCorreoCrear);
        txtpswdcrear = findViewById(R.id.txtPswdCrear);
        txtpswdcrearconf = findViewById(R.id.txtPswdCrearConf);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                Intent intent2 = new Intent(getApplicationContext(), Regisuser.class);
                startActivity(intent);
                stopService(intent2);
            }
        });

        btnCrearUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearUsuario();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void crearUsuario()
    {
        try
        {
            String p_nombres = txtnombres.getText().toString();
            String p_apellidos = txtapellidos.getText().toString();
            String p_correocrear = txtcorreocrear.getText().toString();
            String p_pswdcrear = txtpswdcrear.getText().toString();
            String p_pswdcrearconf = txtpswdcrearconf.getText().toString();

            ProgressDialog progressDialog =new ProgressDialog(this);
            progressDialog.setMessage("cargando");


            if (p_nombres.isEmpty()){
                Toast.makeText(this,"Ingrese nombre",Toast.LENGTH_SHORT).show();
            }else if (p_apellidos.isEmpty()){
                Toast.makeText(this,"Ingrese apellido",Toast.LENGTH_SHORT).show();
            }else if (p_correocrear.isEmpty()){
                Toast.makeText(this,"Ingrese Correo",Toast.LENGTH_SHORT).show();
            }else if (p_pswdcrear.isEmpty()){
                Toast.makeText(this,"Ingrese Contraseña",Toast.LENGTH_SHORT).show();
            }else if (p_pswdcrearconf.isEmpty()){
                Toast.makeText(this,"Ingrese Contraseña de confirmacion",Toast.LENGTH_SHORT).show();
            }else if (!p_pswdcrearconf.equals(p_pswdcrear)){
                Toast.makeText(this,"Las contraseñas deben ser iguales",Toast.LENGTH_SHORT).show();
            }else {
                progressDialog.show();
                progressDialog.dismiss();
                StringRequest request =new StringRequest(Request.Method.POST, "http://10.0.2.2/crud/crear_usuario.php",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(Regisuser.this, "Registrado correctamente", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                Intent intent2 = new Intent(getApplicationContext(), Regisuser.class);
                                startActivity(intent);
                                stopService(intent2);

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Regisuser.this,error.getMessage(),Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
                ){

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {

                        Map<String,String>params=new HashMap<>();
                        params.put("nombres",p_nombres);
                        params.put("apellidos",p_apellidos);
                        params.put("usu_usuario",p_correocrear);
                        params.put("usu_password",p_pswdcrear);

                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(Regisuser.this);
                requestQueue.add(request);
            }
            Toast.makeText(this,"Usuario registrado",Toast.LENGTH_SHORT).show();
            txtnombres.setText("");
            txtapellidos.setText("");
            txtcorreocrear.setText("");
            txtpswdcrear.setText("");
            txtpswdcrearconf.setText("");
        }
        catch (Exception ex)
        {
            Toast.makeText(this,"Verificar datos a ingresar",Toast.LENGTH_SHORT).show();
        }
    }
}