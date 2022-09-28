package com.andrewmarques.android.organize.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.andrewmarques.android.organize.R;
import com.andrewmarques.android.organize.model.Movimentacao;

import java.text.DecimalFormat;
import java.util.List;

public class AdapterMovimentacao extends RecyclerView.Adapter<AdapterMovimentacao.MyViewHolder> {

    List<Movimentacao> movimentacoes;
    Context context;

    public AdapterMovimentacao(List<Movimentacao> movimentacoes, Context context) {
        this.movimentacoes = movimentacoes;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_movimentacao, parent, false);
        return new MyViewHolder(itemLista);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Movimentacao movimentacao = movimentacoes.get(position);

        holder.titulo.setText(movimentacao.getDescricao());
        holder.categoria.setText(movimentacao.getCategoria());

        String campoValor = new DecimalFormat( "0.00" ).format(movimentacao.getValor()).replace(',', '.');
        if (movimentacao.getTipo() == "d" || movimentacao.getTipo().equals("d")) {
            holder.valor.setTextColor(context.getResources().getColor(R.color.colorAccentDespesa));
            holder.valor.setText("-" + campoValor);
        }else
        if (movimentacao.getTipo() == "r" || movimentacao.getTipo().equals("r")) {
            holder.valor.setTextColor(context.getResources().getColor(R.color.colorAccentReceita));
            holder.valor.setText(campoValor);
        }
    }


    @Override
    public int getItemCount() {
        return movimentacoes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView titulo, valor, categoria;

        public MyViewHolder(View itemView) {
            super(itemView);

            titulo = itemView.findViewById(R.id.textAdapterDescricao);
            categoria = itemView.findViewById(R.id.textAdapterCategoria);
            valor = itemView.findViewById(R.id.textAdapterValor);
        }

    }

}
