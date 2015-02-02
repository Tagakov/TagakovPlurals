package com.tagakov.tagakovplurals;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.widget.ArrayAdapter;

import com.tagakov.Plural;

import java.util.List;

/**
 * Created by Тагаков Владимир on 01.02.2015.
 */
public class SimplePagerAdapter extends FragmentStatePagerAdapter implements LoaderManager.LoaderCallbacks<List<Plural>> {

    private static final int NUMBERS_FRAGMENT = 0;
    private static final int PLURAL_FRAGMENT = 1;
    private final int PAGE_COUNT = 3;
    private final LoaderManager loaderManager;
    private final Context ctx;

    private ArrayAdapter<String> numbersAdapter;
    private ArrayAdapter<String> pluralsAdapter;

    public SimplePagerAdapter(FragmentManager fm, FragmentActivity activity) {
        super(fm);
        loaderManager = activity.getSupportLoaderManager();
        ctx = activity.getApplicationContext();
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }


    @Override
    public Fragment getItem(int i) {
        loaderManager.initLoader(1, null, this);
        if (i == NUMBERS_FRAGMENT) {
            ListFragment fragment = new ListFragment();
            fragment.setRetainInstance(true);
            numbersAdapter = new ArrayAdapter<>(ctx, android.R.layout.simple_list_item_1);
            fragment.setListAdapter(numbersAdapter);
            return fragment;
        } else if (i == PLURAL_FRAGMENT) {
            ListFragment fragment = new ListFragment();
            fragment.setRetainInstance(true);
            pluralsAdapter = new ArrayAdapter<>(ctx, android.R.layout.simple_list_item_1);
            fragment.setListAdapter(pluralsAdapter);
            return fragment;
        } else {
            return new ConverterFragment();
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case NUMBERS_FRAGMENT:
                return "Цифры";
            case PLURAL_FRAGMENT:
                return "Числительные";
            default:
                return "Конвертер";
        }
    }

    @Override
    public Loader<List<Plural>> onCreateLoader(int id, Bundle args) {
        return new PluralsLoader(ctx);
    }

    @Override
    public void onLoadFinished(Loader<List<Plural>> loader, List<Plural> data) {
        pluralsAdapter.clear();
        numbersAdapter.clear();
        for (Plural plural : data) {
            numbersAdapter.add(plural.getInitialNumberString());
            pluralsAdapter.add(plural.getNumberAsText());
        }
        pluralsAdapter.notifyDataSetChanged();
        numbersAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<List<Plural>> loader) {

    }
}
