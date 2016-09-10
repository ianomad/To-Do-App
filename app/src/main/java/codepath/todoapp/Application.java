/*
 * Created by Iliiazbek Akhmedov on 9/8/2016
 * Copyright (c) 2016.
 */

package codepath.todoapp;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.setDefaultConfiguration(new RealmConfiguration.Builder(this).build());
    }
}
