package all.newspapers.news.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import all.newspapers.news.R;
import all.newspapers.news.adapter.FavoriteAdapter;
import all.newspapers.news.model.NewsModel;
import all.newspapers.news.observer.FilterManager;
import all.newspapers.news.onlclick.RecyclerItemClickListener;
import all.newspapers.news.preference.SharedPreference;

/**
 * Created by ASUS on 4/1/2017.
 */

public class FavoriteFragment extends Fragment implements Observer {

    private RecyclerView mRecycler;
    private ProgressBar mProgressBar;
    private LinearLayoutManager mManager;
    private View rootView;
    private ArrayList<NewsModel> favList = new ArrayList<>();
    private FavoriteAdapter mAdapter;
    SharedPreferences prefs;// = PreferenceManager.getDefaultSharedPreferences(getActivity());

    SharedPreferences.OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
            // listener implementation
          reloadItem();

        }
    };

    private BroadcastReceiver responseReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

          /*  Bundle bundle = intent.getExtras();
            Newspaper newspaper = (Newspaper) bundle.getSerializable("value");*/
           reloadItem();
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.example.broadcast.MY_NOTIFICATION");
        getActivity().registerReceiver(responseReceiver, filter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.news_base_fragment, container, false);
            mRecycler = (RecyclerView) rootView.findViewById(R.id.newsRecyclerView);
            mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBarLoading);
            mProgressBar.setVisibility(View.VISIBLE);
            mManager = new LinearLayoutManager(getActivity());
            //mManager.setReverseLayout(true);
            mManager.setStackFromEnd(false);
            mRecycler.setLayoutManager(mManager);
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        favList = SharedPreference.getInstance(getActivity().getApplicationContext()).loadFavorites(getActivity().getApplicationContext());
        mAdapter = new FavoriteAdapter(getActivity(),  favList);
        mRecycler.setAdapter(mAdapter);
        if (favList == null || favList.isEmpty()) {
            mProgressBar.setVisibility(View.GONE);
            notLoaded().setVisibility(View.VISIBLE);
            notLoaded().setText("You don't select any Favorite item yet");
            return;
        }

        mRecycler.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(view.getId() == R.id.fav_icon){
                    SharedPreference.getInstance(getActivity().getApplicationContext()).removeFavorite(getActivity().getApplicationContext(),favList.get(position));
                    mAdapter.removeItem(position);
                }else{

                }
            }
        }));
        notLoaded().setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
        prefs.registerOnSharedPreferenceChangeListener(listener);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void reloadItem(){
        Log.e("pref", "prefernce chnaged");
        favList.clear();
        favList = SharedPreference.getInstance(getActivity().getApplicationContext()).loadFavorites(getActivity());
        mAdapter.addItem(favList);
        mProgressBar.setVisibility(View.GONE);
        if(favList.isEmpty()){
            notLoaded().setVisibility(View.VISIBLE);
        }else{
            notLoaded().setVisibility(View.GONE);
        }


    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            Log.e("Fragment", "FavoriteFragment visible" +isVisibleToUser);
        }

    }

    public void searchQuery(String query){

    }

    @Override
    public void onDestroy() {
        prefs.unregisterOnSharedPreferenceChangeListener(listener);
        getActivity().unregisterReceiver(responseReceiver);
        super.onDestroy();
    }

    private TextView notLoaded(){
        return (TextView) rootView.findViewById(R.id.not_loaded);
    }

    @Override
    public void update(Observable observable, Object o) {
        if (observable instanceof FilterManager) {
            String query = ((FilterManager) observable).getQuery(); // retrieve the search value
            Log.e("query", query);
            mAdapter.filterData(query);
           /* if(query.isEmpty()){
                mAdapter.addItem(favList);
                return;
            }
            ArrayList<NewsModel> backup = new ArrayList<>();
            backup.addAll(favList);
            favList.clear();
            for(int i = 0; i < backup.size(); i++){
                if(backup.get(i).getTitle().toLowerCase().equalsIgnoreCase(query)){
                    favList.add(backup.get(i));
                }
            }
            mAdapter.addItem(favList);*/
        }
    }

  /*  @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        Log.e("pref", "prefernce chnaged");
        favList.clear();
        favList = SharedPreference.getInstance(getActivity().getApplicationContext()).loadFavorites(getActivity());
        mAdapter.notifyDataSetChanged();
        notLoaded().setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
    }*/
}
