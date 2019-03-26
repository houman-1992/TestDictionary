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


public class fAdapter extends RecyclerView.Adapter<fAdapter.viewHolder>
    {

    private List<String> mData;
    private onClickListener mListener;

    public interface onClickListener
        {
        void onItemClick(int position);
        void onPronClick(int position);
        }

    public void setOnClickListener(onClickListener listener)
        {
        mListener = listener;
        }

    public fAdapter(List<String> m_data)
        {
        mData = m_data;
        }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_main, parent, false);
        return new fAdapter.viewHolder(view, mListener);
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
        private ImageView img_Pron;
        viewHolder(View itemView, final onClickListener listener)
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

