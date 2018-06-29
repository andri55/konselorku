package com.konselorku.android.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.konselorku.android.Adapter.HomeAdapter;
import com.konselorku.android.Adapter.SliderAdapter;
import com.konselorku.android.Helper.NetworkCache;
import com.konselorku.android.Helper.PublicController;
import com.konselorku.android.Model.HomeModel;
import com.konselorku.android.R;

import java.util.Timer;
import java.util.TimerTask;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipe_refresh;
    private LinearLayout lost_internet;

    private HomeAdapter adapter;
    private LinearLayoutManager layoutManager;
    private HomeModel.PostJson postJson;

    ViewPager viewPager;
    TabLayout indicator;

    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 500; // delay in milliseconds before task is to be executed
    final long PERIOD_MS = 5000; // time in milliseconds between successive task executions.
    final long NUM_PAGES = 4; // number of slide.


    public HomeFragment() {
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        viewPager = (ViewPager) v.findViewById(R.id.viewPager);
        indicator = (TabLayout) v.findViewById(R.id.indicator);

        viewPager.setAdapter(new SliderAdapter(getContext()));
        indicator.setupWithViewPager(viewPager, true);
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
            }
        };

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);

        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view_home);
        recyclerView.setNestedScrollingEnabled(false);
        swipe_refresh = (SwipeRefreshLayout) v.findViewById(R.id.swipe_home);
        lost_internet = (LinearLayout) v.findViewById(R.id.lost_home);
        lost_internet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetJSON(PublicController.URL_MAIN);
            }
        });
        lost_internet.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                GetJSON(PublicController.URL_MAIN);
            }
        });

        GetJSON(PublicController.URL_MAIN);
        return v;
    }

    public void GetJSON(String url) {
        swipe_refresh.setRefreshing(true);
        try {
            RequestQueue queue = Volley.newRequestQueue(getActivity());
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    GsonBuilder builder = new GsonBuilder();
                    Gson mGson = builder.create();
                    postJson = mGson.fromJson(response, HomeModel.PostJson.class);
                    adapter = new HomeAdapter(getActivity(), postJson.posts);
                    if (adapter.getItemCount() > 0) {
                        lost_internet.setVisibility(View.INVISIBLE);
                        recyclerView.setVisibility(View.VISIBLE);
                        recyclerView.setAdapter(adapter);
                    }
                    swipe_refresh.setRefreshing(false);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (adapter == null) {
                        lost_internet.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.INVISIBLE);
                    }
                    swipe_refresh.setRefreshing(false);
                }
            }) {
                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String jsonString = new String(response.data);
                    return Response.success(jsonString, NetworkCache.parseIgnoreCacheHeaders(response));
                }
            };
            queue.add(stringRequest);
        } finally {

        }
    }
}
