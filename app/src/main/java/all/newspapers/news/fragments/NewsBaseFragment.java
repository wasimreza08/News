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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import all.newspapers.news.R;
import all.newspapers.news.model.NewsModel;
import all.newspapers.news.viewholder.NewsViewHolder;

/**
 * Created by ASUS on 8/28/2016.
 */
public abstract class NewsBaseFragment extends Fragment {
    private DatabaseReference mDatabase;
    // [END define_database_reference]

    private FirebaseRecyclerAdapter<NewsModel, NewsViewHolder> mAdapter;
    private RecyclerView mRecycler;
    private ProgressBar mProgressBar;
    private LinearLayoutManager mManager;
    private View rootView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        if (mDatabase == null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
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
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

        // Set up FirebaseRecyclerAdapter with the Query
        Query postsQuery = getQuery(mDatabase);
        Log.e("Query", postsQuery.toString() + " null");
        mAdapter = new FirebaseRecyclerAdapter<NewsModel, NewsViewHolder>(NewsModel.class, R.layout.news_item,
                NewsViewHolder.class, postsQuery) {
            @Override
            protected void populateViewHolder(final NewsViewHolder viewHolder, final NewsModel model, final int position) {
                // Bind Post to ViewHolder, setting OnClickListener for the star button
                mProgressBar.setVisibility(View.GONE);
                viewHolder.bindToPost(model, getActivity());
            }
        };
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    public void onDestroyView() {
        if (rootView.getParent() != null) {
            ((ViewGroup) rootView.getParent()).removeView(rootView);
        }
        super.onDestroyView();
    }

    public abstract Query getQuery(DatabaseReference databaseReference);


}
