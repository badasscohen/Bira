package olivegumball.bira;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import java.util.Locale;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ContactViewHolder> {

    private Typeface alef;

    private List<CardInfo> cardList;

    public MyAdapter(List<CardInfo> cardList, AssetManager manager) {
        this.cardList = cardList;
        alef = Typeface.createFromAsset(manager, "Alef-Regular.ttf");
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    @Override
    public void onBindViewHolder(ContactViewHolder contactViewHolder, int i) {
        final CardInfo ci = cardList.get(i);
        contactViewHolder.vTitle.setText(ci.title);
        contactViewHolder.vTitle.setTypeface(alef);
        contactViewHolder.vDistance.setText(ci.distance);
        contactViewHolder.vDistance.setTypeface(alef);
        contactViewHolder.vPrice.setText(ci.price);
        contactViewHolder.vPrice.setTypeface(alef);
        contactViewHolder.vImage.setImageDrawable(ci.image);
        contactViewHolder.vCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!ci.title.equals("טוען תוצאות...") && !ci.title.equals("אין תוצאות!")) {
                    String uri = String.format(Locale.ENGLISH, "geo:%f,%f?q=%f,%f", ci.lat, ci.lon, ci.lat, ci.lon);
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                    v.getContext().startActivity(intent);
                }
            }
        });
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card, viewGroup, false);

        return new ContactViewHolder(itemView);
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        protected TextView vTitle;
        protected TextView vDistance;
        protected TextView vPrice;
        protected ImageView vImage;
        protected CardView vCard;

        public ContactViewHolder(View v) {
            super(v);
            vTitle =  (TextView) v.findViewById(R.id.card_title);
            vDistance = (TextView)  v.findViewById(R.id.card_distance);
            vPrice = (TextView) v.findViewById(R.id.card_price);
            vImage = (ImageView) v.findViewById(R.id.card_image);
            vCard = (CardView) v.findViewById(R.id.card_view);
        }
    }
}