package com.example.dpmestacionamientos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AlquileresAdapter extends
        RecyclerView.Adapter<AlquileresAdapter.MyViewHolder>{

    private List<Alquiler> alquileresList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView estacionamiento, fechahora;

        public MyViewHolder(View view) {
            super(view);
            estacionamiento = (TextView) view.findViewById(R.id.estacionamiento);
            fechahora = (TextView) view.findViewById(R.id.fechahora);
        }
    }


    public AlquileresAdapter(List<Alquiler> alquileresList) {
        this.alquileresList = alquileresList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.alquiler_fila, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Alquiler alquiler = alquileresList.get(position);
        holder.estacionamiento.setText(alquiler.getEstacionamiento());
        holder.fechahora.setText(alquiler.getFechainiciostring());
    }

    @Override
    public int getItemCount() {
        return alquileresList.size();
    }

}
