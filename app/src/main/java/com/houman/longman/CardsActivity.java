package com.houman.longman;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.houman.longman.Adapter.StackTestAdapter;
import com.houman.longman.Database.DatabaseHandler;
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
import java.util.Random;

import link.fls.swipestack.SwipeStack;

public class CardsActivity extends AppCompatActivity
    {
    private FloatingActionButton mButtonLeft, mButtonRight;
    private List<String> VocabList = new ArrayList<>();
    private List<mMemorize> LocalData = new ArrayList<>();
    private List<mMemorize> Result = new ArrayList<>();
    private SwipeStack mSwipeStack;
    private StackTestAdapter mAdapter;
    private DatabaseHandler dbHandler;
    private boolean TtsPermission;
    private boolean SlideShow;
    private int SlideDelay;
    private String DateNow = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
        {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);

        LoadSettings();
        FindViews();
        stackSetup();
        SetData();
        listeners();
        }

    private void listeners()
        {
        // todo set step state
        mSwipeStack.setListener(new SwipeStack.SwipeStackListener()
            {
            @Override
            public void onViewSwipedToLeft(int position)
                {
                if (position + 1 < VocabList.size() && TtsPermission)
                    Pronounce(VocabList.get(position + 1), LocalData.get(position).getCate());
                dbHandler.UpdateMemorizeBox(new mMemorize(VocabList.get(position),DateNow,1,""));
                //todo update memorizeBox with Result
                }

            @Override
            public void onViewSwipedToRight(int position)
                {
                if (position + 1 < VocabList.size() && TtsPermission)
                    Pronounce(VocabList.get(position + 1), LocalData.get(position).getCate());
                dbHandler.UpdateMemorizeBox(new mMemorize(LocalData.get(position).getWord(),DateNow,LocalData.get(position).getStep() + 1,""));
                //todo update memorizeBox with Result
                }

            @Override
            public void onStackEmpty()
                {
                Toast.makeText(CardsActivity.this, "Well Done!\n No more card's left", Toast.LENGTH_SHORT).show();
                }
            });

        mAdapter.setOnItemClickListener(new StackTestAdapter.onItemClickListener()
            {
            @Override
            public void onItemClick(int position)
                {
                String S = VocabList.get(position);
                find_Word(S);
                }

            @Override
            public void onPronounce(int position)
                {
                Pronounce(VocabList.get(position), LocalData.get(position).getCate());
                }

            @Override
            public void onGoogle(int position)
                {
                String S = VocabList.get(position);
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, S + " meaning");
                startActivity(intent);
                }

            @Override
            public void onDelete(int position)
                {
                deleteItem(position);
                }
            });

        mButtonLeft.setOnClickListener(new View.OnClickListener()
            {
            @Override
            public void onClick(View v)
                {
                mSwipeStack.onViewSwipedToLeft();
                }
            });
        mButtonRight.setOnClickListener(new View.OnClickListener()
            {
            @Override
            public void onClick(View v)
                {
                mSwipeStack.swipeTopViewToRight();
                }
            });
        }

    private void find_Word(String fWord)
        {
        fWord = fWord.replaceAll("[^a-zA-Z]", "").toLowerCase();
        fWord = dbHandler.getWordRoot(fWord);
        Intent myIntent = new Intent(CardsActivity.this, ShowActivity.class);
        myIntent.putExtra("HWD", fWord);
        startActivity(myIntent);
        }

    public void OnlinePronounce(String Text, String Cate)
        {
        Text = Text.toLowerCase();
        Text = dbHandler.getWordRoot(Text);
        Text = dbHandler.getUSFileName(Text);
        Text =getResources().getString(R.string.amePronLink) + Text + "?version=" + getResources().getString(R.string.LongManV);
        new OnlinePronounce(this).execute(Text);
        }

    public void Pronounce(String Text, String Cate)
        {
        String FilePath = Environment.getExternalStorageDirectory().toString() + "/English/cash/" +
                Cate + "/" + Text + ".mp3";

        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try
            {
            mediaPlayer.setDataSource(FilePath);
            mediaPlayer.prepare();
            }
        catch (IOException e)
            {
            e.printStackTrace();
            return;
            }
        mediaPlayer.start();
        }

    private void deleteItem(int position)
        {
        // todo delete Item from memorizeBox
        }

    private void SetData()
        {
        dbHandler = new DatabaseHandler(CardsActivity.this);
        List<mMemorize> mMemorizes = new ArrayList<>(dbHandler.getVocabMemoriseBox());
        VocabList.clear();
        LocalData.clear();
        for (mMemorize m : mMemorizes)
            {
            int DiffDays = getDifference(m.getDate());
            int Step = m.getStep() - 1;
            int minDays = (int) Math.pow(2,Step);
            if (DiffDays >= minDays)
                {
                VocabList.add(m.getWord());
                LocalData.add(new mMemorize(m.getWord(),"",m.getStep(),""));
                Toast.makeText(this, "djsfkdsjfb", Toast.LENGTH_SHORT).show();
                }
            }
        //todo use RandOrder method
        mAdapter.notifyDataSetChanged();
        }

    private List<String> RandOrder(List<String> ListIn)
        {
        List<String> ListOut = new ArrayList<>();
        while (ListIn.size() > 0)
            {
            int size = ListIn.size();
            if (size > 1)
                {
                int rnd = new Random().nextInt(ListIn.size());
                ListOut.add(ListIn.get(rnd));
                ListIn.remove(rnd);
                }
            else
                {
                ListOut.add(ListIn.get(0));
                ListIn.clear();
                }
            }
        return ListOut;
        }

    private void LoadSettings()
        {
        TtsPermission = getSharedPreferences("appInfo", MODE_PRIVATE).getBoolean("pTtsPermission", true);
        SlideShow = getSharedPreferences("appInfo", MODE_PRIVATE).getBoolean("pSlideShow", false);
        SlideDelay = getSharedPreferences("appInfo", MODE_PRIVATE).getInt("pSlideDelay", 2500);
        }

    private void FindViews()
        {
        mSwipeStack = findViewById(R.id.CardsStack);
        mButtonLeft = findViewById(R.id.CardsSwipeLeft);
        mButtonRight = findViewById(R.id.CardsSwipeRight);
        Toolbar toolbar = findViewById(R.id.CardsToolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        }

    private void stackSetup()
        {
        VocabList = new ArrayList<>();
        mAdapter = new StackTestAdapter(VocabList, this);
        mSwipeStack.setAdapter(mAdapter);
        }

    private int getDifference(String date1)
        {
        @SuppressLint("SimpleDateFormat")
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        DateNow = df.format(Calendar.getInstance().getTime());// Time Now

        Date dateObj1 = null;
        Date dateObj2 = null;
        try
            {

            dateObj1 = df.parse(date1);
            dateObj2 = df.parse(DateNow);
            }
        catch (ParseException e)
            {
            e.printStackTrace();
            }
        assert dateObj2 != null;
        long diff = dateObj2.getTime() - dateObj1.getTime();
        return (int) (diff / (86400000)); // diff days
        }
    }
