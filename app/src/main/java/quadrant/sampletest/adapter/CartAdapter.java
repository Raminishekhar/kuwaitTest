package quadrant.sampletest.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import quadrant.sampletest.R;
import quadrant.sampletest.array.CartModel;


/**
 * Created by volive on 8/6/2016.
 */
public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyOfferViewHolder> {

    private List<CartModel> offerList;

    public class MyOfferViewHolder extends RecyclerView.ViewHolder {
        public TextView offerPlace, offer, offerCost, offerTime, offerBook;
        public ImageView offerImage;

        public MyOfferViewHolder(View view) {
            super(view);
            offerPlace = (TextView) view.findViewById(R.id.textview);

        }
    }

    public CartAdapter(List<CartModel> offerList) {
        this.offerList = offerList;
    }

    @Override
    public MyOfferViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_listview, parent, false);

        return new MyOfferViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyOfferViewHolder holder, int position) {
        CartModel offer = offerList.get(position);
        holder.offerPlace.setText(offer.getName());

    }

    @Override
    public int getItemCount() {
        return offerList.size();
    }
}