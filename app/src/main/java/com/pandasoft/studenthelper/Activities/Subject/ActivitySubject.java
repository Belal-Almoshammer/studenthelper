package com.pandasoft.studenthelper.Activities.Subject;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.pandasoft.studenthelper.R;
import com.pandasoft.studenthelper.Tools.MyToolsCls;

import java.util.ArrayList;
import java.util.List;

public class ActivitySubject extends AppCompatActivity {
    String id_sub;
    TabLayout tablayout;
    ViewPager viewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);
        initialize();
        setTabs();
    }

    private void initialize() {

        // set init views
        Toolbar toolbar = findViewById(R.id.toolbar);
        AppBarLayout appBar = findViewById(R.id.appbar);
        ImageView img = findViewById(R.id.subject_image);
        tablayout = findViewById(R.id.tab_layout);
        viewpager = findViewById(R.id.view_pager);
        // get data

        Bundle bundle = getIntent().getExtras();

        toolbar.setTitle(bundle.getString("subject_name"));
        appBar.setBackgroundColor(Color.parseColor(bundle.getString("color_code")));
        appBar.setStatusBarForegroundColor(Color.parseColor(bundle.getString("color_code")));
        getWindow().setStatusBarColor(Color.parseColor(bundle.getString("color_code")));

        this.id_sub = bundle.getString("id_sub");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        try {
            MyToolsCls.setImageSrc(this, img, bundle.getString("image"));
        } catch (Exception e) {
            Log.e("ActivitySubject", "onCreate: " + e.getMessage());
        }

    }


    private void setTabs() {
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);

        FragmentBooks fragmentBooks = new FragmentBooks(id_sub);
        FragmentVideos fragmentVideos = new FragmentVideos(id_sub);
        FragmentQuizes fragmentQuizes = new FragmentQuizes(id_sub);

        pagerAdapter.addFragment(fragmentBooks, "الكتب");
        pagerAdapter.addFragment(fragmentQuizes, "التدريبات");
        pagerAdapter.addFragment(fragmentVideos, "الفيديوهات");

        viewpager.setAdapter(pagerAdapter);
        tablayout.setupWithViewPager(viewpager);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finishAfterTransition();
        return super.onOptionsItemSelected(item);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentsTitles = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        public void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentsTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentsTitles.get(position);
        }
    }


}