package com.kuraps.tiketsaya;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.TicketViewHolder> {

    DatabaseReference reference3;
    Context context;
    ArrayList<MyTicket> myTicket;

    public TicketAdapter(Context context, ArrayList<MyTicket> myTicket) {
        this.context = context;
        this.myTicket = myTicket;
    }

    @NonNull
    @Override
    public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_myticket, parent, false);
        return new TicketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketViewHolder holder, int position) {

        final String nama_wisata = myTicket.get(position).getWisata_name();
        reference3 = FirebaseDatabase.getInstance().getReference("Wisata").child(nama_wisata);
        reference3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Picasso.with(holder.itemView.getContext())
                        .load(snapshot.child("wisata_icon").getValue().toString())
                        .centerCrop()
                        .fit()
                        .placeholder(R.drawable.icon_nopic).error(R.drawable.icon_nopic)
                        .into(holder.xicon);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.xnama_wisata.setText(myTicket.get(position).getWisata_name());
        holder.xlokasi.setText(myTicket.get(position).getWisata_location());
        String tiket = context.getResources().getString(R.string.ticket);
        String tikets = context.getResources().getString(R.string.tickets);
        if(myTicket.get(position).getTicket_total().equals("1")){
            // TIKET = tiket = 1
            holder.xjumlah_tiket.setText(myTicket.get(position).getTicket_total()+" "+tiket);
        }else{
            // TIKETS = tiket lebih dari 1
            holder.xjumlah_tiket.setText(myTicket.get(position).getTicket_total()+" "+tikets);
        }
        final String ticket_total = myTicket.get(position).getTicket_total();
        final String id_ticket = myTicket.get(position).getId_ticket();
        final String date_buy = myTicket.get(position).getDate_buy();
        final String time_buy = myTicket.get(position).getTime_buy();
        final String date_wisata = myTicket.get(position).getDate_wisata();
        final String expired_time = myTicket.get(position).getExpired_time();
        final String pesawat = myTicket.get(position).getPesawat();
        final String lokasi = myTicket.get(position).getWisata_location();
        final String total_harga = myTicket.get(position).getTotal_harga();
        holder.xtotal_harga.setText("US $"+total_harga);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotomyticketdetails = new Intent(context, MyTicketDetailAct.class);
                gotomyticketdetails.putExtra("id_ticket", id_ticket);
                gotomyticketdetails.putExtra("wisata_name", nama_wisata);
                gotomyticketdetails.putExtra("wisata_location", lokasi);
                gotomyticketdetails.putExtra("ticket_total", ticket_total);
                gotomyticketdetails.putExtra("date_buy", date_buy);
                gotomyticketdetails.putExtra("time_buy", time_buy);
                gotomyticketdetails.putExtra("date_wisata", date_wisata);
                gotomyticketdetails.putExtra("expired_time", expired_time);
                gotomyticketdetails.putExtra("pesawat", pesawat);
                gotomyticketdetails.putExtra("total_harga", total_harga);
                context.startActivity(gotomyticketdetails);
            }
        });

    }

    @Override
    public int getItemCount() {
        return myTicket.size();
    }

    public static class TicketViewHolder extends RecyclerView.ViewHolder {
        TextView xnama_wisata, xlokasi, xjumlah_tiket,xtotal_harga;
        ImageView xicon;

        public TicketViewHolder(@NonNull View itemView) {
            super(itemView);

            xicon = itemView.findViewById(R.id.xicon);
            xnama_wisata = itemView.findViewById(R.id.xnama_wisata);
            xlokasi = itemView.findViewById(R.id.xlokasi);
            xjumlah_tiket = itemView.findViewById(R.id.xjumlah_tiket);
            xtotal_harga = itemView.findViewById(R.id.xtotal_harga);

        }
    }
}