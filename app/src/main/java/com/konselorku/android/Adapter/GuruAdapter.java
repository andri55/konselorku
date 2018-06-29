package com.konselorku.android.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.konselorku.android.Model.GuruModel;
import com.konselorku.android.R;

import java.util.ArrayList;
import java.util.List;

public class GuruAdapter extends RecyclerView.Adapter<GuruAdapter.MyViewHolder>
        implements Filterable {
    private Context context;
    private List<GuruModel> guruList;
    private List<GuruModel> guruListFiltered;
    private GuruAdapterListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, sekolah;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.txt_nama_guru);
            sekolah = view.findViewById(R.id.txt_sekolah_guru);
            thumbnail = view.findViewById(R.id.img_foto_guru);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onGuruSelected(guruListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }


    public GuruAdapter(Context context, List<GuruModel> guruList, GuruAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.guruList = guruList;
        this.guruListFiltered = guruList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_row_guru, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final GuruModel contact = guruListFiltered.get(position);
        holder.name.setText(contact.getName());
        holder.sekolah.setText(contact.getSekolah());

        Glide.with(context)
                .load(contact.getImage())
                .apply(RequestOptions.circleCropTransform())
                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return guruListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    guruListFiltered = guruList;
                } else {
                    List<GuruModel> filteredList = new ArrayList<>();
                    for (GuruModel row : guruList) {
                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getSekolah().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    guruListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = guruListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                guruListFiltered = (ArrayList<GuruModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface GuruAdapterListener {
        void onGuruSelected(GuruModel contact);
    }
}