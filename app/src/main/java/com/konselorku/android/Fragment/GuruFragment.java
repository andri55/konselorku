package com.konselorku.android.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.konselorku.android.Adapter.GuruAdapter;
import com.konselorku.android.Model.GuruModel;
import com.konselorku.android.MyApplication;
import com.konselorku.android.R;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

public class GuruFragment extends Fragment implements GuruAdapter.GuruAdapterListener {

    private static final String TAG = GuruFragment.class.getSimpleName();
    private RecyclerView recyclerView;
    private List<GuruModel> guruList;
    private GuruAdapter mAdapter;
    private LinearLayout refresh;
    private ImageView imgGuru, closeGuru, phoneGuru, mailGuru, shareGuru;
    private TextView namaGuru, sekolahGuru;

    private static final String URL = "https://api.androidhive.info/json/contacts.json";

    public GuruFragment() {
    }

    public static GuruFragment newInstance(String param1, String param2) {
        GuruFragment fragment = new GuruFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_guru, container, false);

        refresh = (LinearLayout) v.findViewById(R.id.lost_guru);
        refresh.setVisibility(View.INVISIBLE);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchGuru();
            }
        });

        recyclerView = v.findViewById(R.id.recycler_view_guru);

        guruList = new ArrayList<>();
        mAdapter = new GuruAdapter(getActivity(), guruList, this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(mAdapter);
        alphaAdapter.setFirstOnly(false);
        alphaAdapter.setDuration(500);
        ScaleInAnimationAdapter scaleAdapter = new ScaleInAnimationAdapter(alphaAdapter);
        scaleAdapter.setFirstOnly(false);
        recyclerView.setAdapter(new ScaleInAnimationAdapter(scaleAdapter));

        fetchGuru();
        return v;
    }

    private void fetchGuru() {
        JsonArrayRequest request = new JsonArrayRequest(URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response == null) {
                            refresh.setVisibility(View.VISIBLE);
                            return;
                        }

                        List<GuruModel> items = new Gson().fromJson(response.toString(), new TypeToken<List<GuruModel>>() {
                        }.getType());
                        refresh.setVisibility(View.INVISIBLE);

                        guruList.clear();
                        guruList.addAll(items);
                        mAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                refresh.setVisibility(View.VISIBLE);
            }
        });

        MyApplication.getInstance().addToRequestQueue(request);
    }

    @Override
    public void onGuruSelected(GuruModel guru) {
        final SweetAlertDialog dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.NORMAL_TYPE)
                .hideConfirmButton();
        final View v = getLayoutInflater().inflate(R.layout.dialog_guru,null);
        dialog.setCustomView(v);
        imgGuru = v.findViewById(R.id.img_dialog_guru);
        namaGuru = v.findViewById(R.id.nama_dialog_guru);
        sekolahGuru = v.findViewById(R.id.sekolah_dialog_guru);
        closeGuru = v.findViewById(R.id.img_cancel_guru);
        phoneGuru = v.findViewById(R.id.phone_dialog_guru);
        mailGuru = v.findViewById(R.id.mail_dialog_guru);
        shareGuru = v.findViewById(R.id.share_dialog_guru);
        Glide.with(getContext())
                .load(guru.getImage())
                .apply(RequestOptions.circleCropTransform())
                .into(imgGuru);
        namaGuru.setText(guru.getName());
        sekolahGuru.setText(guru.getSekolah());
        closeGuru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        phoneGuru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts(
                        "tel", sekolahGuru.getText().toString(), null)); // ganti phone guru
                startActivity(phoneIntent);
            }
        });
        mailGuru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mailIntent = new Intent(Intent.ACTION_SEND);
                mailIntent.setType("plain/text");
                mailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"info@konselorku.com"}); // ganti email guru
                mailIntent.putExtra(Intent.EXTRA_SUBJECT, "subject");
                mailIntent.putExtra(Intent.EXTRA_TEXT, "mail body");
                Intent new_intent = Intent.createChooser(mailIntent, "Send mail");
                new_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(new_intent);
            }
        });
        shareGuru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, namaGuru.getText().toString()); // ganti nama guru
                shareIntent.putExtra(Intent.EXTRA_TEXT, namaGuru.getText().toString() + ": " + sekolahGuru.getText().toString()); // ganti phone guru
                Intent new_intent = Intent.createChooser(shareIntent, "Share contact");
                new_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(new_intent);
            }
        });
        dialog.show();
    }
}
