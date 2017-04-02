package all.newspapers.news.observer;

import java.util.Observable;

/**
 * Created by ASUS on 4/2/2017.
 */

public class FilterManager extends Observable {
    private String query;

    public void setQuery(String query) {
        this.query = query;
        setChanged();
        notifyObservers();
    }

    public String getQuery() {
        return query;
    }
}
