package all.newspapers.news.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import all.newspapers.news.R;
import all.newspapers.news.model.NewsModel;

/**
 * Created by ASUS on 8/15/2016.
 */
public class NewsViewHolder extends RecyclerView.ViewHolder {
    public ImageView mNewsImage, favIcon;
    public TextView mTextViewTitle, mTextViewDetail;
    private ProgressBar mProgressBar;
    public RelativeLayout itemCard;

    public NewsViewHolder(View itemView) {
        super(itemView);
        mTextViewTitle = (TextView) itemView.findViewById(R.id.newsTitle);
        itemCard = (RelativeLayout) itemView.findViewById(R.id.full_item);
        favIcon = (ImageView) itemView.findViewById(R.id.fav_icon);
        // mProgressBar = (ProgressBar) v.findViewById(R.id.progressBarLoading);
    }

    public void bindToPost(final NewsModel post, /*View.OnClickListener starClickListener,*/ final Context context) {
        mTextViewTitle.setText(post.getTitle());
    }
}
