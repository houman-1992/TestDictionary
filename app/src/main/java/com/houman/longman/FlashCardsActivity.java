package com.houman.longman;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.houman.longman.Adapter.flashAdapter;
import com.houman.longman.Database.DatabaseHandler;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import static com.houman.longman.TextActivity.FILE_PICKER_REQUEST_CODE;
import static com.houman.longman.TextActivity.PERMISSIONS_REQUEST_CODE;

public class FlashCardsActivity extends AppCompatActivity
    {
    private String DefaultPath;
    private flashAdapter fAdapter;
    private List<String> flashList = new ArrayList<>();
    private DatabaseHandler dbHandler;
    private ProgressDialog pD;

    @Override
    protected void onCreate(Bundle savedInstanceState)
        {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_cards);

        Toolbar mToolbar = findViewById(R.id.fToolbar);
        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        RecyclerView fRecyclerView = findViewById(R.id.fRecyclerView);

        fRecyclerView.setLayoutManager(new LinearLayoutManager(FlashCardsActivity.this));
        fAdapter = new flashAdapter(flashList);
        fRecyclerView.setAdapter(fAdapter);
        dbHandler = new DatabaseHandler(FlashCardsActivity.this);
        ShowFlashCards("");
        fAdapter.setOnItemClickListener(new flashAdapter.onItemClickListener()
            {
            @Override
            public void onDeleteClick(int position)
                {

                }

            @Override
            public void onItemClick(int position)
                {
                Intent myIntent = new Intent(FlashCardsActivity.this, PracticeActivity.class);
                myIntent.putExtra("Flash", flashList.get(position));
                startActivity(myIntent);

//                Intent myIntent = new Intent(FlashCardsActivity.this, DictationPractice.class);
//                myIntent.putExtra("Flash", flashList.get(position));
//                startActivity(myIntent);
                }
            });

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
                Log.d("djfsd", "move left or right!");
                }
            }).attachToRecyclerView(fRecyclerView);

        }

    private void deleteItem(int position)
        {
        dbHandler.DeleteFlash(flashList.get(position));
        deleteFiles(flashList.get(position));
        flashList.remove(position);
        fAdapter.notifyItemRemoved(position);
        }

    private void deleteFiles(String s)
        {
        File dir =
                new File(Environment.getExternalStorageDirectory()+"/English/cash/" + s);
        if (dir.isDirectory())
            {
            String[] children = dir.list();
            for (String aChildren : children)
                new File(dir, aChildren).delete();
            dir.delete();
            }
        }

    private void ShowFlashCards(String Search)
        {
        flashList.clear();
        flashList.addAll(dbHandler.getTablesName(Search));
        fAdapter.notifyDataSetChanged();
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
        {
        getMenuInflater().inflate(R.menu.flash_menu, menu);
        return true;
        }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
        {
        int id = item.getItemId();
        if (id == R.id.importFile)
            checkPermissionsAndOpenFilePicker();

        return super.onOptionsItemSelected(item);
        }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults)
        {
        switch (requestCode)
            {
            case PERMISSIONS_REQUEST_CODE:
            {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
//                String permissionW = Manifest.permission.WRITE_EXTERNAL_STORAGE;
//                if (ContextCompat.checkSelfPermission(this, permissionW) != PackageManager.PERMISSION_GRANTED)
//                    {
//                    ActivityCompat.requestPermissions(this, new String[]{permissionW}, PERMISSIONS_REQUEST_CODE);
//                    }
//                else
                openFilePicker();
                }
            else
                {
                showError();
                }
            }
            }
        }

    private void checkPermissionsAndOpenFilePicker()
        {
        String permissionR = Manifest.permission.READ_EXTERNAL_STORAGE;
        String permissionW = Manifest.permission.WRITE_EXTERNAL_STORAGE;

        if (ContextCompat.checkSelfPermission(this, permissionR) != PackageManager.PERMISSION_GRANTED)
            {
            ActivityCompat.requestPermissions(this, new String[]{permissionR, permissionW},
                    PERMISSIONS_REQUEST_CODE);
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission))
//                {
//                showError();
//                }
//            else
//                {
//                ActivityCompat.requestPermissions(this, new String[]{permission}, PERMISSIONS_REQUEST_CODE);
//                }
            }
        else
            {
            openFilePicker();
            }
        }

    private void showError()
        {
        Toast.makeText(this, "Allow external storage reading", Toast.LENGTH_SHORT).show();
        }

    private void openFilePicker()
        {
        DefaultPath = getSharedPreferences("appInfo", MODE_PRIVATE).getString("DefaultPathFlash",
                "");
        new MaterialFilePicker()
                .withActivity(this)
                .withRequestCode(FILE_PICKER_REQUEST_CODE)
                .withHiddenFiles(false)
                .withPath(DefaultPath)
                .withFilter(Pattern.compile(".*\\.txt$"))
                .withTitle("Choose file")
                .start();
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
                new DownloadFileAsync().execute(FilePath);
            }
        }

    protected void show_progress()
        {
        pD = new ProgressDialog(FlashCardsActivity.this);
        pD.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pD.setMessage("Importing file ...");
        pD.setCancelable(false);
        pD.show();
        }

    protected void updateProgress(int progress)
        {
        pD.setProgress(progress);
        }

    protected void dismiss()
        {
        if (pD.isShowing())
            pD.dismiss();
        Toast.makeText(this, "File added successfully", Toast.LENGTH_SHORT).show();
        ShowFlashCards("");
        }

    @SuppressLint("StaticFieldLeak")
    class DownloadFileAsync extends AsyncTask<String, Integer, String>
        {

        @Override
        protected void onPreExecute()
            {
            super.onPreExecute();
            show_progress();
            }

        @Override
        protected String doInBackground(String... Data)
            {
            String FilePath = Data[0];
            String Path = FilePath.substring(FilePath.lastIndexOf("/") + 1);
            if (! Path.equals(DefaultPath))
                {
                DefaultPath = FilePath.replace(Path, "");
                getSharedPreferences("appInfo", MODE_PRIVATE).edit().putString("DefaultPathFlash", DefaultPath).apply();
                }
            Path = Path.replace(".txt", "");
            String DownloadFilePath = Environment.getExternalStorageDirectory().toString() + "/English/cash/" + Path;
            File dir = new File(DownloadFilePath);
            if (! (dir.exists() && dir.isDirectory()))
                dir.mkdirs();

            List<String> buf = read_file(FilePath);
            dbHandler.CreateTable(Path);
            dbHandler.AddVocab(Path, buf);

            for (int i = 0; i < buf.size(); i++)
                {
                String Word = buf.get(i);
                String Text = Word;
                Text = Text.toLowerCase();
                Text = dbHandler.getWordRoot(Text);
                Text = dbHandler.getUSFileName(Text);
                Text = getResources().getString(R.string.amePronLink) + Text + "?version=" + getResources().getString(R.string.LongManV);
                Download(Text, Word, DownloadFilePath);
                publishProgress((int) ((i * 100) / buf.size()));
                }
            return null;
            }
//todo add che network
        private void Download(String Text, String Name, String Path)
            {
            int count;
            Path = Path + "/" + Name + ".mp3";
            try
                {
                URL url = new URL(Text);
                URLConnection connection = url.openConnection();
                connection.connect();
                InputStream input = new BufferedInputStream(url.openStream());
                OutputStream output = new FileOutputStream(Path);
                byte data[] = new byte[1024];
                while ((count = input.read(data)) != - 1)
                    output.write(data, 0, count);

                output.flush();
                output.close();
                input.close();
                }
            catch (Exception e)
                {
                e.printStackTrace();
                }
            }

        protected void onProgressUpdate(Integer... values)
            {
            updateProgress(values[0]);
            }

        @Override
        protected void onPostExecute(String unused)
            {
            dismiss();
            }
        }
    }
