package all.newspapers.news.fragments;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

/**
 * Created by bjit-16 on 3/31/17.
 */

public class MagazineFragment extends NewsBaseFragment {
    @Override
    public Query getQuery(FirebaseDatabase databaseReference) {
        Query recentPostsQuery = databaseReference.getReference("news").orderByChild("type").equalTo("magazine");
        return recentPostsQuery;
    }
}
