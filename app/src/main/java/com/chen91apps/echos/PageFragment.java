package com.chen91apps.echos;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.google.android.material.tabs.TabLayout;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


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

    private OnFragmentInteractionListener mListener;

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
            listTexts.add(new ListInfoPair("新闻", "1", ListInfoPair.TYPE_LIST));
            listTexts.add(new ListInfoPair("推荐", "2", ListInfoPair.TYPE_LIST));
            listTexts.add(new ListInfoPair("北京", "3", ListInfoPair.TYPE_LIST));
            listTexts.add(new ListInfoPair("体育", "4", ListInfoPair.TYPE_LIST));
            listTexts.add(new ListInfoPair("娱乐", "5", ListInfoPair.TYPE_LIST));
            listTexts.add(new ListInfoPair("经济", "6", ListInfoPair.TYPE_LIST));
            listTexts.add(new ListInfoPair("科技", "7", ListInfoPair.TYPE_LIST));
            listTexts.add(new ListInfoPair("民生", "8", ListInfoPair.TYPE_LIST));
            listTexts.add(new ListInfoPair("教育", "9", ListInfoPair.TYPE_LIST));
            listTexts.add(new ListInfoPair("政治", "10", ListInfoPair.TYPE_LIST));
        } else if (paramType == getResources().getString(R.string.mainactivaty_tag_community))
        {
            listTexts.add(new ListInfoPair("板块", "11", ListInfoPair.TYPE_MANAGER));
            listTexts.add(new ListInfoPair("关注", "12", ListInfoPair.TYPE_LIST));
            listTexts.add(new ListInfoPair("推荐", "13", ListInfoPair.TYPE_LIST));
        } else if (paramType == getResources().getString(R.string.mainactivaty_tag_rss))
        {
            listTexts.add(new ListInfoPair("推荐", "14", ListInfoPair.TYPE_LIST));
        }

        for (ListInfoPair info : listTexts)
        {
            if (info.type == ListInfoPair.TYPE_LIST)
            {
                listFrames.add(ListFragment.newInstance(info.url));
            } else
            {
                listFrames.add(ListFragment.newInstance(info.url));
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        super.onActivityCreated(savedInstanceState);

        return inflater.inflate(R.layout.fragment_page, container, false);
    }

    @Override
    public void onStart()
    {
        super.onStart();

        System.out.println("Start");

        TabLayout tabLayout = (TabLayout) getView().findViewById(R.id.subtablayout);

        for (ListInfoPair info : listTexts) {
            tabLayout.addTab(tabLayout.newTab().setText(info.title));
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                showPage(tab.getPosition(), false);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // TODO
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // TODO
            }
        });

        showPage(0, true);
    }

    private void showPage(int index, boolean init)
    {
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();

        if (init)
        {
            fragmentTransaction.add(R.id.newslist_framelayout, listFrames.get(index), listFrames.get(index).getTag());
        } else
        {
            fragmentTransaction.replace(R.id.newslist_framelayout, listFrames.get(index), listFrames.get(index).getTag());
        }
        fragmentTransaction.commit();
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
