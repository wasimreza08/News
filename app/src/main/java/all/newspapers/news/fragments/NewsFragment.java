package all.newspapers.news.fragments;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

/**
 * Created by bjit-16 on 3/31/17.
 */

public class NewsFragment extends NewsBaseFragment {
    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        Query recentPostsQuery = databaseReference.child("news");
        return recentPostsQuery;
    }
}
