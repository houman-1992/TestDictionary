package com.houman.longman.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.houman.longman.Models.MainModel;
import com.houman.longman.R;

import java.util.List;


public class MainAdapter extends RecyclerView.Adapter<MainAdapter.viewHolder>
    {

    private List<MainModel> mData;
    private onItemClickListener mListener;

    public interface onItemClickListener
        {
        void onItemClick(int position);
        void onPronClick(int position);
        }

    public void setOnItemClickListener(onItemClickListener listener)
        {
        mListener = listener;
        }

    public MainAdapter(List<MainModel> m_data)
        {
        mData = m_data;
        }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_main, parent, false);
        return new MainAdapter.viewHolder(view, mListener);
        }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position)
        {
        holder.textView_Main_Word.setText(mData.get(position).getHWD());
        }

    @Override
    public int getItemCount()
        {
        return mData.size();
        }

    static class viewHolder extends RecyclerView.ViewHolder
        {
        private TextView textView_Main_Word;
        private ImageView img_Pron;
        viewHolder(View itemView, final onItemClickListener listener)
            {
            super(itemView);
            textView_Main_Word = itemView.findViewById(R.id.text_Long_man);
            img_Pron = itemView.findViewById(R.id.img_Pron);

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
            img_Pron.setOnClickListener(new View.OnClickListener()
                {
                @Override
                public void onClick(View v)
                    {
                    if (listener != null)
                        {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION)
                            {
                            listener.onPronClick(position);
                            }
                        }
                    }
                });
            }
        }
    }

