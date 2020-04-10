package com.example.estacionaaqui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EstacionamientoServiciosAdapter extends
        RecyclerView.Adapter<EstacionamientoServiciosAdapter.MyViewHolder>
        implements View.OnClickListener{

    private List<EstacionamientoServicio> estacionamientoserviciosList;
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

        public TextView descripcion, tarifa;

        public MyViewHolder(View view) {
            super(view);
            descripcion = (TextView) view.findViewById(R.id.descripcion);
            tarifa = (TextView) view.findViewById(R.id.tarifa);
        }
    }

    public EstacionamientoServiciosAdapter(List<EstacionamientoServicio> estacionamientoserviciosList) {
        this.estacionamientoserviciosList = estacionamientoserviciosList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.estacionamientoservicio_fila, parent, false);

        itemView.setOnClickListener(this);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        EstacionamientoServicio estacionamientoservicio = estacionamientoserviciosList.get(position);
        holder.descripcion.setText(estacionamientoservicio.getDescripcion());
        holder.tarifa.setText(estacionamientoservicio.getTarifa().toString());
    }

    @Override
    public int getItemCount() {
        return estacionamientoserviciosList.size();
    }
}
