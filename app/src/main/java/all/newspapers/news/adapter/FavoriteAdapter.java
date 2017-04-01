package all.newspapers.news.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import all.newspapers.news.R;
import all.newspapers.news.model.NewsModel;
import all.newspapers.news.viewholder.NewsViewHolder;

/**
 * Created by ASUS on 4/1/2017.
 */

public class FavoriteAdapter extends RecyclerView.Adapter<NewsViewHolder> {
    private ArrayList<NewsModel> mList;
    private Context mContext;
    public FavoriteAdapter(Context context, ArrayList<NewsModel> list){
        mContext = context;
        mList = list;
    }
    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, null);
        NewsViewHolder viewHolder = new NewsViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        holder.mTextViewTitle.setText(mList.get(position).getTitle());
        holder.favIcon.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.love_icon4));
    }

    public void removeItem(int position){
        mList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mList.size());
    }

    public void addItem(List<NewsModel> newspapers){
       mList.clear();
        mList.addAll(newspapers);
        /*for(NewsModel newspaper: newspapers){
            this.newspapers.add(newspaper);
        }*/
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
