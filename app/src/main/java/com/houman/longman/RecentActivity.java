package com.houman.longman;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.houman.longman.Adapter.mAdapter;
import com.houman.longman.Database.DatabaseHandler;
import com.houman.longman.Pronounce.OnlinePronounce;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RecentActivity extends AppCompatActivity
    {
    private Toolbar toolbar;
    private RecyclerView mRecyclerView;
    private MaterialSearchView mSearchView;
    private List<String> MarkedList = new ArrayList<>();
    private mAdapter Adapter;
    private DatabaseHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState)
        {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent);
        FindWives();
        setupFunctions();
        }

    private void FindWives()
        {
        toolbar = findViewById(R.id.ReToolbar);
        mRecyclerView = findViewById(R.id.RecentRecyclerView);
        mSearchView = findViewById(R.id.RecentSearch_view);
        }

    private void setupFunctions()
        {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        mSearchView.setCursorDrawable(R.drawable.custom_cursor);

        dbHandler = new DatabaseHandler(RecentActivity.this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(RecentActivity.this));
        Adapter = new mAdapter(MarkedList);
        mRecyclerView.setAdapter(Adapter);

        search("");

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT)
            {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target)
                {
                return false;
                }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction)
                {
                deleteItem(viewHolder.getAdapterPosition());
                }
            }).attachToRecyclerView(mRecyclerView);

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
                search(newText);
                return false;
                }
            });

        Adapter.setOnItemClickListener(new mAdapter.onItemClickListener()
            {
            @Override
            public void onItemClick(int position)
                {
                Intent myIntent = new Intent(RecentActivity.this, ShowActivity.class);
                myIntent.putExtra("HWD", MarkedList.get(position));
                startActivity(myIntent);
                }

            @Override
            public void onPronClick(int position)
                {
                Pronounce(MarkedList.get(position));
                }
            });
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
        {
        getMenuInflater().inflate(R.menu.favorite_menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        mSearchView.setMenuItem(item);
        return true;
        }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
        {
        if (item.getItemId() == R.id.action_clc)
            Alert();
        return super.onOptionsItemSelected(item);
        }

    private void deleteItem(int position)
        {
        dbHandler.RecentDelete(MarkedList.get(position));
        MarkedList.remove(position);
        Adapter.notifyItemRemoved(position);
        }

    private void deleteAll()
        {
        dbHandler.RecentDeleteAll();
        search("");
        }

    private void Alert()
        {

        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Warning!");
        alertDialogBuilder.setMessage("Are you sure about this?");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton(R.string.OK, null);
        alertDialogBuilder.setNegativeButton(R.string.CANCEL, null);

        final android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener()
            {
            @Override
            public void onShow(DialogInterface dialog)
                {
                //todo modify
                alertDialog.getButton(android.support.v7.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorAccent));
                alertDialog.getButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.theme_primary));

                Button btnPositive = alertDialog.getButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE);
                btnPositive.setOnClickListener(new View.OnClickListener()
                    {
                    @Override
                    public void onClick(View v)
                        {
                        deleteAll();
                        alertDialog.dismiss();
                        }
                    });

                Button btnNegative = alertDialog.getButton(android.support.v7.app.AlertDialog.BUTTON_NEGATIVE);
                btnNegative.setOnClickListener(new View.OnClickListener()
                    {
                    @Override
                    public void onClick(View v)
                        {
                        alertDialog.dismiss();
                        }
                    });
                }
            });

        alertDialog.show();
        }

    private void search(String Search)
        {
        MarkedList.clear();
        MarkedList.addAll(0, dbHandler.getRecent(Search));
        Adapter.notifyDataSetChanged();
        }

    public void onBackPressed()
        {
        if (mSearchView.isSearchOpen())
            {
            mSearchView.closeSearch();
            }
        else
            {
            super.onBackPressed();
            }
        }

    public void Pronounce(String Text)
        {
        Text = Text.toLowerCase();
        Text = dbHandler.getWordRoot(Text);
        Text = dbHandler.getUSFileName(Text);
        Text =getResources().getString(R.string.amePronLink) + Text + "?version=" + getResources().getString(R.string.LongManV);
        new OnlinePronounce(this).execute(Text);
        }
    }