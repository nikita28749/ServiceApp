package com.example.nikitamathur.myapplication;

/**
 * Created by Nikita Mathur on 10/2/2016.
 */

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class ServiceA  extends Service {
    private boolean success = false;
    public static final String NOTIFICATION = "service receiver";

    private final IBinder binder = new MyBinder();

    public class MyBinder extends Binder {
        ServiceA getService() {
            return ServiceA.this;
        }
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return binder;
    }

    @Override
    public int onStartCommand (Intent intent, int flags, int startId){
        String urlPath = intent.getStringExtra("urlpath");
        String fileName = intent.getStringExtra("filename");
        new DownloadFileFromURL().execute(urlPath,fileName);
        return START_STICKY;
    }

    class DownloadFileFromURL extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            System.out.println("Download Starting");
        }

        protected String doInBackground(String... params) {
            int count;
            File output = new File(getBaseContext().getFilesDir(),params[1]);
            try {

                System.out.println("Downloading " + params[1]);
                URL url = new URL(params[0]);

                URLConnection connection = url.openConnection();
                connection.connect();
                InputStream inputStream = new BufferedInputStream(url.openStream(), 8192);

                OutputStream outputStream = new FileOutputStream(output.getPath());
                byte data[] = new byte[1024];

                long total = 0;
                while ((count = inputStream.read(data)) != -1) {
                    total += count;
                    outputStream.write(data, 0, count);

                }
                outputStream.flush();
                outputStream.close();
                inputStream.close();
                success = true;
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
            if (success) setDownloadedFlag(params[1]);
            else setFailedFlag(params[1]);
            publishResults(params[1]);
            return null;
        }
        @Override
        protected void onPostExecute(String file_url) {
            System.out.println("onPostExecute");
            stopSelf();
        }

        private void publishResults(String filename) {
            Intent intent = new Intent(NOTIFICATION);
            intent.putExtra("file", filename);
            sendBroadcast(intent);
        }

        private void setDownloadedFlag (String filename) {
            if (filename.equals(DownloadPDF.file1))DownloadPDF.file1Flag = 2;
            else if (filename.equals(DownloadPDF.file2)) DownloadPDF.file2Flag = 2;
            else if (filename.equals(DownloadPDF.file3)) DownloadPDF.file3Flag = 2;
            else if (filename.equals(DownloadPDF.file4)) DownloadPDF.file4Flag = 2;
            else if (filename.equals(DownloadPDF.file5)) DownloadPDF.file5Flag = 2;
        }

        private void setFailedFlag (String filename) {
            if (filename.equals(DownloadPDF.file1)) DownloadPDF.file1Flag = -1;
            else if (filename.equals(DownloadPDF.file2)) DownloadPDF.file2Flag = -1;
            else if (filename.equals(DownloadPDF.file3)) DownloadPDF.file3Flag = -1;
            else if (filename.equals(DownloadPDF.file4)) DownloadPDF.file4Flag = -1;
            else if (filename.equals(DownloadPDF.file5)) DownloadPDF.file5Flag = -1;
        }
    }
}
