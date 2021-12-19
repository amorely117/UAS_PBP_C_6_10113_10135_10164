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

import com.aurelia_truly_jessica.transvice_test.AddEditActivity;
import com.aurelia_truly_jessica.transvice_test.BookingActivity;
import com.aurelia_truly_jessica.transvice_test.Model.Booking;
import com.aurelia_truly_jessica.transvice_test.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.ViewHolder> implements Filterable {
    private List<Booking> bookingList, filteredBookingList;
    private Context context;

    public BookingAdapter(List<Booking> bookingList, Context context) {
        this.bookingList = bookingList;
        filteredBookingList = new ArrayList<>(bookingList);
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.activity_booking_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Booking booking = bookingList.get(position);
        holder.tvTglService.setText(booking.getTgl_service());
        holder.tvWaktuService.setText(booking.getWaktu_service());
        holder.tvJenisService.setText(booking.getJenis_service());
        holder.tvJenisKendaraan.setText(booking.getJenis_kendaraan());

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(context);
                materialAlertDialogBuilder.setTitle("Konfirmasi")
                        .setMessage("Apakah anda yakin ingin menghapus data booking ini?")
                        .setNegativeButton("Batal", null)
                        .setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (context instanceof BookingActivity)
                                    ((BookingActivity) context).deleteBooking(booking.getId());
                            }
                        })
                        .show();
            }
        });

        holder.cvBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, AddEditActivity.class);
                i.putExtra("id", booking.getId());
                if (context instanceof BookingActivity)
                    ((BookingActivity) context).startActivityForResult(i, BookingActivity.LAUNCH_ADD_ACTIVITY);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public void setBookingList(List<Booking> bookingList) {
        this.bookingList = bookingList;
        filteredBookingList = new ArrayList<>(bookingList);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charSequenceString = charSequence.toString();
                List<Booking> filtered = new ArrayList<>();
                if (charSequenceString.isEmpty()) {
                    filtered.addAll(bookingList);
                } else {
                    for (Booking booking : bookingList) {
                        if (booking.getJenis_service().toLowerCase()
                                .contains(charSequenceString.toLowerCase()))
                            filtered.add(booking);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filtered;
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredBookingList.clear();
                filteredBookingList.addAll((List<Booking>) filterResults.values);
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTglService, tvWaktuService, tvJenisKendaraan, tvJenisService;
        ImageButton btnDelete;
        CardView cvBooking;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTglService = itemView.findViewById(R.id.tglService);
            tvWaktuService = itemView.findViewById(R.id.waktuService);
            tvJenisKendaraan = itemView.findViewById(R.id.jenisKendaraan);
            tvJenisService = itemView.findViewById(R.id.jenisService);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            cvBooking = itemView.findViewById(R.id.cvBooking);
        }
    }
}
