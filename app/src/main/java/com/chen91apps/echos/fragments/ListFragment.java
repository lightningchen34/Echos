package com.chen91apps.echos.fragments;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.chen91apps.echos.R;
import com.chen91apps.echos.ViewActivity;
import com.chen91apps.echos.utils.listitem.DefaultListItemInfo;
import com.chen91apps.echos.utils.listitem.ImageListItemInfo;
import com.chen91apps.echos.utils.listitem.ListItemAdapter;
import com.chen91apps.echos.utils.listitem.ListItemInfo;
import com.chen91apps.echos.utils.listitem.PlainListItemInfo;
import com.chen91apps.echos.views.MyListView;

import java.util.LinkedList;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListFragment extends Fragment implements MyListView.MyListViewPullListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM_URL = "param_url";
    private static final String ARG_PARAM_TYPE = "param_type";

    // TODO: Rename and change types of parameters
    private String param_URL;
    private int param_TYPE;

    private int savedPosition;
    private int savedTop;

    private OnFragmentInteractionListener mListener;

    private ListItemAdapter adapter;
    private MyListView listview;

    public ListFragment() {
        // Required empty public constructor
        savedPosition = 0;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param paramUrl Parameter 1.
     * @return A new instance of fragment ListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListFragment newInstance(String paramUrl, int paramType) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM_URL, paramUrl);
        args.putInt(ARG_PARAM_TYPE, paramType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            param_URL = getArguments().getString(ARG_PARAM_URL);
            param_TYPE = getArguments().getInt(ARG_PARAM_TYPE);
        }

        LinkedList<ListItemInfo> data = new LinkedList<>();

        Random random = new Random();

        for (int i = 0; i < 10; ++i)
            data.add(getinfo(random));

        adapter = new ListItemAdapter(data, getContext());

        savedPosition = savedTop = 0;
        System.out.println("OnCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onActivityCreated(savedInstanceState);
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    String getUrl(Random random)
    {
        return "http://cloud.lightning34.cn/kurumi/" + String.valueOf(random.nextInt(9) + 1) + ".jpg";
    }

    String[] strings = {
            "【催泪MAD】时间女孩时崎狂三",
            "时崎狂三战斗曲——狂三告诉你，牺牲自己为了大局才是正义！【MAD】",
            "【约会大作战/时崎狂三】你不是一人在战斗，你不会永远孤独",
            "【约会大作战】时崎狂三第一季出场合集片段！！",
            "大家好我是练习说骚话，时长两年半的时间女孩时崎狂三。",
            "【时崎狂三/泪燃/悲愿】除了灵力，什么都可以给你哦，士道桑",
            "【时崎狂三】终有一日能和士道再次相遇",
            "【MMD】你看起来真的很美味~时崎狂三",
            "亲，你的时崎狂三到了，请查收",
            "【高甜预警/时崎狂三ⅹ五河士道】有点甜",
            "约会大作战 时崎狂三 三三这么可爱不可能是狐狸精！",
            "为了你轮回204次又何妨！盘点时崎狂三拯救士道的那些片段"

    };

    ListItemInfo getinfo(Random random)
    {
        int type = random.nextInt(3);
        if (type == 0)
        {
            return new PlainListItemInfo(strings[random.nextInt(strings.length)], "更新时间：1970-1-1");
        } else if (type == 1)
        {
            return new DefaultListItemInfo(strings[random.nextInt(strings.length)], "更新时间：2019-8-12", getUrl(random));
        } else
        {
            return new ImageListItemInfo(strings[random.nextInt(strings.length)], getUrl(random), getUrl(random), getUrl(random));
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        listview = (MyListView) getView().findViewById(R.id.mylistview);
        listview.setPullListener(this);

        listview.setAdapter(adapter);

        listview.setOnItemClickListener((AdapterView<?> adapterView, View view, int i, long l) -> {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(view.getContext(), ViewActivity.class));
            startActivity(intent);
        });

        listview.setSelectionFromTop(savedPosition, savedTop);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onListFragmentInteraction(uri);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        savedPosition = listview.getFirstVisiblePosition();
        savedTop = (listview.getChildAt(0)).getTop();
        System.out.println("Pause" + param_URL + " " + savedPosition);
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

    @Override
    public void toRefreshListView() {
        // TODO
    }

    @Override
    public void toUpdateListView() {
        // TODO
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
        void onListFragmentInteraction(Uri uri);
    }
}
