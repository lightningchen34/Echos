package com.chen91apps.echos.fragments;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import com.chen91apps.echos.ChannelActivity;
import com.chen91apps.echos.NewPostActivity;
import com.chen91apps.echos.R;
import com.chen91apps.echos.RSSChannelActivity;
import com.chen91apps.echos.channel.ChannelInfo;
import com.chen91apps.echos.channel.RSSChannelInfo;
import com.chen91apps.echos.utils.pairs.ListInfoPair;
import com.chen91apps.echos.utils.tabviews.TabViewHelper;
import com.google.android.material.tabs.TabLayout;

import java.nio.channels.Channel;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PageFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM_TYPE = "param_type";
    private static final String ARG_PARAM_UNDEFINED = "param_undefined";

    // TODO: Rename and change types of parameters
    private String paramType;
    private String paramUndefined;

    private ArrayList<ListInfoPair> listTexts;
    private ArrayList<Fragment> listFrames;
    private FragmentPagerAdapter adapter;

    private ArrayList<Fragment> newsFrames;

    private OnFragmentInteractionListener mListener;

    private List<Integer> indexes;

    private TabLayout tabLayout;
    private HorizontalScrollView scrollView;
    private ViewPager viewpager;
    private TextView tv;

    private int scrollViewPos;

    private Runnable scrollRunnable = new Runnable() {
        public void run() {
            System.out.println("to" + (scrollViewPos - 75));
            scrollView.smoothScrollTo(scrollViewPos - 75, 0);
        }
    };

    public PageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param paramType Parameter 1.
     * @param paramUndefined Parameter 2.
     * @return A new instance of fragment PageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PageFragment newInstance(String paramType, String paramUndefined) {
        PageFragment fragment = new PageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM_TYPE, paramType);
        args.putString(ARG_PARAM_UNDEFINED, paramUndefined);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            paramType = getArguments().getString(ARG_PARAM_TYPE);
            paramUndefined = getArguments().getString(ARG_PARAM_UNDEFINED);
        }

        listTexts = new ArrayList<ListInfoPair>();
        listFrames = new ArrayList<Fragment>();

        if (paramType == getResources().getString(R.string.mainactivaty_tag_news))
        {
            init_news_tabs();
        } else if (paramType == getResources().getString(R.string.mainactivaty_tag_community))
        {
            listTexts.add(new ListInfoPair("社区", "13", ListInfoPair.TYPE_COMMUNITY));
            init_fragments();
        } else if (paramType == getResources().getString(R.string.mainactivaty_tag_rss))
        {
            init_rss_tabs();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        super.onActivityCreated(savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_page, container, false);

        tabLayout = (TabLayout) view.findViewById(R.id.subtablayout);
        scrollView = (HorizontalScrollView) view.findViewById(R.id.page_scrollview);
        viewpager = (ViewPager) view.findViewById(R.id.page_viewpager);
        tv = (TextView) view.findViewById(R.id.channel_manager_label);

        initPages();
        initTabs();
        initChannelManager();

        return view;
    }

    @Override
    public void onStart()
    {
        super.onStart();
    }

    public void initChannelManager()
    {
        if (paramType == getResources().getString(R.string.mainactivaty_tag_news))
        {
            tv.setOnClickListener((View v) -> {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName(this.getContext(), ChannelActivity.class));
                startActivityForResult(intent, 301);
            });
        } else if (paramType == getResources().getString(R.string.mainactivaty_tag_community))
        {
            tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_add, 0, 0, 0);
            tv.setOnClickListener((View v) -> {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName(this.getContext(), NewPostActivity.class));
                startActivityForResult(intent, 302);
            });

        } else if (paramType == getResources().getString(R.string.mainactivaty_tag_rss)) {
            tv.setOnClickListener((View v) -> {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName(this.getContext(), RSSChannelActivity.class));
                startActivityForResult(intent, 303);
            });
        }
    }

    private void init_news_tabs()
    {
        if (newsFrames == null)
        {
            newsFrames = new ArrayList<>();
            for (int i = 0; i < ChannelInfo.size(); ++i)
            {
                newsFrames.add(ListFragment.newInstance(ChannelInfo.getString(i), "", ListInfoPair.TYPE_NEWS));
            }
        }

        listTexts.clear();
        indexes = ChannelInfo.getIndexes();
        System.out.println("init" + indexes.size());
        for (Integer index : indexes)
        {
            listTexts.add(new ListInfoPair(ChannelInfo.getTitle(index), ChannelInfo.getString(index), ListInfoPair.TYPE_NEWS));
        }
        listFrames.clear();
        for (Integer index : indexes)
        {
            listFrames.add(newsFrames.get(index));
        }
    }

    private void init_rss_tabs()
    {
        RSSChannelInfo.load();
        listTexts.clear();
        for (Pair<String, String> pair : RSSChannelInfo.get())
        {
            listTexts.add(new ListInfoPair(pair.first, pair.second, ListInfoPair.TYPE_RSS));
        }
        listFrames.clear();
        for (ListInfoPair info : listTexts)
        {
            listFrames.add(ListFragment.newInstance(info.url, "", info.type));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (paramType == getResources().getString(R.string.mainactivaty_tag_news))
        {
            int position = tabLayout.getSelectedTabPosition();
            init_news_tabs();
            adapter.notifyDataSetChanged();

            if (position > listTexts.size())
            {
                tabLayout.getTabAt(0).select();
            }
        } else if (paramType == getResources().getString(R.string.mainactivaty_tag_rss))
        {
            int position = tabLayout.getSelectedTabPosition();
            init_rss_tabs();
            adapter.notifyDataSetChanged();
            if (position > listTexts.size())
            {
                tabLayout.getTabAt(0).select();
            }
        }
        initTabStyle();
    }

    private void init_fragments()
    {
        listFrames.clear();
        for (ListInfoPair info : listTexts)
        {
            listFrames.add(ListFragment.newInstance(info.url, "", info.type));
        }
    }

    public void initPages()
    {
        adapter = new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return listFrames.get(position);
            }

            @Override
            public int getCount() {
                return listFrames.size();
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                super.destroyItem(container, position, object);
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return listTexts.get(position).title;
            }

            @Override
            public int getItemPosition(@NonNull Object object) {
                return PagerAdapter.POSITION_NONE;
            }

            @Override
            public long getItemId(int position) {
                if (indexes != null)
                {
                    return indexes.get(position);
                } else
                {
                    return listFrames.get(position).hashCode();
                }
                // return super.getItemId(position);
            }
        };
        viewpager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewpager);
    }

    public void initTabStyle()
    {
        for (int i = 0; i < tabLayout.getTabCount(); ++i)
        {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            TabViewHelper.setTextTabActive(tab, listTexts.get(i), tab.isSelected());
        }
    }

    public void initTabs()
    {
        initTabStyle();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int index = tab.getPosition();
                TabViewHelper.setTextTabActive(tab, listTexts.get(index), true);

                ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
                int pos = 0;
                for (int i = 0; i < index; ++i)
                {
                    View temp = vg.getChildAt(i);
                    if (temp != null)
                        pos += temp.getWidth();
                }
                scrollViewPos = pos - 75;

                Handler handler = new Handler();
                handler.post(scrollRunnable);
                System.out.println("scroll" + tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int index = tab.getPosition();
                if (index < listTexts.size())
                    TabViewHelper.setTextTabActive(tab, listTexts.get(index), false);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                onTabSelected(tab);
            }
        });
    }

    @Override
    public void onDestroy() {
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        for (Fragment fragment : listFrames)
        {
            if (fragment.isAdded())
            {
                fragmentTransaction.remove(fragment);
            }
        }
        fragmentTransaction.commitAllowingStateLoss();
        super.onDestroy();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onPageFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onPageFragmentInteraction(Uri uri);
    }
}
