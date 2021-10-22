package br.com.smartzu.acolherapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterVisitantes extends RecyclerView.Adapter<AdapterVisitantes.MyViewHolder> {

    private List<Visitante> visitantes;
    private Context context;

    public AdapterVisitantes(List<Visitante> visitantes, Context context) {
        this.visitantes = visitantes;
        this.context = context;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_lista, parent, false);

        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Visitante visitante = visitantes.get(position);
        holder.nome.setText(visitante.getNomeVisitante());
        holder.dataVisita.setText(visitante.getDataVisita());
        holder.ddd.setText("DDD: " + visitante.getDddVisitante());
        holder.telefone.setText("TEL: " + visitante.getTelVisitante());

    }

    @Override
    public int getItemCount() {
        return visitantes.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nome;
        TextView dataVisita;
        TextView ddd;
        TextView telefone;


        public MyViewHolder(View itemView) {
            super(itemView);

            nome = itemView.findViewById(R.id.text_nome);
            dataVisita = itemView.findViewById(R.id.text_dia_visita);
            ddd = itemView.findViewById(R.id.txt_ddd);
            telefone = itemView.findViewById(R.id.text_tel);
        }
    }

}
