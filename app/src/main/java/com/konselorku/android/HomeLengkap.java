package com.konselorku.android;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.support.annotation.Dimension;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.konselorku.android.Adapter.KomentarAdapter;
import com.konselorku.android.Helper.PublicController;
import com.konselorku.android.Model.HomeModel;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HomeLengkap extends AppCompatActivity implements View.OnClickListener {


    private TextView txt_title, txt_date, txt_author;
    private WebView wv_content;
    private ImageView img_title;
    private Button btn_komentar;
    private RecyclerView lst_komentar;
    private LinearLayoutManager layoutManager;
    private KomentarAdapter adapter;
    private HomeModel.KomentarPost komentarPost;
    private AlertDialog.Builder dialog;
    private View dialogView;
    private LayoutInflater inflater;
    private PublicController komentar;
    private FloatingActionButton fab_share;
    private String str_position, str_id, str_url, str_thumbnail, str_title, str_author, str_date, str_category, str_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_lengkap);

        img_title = findViewById(R.id.img_title_home_lengkap);
        txt_title = findViewById(R.id.txt_title_home_lengkap);
        txt_author = findViewById(R.id.txt_author_home_lengkap);
        txt_date = findViewById(R.id.txt_date_home_lengkap);
        wv_content = findViewById(R.id.wv_content_home_lengkap);
        lst_komentar = findViewById(R.id.lst_komentar_home_lengkap);
        btn_komentar = findViewById(R.id.btn_komentar_home_lengkap);
        btn_komentar.setOnClickListener(this);
        fab_share = findViewById(R.id.fab_home_lengkap);
        fab_share.setOnClickListener(this);

        str_position = this.getIntent().getStringExtra("position");
        str_id = this.getIntent().getStringExtra("id");
        str_url = this.getIntent().getStringExtra("url");
        str_thumbnail = this.getIntent().getStringExtra("thumbnail");
        str_title = this.getIntent().getStringExtra("title");
        str_author = this.getIntent().getStringExtra("author");
        str_date = this.getIntent().getStringExtra("date");
        str_category = this.getIntent().getStringExtra("category");
        str_content = this.getIntent().getStringExtra("content");

        Picasso.with(this).load(str_thumbnail).placeholder(R.color.gray50).into(img_title);
        layoutManager = new LinearLayoutManager(this);
        lst_komentar.setLayoutManager(layoutManager);

        txt_title.setText(str_title);
        txt_author.setText(str_author + ".");
        txt_date.setText(parseDate(str_date));
        wv_content.setScrollContainer(false);
        wv_content.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        wv_content.loadData(getHtmlData("<div>" + str_content + "</div>"), "text/html; charset=utf-8", "utf-8");
        wv_content.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        GetJSON(PublicController.URL_GET_POST + str_id);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void GetJSON(String url) {
        try {
            RequestQueue queue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                ;

                @Override
                public void onResponse(String response) {
                    JsonParser parser = new JsonParser();
                    JsonObject rootObejct = parser.parse(response).getAsJsonObject();
                    JsonElement projectElement = rootObejct.get("post");
                    GsonBuilder builder = new GsonBuilder();
                    Gson mGson = builder.create();

                    komentarPost = mGson.fromJson(projectElement, HomeModel.KomentarPost.class);
                    adapter = new KomentarAdapter(getApplicationContext(), komentarPost.comments);
                    lst_komentar.setAdapter(adapter);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(),
                            "Check your Internet Connection.",
                            Toast.LENGTH_SHORT).show();
                }
            });
            queue.add(stringRequest);
        } finally {

        }
    }

    private String getHtmlData(String bodyHTML) {
        String head = "<head>" +
                "<style type=\"text/css\">\n" +
                "img{" +
                "max-width: 100%; " +
                "width:auto; height: " +
                "auto;}" +
                "body{" +
                "    font-size: medium;\n" +
                "    text-align: justify;" +
                "line-height: 150%;" +
                "overflow: auto;" +
                "overflow-y: hidden;}" +
                "</style>" +
                "</head>";
        return "<html>" + head + "<body text=\"#616161\" >" + bodyHTML + "</body></html>";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_komentar_home_lengkap:
                DialogForm();
                break;
            case R.id.fab_home_lengkap:
                PublicController.SharedContent(str_title, str_url, getApplicationContext());
                break;
        }
    }

    private void DialogForm() {
        final TextView txtPesan;
        dialog = new AlertDialog.Builder(this);
        inflater = this.getLayoutInflater();
        dialogView = inflater.inflate(R.layout.dialog_komentar, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setIcon(R.drawable.ic_profil);
        dialog.setTitle("Comment");

        txtPesan = (EditText) dialogView.findViewById(R.id.txtPesan_home_lengkap);
        txtPesan.setText(null);

        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String BASE_URL = "http://abbskp.sch.id/?json=submit_comment&post_id=";
                String POST_ID = str_id;
                String NAME = "Guest";
                String EMAIL = "guest@gmail.com";
                String CONTENT = txtPesan.getText().toString();
                String URL = BASE_URL + POST_ID + "&name=" + NAME + "&email=" + EMAIL + "&content=" + CONTENT;
                komentar = new PublicController();
                komentar.URLAccess(URL, getApplication());
                dialog.dismiss();
            }
        });

        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public String parseDate(String time) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "dd MMM yyyy h:mm a";
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

    public void back(View v) {
        onBackPressed();
    }
}
