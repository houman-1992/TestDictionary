package com.houman.longman;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.houman.longman.Adapter.StackTestAdapter;
import com.houman.longman.Database.DatabaseHandler;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import link.fls.swipestack.SwipeStack;

public class PracticeActivity extends AppCompatActivity
    {
    private FloatingActionButton mButtonLeft, mButtonRight;
    private List<String> VocabList = new ArrayList<>();
    private List<String> VocabListUnKnown = new ArrayList<>();
    private SwipeStack mSwipeStack;
    private StackTestAdapter mAdapter;
    private String tempValue = "";
    private DatabaseHandler dbHandler;
    private boolean TtsPermission;
    private boolean SlideShow;
    private int SlideDelay;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState)
        {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_stack);

        Bundle extras = getIntent().getExtras();
        if (extras != null)
            tempValue = extras.getString("Flash");

        LoadSettings();
        FindViews();
        stackSetup();
        listeners();
        }

    private void LoadSettings()
        {
        TtsPermission = getSharedPreferences("appInfo", MODE_PRIVATE).getBoolean("pTtsPermission"
                , true);
        SlideShow = getSharedPreferences("appInfo", MODE_PRIVATE).getBoolean("pSlideShow", false);
        SlideDelay = getSharedPreferences("appInfo", MODE_PRIVATE).getInt("pSlideDelay", 2500);
        }

    private void FindViews()
        {
        mSwipeStack = findViewById(R.id.swipeStack);
        mButtonLeft = findViewById(R.id.buttonSwipeLeft);
        mButtonRight = findViewById(R.id.buttonSwipeRight);
        Toolbar toolbar = findViewById(R.id.pToolbar);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        }

    private void stackSetup()
        {
        VocabList = new ArrayList<>();
        mAdapter = new StackTestAdapter(VocabList, this);
        mSwipeStack.setAdapter(mAdapter);
        dbHandler = new DatabaseHandler(PracticeActivity.this);
        VocabList.addAll(dbHandler.getFlashCardItems(tempValue));
        mAdapter.notifyDataSetChanged();
        }

    private void listeners()
        {
        mSwipeStack.setListener(new SwipeStack.SwipeStackListener()
            {
            @Override
            public void onViewSwipedToLeft(int position)
                {
                if (position + 1 < VocabList.size() && TtsPermission)
                    Pronounce(VocabList.get(position + 1));
                VocabListUnKnown.add(VocabList.get(position));
                }

            @Override
            public void onViewSwipedToRight(int position)
                {
                if (position + 1 < VocabList.size() && TtsPermission)
                    Pronounce(VocabList.get(position + 1));
                }

            @Override
            public void onStackEmpty()
                {
                if (VocabListUnKnown.size() > 0)
                    {
                    Toast.makeText(PracticeActivity.this, "Practice these one more time!",
                            Toast.LENGTH_SHORT).show();
                    VocabList.clear();
                    VocabList.addAll(VocabListUnKnown);
                    VocabListUnKnown.clear();
                    StackRefresh();
                    }
                else
                    Toast.makeText(PracticeActivity.this, "Well Done!\n No more card's left",
                            Toast.LENGTH_SHORT).show();
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
                Pronounce(VocabList.get(position));
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

    private void Show_delay()
        {
        LayoutInflater inflater = LayoutInflater.from(this);
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.dialog_delay, null);

        final EditText editText_delay = view.findViewById(R.id.editText_delay);
        editText_delay.setHint(SlideDelay + "");

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setTitle("Delay Time");
        alertDialogBuilder.setMessage("Please Insert Delay Time (ms)");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton(R.string.OK, null);
        alertDialogBuilder.setNegativeButton(R.string.CANCEL, null);

        final AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener()
            {
            @Override
            public void onShow(DialogInterface dialog)
                {
                //todo modify
                Button btnPositive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorAccent));
                btnPositive.setTextColor(getResources().getColor(R.color.theme_primary));

                btnPositive.setOnClickListener(new View.OnClickListener()
                    {
                    @Override
                    public void onClick(View v)
                        {
                        String Time = editText_delay.getText().toString();
                        if (Time.compareTo("") > 0)
                            {
                            SlideDelay = Integer.parseInt(Time);
                            getSharedPreferences("appInfo", MODE_PRIVATE).edit().putInt(
                                    "pSlideDelay", SlideDelay).apply();
                            Toast.makeText(PracticeActivity.this, Time + " ms delay applied!",
                                    Toast.LENGTH_SHORT).show();
                            }

                        alertDialog.dismiss();
                        }
                    });
                }
            });

        alertDialog.show();
        }

    private void Vocab_SlideShow(Boolean Show)
        {
        if (Show)
            mHandler.postDelayed(SlideShowRunnable, SlideDelay);
        else
            mHandler.removeCallbacks(SlideShowRunnable);
        }

    private void nextVocab()
        {
        mSwipeStack.swipeTopViewToLeft();
        }

    private void ReverseItems()
        {
        List<String> buff = new ArrayList<>();
        for (int i = (VocabList.size() - 1); i >=0; i--)
            buff.add(VocabList.get(i));
        VocabList.clear();
        VocabList.addAll(buff);
        StackRefresh();
        }

    private void StackRefresh()
        {
        mSwipeStack.resetStack();
        }

    public void Pronounce(String Text)
        {
        String FilePath = Environment.getExternalStorageDirectory().toString() + "/English/cash/" +
                tempValue + "/" + Text + ".mp3";

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
        dbHandler.DeleteFlashItem(tempValue, VocabList.get(position));
        VocabList.remove(position);
        StackRefresh();
        }

    private void find_Word(String fWord)
        {
        fWord = fWord.replaceAll("[^a-zA-Z]", "").toLowerCase();
        fWord = dbHandler.getWordRoot(fWord);
        Intent myIntent = new Intent(PracticeActivity.this, ShowActivity.class);
        myIntent.putExtra("HWD", fWord);
        startActivity(myIntent);
        }

@Override
public boolean onCreateOptionsMenu(Menu menu)
    {
    getMenuInflater().inflate(R.menu.practice_menu, menu);
    menu.getItem(0).setChecked(TtsPermission);
    menu.getItem(1).setChecked(SlideShow);
    return true;
    }

@Override
public boolean onOptionsItemSelected(MenuItem item)
    {
    int id = item.getItemId();
    switch (id)
        {
        case R.id.Pru:
            if (item.isCheckable())
                {
                TtsPermission = ! item.isChecked();
                item.setChecked(TtsPermission);
                getSharedPreferences("appInfo", MODE_PRIVATE).edit().putBoolean(
                        "pTtsPermission", TtsPermission).apply();
                }
            break;
        case R.id.Slide:
            if (item.isCheckable())
                {
                SlideShow = ! item.isChecked();
                item.setChecked(SlideShow);
                getSharedPreferences("appInfo", MODE_PRIVATE).edit().putBoolean("pSlideShow",
                        SlideShow).apply();
                Vocab_SlideShow(SlideShow);
                }
            break;
        case R.id.Delay:
            Show_delay();
            break;
        case R.id.Refresh:
            StackRefresh();
            break;
        case R.id.Reverse:
            ReverseItems();
            break;
        case R.id.Add:
            @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            String Date = df.format(Calendar.getInstance().getTime());
            dbHandler.AddMemorizeBox(tempValue, Date);
            break;
        }
    return super.onOptionsItemSelected(item);
    }

    private Runnable SlideShowRunnable = new Runnable()
        {
        @Override
        public void run()
            {
            nextVocab();
            mHandler.postDelayed(this, SlideDelay);
            }
        };
}
