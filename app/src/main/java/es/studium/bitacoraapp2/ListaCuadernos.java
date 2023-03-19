package es.studium.bitacoraapp2;

public class ListaCuadernos {

    private String nombreCuaderno;

    private long idCuaderno; // El ID de la BD

    public ListaCuadernos(String nombreCuaderno) {
        this.nombreCuaderno = nombreCuaderno;
    }

    public ListaCuadernos(String nombreCuaderno, long idCuaderno) {
        this.nombreCuaderno = nombreCuaderno;
        this.idCuaderno = idCuaderno;
    }
    public String getNombreCuaderno() { return nombreCuaderno; }

    public void setNombreCuaderno(String nombreCuaderno) { this.nombreCuaderno = nombreCuaderno; }

    public long getIdCuaderno() { return idCuaderno;    }

    public void setIdCuaderno(long idCuaderno) { this.idCuaderno = idCuaderno;    }

    @Override
    public String toString() {
        return "Cuadernos{" +
                "id:='" + idCuaderno + '\'' +
                ", Nombre=" + nombreCuaderno +
                '}';
    }
}
