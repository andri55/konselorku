package com.konselorku.android.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.konselorku.android.Holder.KategoriHolder;
import com.konselorku.android.Model.HomeModel;
import com.konselorku.android.R;

import java.util.List;

public class KomentarAdapter extends RecyclerView.Adapter<KategoriHolder> {
    public List<HomeModel.KomentarPost.Komentar> itemList;
    public Context context;

    public KomentarAdapter(Context context, List<HomeModel.KomentarPost.Komentar> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public KategoriHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.carditem_vertical, null);
        KategoriHolder v = new KategoriHolder(layoutView);
        return v;
    }

    @Override
    public void onBindViewHolder(KategoriHolder holder, int position) {
        holder.txt_judul.setText(itemList.get(position).name);
        holder.txt_deskripsi.setText(Html.fromHtml(itemList.get(position).content));
        // char test = itemList.get(position).name.charAt(0);
        // holder.img_item.setText(String.valueOf(test));
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }
}
