package com.mc.nad.api;

import android.os.AsyncTask;
import android.os.Environment;

import com.mc.nad.util.UnpackZip;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class DownloadDocs extends AsyncTask<String, Void, Boolean> {

    private static final String DIRECTORY_FILES = "Android/data/com.mc.nad/files/docs";

    private static AdapterListener adapterListener;

    public interface AdapterListener {
        void onReady(boolean status);
    }

    public static String getFilePathToWebView(String fileName) {
        return "file:///" +
                Environment.getExternalStoragePublicDirectory(DIRECTORY_FILES).getPath() + File.separator +
                fileName +
                ".html";
    }

    public static void setAdapterListener(AdapterListener adapterListener) {
        DownloadDocs.adapterListener = adapterListener;
    }

    protected void onPreExecute() {

    }

    protected Boolean doInBackground(String... strings) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(Api.URL_ZIP)
                .addHeader("Content-Type", "application/zip").build();
        Response response;
        try {
            response = client.newCall(request).execute();
            if (!response.isSuccessful()) throw new IOException(String.valueOf(response));
            InputStream in = response.body().byteStream();
            new UnpackZip().execute(Environment.getExternalStoragePublicDirectory(DIRECTORY_FILES), in);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    protected void onPostExecute(Boolean status) {
        if (adapterListener != null) {
            adapterListener.onReady(status);
        }
    }
}