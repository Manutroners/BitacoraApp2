package es.studium.bitacoraapp2;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class Editar2 extends AppCompatActivity {

    private Button btnEditar, btnCancelar;
    private EditText etFecha,etTexto;
    ModificacionRemota hiloEditar2;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar2);

        etFecha = findViewById(R.id.TextFecha);
        etTexto = findViewById(R.id.TextCompleto);
        btnEditar = findViewById(R.id.buttonAceptar2);
        btnCancelar = findViewById(R.id.buttonCancelar2);
        Bundle extras = getIntent().getExtras();
        // Si no hay datos, salimos
        if (extras == null) {
            finish();
            return;
        }
        Long idLong = extras.getLong("idApunte");
        String fecha = extras.getString("fechaApunte");
        String texto = extras.getString("textoApunte");
        String id = String.valueOf(idLong);

        etFecha.setText(fecha);
        etTexto.setText(texto);
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String NuevaFecha = String.valueOf(etFecha.getText());
                String NuevoText = String.valueOf(etTexto.getText());
                // Simplemente cambiamos de actividad
                hiloEditar2 = new ModificacionRemota(id,NuevaFecha,NuevoText);
                hiloEditar2.execute();
            }
        });
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public class ModificacionRemota extends AsyncTask<Void,Void,String>
    {
        String idApunte;
        String fechaApunte;
        String textoApunte;
        // Constructor
        public ModificacionRemota(String idApunte,String fechaApunte,String textoApunte)
        {
            this.idApunte = idApunte;
            this.fechaApunte = fechaApunte;
            this.textoApunte = textoApunte;
        }
        @Override
        protected String doInBackground(Void... strings) {
            System.out.println("2");
            try
            {
                System.out.println("3");
                System.out.println(idApunte+" / "+fechaApunte+" / "+textoApunte);
                StringBuilder response = new StringBuilder();
                Uri uri = new Uri.Builder()
                        .scheme("http")
                        .authority("192.168.18.231")
                        .path("/ApiRest/apuntes.php")
                        .appendQueryParameter("idApunte", this.idApunte)
                        .appendQueryParameter("fechaApunte", this.fechaApunte)
                        .appendQueryParameter("textoApunte", this.textoApunte)
                        .build();
                // Create connection
                URL url = new URL(uri.toString());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(15000);
                connection.setConnectTimeout(15000);
                connection.setRequestMethod("PUT");
                connection.setDoInput(true);
                connection.setDoOutput(true);
                int responseCode=connection.getResponseCode();
                if (responseCode == HttpsURLConnection.HTTP_OK)
                {
                    String line;
                    BufferedReader br=new BufferedReader(new
                            InputStreamReader(connection.getInputStream()));
                    while ((line=br.readLine()) != null)
                    {
                        response.append(line);
                    }
                }
                else
                {
                    response = new StringBuilder();
                }
                connection.getResponseCode();
                if (connection.getResponseCode() == 200)
                {
                    // Success
                    Log.println(Log.ASSERT,"Resultado", "Registro modificado:"+response);
                    connection.disconnect();
                }
                else
                {
                    // Error handling code goes here
                    Log.println(Log.ASSERT,"Error", "Error");
                }
            }
            catch (Exception e)
            {
                Log.println(Log.ERROR,"Error", "¡Conexión fallida!2");
            }
            Intent intent = new Intent(Editar2.this,
                    MainActivity.class);
            startActivity(intent);
//            adaptadorFrases = new AdaptadorFrases(ListaCompleta);
//            RecyclerView.LayoutManager mLayoutManager =
//                    new LinearLayoutManager(getApplicationContext());
//            recyclerView.setLayoutManager(mLayoutManager);
//            recyclerView.setItemAnimator(new DefaultItemAnimator());
//            recyclerView.setAdapter(adaptadorFrases);
//            Intent intent = new Intent(MainActivity2.this,
//                    MainActivity2.class);
//            startActivity(intent);

            return null;
        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
        }
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }
}