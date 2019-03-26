package com.houman.longman;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.houman.longman.Adapter.MainAdapter;
import com.houman.longman.Database.DatabaseHandler;
import com.houman.longman.Models.MainModel;
import com.houman.longman.Pronounce.OnlinePronounce;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
    {

    private ProgressDialog pD;
    private MaterialSearchView mSearchView;
    private MainAdapter mMainAdapter;
    private List<MainModel> VocabList = new ArrayList<>();
    private DatabaseHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState)
        {

//        Intent myIntent = new Intent(MainActivity.this, CardsActivity.class);
//        startActivity(myIntent);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Setup();
        Start();
        SetListeners();
        }

    private void Setup()
        {
        Toolbar mToolbar = findViewById(R.id.mToolbar);
//        mFab = findViewById(R.id.mFab);
        DrawerLayout mDrawer = findViewById(R.id.drawer_layout);
        NavigationView mNavigationView = findViewById(R.id.nav_view);
        mSearchView = findViewById(R.id.mSearchView);
        RecyclerView mRecyclerView = findViewById(R.id.mRecyclerView);

        mSearchView.setCursorDrawable(R.drawable.custom_cursor);
        mSearchView.setVoiceSearch(true);
        mSearchView.showVoice(true);

        setSupportActionBar(mToolbar);

        ActionBarDrawerToggle mToggle = new ActionBarDrawerToggle(
                this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(mToggle);
        mToggle.syncState();
        mNavigationView.setNavigationItemSelectedListener(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        mMainAdapter = new MainAdapter(VocabList);
        mRecyclerView.setAdapter(mMainAdapter);
        }

    private void Start()
        {
        dbHandler = new DatabaseHandler(MainActivity.this);
        boolean isFirstRun = getSharedPreferences("appInfo", MODE_PRIVATE).getBoolean("isFirstRun", true);
        if (isFirstRun)
            {
            dbHandler.getReadableDatabase();
            dbHandler.getWritableDatabase();
            new copyFile().execute();
            }
        else
            Search("");
        }

    private void SetListeners()
        {
        mSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener()
            {
            @Override
            public boolean onQueryTextSubmit(String query)
                {
                return false;
                }

            @Override
            public boolean onQueryTextChange(String newText)
                {
                Search(newText);
                return false;
                }
            });

        mMainAdapter.setOnItemClickListener(new MainAdapter.onItemClickListener()
            {
            @Override
            public void onItemClick(int position)
                {
                Intent myIntent = new Intent(MainActivity.this, ShowActivity.class);
                myIntent.putExtra("HWD", VocabList.get(position).getHWD());
                startActivity(myIntent);
                }

            @Override
            public void onPronClick(int position)
                {
                Pronounce(VocabList.get(position).getFile());
                }
            });

//        mFab.setOnClickListener(new View.OnClickListener()
//            {
//            @Override
//            public void onClick(View view)
//                {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//                }
//            });
        }

    private void Pronounce(String Text)
        {
        Text =getResources().getString(R.string.amePronLink) + Text + "?version=" + getResources().getString(R.string.LongManV);
        new OnlinePronounce(this).execute(Text);
        }

    @Override
    public void onBackPressed()
        {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
            {
            drawer.closeDrawer(GravityCompat.START);
            }
        else if (mSearchView.isSearchOpen())
            {
            mSearchView.closeSearch();
            }
        else
            {
            super.onBackPressed();
            }
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
        {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        mSearchView.setMenuItem(menu.findItem(R.id.action_search));
        return true;
        }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
        {
        int id = item.getItemId();
        if (id == R.id.action_settings)
            {
            return true;
            }

        return super.onOptionsItemSelected(item);
        }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
        {
        int id = item.getItemId();

        Intent myIntent = null;
        switch (id)
            {
            case R.id.nav_text_reading:
                myIntent = new Intent(MainActivity.this, TextActivity.class);
                break;
            case R.id.nav_review:
                myIntent = new Intent(MainActivity.this, CardsActivity.class);
                break;
            case R.id.nav_dictation:
                myIntent = new Intent(MainActivity.this, DictationActivity.class);
                break;
            case R.id.nav_favorite:
                myIntent = new Intent(MainActivity.this, FavoriteActivity.class);
                break;
            case R.id.nav_history:
                myIntent = new Intent(MainActivity.this, RecentActivity.class);
                break;
            case R.id.nav_flash_cards:
                myIntent = new Intent(MainActivity.this, FlashCardsActivity.class);
                break;
            case R.id.nav_Memorize:
                myIntent = new Intent(MainActivity.this, MemorizeBoxActivity.class);
                break;
            }

        if (myIntent != null)
            startActivity(myIntent);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
        }

//    private void TS(String TS)
//        {
//        Toast.makeText(this, TS, Toast.LENGTH_SHORT).show();
//        }

    private void Search(String Search)
        {
        VocabList.clear();
        VocabList.addAll(dbHandler.getLongMan(Search));
        mMainAdapter.notifyDataSetChanged();
        }

    protected void show_progress()
        {
        pD = new ProgressDialog(MainActivity.this);
        pD.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pD.setMessage("Extracting database ...");
        pD.setCancelable(false);
        pD.show();
        }

    protected void updateProgress(int progress)
        {
        pD.setProgress(progress);
        }

    protected void dismiss()
        {
        if (pD.isShowing())
            pD.dismiss();
        Search("");
        getSharedPreferences("appInfo", MODE_PRIVATE).edit().putBoolean("isFirstRun", false).apply();
        }

    @SuppressLint("StaticFieldLeak")
    private class copyFile extends AsyncTask<String, Integer, String>
        {
        @Override
        protected String doInBackground(String... strings)
            {
            try
                {
                InputStream inputStream;
                String outFileName;
                OutputStream outputStream;
                inputStream = MainActivity.this.getAssets().open("LongMan.db");
                outFileName = MainActivity.this.getApplicationInfo().dataDir + "/databases/" + "LongMan.db";
                outputStream = new FileOutputStream(outFileName);
                byte[] buff = new byte[1024];
                int length, i = 0;

                while ((length = inputStream.read(buff)) > 0)
                    {
                    outputStream.write(buff, 0, length);
                    publishProgress(i++, 136320);
                    }

                outputStream.flush();
                outputStream.close();

                }
            catch (Exception e)
                {
                e.printStackTrace();
                }
            return null;
            }

        @Override
        protected void onPreExecute()
            {
            super.onPreExecute();
            show_progress();
            }

        @Override
        protected void onProgressUpdate(Integer... values)
            {
            super.onProgressUpdate(values);
            float progressValue;
            progressValue = ((float) values[0] / (float) values[1]) * 100;
            updateProgress((int) progressValue);
            }

        @Override
        protected void onPostExecute(String s)
            {
            super.onPostExecute(s);
            dismiss();
            }
        }
    }
