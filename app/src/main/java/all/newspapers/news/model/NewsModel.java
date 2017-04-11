package all.newspapers.news.model;

import java.io.Serializable;

/**
 * Created by bjit-16 on 3/31/17.
 */

public class NewsModel implements Serializable {
    private String title;
    private String link;

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    private boolean isFavorite;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String type;
}
