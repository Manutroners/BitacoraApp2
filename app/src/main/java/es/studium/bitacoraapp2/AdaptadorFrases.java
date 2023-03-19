package es.studium.bitacoraapp2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.studium.bitacoraapp2.ListaCuadernos;
import es.studium.bitacoraapp2.R;

public class AdaptadorFrases extends RecyclerView.Adapter<AdaptadorFrases.ViewHolder> {

    private List<ListaCuadernos> listaDeFrases;

    public AdaptadorFrases(List<ListaCuadernos> listaDeFrases) {
        this.listaDeFrases = listaDeFrases;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fila_frase, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ListaCuadernos listaCuadernos = listaDeFrases.get(position);
        holder.txtNombre.setText(listaCuadernos.getNombreCuaderno());
        holder.txtId.setText(String.valueOf(listaCuadernos.getIdCuaderno()));
    }

    @Override
    public int getItemCount() {
        return listaDeFrases.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtNombre, txtId;

        public ViewHolder(View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtAutor);
            txtId = itemView.findViewById(R.id.txtFrase);
        }
    }
}
