package com.konselorku.android.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.konselorku.android.R;

import java.util.List;

public class SliderAdapter extends PagerAdapter {

    private Context context;
    private Integer[] image = {R.drawable.img_slider1, R.drawable.img_slider2, R.drawable.img_slider3, R.drawable.img_slider4};
    private String[] imageName = {"Tahukah kau, menjadi juara itu indah.\n― Chozin", "Memiliki satu sahabat sejati lebih berharga dari seribu teman yg mementingkan diri sendiri.", "Kita mungkin bisa menunda, tapi waktu tidak akan menunggu.", "Orang boleh pandai setinggi langit, tapi selama ia tidak menulis, ia akan hilang di dalam masyarakat dan dari sejarah.\n― Pramoedya Ananta Toer"};

    public SliderAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return image.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_slider, null);

        TextView txtSlider = (TextView) view.findViewById(R.id.txt_slider);
        ImageView imgSlider = (ImageView) view.findViewById(R.id.img_slider);

        txtSlider.setText(imageName[position]);
        imgSlider.setImageResource(image[position]);

        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view, 0);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);
    }
}