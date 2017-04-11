package all.newspapers.news;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.database.Query;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;

import all.newspapers.news.adapter.CommonNewsAdapter;
import all.newspapers.news.model.NewsModel;

/**
 * Created by bjit-16 on 4/11/17.
 */

public class GlobalSearchActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private MaterialSearchView searchView;
    private ArrayList<NewsModel> mNews = new ArrayList<>();
    private CommonNewsAdapter mAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_global_search);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setTitle(getResources().getString(R.string.app_name));
        mRecyclerView = (RecyclerView) findViewById(R.id.newsRecyclerView);


        setSupportActionBar(toolbar);
        mNews = (ArrayList<NewsModel>) getIntent().getSerializableExtra("data");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CommonNewsAdapter(this, mNews);
        mRecyclerView.setAdapter(mAdapter);

        //mAdapter.addItem(mNews);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        searchView = (MaterialSearchView) findViewById(R.id.search_view);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        searchView.showSearch();

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do some magic
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
             //   filterManager.setQuery(newText);
                mAdapter.filterData(newText);
                return true;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
                finish();
            }
        });

        return true;
    }

}
