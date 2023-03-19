package es.studium.bitacoraapp2;

public class ListaAgendas {

    private long idapunte; // El ID de la BD
    private String fechaApunte;
    private String textoApunte;
    private int idCuadernoFK;

    public ListaAgendas(String fechaApunte,String textoApunte,int idCuadernoFK) {
        this.fechaApunte = fechaApunte;
        this.textoApunte = textoApunte;
        this.idCuadernoFK = idCuadernoFK;
    }

    public ListaAgendas(long idapunte,String fechaApunte,String textoApunte,int idCuadernoFK) {
        this.idapunte = idapunte;
        this.fechaApunte = fechaApunte;
        this.textoApunte = textoApunte;
        this.idCuadernoFK = idCuadernoFK;
    }

    public long getIdapunte() {
        return idapunte;
    }

    public void setIdapunte(long idapunte) {
        this.idapunte = idapunte;
    }

    public String getFechaApunte() {
        return fechaApunte;
    }

    public void setFechaApunte(String fechaApunte) {
        this.fechaApunte = fechaApunte;
    }

    public String getTextoApunte() {
        return textoApunte;
    }

    public void setTextoApunte(String textoApunte) {
        this.textoApunte = textoApunte;
    }

    public int getIdCuadernoFK() {
        return idCuadernoFK;
    }

    public void setIdCuadernoFK(int idCuadernoFK) {
        this.idCuadernoFK = idCuadernoFK;
    }
}
