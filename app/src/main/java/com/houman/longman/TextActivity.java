package com.houman.longman;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.ClickableSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.houman.longman.Database.DatabaseHandler;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;
import java.util.regex.Pattern;

public class TextActivity extends AppCompatActivity
    {
    public static final int PERMISSIONS_REQUEST_CODE = 0;
    public static final int FILE_PICKER_REQUEST_CODE = 1;
    private DatabaseHandler dbHandler;

    String DefaultPath;
    TextView textView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
        {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);
        toolbar = findViewById(R.id.TToolbar);
        textView = findViewById(R.id.TextViewT);
        toolbar.setTitle("Text");

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        dbHandler = new DatabaseHandler(TextActivity.this);
        loadSettings();

        checkPermissionsAndOpenFilePicker();
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
        {
        getMenuInflater().inflate(R.menu.text_menu, menu);
        return true;
        }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
        {
        int id = item.getItemId();
        if (id == R.id.newFile)
            openFilePicker();
        return super.onOptionsItemSelected(item);
        }

    private void loadSettings()
        {
        DefaultPath = getSharedPreferences("appInfo", MODE_PRIVATE).getString("DefaultPath1", "");
        }

    private void checkPermissionsAndOpenFilePicker()
        {
        String permission = Manifest.permission.READ_EXTERNAL_STORAGE;

        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED)
            {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission))
                {
                showError();
                }
            else
                {
                ActivityCompat.requestPermissions(this, new String[]{permission}, PERMISSIONS_REQUEST_CODE);
                }
            }
        else
            {
            openFilePicker();
            }
        }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults)
        {
        switch (requestCode)
            {
            case PERMISSIONS_REQUEST_CODE:
            {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                openFilePicker();
                }
            else
                {
                showError();
                }
            }
            }
        }

    private void showError()
        {
        Toast.makeText(this, "Allow external storage reading", Toast.LENGTH_SHORT).show();
        }

    private void openFilePicker()
        {
        new MaterialFilePicker()
                .withActivity(this)
                .withRequestCode(FILE_PICKER_REQUEST_CODE)
                .withPath(DefaultPath)
                .withHiddenFiles(false)
                .withFilter(Pattern.compile(".*\\.txt$"))
                .withTitle("Choose file")
                .start();
        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
        {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FILE_PICKER_REQUEST_CODE && resultCode == RESULT_OK)
            {
            String FilePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);

            if (FilePath != null)
                {
                String Path, buffer, FileName;
                FileName = FilePath.substring(FilePath.lastIndexOf("/") + 1);
                buffer = FileName.replace(".txt", "");
                toolbar.setTitle(buffer.substring(0, 1).toUpperCase() + buffer.substring(1));

                Path = FilePath.replace(FileName, "");
                if (! Path.equals(DefaultPath))
                    {
                    DefaultPath = Path;
                    getSharedPreferences("appInfo", MODE_PRIVATE).edit().putString("DefaultPath1", DefaultPath).apply();
                    }
                read_file(FilePath);
                }
            }
        }

    private void read_file(String filePath)
        {

        File file = new File(filePath);

        final StringBuilder text = new StringBuilder();

        try
            {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null)
                {
                text.append(line);
                text.append('\n');
                }
            br.close();
            }
        catch (IOException e)
            {
            e.printStackTrace();
            }

        load_text(text.toString());
        }

    private void load_text(String textM)
        {
        StringBuilder buf = new StringBuilder();

        textView.setText("");
        textM = textM.replace("\n", " \n");
        textM = textM.replace("\n\n", " \n\n");
        String[] textS = textM.split(" ");

        for (String text : textS)
            buf.append("<a>").append(text).append(" ").append("</a>");

        String[] devFull = buf.toString().split("<a>");
        textView.append(devFull[0]);

        SpannableString[] link = new SpannableString[devFull.length - 1];

        ClickableSpan[] cs = new ClickableSpan[devFull.length - 1];
        String linkWord;
        String[] devDevFull;

        for (int i = 1; i < devFull.length; i++)
            {
            devDevFull = devFull[i].split("</a>");
            link[i - 1] = new SpannableString(devDevFull[0]);
            linkWord = devDevFull[0];
            final String a = linkWord;
            cs[i - 1] = new ClickableSpan()
                {
                private String w = a;

                @Override
                public void updateDrawState(@NonNull TextPaint ds)
                    {
                    ds.setUnderlineText(false);
                    }

                @Override
                public void onClick(@NonNull View widget)
                    {
                    find_Word(w);
                    }
                };

            link[i - 1].setSpan(cs[i - 1], 0, linkWord.length(), 0);
            textView.append(link[i - 1]);
            }
        makeLinksFocusable(textView);
        }

    private void makeLinksFocusable(TextView tv)
        {
        MovementMethod m = tv.getMovementMethod();
        if (! (m instanceof LinkMovementMethod))
            {
            if (tv.getLinksClickable())
                {
                tv.setMovementMethod(LinkMovementMethod.getInstance());
                }
            }
        }

    private void find_Word(String fWord)
        {
        fWord = fWord.replaceAll("[^a-zA-Z]", "").toLowerCase();
        fWord = dbHandler.getWordRoot(fWord);
        Intent myIntent = new Intent(TextActivity.this, ShowActivity.class);
        myIntent.putExtra("HWD", fWord);
        startActivity(myIntent);
        }
    }
