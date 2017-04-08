package all.newspapers.news;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.thefinestartist.finestwebview.ads.GoogleAds;

import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

import all.newspapers.news.fragments.FavoriteFragment;
import all.newspapers.news.fragments.MagazineFragment;
import all.newspapers.news.fragments.NewsFragment;
import all.newspapers.news.observer.FilterManager;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    private DrawerLayout drawerLayout;
    private RelativeLayout content;
    private NavigationView navigationView;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private MaterialSearchView searchView;
//    private static ActivityName instance;

    private  FilterManager filterManager;
    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener;

    private BroadcastReceiver responseReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

          /*  Bundle bundle = intent.getExtras();
            Newspaper newspaper = (Newspaper) bundle.getSerializable("value");*/
            Log.e("pref", "preference changed" );
            int size = adapter.mFragmentList.size();
            for(int i = 0; i < size; i++){
                Fragment item = adapter.mFragmentList.get(i);
                if(item instanceof NewsFragment){
                    ((NewsFragment) item).preferenceChanged();
                } else if(item instanceof MagazineFragment){
                    ((MagazineFragment) item).preferenceChanged();
                }else if(item instanceof FavoriteFragment){
                    ((FavoriteFragment) item).preferenceChanged();
                }
            }
        }
    };


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
        GoogleAds.getGoogleAds(getApplicationContext()).requestNewInterstitial();
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
        TabLayout.Tab tab = tabLayout.getTabAt(1);
        if (tab != null) {
            tab.select();
        }

        preferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
                Log.e("pref", "preference changed" + s);
                int size = adapter.mFragmentList.size();
                for(int i = 0; i < size; i++){
                    Fragment item = adapter.mFragmentList.get(i);
                    if(item instanceof NewsFragment){
                        ((NewsFragment) item).preferenceChanged();
                    } else if(item instanceof MagazineFragment){
                        ((MagazineFragment) item).preferenceChanged();
                    }else if(item instanceof FavoriteFragment){
                        ((FavoriteFragment) item).preferenceChanged();
                    }
                }
            }
        };

        IntentFilter filter = new IntentFilter();
        filter.addAction("com.example.broadcast.MY_NOTIFICATION");
        registerReceiver(responseReceiver, filter);

        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).registerOnSharedPreferenceChangeListener(preferenceChangeListener);

    }




    public  FilterManager getFilterManager() {
        return filterManager; // return the observable class
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        Log.e("pref", "preference changed" + s);
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

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).unregisterOnSharedPreferenceChangeListener(preferenceChangeListener);
        unregisterReceiver(responseReceiver);
        super.onDestroy();
    }

    private void setupViewPager(ViewPager viewPager) {
        NewsFragment newsFragment = new NewsFragment();
        MagazineFragment magazineFragment = new MagazineFragment();
        FavoriteFragment favoriteFragment = new FavoriteFragment();
        adapter = new ViewPagerAdapter(this, getSupportFragmentManager(), filterManager);
        adapter.addFragment(favoriteFragment, favoriteFragment, getResources().getString(R.string.fav));
        adapter.addFragment(newsFragment, newsFragment, getResources().getString(R.string.news_paper));
        adapter.addFragment(magazineFragment, magazineFragment, getResources().getString(R.string.magazine));

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


    private void linkSuggestion(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_text, null);
        alertDialogBuilder.setView(dialogView);

        final EditText editText = (EditText) dialogView.findViewById(R.id.edit_suggestion);

        // set title
        alertDialogBuilder.setTitle("Suggestion");

        // set dialog message
        alertDialogBuilder
                .setMessage("Suggest any link that you think the app should contains.")
                .setCancelable(false)
                .setPositiveButton("Send",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        // current activity
                       // MainActivity.this.finish();
                        sendMail(editText.getText().toString());
                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }



   private void sendMail(String body){
       Log.i("Send email", "");
       String[] TO = {"wasimreza0807021@gmail.com"};
       String[] CC = {""};
       Intent emailIntent = new Intent(Intent.ACTION_SEND);

       emailIntent.setData(Uri.parse("mailto:"));
       emailIntent.setType("text/plain");
       emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
       //emailIntent.putExtra(Intent.EXTRA_CC, CC);
       emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
       emailIntent.putExtra(Intent.EXTRA_TEXT, body);

       try {
           startActivity(Intent.createChooser(emailIntent, "Send mail..."));
           //finish();
           Log.i("Finished  email...", "");
       } catch (android.content.ActivityNotFoundException ex) {
           Toast.makeText(MainActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
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
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);  // OPEN DRAWER
                return true;
            case R.id.action_search:
               // onSearchRequested(item);
                return true;

            case R.id.action_add:
                // onSearchRequested(item);
                //sendMail("test mail");
                linkSuggestion();
                return true;
            default:
                return false;
        }
    }


}
