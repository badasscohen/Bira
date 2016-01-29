package olivegumball.bira;

import android.graphics.drawable.Drawable;

/**
 * Created by ariel on 1/28/16.
 */
public class CardInfo {
    protected String title;
    protected String distance;
    protected String price;
    protected Drawable image;
    protected double lat;
    protected double lon;

    public CardInfo(String title, String distance, String price, Drawable image, double lat, double lon){
        this.title = title;
        this.distance = distance;
        this.price = price;
        this.image = image;
        this.lat = lat;
        this.lon = lon;
    }
}
