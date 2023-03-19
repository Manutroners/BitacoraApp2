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

public class AgregarFraseActivity2 extends AppCompatActivity {
    private Button btnAgregarFrase, btnCancelarNuevaFrase;
    private EditText  etFecha,etTexto;
    private RecyclerView recyclerView;
    private AdaptadorFrases2 adaptadorFrases2;
    ObtenerBDService hiloConexion;
    List<ListaAgendas> ListaCompleta = new ArrayList<>();;
    JSONArray result;
    JSONObject jsonobject;
    Long id;
    String nombre;
    String ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_frase2);
        // Instanciar vistas
        Bundle extras = getIntent().getExtras();
        // Si no hay datos, salimos
        if (extras == null) {
            finish();
            return;
        }

        String idFK = extras.getString("idCuadernoFK");
        //ID = String.valueOf(idFK);
        System.out.println(idFK);
        etTexto = findViewById(R.id.txtTexto);
        etFecha = findViewById(R.id.txtFecha);

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
                etFecha.setError(null);

                String fecha = etFecha.getText().toString(),
                        texto = etTexto.getText().toString();
                //Integer FKnum = etFK.getId();
                if ("".equals(texto)) {
                    etTexto.setError("Escribe el texto de la frase");
                    etTexto.requestFocus();
                    return;
                }
                if ("".equals(fecha)) {
                    etFecha.setError("Escribe el autor de la frase");
                    etFecha.requestFocus();
                    return;
                }

              //  ListaAgendas nuevaLista = new ListaAgendas(fecha,texto,FKnum);
                hiloConexion = new ObtenerBDService(
                        etFecha.getText().toString(),
                        etTexto.getText().toString()
                        ,idFK);
                hiloConexion.execute();

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

        String fechaApuntes;
        String textoApuntes;
        String idCuadernoFK;

        // Constructor
        public ObtenerBDService(String fechaApuntes,String textoApuntes,String idCuadernoFK)
        {
            this.fechaApuntes = fechaApuntes;
            this.textoApuntes = textoApuntes;
            this.idCuadernoFK = idCuadernoFK;
            System.out.println("1");
        }
        @Override
        protected String doInBackground(Void... argumentos) {
            System.out.println("2");
            try
            {
                System.out.println("3");
                // Crear la URL de conexión al API
                URL url = new URL("http://192.168.18.231/ApiRest/apuntes.php");
                // Crear la conexión HTTP
                HttpURLConnection myConnection = (HttpURLConnection) url.openConnection();
                // Establecer método de comunicación.
                myConnection.setRequestMethod("POST");
                // Conexión exitosa
                HashMap<String, String> postDataParams = new HashMap<>();
                postDataParams.put("fechaApunte", this.fechaApuntes);
                postDataParams.put("textoApunte", this.textoApuntes);
                postDataParams.put("idCuadernoFK",this.idCuadernoFK);
                System.out.println(getPostDataString(postDataParams));
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
                    Intent intent = new Intent(AgregarFraseActivity2.this,
                            MainActivity2.class);
                    startActivity(intent);
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

            Intent intent = new Intent(AgregarFraseActivity2.this,
                    MainActivity.class);
            startActivity(intent);
//            adaptadorFrases2 = new AdaptadorFrases2(ListaCompleta);
//            RecyclerView.LayoutManager mLayoutManager =
//                    new LinearLayoutManager(getApplicationContext());
//            recyclerView.setLayoutManager(mLayoutManager);
//            recyclerView.setItemAnimator(new DefaultItemAnimator());
//            recyclerView.setAdapter(adaptadorFrases2);


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