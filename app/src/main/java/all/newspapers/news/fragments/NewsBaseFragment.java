package all.newspapers.news.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Observer;

import all.newspapers.news.R;
import all.newspapers.news.adapter.CommonNewsAdapter;
import all.newspapers.news.model.NewsModel;
import all.newspapers.news.onlclick.RecyclerItemClickListener;
import all.newspapers.news.preference.SharedPreference;
import all.newspapers.news.viewholder.NewsViewHolder;

/**
 * Created by ASUS on 8/28/2016.
 */
public abstract class NewsBaseFragment extends Fragment {
    private DatabaseReference mDatabase;
    private FirebaseDatabase database;
    // [END define_database_reference]

    private FirebaseRecyclerAdapter<NewsModel, NewsViewHolder> mAdapter;
    CommonNewsAdapter adapter;
    ArrayList<NewsModel> mList = new ArrayList<>();
    private RecyclerView mRecycler;
    private ProgressBar mProgressBar;
    private LinearLayoutManager mManager;
    private View rootView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        if (mDatabase == null) {
            database = FirebaseDatabase.getInstance();
            // database.setPersistenceEnabled(true);
            mDatabase = database.getReference();
        }
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.news_base_fragment, container, false);
            mRecycler = (RecyclerView) rootView.findViewById(R.id.newsRecyclerView);
            mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBarLoading);
            mProgressBar.setVisibility(View.VISIBLE);
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Set up Layout Manager, reverse layout
        mManager = new LinearLayoutManager(getActivity());
        //mManager.setReverseLayout(true);
        mManager.setStackFromEnd(false);
        mRecycler.setLayoutManager(mManager);

        // Set up FirebaseRecyclerAdapter with the Query
        Query postsQuery = getQuery(database);
        Log.e("Query", postsQuery+"");
        adapter = new CommonNewsAdapter(getActivity(), mList);
        final ArrayList<NewsModel> fav = SharedPreference.getInstance(getActivity().getApplicationContext()).loadFavorites(getActivity().getApplicationContext());
        postsQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                NewsModel newPost = dataSnapshot.getValue(NewsModel.class);
                //mList.add(newPost);
                newPost.setFavorite(false);
                for (NewsModel news : fav) {
                    if (news.getLink().equals(newPost.getLink())) {
                        newPost.setFavorite(true);
                    }
                }
                adapter.addItem(newPost);
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mAdapter = new FirebaseRecyclerAdapter<NewsModel, NewsViewHolder>(NewsModel.class, R.layout.news_item,
                NewsViewHolder.class, postsQuery) {
            @Override
            protected void populateViewHolder(final NewsViewHolder viewHolder, final NewsModel model, final int position) {
                // Bind Post to ViewHolder, setting OnClickListener for the star button
                mProgressBar.setVisibility(View.GONE);
                model.setFavorite(false);
                for (NewsModel news : fav) {
                    if (news.getLink().equals(model.getLink())) {
                        model.setFavorite(true);
                    }
                }
              /*  if(fav.contains(model)){
                    model.setFavorite(true);
                }else{
                    model.setFavorite(false);
                }*/
                viewHolder.bindToPost(model, getActivity());

            }
        };
        mRecycler.setAdapter(adapter);
    }

    public void preferenceChanged(){
        Log.e("pref", "change on going");
    }


    @Override
    public void onDestroyView() {
        if (rootView.getParent() != null) {
            ((ViewGroup) rootView.getParent()).removeView(rootView);
        }
        super.onDestroyView();
    }

    public abstract Query getQuery(FirebaseDatabase databaseReference);


}
