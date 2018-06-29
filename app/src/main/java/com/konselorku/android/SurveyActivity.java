package com.konselorku.android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class SurveyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
    }

    public void betaTester(View v) {
        Toast.makeText(this, "BETA TESTER", Toast.LENGTH_LONG).show();
    }

    public void pelajar(View v) {
        Toast.makeText(this, "PELAJAR", Toast.LENGTH_LONG).show();
    }

    public void back(View v) {
        onBackPressed();
    }
}
