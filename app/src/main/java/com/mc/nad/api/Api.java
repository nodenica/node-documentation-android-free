package com.mc.nad.api;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.mc.nad.Config;
import com.mc.nad.models.DocsModel;
import com.mc.nad.models.ModuleLocalModel;
import com.mc.nad.models.ModuleModel;
import com.tumblr.remember.Remember;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.realm.Realm;
import io.realm.RealmResults;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Api extends AsyncTask<String, Void, JSONObject> {

    public static final String URL_ZIP = "https://nodenicausercontent.herokuapp.com/dist/docs.zip";
    public static final String URL_JSON = "https://nodenicausercontent.herokuapp.com/";

    private static AdapterListener adapterListener;

    public interface AdapterListener {
        void onReady(JSONObject result);
        void onError(int statusCode);
    }

    public static void setAdapterListener(AdapterListener adapterListener) {
        Api.adapterListener = adapterListener;
    }

    protected void onPreExecute() {

    }

    protected JSONObject doInBackground(String... strings) {
        JSONObject jsonObject = null;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(URL_JSON)
                .build();

        Response response;
        try {
            response = client.newCall(request).execute();
            if (!response.isSuccessful()) throw new IOException(String.valueOf(response));

            jsonObject = new JSONObject(response.body().string());
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private void truncateModuleLocal() {
        // instance realm
        Realm realm = Realm.getDefaultInstance();

        // delete old data if exists
        final RealmResults<ModuleLocalModel> moduleLocalModels = realm.where(ModuleLocalModel.class).findAll();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                // Delete all matches
                moduleLocalModels.deleteAllFromRealm();
            }
        });
    }

    protected void onPostExecute(final JSONObject result) {
        if (result != null && result.has("version")) {
            // convert json to class
            final DocsModel docsModel = new Gson().fromJson(result.toString(), DocsModel.class);

            // truncate module local
            truncateModuleLocal();

            for (ModuleModel moduleModel : docsModel.getModules()) {
                /// instance realm
                Realm realm = Realm.getDefaultInstance();

                realm.beginTransaction();
                // instance moduleLocalModel
                ModuleLocalModel moduleLocalModel = realm.createObject(ModuleLocalModel.class);
                // set title
                moduleLocalModel.setTitle(moduleModel.getTitle());
                // set name
                moduleLocalModel.setName(moduleModel.getName());
                realm.commitTransaction();
            }

            DownloadDocs.setAdapterListener(new DownloadDocs.AdapterListener() {
                @Override
                public void onReady(boolean status) {
                    // call onReady
                    if (adapterListener != null) {
                        Remember.putString(Config.NODE_JS_VERSION, docsModel.getVersion());

                        adapterListener.onReady(result);
                    }
                }
            });
            new DownloadDocs().execute();
        } else {
            // call onError
            if (adapterListener != null) {
                adapterListener.onError(500);
            }
        }
    }

}
