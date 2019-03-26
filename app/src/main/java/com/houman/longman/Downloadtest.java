package com.houman.longman;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class Downloadtest extends AppCompatActivity
    {

    @Override
    protected void onCreate(Bundle savedInstanceState)
        {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloadtest);

        startDownload();
        }


    private void startDownload()
        {
        String url = "https://d27ucmmhxk51xv.cloudfront.net/media/english/exaProns/p008-001619353.mp3?version=1.1.82";
        new DownloadFileAsync().execute(url);
        }

    @SuppressLint("StaticFieldLeak")
    class DownloadFileAsync extends AsyncTask<String, String, String>
        {

        @Override
        protected void onPreExecute()
            {
            super.onPreExecute();
//            showDialog(DIALOG_DOWNLOAD_PROGRESS);
            }

        @Override
        protected String doInBackground(String... aurl)
            {
            int count;
            try
                {
                URL url = new URL(aurl[0]);
                URLConnection conexion = url.openConnection();
                conexion.connect();
                int lenghtOfFile = conexion.getContentLength();
                Log.d("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);
                InputStream input = new BufferedInputStream(url.openStream());
                String path = getSharedPreferences("appInfo", MODE_PRIVATE).getString(
                        "DefaultPath", "");
                path += "/cash/f.mp3";
                OutputStream output = new FileOutputStream(path);
                byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != - 1)
                    {
                    total += count;
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                    output.write(data, 0, count);
                    }

                output.flush();
                output.close();
                input.close();
                }
            catch (Exception e)
                {
                e.printStackTrace();
                }
            return null;
            }

        protected void onProgressUpdate(String... progress)
            {
            Log.d("ANDRO_ASYNC", progress[0]);
//            mProgressDialog.setProgress(Integer.parseInt(progress[0]));
            }

        @Override
        protected void onPostExecute(String unused)
            {
//            dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
            }
        }

    }
