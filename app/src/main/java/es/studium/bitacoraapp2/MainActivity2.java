package es.studium.bitacoraapp2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity2 extends AppCompatActivity {

    private List<ListaCuadernos> listaDeFrases;
    List items = new ArrayList();
    private RecyclerView recyclerView;
    private AdaptadorFrases2 adaptadorFrases2;
    //private FrasesController frasesController;
    private FloatingActionButton fabAgregarFrase;
    private Button Volver,Editar;
    ObtenerBDService hiloConexion;
    ObtenerBDServiceBorrar hiloBorrar;

    List<ListaAgendas> ListaAgendas = new ArrayList<>();;
    JSONArray result;
    JSONObject jsonobject;
    Long id;
    String fecha;
    String texto;
    int FK;
    long idFK;
    String ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        recyclerView = findViewById(R.id.recyclerViewFrases);
        fabAgregarFrase = findViewById(R.id.floatingActionButton);
        Volver = findViewById(R.id.buttonVolver);
        Editar = findViewById(R.id.buttonEditar);
        Bundle extras = getIntent().getExtras();
        // Si no hay datos, salimos
        if (extras == null) {
            finish();
            return;
        }

        idFK = extras.getLong("idCuaderno");
        String textoFrase = extras.getString("textoCuaderno");
        ID = String.valueOf(idFK);
        hiloConexion = new MainActivity2.ObtenerBDService(ID);
        hiloConexion.execute();
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(),
                recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override // Un toque sencillo
            public void onClick(View view, int position) {
                // Pasar a la actividad EditarFraseActivity.java
                ListaAgendas fraseSeleccionada = ListaAgendas.get(position);
                Intent intent = new Intent(MainActivity2.this,
                        Editar2.class);
                intent.putExtra("idApunte",fraseSeleccionada.getIdapunte());
                intent.putExtra("fechaApunte",fraseSeleccionada.getFechaApunte());
                intent.putExtra("textoApunte",fraseSeleccionada.getTextoApunte());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {
                //String id = String.valueOf(ListaCompleta.get(position).getIdCuaderno());
                System.out.println(ListaAgendas.get(position).getIdapunte());
//                final ListaCuadernos fraseParaEliminar = listaDeFrases.get(position);
                String id = String.valueOf(ListaAgendas.get(position).getIdapunte());
                AlertDialog dialog = new AlertDialog
                        .Builder(MainActivity2 .this)
                        .setPositiveButton("Sí, eliminar",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        //frasesController.eliminarFrase(fraseParaEliminar);
                                        hiloBorrar = new ObtenerBDServiceBorrar(id);
                                        hiloBorrar.execute();


                                    }
                                })
                        .setNegativeButton("Cancelar",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.dismiss();
                                    }
                                })
                        .setTitle("Confirmar")
                        .setMessage("¿Eliminar el cuaderno " +
                                ID.toString() + "?")
                        .create();
                dialog.show();
            }

        }));
        fabAgregarFrase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Simplemente cambiamos de actividad
                Intent intent = new Intent(MainActivity2.this,
                        AgregarFraseActivity2.class);
                intent.putExtra("idCuadernoFK",ID );
                startActivity(intent);
            }
        });
        Volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Simplemente cambiamos de actividad
                Intent intent = new Intent(MainActivity2.this,
                        MainActivity.class);
                startActivity(intent);
            }
        });
        Editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity2.this,
                        Editar.class);
                intent.putExtra("idCuadernoFK",ID );
                intent.putExtra("nombreCuaderno",textoFrase);
                startActivity(intent);
                // Simplemente cambiamos de actividad
                //hiloEditar = new ModificacionRemota();
                //hiloEditar.execute();
            }
        });
    }
    public class ObtenerBDService extends AsyncTask<Void,Void,String>
    {
        // Atributos
        String idCuadernoFK;
        // Constructor
        public ObtenerBDService(String id)
        {
            this.idCuadernoFK = id;
        }

        @Override
        protected String doInBackground(Void... voids)
        {
            System.out.println("1");
            try
            {
                System.out.println("2");
                // Crear la URL de conexión al API
                URI baseUri = new URI("http://192.168.18.231/ApiRest/apuntes.php");
                String[] parametros = {"idCuaderno",this.idCuadernoFK};
                URI uri = applyParameters(baseUri, parametros);
                // Create connection
                HttpURLConnection myConnection = (HttpURLConnection)
                        uri.toURL().openConnection();
                // Establecer método. Por defecto GET.
                myConnection.setRequestMethod("GET");
                if (myConnection.getResponseCode() == 200)
                {
                    System.out.println("3");
                    // Conexión exitosa
                    // Creamos Stream para la lectura de datos desde el servidor
                    InputStream responseBody = myConnection.getInputStream();
                    InputStreamReader responseBodyReader =
                            new InputStreamReader(responseBody, StandardCharsets.UTF_8);
                    // Creamos Buffer de lectura
                    BufferedReader bR = new BufferedReader(responseBodyReader);
                    String line;
                    StringBuilder responseStrBuilder = new StringBuilder();
                    // Leemos el flujo de entrada
                    while ((line = bR.readLine()) != null)
                    {
                        responseStrBuilder.append(line);
                    }
                    // Parseamos respuesta en formato JSON
                    result = new JSONArray(responseStrBuilder.toString());
                    // Nos quedamos solamente con la primera
                    //posicion = 0;
                    for(int i=0;i<result.length();i++) {
                        jsonobject = result.getJSONObject(i);
                            id = jsonobject.getLong("idApunte");
                            fecha = jsonobject.getString("fechaApunte");
                            texto = jsonobject.getString("textoApunte");
                            FK = jsonobject.getInt("idCuadernoFK");
                            ListaAgendas.add(new ListaAgendas(id,fecha,texto,FK));
                        System.out.println(id+fecha+texto+FK);
                        // Sacamos dato a dato obtenido


                    }
                    System.out.println(ListaAgendas.toString());
                    while (ListaAgendas == null)
                    {

                    }
                    responseBody.close();
                    responseBodyReader.close();
                    myConnection.disconnect();
                }
                else
                {
                    // Error en la conexión
                    Log.println(Log.ERROR,"Error", "¡Conexión fallida!1");
                }
            }
            catch(Exception e)
            {
                Log.println(Log.ASSERT,"Excepción", e.getMessage());
            }
            adaptadorFrases2 = new AdaptadorFrases2(ListaAgendas);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adaptadorFrases2);

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
        URI applyParameters(URI uri, String[] urlParameters)
        {
            StringBuilder query = new StringBuilder();
            boolean first = true;
            for (int i = 0; i < urlParameters.length; i += 2)
            {
                if (first)
                {
                    first = false;
                }
                else
                {
                    query.append("&");
                }
                try
                {
                    query.append(urlParameters[i]).append("=")
                            .append(URLEncoder.encode(urlParameters[i + 1], "UTF-8"));
                }
                catch (UnsupportedEncodingException ex)
                {
                    /* As URLEncoder are always correct, this exception
                     * should never be thrown. */
                    throw new RuntimeException(ex);
                }
            }
            try
            {
                return new URI(uri.getScheme(), uri.getAuthority(),
                        uri.getPath(), query.toString(), null);
            }
            catch (Exception ex)
            {
                /* As baseUri and query are correct, this exception
                 * should never be thrown. */
                throw new RuntimeException(ex);
            }
        }
    }
    public class ObtenerBDServiceBorrar extends AsyncTask<String,Void,String>
    {

        // Atributos
        String idApunte;
        // Constructor
        public ObtenerBDServiceBorrar(String id)
        {
            this.idApunte = id;
        }
        @Override
        protected String doInBackground(String... strings) {
            System.out.println("2");
            try
            {
                System.out.println("3");
                System.out.println(this.idApunte);
                URI baseUri = new URI("http://192.168.18.231/ApiRest/apuntes.php");
                String[] parametros = {"idApunte",this.idApunte};
                URI uri = applyParameters(baseUri, parametros);
                // Create connection
                HttpURLConnection myConnection = (HttpURLConnection)
                        uri.toURL().openConnection();
                // Establecer método. Por defecto GET.
                myConnection.setRequestMethod("DELETE");
                if (myConnection.getResponseCode() == 200)
                {

                    System.out.println("4");
                    // Success
                    Log.println(Log.ASSERT,"Resultado", "Registro borrado");
                    myConnection.disconnect();
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
            Intent intent = new Intent(MainActivity2.this,
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
        URI applyParameters(URI uri, String[] urlParameters)
        {
            StringBuilder query = new StringBuilder();
            boolean first = true;
            for (int i = 0; i < urlParameters.length; i += 2)
            {
                if (first)
                {
                    first = false;
                }
                else
                {
                    query.append("&");
                }
                try
                {
                    query.append(urlParameters[i]).append("=")
                            .append(URLEncoder.encode(urlParameters[i + 1], "UTF-8"));
                }
                catch (UnsupportedEncodingException ex)
                {
                    /* As URLEncoder are always correct, this exception
                     * should never be thrown. */
                    throw new RuntimeException(ex);
                }
            }
            try
            {
                return new URI(uri.getScheme(), uri.getAuthority(),
                        uri.getPath(), query.toString(), null);
            }
            catch (Exception ex)
            {
                /* As baseUri and query are correct, this exception
                 * should never be thrown. */
                throw new RuntimeException(ex);
            }
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