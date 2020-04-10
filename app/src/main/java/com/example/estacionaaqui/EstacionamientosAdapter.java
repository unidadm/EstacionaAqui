package com.example.estacionaaqui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EstacionamientosAdapter extends
        RecyclerView.Adapter<EstacionamientosAdapter.MyViewHolder>
        implements View.OnClickListener{

    private List<Estacionamiento> estacionamientosList;
    private View.OnClickListener listener;

    public void setOnClickListener(View.OnClickListener listener){
        this.listener=listener;
    }

    @Override
    public void onClick(View view) {
        if (listener != null){
            listener.onClick(view);
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView nombre, distrito, precio;

        public MyViewHolder(View view) {
            super(view);
            nombre = (TextView) view.findViewById(R.id.nombre);
            distrito = (TextView) view.findViewById(R.id.distrito);
            precio = (TextView) view.findViewById(R.id.precio);
        }
    }


    public EstacionamientosAdapter(List<Estacionamiento> estacionamientosList) {
        this.estacionamientosList = estacionamientosList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.estacionamiento_fila, parent, false);

        itemView.setOnClickListener(this);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Estacionamiento estacionamiento = estacionamientosList.get(position);
        holder.nombre.setText(estacionamiento.getNombre());
        holder.distrito.setText(estacionamiento.getDistrito());
        holder.precio.setText(estacionamiento.getPreciohora().toString());
    }

    @Override
    public int getItemCount() {
        return estacionamientosList.size();
    }

}
