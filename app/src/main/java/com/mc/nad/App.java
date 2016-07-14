package com.mc.nad;

import android.app.Application;

import com.mc.nad.util.Migration;
import com.tumblr.remember.Remember;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.exceptions.RealmMigrationNeededException;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // realm
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
                .name("node.realm")
                .schemaVersion(Config.REALM_SCHEMA_VERSION)
                .deleteRealmIfMigrationNeeded()
                .migration(new Migration())
                .build();

        try {
            Realm.setDefaultConfiguration(realmConfiguration);
        } catch (RealmMigrationNeededException e){
            try {
                Realm.deleteRealm(realmConfiguration);
                //Realm file has been deleted.
                Realm.setDefaultConfiguration(realmConfiguration);
            } catch (Exception ex){
                throw ex;
                //No Realm file to remove.
            }
        }

        // remember
        Remember.init(getApplicationContext(), "com.mc.nad");
    }
}