package com.chen91apps.echos.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chen91apps.echos.R;
import com.chen91apps.echos.utils.listitem.ListItemAdapter;
import com.chen91apps.echos.utils.listitem.ListItemInfo;
import com.chen91apps.echos.views.MyListView;

import java.util.LinkedList;


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

    // TODO: Rename and change types of parameters
    private String param_URL;

    private OnFragmentInteractionListener mListener;

    public ListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param paramUrl Parameter 1.
     * @return A new instance of fragment ListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListFragment newInstance(String paramUrl) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM_URL, paramUrl);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            param_URL = getArguments().getString(ARG_PARAM_URL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onActivityCreated(savedInstanceState);
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        MyListView lv = (MyListView) getView().findViewById(R.id.mylistview);
        lv.setPullListener(this);

        LinkedList<ListItemInfo> data = new LinkedList<>();

        if (param_URL == "4") {
            data.add(new ListItemInfo("世界杯巡礼之菲律宾：球风硬朗的亚洲劲旅", "更新时间：2019-8-12", BitmapFactory.decodeResource(getResources(), R.mipmap.test)));
            data.add(new ListItemInfo("马丁-路德-金日部分赛程出炉", "更新时间：2019-8-12", BitmapFactory.decodeResource(getResources(), R.mipmap.test)));
            data.add(new ListItemInfo("百大球员第56：扎克-拉文，扣篮王的超级火力", "更新时间：2019-8-12", BitmapFactory.decodeResource(getResources(), R.mipmap.test)));
            data.add(new ListItemInfo("[深度]雷霆新赛季可能的四套首发阵容", "更新时间：2019-8-12", BitmapFactory.decodeResource(getResources(), R.mipmap.test)));
            data.add(new ListItemInfo("西决之王！2000年后科比7次分区决赛全部获胜", "更新时间：2019-8-12", BitmapFactory.decodeResource(getResources(), R.mipmap.test)));
            data.add(new ListItemInfo("官方：虎扑招聘中国篮球运营实习生", "更新时间：2019-8-12", BitmapFactory.decodeResource(getResources(), R.mipmap.test)));
            data.add(new ListItemInfo("世界杯巡礼之菲律宾：球风硬朗的亚洲劲旅", "更新时间：2019-8-12", BitmapFactory.decodeResource(getResources(), R.mipmap.test)));
            data.add(new ListItemInfo("马丁-路德-金日部分赛程出炉", "更新时间：2019-8-12", BitmapFactory.decodeResource(getResources(), R.mipmap.test)));
            data.add(new ListItemInfo("百大球员第56：扎克-拉文，扣篮王的超级火力", "更新时间：2019-8-12", BitmapFactory.decodeResource(getResources(), R.mipmap.test)));
            data.add(new ListItemInfo("[深度]雷霆新赛季可能的四套首发阵容", "更新时间：2019-8-12", BitmapFactory.decodeResource(getResources(), R.mipmap.test)));
            data.add(new ListItemInfo("西决之王！2000年后科比7次分区决赛全部获胜", "更新时间：2019-8-12", BitmapFactory.decodeResource(getResources(), R.mipmap.test)));
            data.add(new ListItemInfo("官方：虎扑招聘中国篮球运营实习生", "更新时间：2019-8-12", BitmapFactory.decodeResource(getResources(), R.mipmap.test)));
        } else
        {
            data.add(new ListItemInfo("暂无", "更新时间：1970-1-1", BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher)));
        }

        ListItemAdapter adapter = new ListItemAdapter(data, getContext());
        lv.setAdapter(adapter);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onListFragmentInteraction(uri);
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
