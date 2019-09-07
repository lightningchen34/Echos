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
import android.widget.TextView;

import com.chen91apps.echos.MainActivity;
import com.chen91apps.echos.R;
import com.chen91apps.echos.ViewActivity;
import com.chen91apps.echos.utils.articles.News;
import com.chen91apps.echos.utils.articles.Post;
import com.chen91apps.echos.utils.listitem.DefaultListItemInfo;
import com.chen91apps.echos.utils.listitem.ImageListItemInfo;
import com.chen91apps.echos.utils.listitem.ListItemAdapter;
import com.chen91apps.echos.utils.listitem.ListItemInfo;
import com.chen91apps.echos.utils.listitem.PlainListItemInfo;
import com.chen91apps.echos.utils.pairs.ListInfoPair;
import com.chen91apps.echos.utils.retrofit.RetrofitService;
import com.chen91apps.echos.views.MyListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.POWER_SERVICE;
import static com.chen91apps.echos.MainActivity.echosService;


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
    private static final String ARG_PARAM_TEXT = "param_text";
    private static final String ARG_PARAM_TYPE = "param_type";

    // TODO: Rename and change types of parameters
    private String param_URL;
    private String param_Text;
    private int param_TYPE;// = ListInfoPair.TYPE_NEWS;

    private int savedPosition;
    private int savedTop;

    private OnFragmentInteractionListener mListener;

    private LinkedList<News.DataBean> dataBeans;
    private LinkedList<ListItemInfo> data;
    private ListItemAdapter adapter;
    private MyListView listview;
    private String currentTime;
    private String endTime;

    private int lastPost_id;

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
    public static ListFragment newInstance(String paramUrl, String paramText, int paramType) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM_URL, paramUrl);
        args.putString(ARG_PARAM_TEXT, paramText);
        args.putInt(ARG_PARAM_TYPE, paramType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            param_URL = getArguments().getString(ARG_PARAM_URL);
            param_Text = getArguments().getString(ARG_PARAM_TEXT);
            param_TYPE = getArguments().getInt(ARG_PARAM_TYPE);
        }

        data = new LinkedList<>();

        String saved = MainActivity.acache.getAsString("data_cache:" + param_URL);
        if (saved != null)
        {
            Type type = new TypeToken<LinkedList<ListItemInfo>>(){}.getType();
            Gson gson = new Gson();
            data = gson.fromJson(saved, type);
        }
        else
        {
            if(param_TYPE == ListInfoPair.TYPE_NEWS) {
                SimpleDateFormat dp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                currentTime = dp.format(new Date());
                System.out.println(currentTime);
                Call<News> call = MainActivity.newsService.getNews(20, "", currentTime, param_Text, param_URL);
                call.enqueue(new Callback<News>() {
                    @Override
                    public void onResponse(Call<News> call, Response<News> response) {
                        if (response.body().getPageSize() > 0)
                            getinfo(response.body().getData());
                        else
                            System.out.println("没有新闻");
                    }

                    @Override
                    public void onFailure(Call<News> call, Throwable t) {
                        System.out.println("访问失败");
                    }
                });
            }
            else
            {
                Call<Post> call = MainActivity.echosService.getPost(0);
                call.enqueue(new Callback<Post>() {
                    @Override
                    public void onResponse(Call<Post> call, Response<Post> response) {
                        if(response.body().getData().size()>0)
                            getPostInfo(response.body().getData());
                    }

                    @Override
                    public void onFailure(Call<Post> call, Throwable t) {

                    }
                });
            }
        }

        adapter = new ListItemAdapter(data, getContext());

        savedPosition = savedTop = 0;
        System.out.println("OnCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onActivityCreated(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        view.setId(this.hashCode());
        System.out.println("id" + view.getId());
        return view;
    }

    void getinfo(List<News.DataBean> stream)
    {
        String image, video;
        for (int i = 0; i < stream.size(); i++) {
            image = stream.get(i).getImage();
            image = image.substring(1, image.length() - 1);
            if (image.length() == 0)
                data.add(new PlainListItemInfo(stream.get(i).getTitle(), "更新时间：" + stream.get(i).getPublishTime(), stream.get(i)));
            else {
                String imgurl[] = image.split(", ");
                if (imgurl.length <= 2)
                    data.add(new DefaultListItemInfo(stream.get(i).getTitle(), stream.get(i).getPublishTime(), imgurl[0], stream.get(i)));
                else
                    data.add(new ImageListItemInfo(stream.get(i).getTitle(), imgurl[0], imgurl[1], imgurl[2], stream.get(i)));

            }
            endTime = stream.get(stream.size() - 1).getPublishTime();
            adapter.notifyDataSetChanged();
        }
    }

    void getPostInfo(List<Post.DataBean> stream)
    {
        for(int i=0;i<stream.size()&&i<20;i++)
            data.add(new PlainListItemInfo(stream.get(i).getTitle(),stream.get(i).getAuthor()+" 发布于 "+stream.get(i).getCreate_time(), stream.get(i)));
        lastPost_id = stream.get(0).getPost_id()-20;
        adapter.notifyDataSetChanged();
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
            intent.putExtra("type", param_TYPE);
            intent.putExtra("content", new Gson().toJson(data.get(i - 1).getContent()));
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
        if (listview.getChildAt(0) != null)
            savedTop = (listview.getChildAt(0)).getTop();
        else
            savedTop = 0;
        System.out.println("Pause" + param_URL + " " + savedPosition);

        List<ListItemInfo> tmpdata;
        if (data.size() > 20)
        {
            tmpdata = data.subList(0, 19);
        } else
        {
            tmpdata = data;
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
        if(this.param_TYPE == ListInfoPair.TYPE_NEWS) {
            try {
                SimpleDateFormat dp = new SimpleDateFormat("yyyy-MM-dd");
                Date sDate = dp.parse(currentTime);
                Calendar c = Calendar.getInstance();
                c.setTime(sDate);
                c.add(Calendar.DAY_OF_MONTH, 1);
                currentTime = dp.format(c.getTime());
                Call<News> call = MainActivity.newsService.getNews(20, "", currentTime, param_Text, param_URL);
                call.enqueue(new Callback<News>() {
                    @Override
                    public void onResponse(Call<News> call, Response<News> response) {
                        if (response.body().getPageSize() > 0) {
                            data.clear();
                            getinfo(response.body().getData());
                        } else
                            System.out.println("没有新闻");
                        listview.refreshFinish();
                    }

                    @Override
                    public void onFailure(Call<News> call, Throwable t) {
                        System.out.println("访问失败");
                    }
                });

            } catch (ParseException e) {
                System.out.println(e);
            }
        }

        else
        {
            Call<Post> call = MainActivity.echosService.getPost(0);
            call.enqueue(new Callback<Post>() {
                @Override
                public void onResponse(Call<Post> call, Response<Post> response) {
                    if(response.body().getData().size()>0) {
                        data.clear();
                        getPostInfo(response.body().getData());
                    }
                    else
                        System.out.println("没有帖子");
                    listview.refreshFinish();
                }

                @Override
                public void onFailure(Call<Post> call, Throwable t) {
                    System.out.println("加载帖子失败");
                }
            });
        }
    }

    @Override
    public void toUpdateListView()
    {
        if(this.param_TYPE == ListInfoPair.TYPE_NEWS) {
            System.out.println(endTime);
            SimpleDateFormat dp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date sDate = new Date();
            try {
                sDate = dp.parse(endTime);
            } catch (ParseException e) {
            }
            Calendar c = Calendar.getInstance();
            c.setTime(sDate);
            c.add(Calendar.SECOND, -1);
            endTime = dp.format(c.getTime());
            System.out.println(endTime);

            Call<News> call = MainActivity.newsService.getNews(10, "", endTime, param_Text, param_URL);
            call.enqueue(new Callback<News>() {
                @Override
                public void onResponse(Call<News> call, Response<News> response) {
                    if (response.body().getPageSize() > 0) {
                        getinfo(response.body().getData());
                    } else
                        System.out.println("没有新闻");
                    listview.refreshFinish();
                }

                @Override
                public void onFailure(Call<News> call, Throwable t) {
                    System.out.println("访问失败");
                }
            });
        }
        else
        {
            System.out.println(lastPost_id+" this is the lastPostID");
            Call<Post> call = MainActivity.echosService.getPost(lastPost_id);
            call.enqueue(new Callback<Post>() {
                @Override
                public void onResponse(Call<Post> call, Response<Post> response) {
                    TextView tv = listview.findViewById(R.id.footer_textinfo);
                    if(response.body().getData().size()>0) {
                        System.out.println("now in get footer data");
                        getPostInfo(response.body().getData());
                        tv.setText("正在加载新内容");
                    }
                    else
                        tv.setText("所有内容加载完毕");
                    listview.refreshFinish();
                }
                @Override
                public void onFailure(Call<Post> call, Throwable t) {

                }
            });
        }
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
