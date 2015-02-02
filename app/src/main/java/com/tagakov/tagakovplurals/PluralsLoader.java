package com.tagakov.tagakovplurals;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.tagakov.Plural;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Тагаков on 01.02.2015.
 */
public class PluralsLoader extends AsyncTaskLoader<List<Plural>> {
    public PluralsLoader(Context applicationContext) {
        super(applicationContext);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }


    @Override
    public List<Plural> loadInBackground() {
        List<Plural> result = new ArrayList<>();
        try {
            InputStream input = getContext().getAssets().open("input.txt");
            Scanner scanner = new Scanner(input);
            while (scanner.hasNext()) {
                result.add(new Plural(scanner.nextLine()));
            }
        } catch (IOException e) {
            Log.e("TagakovPlurals", "Cant load data from assets!", e);
            return null;
        }
        return result;
    }


}
