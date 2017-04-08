
package all.newspapers.news.viewholder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.thefinestartist.finestwebview.FinestWebView;
import com.thefinestartist.finestwebview.ads.GoogleAds;

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

    public void bindToPost(final NewsModel post,
            /* View.OnClickListener starClickListener, */ final Context context) {
        mTextViewTitle.setText(post.getTitle());
        if (post.isFavorite()) {
            favIcon.setImageDrawable(context.getResources().getDrawable(R.mipmap.love_icon4));
        } else {
            favIcon.setImageDrawable(context.getResources().getDrawable(R.mipmap.love_icon3));
        }
        itemCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadWebView(post, context);
            }
        });
        favIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (post.isFavorite()) {
                    favIcon.setImageDrawable(
                            context.getResources().getDrawable(R.mipmap.love_icon3));
                    post.setFavorite(false);
                    // favItems.remove(post.getLink());
                    SharedPreference.getInstance(context).removeFavorite(context, post);

                } else {
                    favIcon.setImageDrawable(
                            context.getResources().getDrawable(R.mipmap.love_icon4));
                    post.setFavorite(true);
                    SharedPreference.getInstance(context).addFavorite(context, post);
                }

                sendBroadcast(post, context);

            }
        });
    }

    public void bindToPostFav(final NewsModel post,
            /* View.OnClickListener starClickListener, */ final Context context) {

        itemCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadWebView(post, context);

            }
        });


    }

    private void loadWebView(NewsModel post, Context context) {
        GoogleAds.getGoogleAds(context).requestNewInterstitial();
        new FinestWebView.Builder(context).theme(R.style.FinestWebViewTheme)
                .titleDefault(post.getTitle())
                .showUrl(false)
                .statusBarColorRes(R.color.bluePrimaryDark)
                .toolbarColorRes(R.color.bluePrimary)
                .titleColorRes(R.color.finestWhite)
                .urlColorRes(R.color.bluePrimaryLight)
                .iconDefaultColorRes(R.color.finestWhite)
                .progressBarColorRes(R.color.finestWhite)
                .stringResCopiedToClipboard(R.string.copied_to_clipboard)
                .stringResCopiedToClipboard(R.string.copied_to_clipboard)
                .stringResCopiedToClipboard(R.string.copied_to_clipboard)
                .showSwipeRefreshLayout(true)
                .swipeRefreshColorRes(R.color.bluePrimaryDark)
                .menuSelector(R.drawable.selector_light_theme)
                .menuTextGravity(Gravity.CENTER)
                .menuTextPaddingRightRes(R.dimen.defaultMenuTextPaddingLeft)
                .dividerHeight(0)
                .gradientDivider(false)
                .setCustomAnimations(R.anim.slide_up, R.anim.hold, R.anim.hold, R.anim.slide_down)
                .show(post.getLink());
    }

    public void sendBroadcast(NewsModel newspaper, Context context) {
        Intent intent = new Intent();
        intent.setAction("com.example.broadcast.MY_NOTIFICATION");
        /*
         * Bundle bundle = new Bundle(); bundle.putSerializable("value", (Serializable) newspaper);
         * intent.putExtras(bundle);
         */
        context.sendBroadcast(intent);
    }
}
