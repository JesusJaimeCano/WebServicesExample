package e.jesus.webservicesexample;

/**
 * Created by Jesus on 26/12/2017.
 */

public class Comentario {
    String usuario;
    String comentario;
    int valoracion;

    public Comentario(String usuario, String comentario, int valoracion) {
        this.usuario = usuario;
        this.comentario = comentario;
        this.valoracion = valoracion;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getComentario() {
        return comentario;
    }

    public int getValoracion() {
        return valoracion;
    }



}
