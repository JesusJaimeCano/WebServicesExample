package e.jesus.webservicesexample;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jesus on 26/12/2017.
 */

public class AgregarResenaActivity extends AppCompatActivity {

    EditText usuario, comentario;
    Button agrgarResena;
    SeekBar calificaicon;
    int calif = 0;
    int restaurante = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agregar_comentario);

        usuario = findViewById(R.id.usuarionuevo);
        comentario = findViewById(R.id.comentarionuevo);
        agrgarResena = findViewById(R.id.nuevoregistroButton);
        calificaicon = findViewById(R.id.seek);

        calificaicon.setMax(5);
        calificaicon.setProgress(1);
        calificaicon.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                calif = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(AgregarResenaActivity.this, "Asignaste: "+ calif+ " calificación", Toast.LENGTH_SHORT).show();
            }
        });

        agrgarResena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subirComentario();
            }
        });

    }

    public void subirComentario(){

        final ProgressDialog loading = ProgressDialog.show(this,"Subiendo Comentario", "Espere Por favor",  false, false);
        String url = "http://alexchaps.com/atl/nuevaResena.php";

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        Toast.makeText(AgregarResenaActivity.this, "El comentario se subio exitosamente", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        Toast.makeText(AgregarResenaActivity.this, "Error al subir comentario intenta de nuevo", Toast.LENGTH_SHORT).show();
                    }
                }
        ){

            protected Map<String,String> getParams(){

                Map<String, String> params = new HashMap<>();
                params.put("restaurante", String.valueOf(restaurante));
                params.put("usuario", usuario.getText().toString());
                params.put("comentario", comentario.getText().toString());
                params.put("estrellas", String.valueOf(calif));
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(stringRequest);

    }
}
