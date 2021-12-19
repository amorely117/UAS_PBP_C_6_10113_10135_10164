package com.aurelia_truly_jessica.transvice_test.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.aurelia_truly_jessica.transvice_test.EditAddActivity;
import com.aurelia_truly_jessica.transvice_test.Model.Service;
import com.aurelia_truly_jessica.transvice_test.R;
import com.aurelia_truly_jessica.transvice_test.ServiceActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ViewHolder> implements Filterable {
    private List<Service> serviceList, filteredServiceList;
    private Context context;

    public ServiceAdapter(List<Service> serviceList, Context context) {
        this.serviceList = serviceList;
        filteredServiceList = new ArrayList<>(serviceList);
        this.context = context;
    }

    @NonNull
    @Override
    public ServiceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.activity_service_adapter, parent, false);
        return new ServiceAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceAdapter.ViewHolder holder, int position) {
        Service service = serviceList.get(position);
        holder.tvJenisService.setText(service.getJenis_service());
        holder.tvLamaPengerjaan.setText(service.getLama_pengerjaan());
        holder.tvJenisKendaraan.setText(service.getJenis_kendaraan());

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(context);
                materialAlertDialogBuilder.setTitle("Konfirmasi")
                        .setMessage("Apakah anda yakin ingin menghapus data service ini?")
                        .setNegativeButton("Batal", null)
                        .setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (context instanceof ServiceActivity)
                                    ((ServiceActivity) context).deleteService(service.getId());
                            }
                        })
                        .show();
            }
        });

        holder.cvService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, EditAddActivity.class);
                i.putExtra("id", service.getId());
                if (context instanceof ServiceActivity)
                    ((ServiceActivity) context).startActivityForResult(i, ServiceActivity.LAUNCH_ADD_ACTIVITY);
            }
        });
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    public void setServiceList(List<Service> serviceList) {
        this.serviceList = serviceList;
        filteredServiceList = new ArrayList<>(serviceList);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charSequenceString = charSequence.toString();
                List<Service> filtered = new ArrayList<>();
                if (charSequenceString.isEmpty()) {
                    filtered.addAll(serviceList);
                } else {
                    for (Service service : serviceList) {
                        if (service.getJenis_service().toLowerCase()
                                .contains(charSequenceString.toLowerCase()))
                            filtered.add(service);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filtered;
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredServiceList.clear();
                filteredServiceList.addAll((List<Service>) filterResults.values);
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvJenisService, tvLamaPengerjaan, tvJenisKendaraan;
        ImageButton btnDelete;
        CardView cvService;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvJenisService = itemView.findViewById(R.id.jenis_Service);
            tvLamaPengerjaan = itemView.findViewById(R.id.lama_Pengerjaan);
            tvJenisKendaraan = itemView.findViewById(R.id.jenis_Kendaraan);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            cvService = itemView.findViewById(R.id.cvService);
        }
    }
}
