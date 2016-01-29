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

    public CardInfo(String title, String distance, String price, Drawable image){
        this.title = title;
        this.distance = distance;
        this.price = price;
        this.image = image;
    }
}
