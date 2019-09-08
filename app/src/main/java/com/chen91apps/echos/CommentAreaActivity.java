package com.chen91apps.echos;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.chen91apps.echos.utils.Configure;
import com.chen91apps.echos.utils.articles.Post_Comments;
import com.chen91apps.echos.utils.listitem.ListItemAdapter;
import com.chen91apps.echos.utils.listitem.ListItemInfo;
import com.chen91apps.echos.views.MyListView;

import java.util.LinkedList;

public class CommentAreaActivity extends AppCompatActivity implements MyListView.MyListViewPullListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(Configure.day_or_night ? R.style.Mytheme : R.style.Mytheme_Night);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_area);
        initToolBar();

        MyListView listView = (MyListView)findViewById(R.id.comment_area_listView);
        listView.setPullListener(this);
        data = new LinkedList<>();
        adapter = new ListItemAdapter(data,CommentAreaActivity.this);
        listView.setAdapter(adapter);

        EditText myComments = (EditText)findViewById(R.id.comment_area_mycomment);
        Button submit = (Button)findViewById(R.id.comment_area_submit);



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CommentAreaActivity.this,"提交成功",Toast.LENGTH_LONG).show();
                // TO DO
                String data = myComments.getText().toString();
            }
        });

    }

    public void getCommentsInfo(LinkedList<Post_Comments.DataBean> stream)
    {

    }

    public void toRefreshListView()
    {

    }

    public void toUpdateListView()
    {

    }

    LinkedList<ListItemInfo> data;
    ListItemAdapter adapter;
    private int lastComment_id;
    int postid = 1;

    public void initToolBar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.comment_area_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_goback);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }
}
