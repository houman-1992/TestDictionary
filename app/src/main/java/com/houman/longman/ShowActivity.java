package com.houman.longman;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.houman.longman.Adapter.AdapterShow;
import com.houman.longman.Database.DatabaseHandler;
import com.houman.longman.Models.MainModel;
import com.houman.longman.Models.Model_Card;
import com.houman.longman.Pronounce.OnlinePronounce;
import com.houman.longman.Models.xmlModle.Entery;
import com.houman.longman.Models.xmlModle.Head.Head;
import com.houman.longman.Models.xmlModle.Sense.EXAMPLE;
import com.houman.longman.Models.xmlModle.Sense.ExpandableInformation;
import com.houman.longman.Models.xmlModle.Sense.Sense;
import com.r0adkll.slidr.Slidr;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ShowActivity extends AppCompatActivity
    {

    private RecyclerView mRecyclerView;
    private AdapterShow mAdapter;
    private List<Model_Card> MarkedList = new ArrayList<>();
    private String tempValue = "";
    private boolean pronCondition = true;
    private boolean PronPermission;
    private boolean Marked;
    private DatabaseHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState)
        {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        Toolbar mToolbar = findViewById(R.id.sToolbar);
        mRecyclerView = findViewById(R.id.sRecyclerView);

        Slidr.attach(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null)
            tempValue = extras.getString("HWD");

        mToolbar.setTitle(tempValue);
        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        PronPermission = getSharedPreferences("appInfo", MODE_PRIVATE).getBoolean("PronPermission", true);

        setUp();

        dbHandler = new DatabaseHandler(ShowActivity.this);
        List<MainModel> EnBuff = new ArrayList<>(dbHandler.getWordEn(tempValue));
        dbHandler.addRecently(tempValue);

        MarkedList.clear();
        for (int i = 0; i < EnBuff.size(); i++)
            MarkedList.addAll(parsData(EnBuff.get(i).getHWD(), EnBuff.get(i).getFile()));
        mAdapter.notifyDataSetChanged();

        Marked = dbHandler.CheckMark(tempValue);
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
        {
        getMenuInflater().inflate(R.menu.show_menu, menu);
        menu.getItem(2).setChecked(PronPermission);
        if (Marked)
            menu.getItem(1).setIcon(R.drawable.ic_marked);
        return true;
        }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
        {
        int id = item.getItemId();
        switch (id)
            {
            case R.id.action_copy:
                CopyToClipboard();
                break;
            case R.id.action_mark:
                MarkWord(item);
                break;
            case R.id.action_Apron:
                if (item.isCheckable())
                    {
                    PronPermission = ! item.isChecked();
                    item.setChecked(PronPermission);
                    getSharedPreferences("appInfo", MODE_PRIVATE).edit().putBoolean("PronPermission", PronPermission).apply();
                    }
                break;
            }
        return super.onOptionsItemSelected(item);
        }

    private void MarkWord(MenuItem item)
        {
        Marked = !Marked;
        if (Marked)
            item.setIcon(R.drawable.ic_marked);
        else
            item.setIcon(R.drawable.ic_unmarked);
        dbHandler.markWord(tempValue,Marked);
        }

    private void CopyToClipboard()
        {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("", tempValue);
        assert clipboard != null;
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, tempValue + "copied to clipboard", Toast.LENGTH_SHORT).show();
        }

    private void setUp()
        {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(ShowActivity.this));
        mAdapter = new AdapterShow(MarkedList,ShowActivity.this);
        mRecyclerView.setAdapter(mAdapter);
        }

    private List<Model_Card> parsData(String XMLString, String id)
        {
        XmlPullParserFactory pullParserFactory;
        List<Model_Card> list = new ArrayList<>();
        try
            {
            pullParserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = pullParserFactory.newPullParser();

            InputStream stream = new ByteArrayInputStream(XMLString.getBytes(StandardCharsets.UTF_8));

            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(stream, null);
            XmlParserP mXmlParser = new XmlParserP();
            Entery mEntery = mXmlParser.pars(parser);

            if (mEntery != null)
                {
                Head head = mEntery.getHead();
                String Word;
                if (! head.getHYPHENATION().equals(""))
                    Word = head.getHYPHENATION();
                else
                    Word = head.getHWD();

                List<Sense> mSenses = mEntery.getSense();
                StringBuilder mBuffer = new StringBuilder();

                Word = Word.toString().replace("#","ˌ").replace("=","ˈ");

                mBuffer.append("<strong><big><font color='red'>");
                mBuffer.append(Word);
                mBuffer.append(" ");
                mBuffer.append(head.getHOMNUM());
                mBuffer.append(" </font></big></strong>");
                mBuffer.append(head.getPronCodes());
                mBuffer.append("<br><strong><big><a href='");
                mBuffer.append(id);
                mBuffer.append("'>&#128266;</a></big></strong>");

                if (pronCondition && PronPermission)
                    {
                    pronCondition = false;
                    String Text = dbHandler.getAmePronName(id);
                    String Link = getResources().getString(R.string.amePronLink);
                    String Version = getResources().getString(R.string.LongManV);
                    Text = Link + Text + "?version=" + Version;
                    new OnlinePronounce(this).execute(Text);
                    }

                list.add(new Model_Card(mBuffer.toString(), "", "", "", 0));

                for (int i = 0; i < mSenses.size(); i++)
                    {
                    Sense mSense = mSenses.get(i);
                    ExpandableInformation mExpandable = mSense.getExpandableInformations();

                    mBuffer = new StringBuilder();

                    try
                        {
                        List<EXAMPLE> examples = new ArrayList<>(mExpandable.getEXAMPLEs());

                        for (int j = 0; j < examples.size() - 1; j++)
                            mBuffer.append(examples.get(j).getEXAMPLEs()).append("<br>");
                        mBuffer.append(examples.get(examples.size() - 1).getEXAMPLEs());
                        }
                    catch (Exception e)
                        {
                        e.printStackTrace();
                        }

                    list.add(new Model_Card(mSense.getDEF(), mBuffer.toString(), "", "", 0));
                    }
                }
            }
        catch (Exception e)
            {
            e.printStackTrace();
            }
        return list;
        }
    }
