package es.tudir.fastfinger.modelo;

/**
 * Created by lscofield on 29/05/2016.
 */
public class Meta {

    private static final String TAG = Meta.class.getSimpleName();
    /*
        Atributos
         */
    private String id_hit;
    private String name;
    private String speed;

    public Meta(String id_hit,
                String name,
                String speed) {
        this.id_hit = id_hit;
        this.name = name;
        this.speed = speed;
    }

    public String getId_evento() {
        return id_hit;
    }

    public String getEvento() {
        return name;
    }

    public String getDescripcion() {
        return speed;
    }
}
