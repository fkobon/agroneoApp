package com.agroneo.app.discuss;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.agroneo.app.discuss.posts.PostsAdaptater;
import com.agroneo.app.discuss.threads.ThreadsAdaptater;
import com.agroneo.app.utils.adapter.ListAdapter;

public class SectionsPager extends FragmentPagerAdapter {

    public SectionsPager(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        //return DiscussFragment.PlaceholderFragment.newInstance(position + 1);
        return new DiscussFragment();
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Test";
    }

    public static class DiscussFragment extends Fragment {
        private ListAdapter adaptater;
        private ListView listView;


        //TODO: pas bon avec un seul fragment, il faut que le back ne ramène pas en haut

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            listView = new ListView(getContext());
            listView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            listView.setDividerHeight(2);
            listView.setClickable(true);
            listView.setTop(10);
            adaptater = new ThreadsAdaptater(getContext(), listView, "/forum");
            listView.setAdapter(adaptater);

            return listView;
        }

        public void load(String url) {

            if (url.matches(".*/([0-9A-Z]+)$")) {
                adaptater = new PostsAdaptater(getContext(), listView, url);
            } else {
                adaptater = new ThreadsAdaptater(getContext(), listView, url);
            }

            listView.setAdapter(adaptater);

        }
    }
}