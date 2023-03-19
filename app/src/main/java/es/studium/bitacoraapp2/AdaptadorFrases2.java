package es.studium.bitacoraapp2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdaptadorFrases2 extends RecyclerView.Adapter<AdaptadorFrases2.ViewHolder> {

    private List<ListaAgendas> listaDeFrases2;

    public AdaptadorFrases2(List<ListaAgendas> listaDeFrases2) {
        this.listaDeFrases2 = listaDeFrases2;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fila_frase_2, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ListaAgendas listaCuadernos = listaDeFrases2.get(position);
        holder.txtFecha.setText(listaCuadernos.getFechaApunte());
        holder.txtTexto.setText(listaCuadernos.getTextoApunte());
    }

    @Override
    public int getItemCount() {
        return listaDeFrases2.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtFecha, txtTexto;

        public ViewHolder(View itemView) {
            super(itemView);
            txtFecha = itemView.findViewById(R.id.txtFrase);
            txtTexto = itemView.findViewById(R.id.txtAutor);
        }
    }
}
