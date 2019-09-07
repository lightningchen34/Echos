package com.chen91apps.echos;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.chen91apps.echos.utils.Configure;
import com.chen91apps.echos.utils.ScoreManager;
import com.chen91apps.echos.utils.ThemeColors;

import java.util.LinkedList;

public class SearchActivity extends AppCompatActivity {

    private boolean searchable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(Configure.day_or_night ? R.style.Mytheme : R.style.Mytheme_Night);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initToolBar();
        initListView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        searchable = true;
    }

    public void initToolBar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.search_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_goback);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        SearchView searchView = (SearchView) findViewById(R.id.search_view);
        searchView.setIconified(false);
        searchView.onActionViewExpanded();

        SearchView.SearchAutoComplete text = (SearchView.SearchAutoComplete) searchView.findViewById(R.id.search_src_text);
        text.setTextColor(ThemeColors.getColor(this, R.attr.text_color));

        searchView.setQueryHint("输入关键词进行搜索");
        searchable = true;

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                if (searchable) {
                    search(query);
                    System.out.println(query);
                    searchable = false;
                    return true;
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    void initListView()
    {
        final LinkedList<String> list = new LinkedList<>();
        list.addAll(ScoreManager.getList());
        ListView listView = (ListView) findViewById(R.id.list_search);
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // System.out.println(list.get(i));
                search(list.get(i));
            }
        });
    }

    void search(String text)
    {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(this, SearchResultActivity.class));
        intent.putExtra("search_value", text);
        intent.putExtra("search_type", "");
        startActivity(intent);
    }
}
