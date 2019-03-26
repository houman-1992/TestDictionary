package com.houman.longman.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.florent37.expansionpanel.ExpansionHeader;
import com.github.florent37.expansionpanel.ExpansionLayout;
import com.houman.longman.Database.DatabaseHandler;
import com.houman.longman.Models.Model_Card;
import com.houman.longman.Pronounce.OnlinePronounce;
import com.houman.longman.R;

import java.util.List;

public class AdapterShow extends RecyclerView.Adapter<AdapterShow.ViewHolder>
    {
    private List<Model_Card> mData;
    private Context mContext;
    private DatabaseHandler dbHandler;

    public AdapterShow(List<Model_Card> mData, Context mContext)
        {
        this.mData = mData;
        this.mContext = mContext;
        dbHandler = new DatabaseHandler(mContext);
        }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expandable_item, parent, false);
        return new AdapterShow.ViewHolder(view);
        }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
        {
        String text;
        Model_Card tempValue;
        tempValue = mData.get(position);
        text = tempValue.getWord();

        setTextViewHTML2(holder.Main_Word, text);

        text = tempValue.getMean();
        if (text.equals(""))
            {
            holder.expansionH.setToggleOnClick(false);
            holder.img.setVisibility(AppCompatImageView.INVISIBLE);
            }
        else
            setTextViewHTML1(holder.Mean_Word, text);
        }

    private void setTextViewHTML1(TextView Tv, String html)
        {
        CharSequence sequence = Html.fromHtml(html);
        SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);
        URLSpan[] urls = strBuilder.getSpans(0, sequence.length(), URLSpan.class);
        int color = Tv.getCurrentTextColor();
        for (URLSpan span : urls)
            {
            makeLinkExamples(strBuilder, span, color);
            }
        Tv.setText(strBuilder);
        Tv.setMovementMethod(LinkMovementMethod.getInstance());
        }

    private void makeLinkExamples(SpannableStringBuilder strBuilder, final URLSpan span,
                                  final int color)
        {
        int start = strBuilder.getSpanStart(span);
        int end = strBuilder.getSpanEnd(span);
        int flags = strBuilder.getSpanFlags(span);
        ClickableSpan clickable = new ClickableSpan()
            {
            public void onClick(@NonNull View view)
                {
                String Text = dbHandler.getExaPronName(span.getURL());
                String Link = mContext.getResources().getString(R.string.exaPronLink);
                String Version = mContext.getResources().getString(R.string.LongManV);
                Text = Link + Text + "?version=" + Version;
                new OnlinePronounce(mContext).execute(Text);
                }

            @Override
            public void updateDrawState(@NonNull TextPaint ds)
                {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.setColor(color);
                }
            };
        strBuilder.setSpan(clickable, start, end, flags);
        strBuilder.removeSpan(span);
        }

    private void setTextViewHTML2(TextView Tv, String html)
        {
        CharSequence sequence = Html.fromHtml(html);
        SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);
        URLSpan[] urls = strBuilder.getSpans(0, sequence.length(), URLSpan.class);
        int color = Tv.getCurrentTextColor();
        for (URLSpan span : urls)
            {
            makeLinkWord(strBuilder, span, color);
            }
        Tv.setText(strBuilder);
        Tv.setMovementMethod(LinkMovementMethod.getInstance());
        }

    private void makeLinkWord(SpannableStringBuilder strBuilder, final URLSpan span,
                                    final int color)
        {
        int start = strBuilder.getSpanStart(span);
        int end = strBuilder.getSpanEnd(span);
        int flags = strBuilder.getSpanFlags(span);
        ClickableSpan clickable = new ClickableSpan()
            {
            public void onClick(@NonNull View view)
                {
                String Text = dbHandler.getAmePronName(span.getURL());
                String Link = mContext.getResources().getString(R.string.amePronLink);
                String Version = mContext.getResources().getString(R.string.LongManV);
                Text = Link + Text + "?version=" + Version;
                new OnlinePronounce(mContext).execute(Text);
                }

            @Override
            public void updateDrawState(@NonNull TextPaint ds)
                {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.setColor(color);
                }
            };
        strBuilder.setSpan(clickable, start, end, flags);
        strBuilder.removeSpan(span);
        }

    @Override
    public int getItemCount()
        {
        return mData.size();
        }

    static class ViewHolder extends RecyclerView.ViewHolder
        {
        private TextView Main_Word;
        private TextView Mean_Word;
        private ExpansionHeader expansionH;
        private ExpansionLayout expansionL;
        private AppCompatImageView img;
        private Button BtnHide;

        ViewHolder(View itemView)
            {
            super(itemView);
            Main_Word = itemView.findViewById(R.id.Ex_Word);
            Mean_Word = itemView.findViewById(R.id.Ex_Mean);
            expansionH = itemView.findViewById(R.id.shdkajsdhk);
            expansionL = itemView.findViewById(R.id.expansionLayout);
            img = itemView.findViewById(R.id.headerIndicator);
            BtnHide = itemView.findViewById(R.id.BtnHide);
            BtnHide.setOnClickListener(new View.OnClickListener()
                {
                @Override
                public void onClick(View v)
                    {
                    expansionL.toggle(true);
                    }
                });
            }
        }
    }