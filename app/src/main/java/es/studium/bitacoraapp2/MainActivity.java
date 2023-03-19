package es.studium.bitacoraapp2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

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

public class MainActivity extends AppCompatActivity {

    private List<ListaCuadernos> listaDeFrases;
    List items = new ArrayList();
    private RecyclerView recyclerView;
    private AdaptadorFrases adaptadorFrases;
    //private FrasesController frasesController;
    private FloatingActionButton fabAgregarFrase;
    ObtenerBDService hiloConexion;
    ObtenerBDServiceBorrar hiloBorrar;
    List<ListaCuadernos> ListaCompleta = new ArrayList<>();;
    JSONArray result;
    JSONObject jsonobject;
    Long id;
    String nombre;

    String IP = "http://192.168.18.231/ApiRest";
    String METHOD = IP +"/contactos.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerViewFrases);
        fabAgregarFrase = findViewById(R.id.floatingActionButton);

        hiloConexion = new ObtenerBDService();
        hiloConexion.execute();

        //acceso = new ConsultaRemota();
        //acceso.execute();

        // Por defecto es una lista vacía,
        // se la ponemos al adaptador y configuramos el recyclerView
        //listaDeFrases = new ArrayList<>();

        // Una vez que ya configuramos el RecyclerView le ponemos los datos de la BD
        //refrescarListaDeCuadernos();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(),
                recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override // Un toque sencillo
            public void onClick(View view, int position) {

                // Pasar a la actividad EditarFraseActivity.java
                ListaCuadernos fraseSeleccionada = ListaCompleta.get(position);
                Intent intent = new Intent(MainActivity.this,
                        MainActivity2.class);
                intent.putExtra("idCuaderno", fraseSeleccionada.getIdCuaderno());
                intent.putExtra("textoCuaderno", fraseSeleccionada.getNombreCuaderno());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {
                System.out.println(ListaCompleta.get(position).getIdCuaderno());
//                final ListaCuadernos fraseParaEliminar = listaDeFrases.get(position);
                String id = String.valueOf(ListaCompleta.get(position).getIdCuaderno());
                AlertDialog dialog = new AlertDialog
                        .Builder(MainActivity.this)
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
                                ListaCompleta.get(position).getNombreCuaderno() + "?")
                        .create();
                dialog.show();
            }


        }));
        fabAgregarFrase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Simplemente cambiamos de actividad
                Intent intent = new Intent(MainActivity.this,
                        AgregarFraseActivity.class);
                startActivity(intent);
            }
        });
    }



    public class ObtenerBDService extends AsyncTask<String,Void,String>
    {
        @Override
            protected String doInBackground(String... strings) {

           // String cadena = strings[0];

            String devuelve = "";

//            if(strings[1] == "1")
//            {
                try
                {
                    URL url = new URL("http://192.168.18.231/ApiRest/cuadernos.php");
                    // Crear la conexión HTTP
                    HttpURLConnection myConnection = (HttpURLConnection) url.openConnection();
                    // Establecer método de comunicación. Por defecto GET.
                    myConnection.setRequestMethod("GET");
                    if (myConnection.getResponseCode() == 200)
                    {
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
                            // Sacamos dato a dato obtenido

                            id = jsonobject.getLong("idCuaderno");
                            nombre = jsonobject.getString("nombreCuaderno");
                            ListaCompleta.add(new ListaCuadernos(nombre,id));
                        }
                        System.out.println(ListaCompleta);
                        while (ListaCompleta == null)
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
                catch (Exception e)
                {
                    Log.println(Log.ERROR,"Error", "¡Conexión fallida!2");
                }

                adaptadorFrases = new AdaptadorFrases(ListaCompleta);
                RecyclerView.LayoutManager mLayoutManager =
                        new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(adaptadorFrases);

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
    public class ObtenerBDServiceBorrar extends AsyncTask<String,Void,String>
    {

        // Atributos
        String idCuaderno;
        // Constructor
        public ObtenerBDServiceBorrar(String id)
        {
            this.idCuaderno = id;
        }
        @Override
        protected String doInBackground(String... strings) {
            System.out.println("2");
            try
            {
                System.out.println("3");
                URI baseUri = new URI("http://192.168.18.231/ApiRest/cuadernos.php");
                String[] parametros = {"idCuaderno",this.idCuaderno};
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

//            adaptadorFrases = new AdaptadorFrases(ListaCompleta);
//            RecyclerView.LayoutManager mLayoutManager =
//                    new LinearLayoutManager(getApplicationContext());
//            recyclerView.setLayoutManager(mLayoutManager);
//            recyclerView.setItemAnimator(new DefaultItemAnimator());
//            recyclerView.setAdapter(adaptadorFrases);
            Intent intent = new Intent(MainActivity.this,
                    MainActivity.class);
            startActivity(intent);

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


    public void refrescarListaDeCuadernos() {
//        // Obtenemos la lista de la BD y se la metemos al RecyclerView
//        if (adaptadorFrases2 == null) return;
//        listaDeFrases = frasesController.obtenerFrases();
//        adaptadorFrases2.setListaDeFrases(listaDeFrases);
//        adaptadorFrases2.notifyDataSetChanged();
    }
}