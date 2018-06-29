package com.konselorku.android.Holder;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.konselorku.android.R;

public class HomeHolder extends RecyclerView.ViewHolder {

    public ImageView img_title;
    public TextView txt_title, txt_desc, txt_author, txt_date, txt_comment_count, txt_categories;
    public CardView card_item;

    public HomeHolder(View itemView) {
        super(itemView);
        img_title = (ImageView) itemView.findViewById(R.id.img_title_home);
        txt_title = (TextView) itemView.findViewById(R.id.txt_title_home);
        txt_desc = (TextView) itemView.findViewById(R.id.txt_desc_home);
        txt_author = (TextView) itemView.findViewById(R.id.txt_author_home);
        txt_date = (TextView) itemView.findViewById(R.id.txt_date_home);
        txt_comment_count = (TextView) itemView.findViewById(R.id.txt_comment_count_home);
        txt_categories = (TextView) itemView.findViewById(R.id.txt_categories_home);
        card_item = (CardView) itemView.findViewById(R.id.home_view);
    }
}
