package com.houman.longman.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.houman.longman.R;

import java.util.List;

public class StackTestAdapter extends BaseAdapter
    {

    private List<String> mData;
    private Context mContext;

    private onItemClickListener mListener;

    public interface onItemClickListener
        {
        void onItemClick(int position);
        void onPronounce(int position);
        void onGoogle(int position);
        void onDelete(int position);
        }

    public void setOnItemClickListener(onItemClickListener listener)
        {
        mListener = listener;
        }

    public StackTestAdapter(List<String> mData, Context mContext)
        {
        this.mData = mData;
        this.mContext = mContext;
        }

    @Override
    public int getCount()
        {
        return mData.size();
        }

    @Override
    public String getItem(int position)
        {
        return mData.get(position);
        }

    @Override
    public long getItemId(int position)
        {
        return position;
        }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
        {
        if (convertView == null)
            convertView = LayoutInflater.from(mContext).inflate(R.layout.card, parent, false);

        TextView textViewCard = convertView.findViewById(R.id.textViewStack);
        TextView textViewCount = convertView.findViewById(R.id.textViewCount);
        ImageView imagePronounce = convertView.findViewById(R.id.sPronounce);
        ImageView imageGoogle= convertView.findViewById(R.id.sGoogle);
        ImageView imageDelete= convertView.findViewById(R.id.sDelete);
        ImageView imageShow= convertView.findViewById(R.id.sShow);

        textViewCard.setText(mData.get(position));
        textViewCount.setText((position + 1) + " / " + getCount());

        imageShow.setOnClickListener(new View.OnClickListener()
            {
            @Override
            public void onClick(View v)
                {
                if (mListener != null)
                    mListener.onItemClick(position);
                }
            });
        imagePronounce.setOnClickListener(new View.OnClickListener()
            {
            @Override
            public void onClick(View v)
                {
                if (mListener != null)
                    mListener.onPronounce(position);
                }
            });
        imageGoogle.setOnClickListener(new View.OnClickListener()
            {
            @Override
            public void onClick(View v)
                {
                if (mListener != null)
                    mListener.onGoogle(position);
                }
            });
        imageDelete.setOnClickListener(new View.OnClickListener()
            {
            @Override
            public void onClick(View v)
                {
                if (mListener != null)
                    mListener.onDelete(position);
                }
            });

        return convertView;
        }
    }