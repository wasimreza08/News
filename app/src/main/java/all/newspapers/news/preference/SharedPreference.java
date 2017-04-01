package all.newspapers.news.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import java.util.ArrayList;

import all.newspapers.news.model.NewsModel;

/**
 * Created by Wasim on 3/13/2017.
 */

public class SharedPreference {
    private SharedPreferences.Editor editor;
    private SharedPreferences settings;
    public static final String PREFS_NAME = "NKDROID_APP";
    public static final String FAVORITES = "Favorite";
    private static SharedPreference sOnPreferenceManager;

    private SharedPreference(Context mContext) {
        settings = PreferenceManager.getDefaultSharedPreferences(mContext);
        editor = settings.edit();
    }

    public static SharedPreference getInstance(Context mContext) {
        if (sOnPreferenceManager == null)
            sOnPreferenceManager = new SharedPreference(mContext);
        return sOnPreferenceManager;
    }
    public void storeFavorites(Context context, ArrayList<NewsModel> favorites) {
// used for store arrayList in json format
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
       // Log.d("dfdfgfdgfgfgf",""+settings);
        editor = settings.edit();
        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(favorites);
        editor.putString(FAVORITES, jsonFavorites);
        editor.commit();
    }
    public boolean checkList(Context context, NewsModel newspaper){
        ArrayList<NewsModel> favorites = loadFavorites(context);
        if(favorites == null){
            return false;
        }
        for (NewsModel item: favorites) {
            if(item.getTitle().equals(newspaper.getTitle())){
                return true;
            }
        }
        return false;
    }
    public ArrayList<NewsModel> loadFavorites(Context context) {
// used for retrieving arraylist from json formatted string
        ArrayList<NewsModel> favorites = new ArrayList<>();
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        if (settings.contains(FAVORITES)) {
            String jsonFavorites = settings.getString(FAVORITES, null);
            Gson gson = new Gson();
            NewsModel[] favoriteItems = gson.fromJson(jsonFavorites,NewsModel[].class);
            for (NewsModel item: favoriteItems) {
                    favorites.add(item);
            }
        }
        return  favorites;
    }
    public void addFavorite(Context context, NewsModel newspaper) {
        ArrayList<NewsModel> favorites = loadFavorites(context);

        if (favorites == null)
            favorites = new ArrayList();
        favorites.add(newspaper);
      //  Log.d("Total bookmarked : ", ""+favorites.size());
        storeFavorites(context, favorites);
    }
    public void removeFavorite(Context context, NewsModel newspaper) {
        ArrayList<NewsModel> favorites = loadFavorites(context);
        //System.out.println("B. Size : "+ favorites.size());
        ArrayList<NewsModel> features = new ArrayList<>();
        for (NewsModel item: favorites) {
            System.out.println(item.getTitle());
            if(item.getTitle().equals(newspaper.getTitle())){

            }else{
                features.add(item);
            }
        }
        if(features != null) {
            storeFavorites(context, features);
        }
       // System.out.println("B. Size : "+ features.size());
    }
    public void clear(Context context){
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.clear();
        editor.commit();
    }
}
