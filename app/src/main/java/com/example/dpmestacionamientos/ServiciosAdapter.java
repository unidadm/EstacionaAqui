package com.example.dpmestacionamientos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ServiciosAdapter extends
        RecyclerView.Adapter<ServiciosAdapter.MyViewHolder>{

    private List<Servicio> serviciosList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tipo, descripcion;

        public MyViewHolder(View view) {
            super(view);
            tipo = (TextView) view.findViewById(R.id.tipo);
            descripcion = (TextView) view.findViewById(R.id.descripcion);
        }
    }


    public ServiciosAdapter(List<Servicio> serviciosList) {
        this.serviciosList = serviciosList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.servicio_fila, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Servicio servicio = serviciosList.get(position);
        holder.tipo.setText(servicio.getTipo());
        holder.descripcion.setText(servicio.getDescripcion());
    }

    @Override
    public int getItemCount() {
        return serviciosList.size();
    }

}
