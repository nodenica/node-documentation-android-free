package com.mc.nad.util;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;

public class Migration implements RealmMigration {

    @Override
    public void migrate(final DynamicRealm realm, long oldVersion, long newVersion) {
        // Access the Realm schema in order to create, modify or delete classes and their fields.
        //RealmSchema schema = realm.getSchema();

        /************************************************
         // Version 1
         ************************************************/
        // Migrate from version 0 to version 1
        if (oldVersion == 0) {
            oldVersion++;
        }
    }
}