package all.newspapers.news.fragments;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import all.newspapers.news.MainActivity;
import all.newspapers.news.model.NewsModel;
import all.newspapers.news.observer.FilterManager;
import all.newspapers.news.preference.SharedPreference;

/**
 * Created by bjit-16 on 3/31/17.
 */

public class NewsFragment extends NewsBaseFragment implements Observer {
    @Override
    public Query getQuery(FirebaseDatabase databaseReference) {
        Query recentPostsQuery = databaseReference.getReference("news").orderByChild("type").equalTo("newspaper");
        return recentPostsQuery;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            Log.e("Fragment", "NewsFragment visible");
         //   ((MainActivity) getActivity()).aliveFragment(this);
        }


    }

    public void preferenceChanged(){
        Log.e("pref", "change on going");
        if(getActivity() == null){
            return;
        }
        ArrayList<NewsModel> favList = SharedPreference.getInstance(getActivity().getApplicationContext()).loadFavorites(getActivity());
        adapter.updateItem(favList);
    }

    @Override
    public void update(Observable observable, Object o) {
        if (observable instanceof FilterManager) {
            String result = ((FilterManager) observable).getQuery(); // retrieve the search value
            Log.e("query", result);
            this.adapter.filterData(result);

        }
    }
}
