package com.konselorku.android.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.konselorku.android.Holder.HomeHolder;
import com.konselorku.android.HomeLengkap;
import com.konselorku.android.Model.HomeModel;
import com.konselorku.android.R;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeHolder> {
    public List<HomeModel.PostJson.Hasil> itemList;
    public Context context;

    public HomeAdapter(Context context, List<HomeModel.PostJson.Hasil> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public HomeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_home, null);
        HomeHolder v = new HomeHolder(layoutView);
        return v;
    }

    @Override
    public void onBindViewHolder(HomeHolder holder, final int position) {
        Picasso.with(context).load(itemList.get(position).thumbnail_images.medium_large.urlThumbnail)
                .placeholder(R.color.gray50).into(holder.img_title);
        holder.txt_title.setText(Html.fromHtml(itemList.get(position).title));
        holder.txt_desc.setText(Html.fromHtml(itemList.get(position).content));
        holder.txt_author.setText(Html.fromHtml(itemList.get(position).author.name));
        String[] date = String.valueOf(Html.fromHtml(itemList.get(position).date)).split(" ");
        holder.txt_date.setText(parseDate(date[0]));
        holder.txt_categories.setText(itemList.get(position).categories.get(0).title);
        holder.txt_comment_count.setText(Html.fromHtml(itemList.get(position).comment_count) + " Comments");
        holder.card_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, HomeLengkap.class);
                i.putExtra("position", String.valueOf(position));
                i.putExtra("id", itemList.get(position).id);
                i.putExtra("url", itemList.get(position).url);
                i.putExtra("thumbnail", itemList.get(position).thumbnail_images.medium_large.urlThumbnail);
                i.putExtra("title", itemList.get(position).title);
                i.putExtra("author", itemList.get(position).author.name);
                i.putExtra("date", itemList.get(position).date);
                i.putExtra("category", itemList.get(position).categories.get(0).title);
                i.putExtra("content", itemList.get(position).content);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }

    public String parseDate(String time) {
        String inputPattern = "yyyy-MM-dd";
        String outputPattern = "dd MMM yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }
}
