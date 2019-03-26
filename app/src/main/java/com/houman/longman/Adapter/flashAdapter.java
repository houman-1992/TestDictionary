package com.houman.longman.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.houman.longman.R;

import java.util.List;

public class flashAdapter extends RecyclerView.Adapter<flashAdapter.viewHolder>
    {
    private List<String> mData;
    private onItemClickListener mListener;

    public interface onItemClickListener
        {
        void onItemClick(int position);
        void onDeleteClick(int position);
        }

    public void setOnItemClickListener(onItemClickListener listener)
        {
        mListener = listener;
        }

    public flashAdapter(List<String> mData)
        {
        this.mData = mData;
        }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_flash, parent, false);
        return new viewHolder(view, mListener);
        }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position)
        {
        holder.textView_Main_Word.setText(mData.get(position));
        }

    @Override
    public int getItemCount()
        {
        return mData.size();
        }

    static class viewHolder extends RecyclerView.ViewHolder
        {
        private TextView textView_Main_Word;

        viewHolder(View itemView, final onItemClickListener listener)
            {
            super(itemView);
            textView_Main_Word = itemView.findViewById(R.id.FlashCardsName);

            itemView.setOnClickListener(new View.OnClickListener()
                {
                @Override
                public void onClick(View v)
                    {
                    if (listener != null)
                        {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION)
                            {
                            listener.onItemClick(position);
                            }
                        }
                    }
                });
            }
        }
    }
