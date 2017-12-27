package e.jesus.webservicesexample;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    String url = "http://alexchaps.com/atl/resenas.php?restaurante=0";
    ArrayList<Comentario> comentarios;
    ListView listaComentarios;
    Button agregar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        comentarios = new ArrayList<>();
        listaComentarios = findViewById(R.id.listaItemsWS);

        agregar = findViewById(R.id.botonAgragarNuevoComentario);

        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AgregarResenaActivity.class);
                startActivity(intent);
            }
        });





    }

    @Override
    protected void onResume() {
        super.onResume();

        ConsultarWebServices(0);
    }

    public void ConsultarWebServices (int idRestaurante){
        comentarios.clear();
        JsonObjectRequest Request = new JsonObjectRequest(
                com.android.volley.Request.Method.GET,
                url + String.valueOf(idRestaurante),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Respuesta sin Limpiar ", response.toString());
                        try {
                            JSONArray obj  = response.getJSONArray("resenas");

                            for (int i = 0; i<obj.length(); i++){
                                JSONObject jsonObject = obj.getJSONObject(i);
                                String comentarioText = jsonObject.getString("comentario");
                                String usuario = jsonObject.getString("usuario");
                                int estrellas = jsonObject.getInt("estrellas");
                                comentarios.add(new Comentario(usuario,comentarioText, estrellas));
                            }

                        }catch (JSONException e){
                            e.printStackTrace();
                        }

                        listaComentarios.setAdapter(new MyAdapatador());

                    }
                },
                new ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Respuesta", "Error" + error.getMessage());
                    }
                }
        );

        AppController.getInstance().addToRequestQueue(Request);
    }

    class MyAdapatador extends ArrayAdapter<Comentario>{
        MyAdapatador(){
            super(MainActivity.this, R.layout.item_ws_personalizado, comentarios);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View row = getLayoutInflater().inflate(R.layout.item_ws_personalizado, parent, false);

            TextView usuario = row.findViewById(R.id.usuarioWSTV);
            TextView comentario = row.findViewById(R.id.comentarioWSTV);
            ImageView imagenCalificacion = row.findViewById(R.id.imagenCalificacionWSIV);

            Comentario comentarioActual = comentarios.get(position);
            usuario.setText(comentarioActual.getUsuario());
            comentario.setText(comentarioActual.getComentario());

            switch (comentarioActual.getValoracion()){
                case 1:
                case 2:
                    imagenCalificacion.setImageResource(R.drawable.enojado_emoticon);
                    break;
                case 3:
                case 4:
                    imagenCalificacion.setImageResource(R.drawable.neutral_emoticon);
                    break;
                case 5:
                    imagenCalificacion.setImageResource(R.drawable.feliz_emoticon);
                    break;
            }
            return row;

        }
    }
}
