package com.andrews.organizzeclone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.andrews.organizzeclone.R;
import com.andrews.organizzeclone.model.Movimentacao;

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

        holder.categoria.setText(movimentacao.getCategoria());
        holder.descricao.setText(movimentacao.getDescricao());
        holder.valor.setText(String.valueOf(movimentacao.getValor()));
        holder.valor.setTextColor(context.getResources().getColor(R.color.colorAccentReceita));
        holder.dia.setText(movimentacao.getData().substring(0, 2));

        if (movimentacao.getTipo().equals("d")) {
            holder.valor.setTextColor(context.getResources().getColor(R.color.colorAccent));
            holder.valor.setText("-" + movimentacao.getValor());
        }
    }


    @Override
    public int getItemCount() {
        return movimentacoes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView valor, categoria, descricao, dia;

        public MyViewHolder(View itemView) {
            super(itemView);

            categoria = itemView.findViewById(R.id.textAdapterCategoria);
            descricao = itemView.findViewById(R.id.textAdapterDescricao);
            valor = itemView.findViewById(R.id.textAdapterValor);
            dia = itemView.findViewById(R.id.textAdapterDia);
        }
    }
}
