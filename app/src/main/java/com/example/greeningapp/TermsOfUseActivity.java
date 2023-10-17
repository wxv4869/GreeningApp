package com.example.greeningapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.tabs.TabLayout;

public class TermsOfUseActivity extends AppCompatActivity {

    TabLayout tab_termsofuse;
    ViewPager viewPager_termsofuse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_of_use);

        Toolbar toolbar = findViewById(R.id.toolbar_termsofuse);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tab_termsofuse = (TabLayout) findViewById(R.id.tab_termsofuse);
        viewPager_termsofuse = (ViewPager) findViewById(R.id.viewPager_termsofuse);

        ViewPagerTermsOfUseAdapter viewPagerTermsOfUseAdapter = new ViewPagerTermsOfUseAdapter(getSupportFragmentManager());
        viewPager_termsofuse.setAdapter(viewPagerTermsOfUseAdapter);

        tab_termsofuse.setupWithViewPager(viewPager_termsofuse);

        Intent intent = getIntent();
        String selectedTab = intent.getStringExtra("selectedTab");

        if ("left".equals(selectedTab)) {
            TabLayout tabLayout = findViewById(R.id.tab_termsofuse);
            tabLayout.getTabAt(0).select();
        } else if ("right".equals(selectedTab)) {
            TabLayout tabLayout = findViewById(R.id.tab_termsofuse);
            tabLayout.getTabAt(1).select();
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) { //뒤로가기
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}