package olivegumball.bira;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ContactViewHolder> {

    private List<CardInfo> cardList;

    public MyAdapter(List<CardInfo> cardList) {
        this.cardList = cardList;
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    @Override
    public void onBindViewHolder(ContactViewHolder contactViewHolder, int i) {
        CardInfo ci = cardList.get(i);
        contactViewHolder.vTitle.setText(ci.title);
        contactViewHolder.vContent.setText(ci.content);
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
        protected TextView vContent;

        public ContactViewHolder(View v) {
            super(v);
            vTitle =  (TextView) v.findViewById(R.id.card_title);
            vContent = (TextView)  v.findViewById(R.id.card_content);
        }
    }
}