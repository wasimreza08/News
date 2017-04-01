package all.newspapers.news.viewholder;

import android.content.Context;
import android.content.Intent;
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
import all.newspapers.news.preference.SharedPreference;

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
        if (post.isFavorite()) {
            favIcon.setImageDrawable(context.getResources().getDrawable(R.mipmap.love_icon4));
        } else {
            favIcon.setImageDrawable(context.getResources().getDrawable(R.mipmap.love_icon3));
        }
        favIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (post.isFavorite()) {
                    favIcon.setImageDrawable(context.getResources().getDrawable(R.mipmap.love_icon3));
                    post.setFavorite(false);
                    // favItems.remove(post.getLink());
                    SharedPreference.getInstance(context).removeFavorite(context, post);

                }else{
                    favIcon.setImageDrawable(context.getResources().getDrawable(R.mipmap.love_icon4));
                    post.setFavorite(true);
                    SharedPreference.getInstance(context).addFavorite(context,post);
                }

                sendBroadcast(post, context);

            }
        });
    }
    private void sendBroadcast(NewsModel newspaper, Context context){
        Intent intent = new Intent();
        intent.setAction("com.example.broadcast.MY_NOTIFICATION");
       /* Bundle bundle = new Bundle();
        bundle.putSerializable("value", (Serializable) newspaper);
        intent.putExtras(bundle);*/
        context.sendBroadcast(intent);
    }
}
