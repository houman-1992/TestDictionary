package com.houman.longman;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import java.util.regex.Pattern;

import static com.houman.longman.TextActivity.FILE_PICKER_REQUEST_CODE;
import static com.houman.longman.TextActivity.PERMISSIONS_REQUEST_CODE;

public class DictationActivity extends AppCompatActivity
    {
    private String DefaultPath;

    private List<String> VocabList = new ArrayList<>();
    private List<String> VocabListKnown = new ArrayList<>();//todo
    private List<String> VocabListUnKnown = new ArrayList<>();
//    private DatabaseHandler dbHandler;
//    private FloatingActionButton pronFab,viewFab,skipFab;
    private TextView mTextView,textViewCount;
    private  EditText mEditText;
    private int position;
    private String Word;
    private TextToSpeech TTS;
    @Override
    protected void onCreate(Bundle savedInstanceState)
        {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictation);
        FindViews();
        setListeners();
        LoadSettings();
//        dbHandler = new DatabaseHandler(DictationActivity.this);
        TTS = new TextToSpeech(DictationActivity.this, TTSListener);
        checkPermissionsAndOpenFilePicker();
        }

    private void FindViews()
        {
        Toolbar toolbar = findViewById(R.id.dictationToolbar);
//        pronFab = findViewById(R.id.pronounceFab);
//        viewFab = findViewById(R.id.viewFab);
//        skipFab = findViewById(R.id.skipFab);
        mTextView = findViewById(R.id.dictationTextView);
        mEditText = findViewById(R.id.DictationEditText);
        textViewCount = findViewById(R.id.textViewCount);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        }

    public void pronounceFabClicked(View v)
        {
        Pronounce(Word);
        }

    public void viewFabClicked(View v)
        {
        mTextView.setVisibility(View.VISIBLE);
        }

    public void skipFabClicked(View v)
        {
        mTextView.setVisibility(View.INVISIBLE);
        mEditText.setText("");
        position++;
        if (position + 1 > VocabList.size())
            {
            position = 0;
            if (VocabListUnKnown.size() > 0)
                {
                VocabList.clear();
                VocabList.addAll(VocabListUnKnown);
                VocabListUnKnown.clear();
                }
            else
                {
                Toast.makeText(DictationActivity.this, "Finished", Toast.LENGTH_SHORT).show();
                }
            }
        VocabListUnKnown.add(Word);
        Word = VocabList.get(position).toLowerCase();
        mTextView.setText(Word);
        Pronounce(Word);
        textViewCount.setText((position + 1) + " / " + VocabList.size());
        }

    private void setListeners()
        {
        mEditText.addTextChangedListener(new TextWatcher()
            {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
                {

                }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
                {
                if(Word.contentEquals(s))
                    {
                    mTextView.setVisibility(View.VISIBLE);
                    VocabListKnown.add(Word);
                    mEditText.setText("");
                    }
                }

            @Override
            public void afterTextChanged(Editable s)
                {

                }
            });
        }

    private void LoadSettings()
        {
        DefaultPath = getSharedPreferences("appInfo", MODE_PRIVATE).getString("DefaultPath", "");
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
                showError();
            }
            }
        }

    private void checkPermissionsAndOpenFilePicker()
        {
        String permission = Manifest.permission.READ_EXTERNAL_STORAGE;
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED)
            {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission))
                showError();
            else
                ActivityCompat.requestPermissions(this, new String[]{permission}, PERMISSIONS_REQUEST_CODE);
            }
        else
            openFilePicker();
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
                .withHiddenFiles(false)
                .withPath(DefaultPath)
                .withFilter(Pattern.compile(".*\\.txt$"))
                .withTitle("Choose file")
                .start();
        }

    private List<String> randomOrder(List<String> ListIn)
        {
        List<String> ListOut = new ArrayList<>();
        while (ListIn.size() > 0)
            {
            int size = ListIn.size();
            if (size > 1)
                {
                int rnd = new Random().nextInt(ListIn.size());
                ListOut.add(ListIn.get(rnd));
                ListIn.remove(rnd);
                }
            else
                {
                ListOut.add(ListIn.get(0));
                ListIn.clear();
                }
            }
        return ListOut;
        }

    private List<String> read_file(String filePath)
        {

        File file = new File(filePath);

        List<String> VocabList = new ArrayList<>();

        try
            {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null)
                {
                VocabList.add(line);
                }
            br.close();
            }
        catch (IOException e)
            {
            e.printStackTrace();
            }
        return VocabList;
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
                String Path = FilePath.substring(FilePath.lastIndexOf("/") + 1);
                Toast.makeText(this, Path, Toast.LENGTH_SHORT).show();
                Path = FilePath.replace(Path, "");
                if (! Path.equals(DefaultPath))
                    {
                    DefaultPath = Path;
                    getSharedPreferences("appInfo", MODE_PRIVATE).edit().putString("DefaultPath", DefaultPath).apply();
                    }
                VocabList.addAll(randomOrder(read_file(FilePath)));
                position = 0;
                Word = VocabList.get(position).toLowerCase();
                mTextView.setText(Word);
                textViewCount.setText((position + 1) + " / " + VocabList.size());
                Pronounce(Word);
                }
            }
        }

    public void Pronounce(String Text)
        {
        TTS.speak(Text, TextToSpeech.QUEUE_FLUSH, null, null);
//        Text = Text.toLowerCase();
//        Text = dbHandler.getWordRoot(Text);
//        Text = dbHandler.getUSFileName(Text);
//        Text = getResources().getString(R.string.amePronLink) + Text + "?version=" + getResources().getString(R.string.LongManV);
//        new OnlinePronounce(this).execute(Text);
        }

    private TextToSpeech.OnInitListener TTSListener = new TextToSpeech.OnInitListener()
        {
        @Override
        public void onInit(int status)
            {
            if (status == TextToSpeech.SUCCESS)
                TTS.setLanguage(Locale.US);
            else
                Toast.makeText(getApplicationContext(), "this device doesn't support TTS", Toast.LENGTH_SHORT).show();
            }
        };
    }
