package com.houman.longman.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.houman.longman.Adapter.fAdapter;
import com.houman.longman.Database.DatabaseHandler;
import com.houman.longman.MemorizeBoxActivity;
import com.houman.longman.Pronounce.OnlinePronounce;
import com.houman.longman.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuppressLint("ValidFragment")
public class bFragment extends Fragment
    {
    private Context mContext;
    private List<String> mData;
    private DatabaseHandler dbHandler;

    public bFragment(Context mContext, List<String> mData)
        {
        this.mContext = mContext;
        this.mData = mData;
        dbHandler = new DatabaseHandler(mContext);
        }

    @Override
    public void onCreate(Bundle savedInstanceState)
        {
        super.onCreate(savedInstanceState);
        }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
        View view = inflater.inflate(R.layout.b_fragment, container, false);
        RecyclerView bRecyclerView = view.findViewById(R.id.bRecyclerView);

        fAdapter RecyclerAdapter = new fAdapter(mData);
        bRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        bRecyclerView.setAdapter(RecyclerAdapter);
        RecyclerAdapter.setOnClickListener(new fAdapter.onClickListener()
            {
            @Override
            public void onItemClick(int position)
                {
                String Data = mData.get(position).toLowerCase();
                ((MemorizeBoxActivity) Objects.requireNonNull(getActivity())).ShowMean(Data);
                }

            @Override
            public void onPronClick(int position)
                {
                Pronounce(mData.get(position));
                }
            });
        return view;
        }

    public void Pronounce(String Text)
        {
        Text = Text.toLowerCase();
        Text = dbHandler.getWordRoot(Text);
        Text = dbHandler.getUSFileName(Text);
        Text = getResources().getString(R.string.amePronLink) + Text + "?version=" + getResources().getString(R.string.LongManV);
        new OnlinePronounce(mContext).execute(Text);
        }
    }
