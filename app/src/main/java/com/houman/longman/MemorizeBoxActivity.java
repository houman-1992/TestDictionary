package com.houman.longman;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.houman.longman.Database.DatabaseHandler;
import com.houman.longman.Fragment.bFragment;
import com.houman.longman.Models.mMemorize;
import com.houman.longman.Pronounce.OnlinePronounce;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class MemorizeBoxActivity extends AppCompatActivity
    {

    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
        {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memorize_box);

        Toolbar toolbar = findViewById(R.id.toolbar);
        viewPager = findViewById(R.id.ViewPager);
        tabLayout = findViewById(R.id.TabLayout);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        DatabaseHandler dbHandler = new DatabaseHandler(MemorizeBoxActivity.this);
        List<mMemorize> mMemorizes = new ArrayList<>(dbHandler.getVocabMemoriseBox());

        @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String Date = df.format(Calendar.getInstance().getTime());

        Log.d("date_test",Date);

        setViewPager(mMemorizes);
        }

    private void setViewPager(List<mMemorize> mMemorizes)
        {
        List<String> DataSetAll = new ArrayList<>();
        List<String> DataSet1 = new ArrayList<>();
        List<String> DataSet2 = new ArrayList<>();
        List<String> DataSet3 = new ArrayList<>();
        List<String> DataSet4 = new ArrayList<>();
        List<String> DataSet5 = new ArrayList<>();
        List<String> DataSetDone = new ArrayList<>();

        for (mMemorize m : mMemorizes)
            {
            String s = m.getWord();
            DataSetAll.add(s);
            switch (m.getStep())
                {
                case 1:
                    DataSet1.add(s);
                    break;
                case 2:
                    DataSet2.add(s);
                    break;
                case 3:
                    DataSet3.add(s);
                    break;
                case 4:
                    DataSet4.add(s);
                    break;
                case 5:
                    DataSet5.add(s);
                    break;
                case 6:
                    DataSetDone.add(s);
                    break;
                }
            }

        Util.ViewPagerAdapter adapter = new Util.ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new bFragment(MemorizeBoxActivity.this, DataSetAll), "All");
        adapter.addFragment(new bFragment(MemorizeBoxActivity.this, DataSet1), "1");
        adapter.addFragment(new bFragment(MemorizeBoxActivity.this, DataSet2), "2");
        adapter.addFragment(new bFragment(MemorizeBoxActivity.this, DataSet3), "3");
        adapter.addFragment(new bFragment(MemorizeBoxActivity.this, DataSet4), "4");
        adapter.addFragment(new bFragment(MemorizeBoxActivity.this, DataSet5), "5");
        adapter.addFragment(new bFragment(MemorizeBoxActivity.this, DataSetDone), "Done");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        }

    public void ShowMean(String Word)
        {
        Intent myIntent = new Intent(MemorizeBoxActivity.this, ShowActivity.class);
        myIntent.putExtra("HWD", Word);
        startActivity(myIntent);
        }
    }