package com.konselorku.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.konselorku.android.Fragment.GuruFragment;
import com.konselorku.android.Fragment.HomeFragment;
import com.konselorku.android.Fragment.LaporFragment;
import com.konselorku.android.Fragment.PenilaianFragment;
import com.konselorku.android.Fragment.ProfilFragment;
import com.konselorku.android.Helper.BottomNavigationViewHelper;

public class MainActivity extends AppCompatActivity {

    final Fragment fragment1 = new HomeFragment();
    final Fragment fragment2 = new GuruFragment();
    final Fragment fragment3 = new LaporFragment();
    final Fragment fragment4 = new PenilaianFragment();
    final Fragment fragment5 = new ProfilFragment();
    Fragment active = fragment1;
    FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fm = getSupportFragmentManager();
        fm.beginTransaction().add(R.id.frame_container, fragment5, "5")
                .add(R.id.frame_container, fragment4, "4")
                .add(R.id.frame_container, fragment3, "3")
                .add(R.id.frame_container, fragment2, "2")
                .add(R.id.frame_container, fragment1, "1")
                .commit();

        fm.beginTransaction().hide(fragment2)
                .hide(fragment3)
                .hide(fragment4)
                .hide(fragment5)
                .commit();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        if (active != fragment1) {
                            fm.beginTransaction().show(fragment1).hide(active).commit();
                            active = fragment1;
                        } else {
                            NestedScrollView scrollView = (NestedScrollView) findViewById(R.id.scroll_view_home);
                            scrollView.smoothScrollTo(0, 0);
                            scrollView.scrollTo(0, 0);
                        }
                        break;
                    case R.id.navigation_guru:
                        if (active != fragment2) {
                            fm.beginTransaction().show(fragment2).hide(active).commit();
                            active = fragment2;
                        } else {
                            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view_guru);
                            recyclerView.smoothScrollToPosition(0);
                        }
                        break;
                    case R.id.navigation_lapor:
                        if (active != fragment3) {
                            fm.beginTransaction().show(fragment3).hide(active).commit();
                            active = fragment3;
                        } else {
                            NestedScrollView scrollView = (NestedScrollView) findViewById(R.id.scroll_view_lapor);
                            scrollView.smoothScrollTo(0, 0);
                            scrollView.scrollTo(0, 0);
                        }
                        break;
                    case R.id.navigation_penilaian:
                        if (active != fragment4) {
                            fm.beginTransaction().show(fragment4).hide(active).commit();
                            active = fragment4;
                        } else {
                            NestedScrollView scrollView = (NestedScrollView) findViewById(R.id.scroll_view_penilaian);
                            scrollView.smoothScrollTo(0, 0);
                            scrollView.scrollTo(0, 0);
                        }
                        break;
                    case R.id.navigation_profil:
                        if (active != fragment5) {
                            fm.beginTransaction().show(fragment5).hide(active).commit();
                            active = fragment5;
                        }
                        break;
                }
                return true;
            }
        });
    }

    public void SurveyActivity(View v) {
        startActivity(new Intent(this, SurveyActivity.class));
    }

    public void NotifikasiActivity(View v) {
        startActivity(new Intent(this, NotifikasiActivity.class));
    }
}
