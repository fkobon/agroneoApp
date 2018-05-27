package com.agroneo.app.discuss;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.agroneo.app.discuss.threads.ThreadsAdaptater;

import java.util.ArrayList;
import java.util.List;

public class SectionsPager extends FragmentPagerAdapter {

    private List<DiscussFragment> fragments = new ArrayList<>();

    public SectionsPager(FragmentManager fm) {
        super(fm);
        DiscussFragment discuss = new DiscussFragment();
        discuss.setUrl("/forum");
        fragments.add(discuss);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragments.get(position).getTitle();
    }

    public static class DiscussFragment extends Fragment {
        private ListView listView;
        private String url;
        private String title;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            listView = new ListView(getContext());
            listView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            listView.setDividerHeight(2);
            listView.setClickable(true);
            load(url);

            return listView;
        }


        public void load(String url) {
            listView.setAdapter(new ThreadsAdaptater(getContext(), listView, url));
        }


        public void setUrl(String url) {
            this.url = url;
        }
    }
}