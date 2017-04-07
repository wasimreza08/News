package all.newspapers.news.app;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.database.FirebaseDatabase;

import all.newspapers.news.R;

/**
 * Created by ASUS on 4/7/2017.
 */

public class OnApplication extends android.app.Application  {
    @Override
    public void onCreate() {
        super.onCreate();
    /* Enable disk persistence  */
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }


}