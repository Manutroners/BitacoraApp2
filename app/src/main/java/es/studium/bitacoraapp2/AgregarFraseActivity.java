package es.studium.bitacoraapp2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AgregarFraseActivity extends AppCompatActivity {
    private Button btnAgregarFrase, btnCancelarNuevaFrase;
    private EditText etTexto;
    private RecyclerView recyclerView;
    private AdaptadorFrases adaptadorFrases;
    AgregarFraseActivity.ObtenerBDService hiloConexion;
    List<ListaCuadernos> ListaCompleta = new ArrayList<>();;
    JSONArray result;
    JSONObject jsonobject;
    Long id;
    String nombre;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_frase);
        // Instanciar vistas
        //recyclerView = findViewById(R.id.recyclerViewFrases);
        //fabAgregarFrase = findViewById(R.id.floatingActionButton);
        etTexto = findViewById(R.id.txtFecha);
        //etAutor = findViewById(R.id.txtAutor);
        btnAgregarFrase = findViewById(R.id.btnAgregarFrase);
        btnCancelarNuevaFrase = findViewById(R.id.btnCancelarNuevaFrase);
        // Crear el controlador
        //frasesController = new FrasesController(AgregarFraseActivity.this);
        // Agregar listener del botón de guardar
        btnAgregarFrase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Resetear errores a ambos
                etTexto.setError(null);
               // etAutor.setError(null);
                String texto = etTexto.getText().toString();
                 //       autor = etAutor.getText().toString();
                if ("".equals(texto)) {
                    etTexto.setError("Escribe el texto de la frase");
                    etTexto.requestFocus();
                    return;
                }
//                if ("".equals(autor)) {
//                    etAutor.setError("Escribe el autor de la frase");
//                    etAutor.requestFocus();
//                    return;
//                }

                ListaCuadernos nuevaLista = new ListaCuadernos(texto);
                hiloConexion = new AgregarFraseActivity.ObtenerBDService(texto);
                hiloConexion.execute();
                // Ya pasó la validación
                //FraseFamosa nuevaFrase = new FraseFamosa(texto, autor);
               // long id = frasesController.nuevaFrase(nuevaFrase);
//                if (id == -1) {
//                    // De alguna manera ocurrió un error
//                    Toast.makeText(AgregarFraseActivity.this,
//                            "Error al guardar. Intenta de nuevo", Toast.LENGTH_SHORT).show();
//                } else {
//                    // Terminar
//                    finish();
//                }
            }
        });

        // El de cancelar simplemente cierra la actividad
        btnCancelarNuevaFrase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public class ObtenerBDService extends AsyncTask<Void,Void,String>
    {
        // Atributos
        String nombreCuaderno;

        // Constructor
        public ObtenerBDService(String nombreCuaderno)
        {
            this.nombreCuaderno = nombreCuaderno;

        }
        @Override
        protected String doInBackground(Void... argumentos) {

            try
            {
                // Crear la URL de conexión al API
                URL url = new URL("http://192.168.18.231/ApiRest/cuadernos.php");
                // Crear la conexión HTTP
                HttpURLConnection myConnection = (HttpURLConnection) url.openConnection();
                // Establecer método de comunicación.
                myConnection.setRequestMethod("POST");
                // Conexión exitosa
                HashMap<String, String> postDataParams = new HashMap<>();
                postDataParams.put("nombreCuaderno", this.nombreCuaderno);
                myConnection.setDoInput(true);
                myConnection.setDoOutput(true);
                OutputStream os = myConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os,
                        StandardCharsets.UTF_8));
                writer.write(getPostDataString(postDataParams));
                writer.flush();
                writer.close();
                os.close();
                myConnection.getResponseCode();
                if (myConnection.getResponseCode() == 200)
                {
                    // Success
                    myConnection.disconnect();
                }
                else
                {
                    // Error handling code goes here
                    Log.println(Log.ASSERT, "Error", "Error");
                }
            }
            catch(Exception e)
            {
                Log.println(Log.ASSERT,"Excepción", e.getMessage());
            }


//            adaptadorFrases = new AdaptadorFrases(ListaCompleta);
//            RecyclerView.LayoutManager mLayoutManager =
//                    new LinearLayoutManager(getApplicationContext());
//            recyclerView.setLayoutManager(mLayoutManager);
//            recyclerView.setItemAnimator(new DefaultItemAnimator());
//            recyclerView.setAdapter(adaptadorFrases);
            Intent intent = new Intent(AgregarFraseActivity.this,
                    MainActivity.class);
            startActivity(intent);

            return (null);
        }
        private String getPostDataString(HashMap<String, String> params)
                throws UnsupportedEncodingException
        {
            StringBuilder result = new StringBuilder();
            boolean first = true;
            for(Map.Entry<String, String> entry : params.entrySet())
            {
                if (first)
                {
                    first = false;
                }
                else
                {
                    result.append("&");
                }
                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            }
            return result.toString();
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