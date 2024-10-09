package com.example.calculadorajava;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistorialAdapter extends RecyclerView.Adapter<HistorialAdapter.CalculoViewHolder> {

    private List<Calculo> calculosList;

    public HistorialAdapter(List<Calculo> calculosList) {
        this.calculosList = calculosList;
    }

    @NonNull
    @Override
    public CalculoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_historial, parent, false);
        return new CalculoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CalculoViewHolder holder, int position) {
        Calculo calculo = calculosList.get(position);
        holder.textoCalculo.setText(calculo.getOperacionCompleta()); // Usar el nuevo método
    }

    @Override
    public int getItemCount() {
        return calculosList.size();
    }

    public static class CalculoViewHolder extends RecyclerView.ViewHolder {
        TextView textoCalculo;

        public CalculoViewHolder(@NonNull View itemView) {
            super(itemView);
            textoCalculo = itemView.findViewById(R.id.textoCalculo);
        }
    }
}
