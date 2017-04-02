package all.newspapers.news;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

import all.newspapers.news.fragments.FavoriteFragment;
import all.newspapers.news.fragments.MagazineFragment;
import all.newspapers.news.fragments.NewsFragment;
import all.newspapers.news.observer.FilterManager;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private RelativeLayout content;
    private NavigationView navigationView;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private MaterialSearchView searchView;
//    private static ActivityName instance;

    private  FilterManager filterManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        content = (RelativeLayout) findViewById(R.id.content);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        filterManager = new FilterManager();
        setupViewPager(viewPager);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank

                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();

      /*  FragmentTransaction fragTransaction = getSupportFragmentManager().beginTransaction();
        //NewsFragment base = new NewsFragment();
        HomeFragment base = new HomeFragment();
        fragTransaction.add(content.getId(), base, "uniqueTag").addToBackStack("uniqueTag");
        fragTransaction.commit();*/
        setSupportActionBar(toolbar);
        tabLayout.setupWithViewPager(viewPager);

    }

    public  FilterManager getFilterManager() {
        return filterManager; // return the observable class
    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private Context context;
        private FilterManager filterManager;
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<Observer> mObserverList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(android.support.v4.app.FragmentManager manager) {
            super(manager);
        }


        public ViewPagerAdapter(Context context, android.support.v4.app.FragmentManager fm,
                                FilterManager filterManager) {
            super(fm);
            this.context = context;
            this.filterManager = filterManager;
        }
        @Override
        public Fragment getItem(int position) {
            filterManager.addObserver( mObserverList.get(position));
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, Observer ob, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
            mObserverList.add(ob);
        }

        public void setTitle(int position, String title) {
            mFragmentTitleList.set(position, title);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        NewsFragment newsFragment = new NewsFragment();
        MagazineFragment magazineFragment = new MagazineFragment();
        FavoriteFragment favoriteFragment = new FavoriteFragment();
        adapter = new ViewPagerAdapter(this, getSupportFragmentManager(), filterManager);
        adapter.addFragment(newsFragment, newsFragment, getResources().getString(R.string.news_paper));
        adapter.addFragment(magazineFragment, magazineFragment, getResources().getString(R.string.magazine));
        adapter.addFragment(favoriteFragment, favoriteFragment, getResources().getString(R.string.fav));
        // adapter.addFragment(new UpcomingFixture(), OnPreferenceManager.getInstance(this).getUpcomingTitle());
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_menu, menu);
        searchView = (MaterialSearchView) findViewById(R.id.search_view);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do some magic
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                filterManager.setQuery(newText);
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
            }
        });

        return true;
    }

    public void aliveFragment(Fragment alive){
        if(alive instanceof NewsFragment){

        }else if(alive instanceof MagazineFragment){

        }else if(alive instanceof FavoriteFragment){

        }
    }

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
               // onSearchRequested(item);
                return true;
            default:
                return false;
        }
    }



    public boolean onSearchRequested(MenuItem item) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
          //  MenuItem mi = item.findItem(R.id.search);
            if(item.isActionViewExpanded()){
                item.collapseActionView();
            } else{
                item.expandActionView();
            }
        } else{
            //onOptionsItemSelected(mMenu.findItem(R.id.search));
        }
        return super.onSearchRequested();
    }

}
