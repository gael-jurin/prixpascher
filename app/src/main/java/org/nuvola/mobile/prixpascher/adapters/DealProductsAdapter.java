
package org.nuvola.mobile.prixpascher.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import org.nuvola.mobile.prixpascher.R;
import org.nuvola.mobile.prixpascher.business.EmptyRecyclerView;
import org.nuvola.mobile.prixpascher.business.Utils;
import org.nuvola.mobile.prixpascher.dto.ProductVO;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class DealProductsAdapter extends EmptyRecyclerView.Adapter<EmptyRecyclerView.ViewHolder> {
    private Context context;
    private List<ProductVO> products;
    OnItemClickListener mItemClickListener;

    public DealProductsAdapter(Context context, List<ProductVO> products){
        this.context=context;
        this.products=products;
    }

    @Override
    public DealsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.deals_products_layout, parent, false);
        DealsViewHolder pvh = new DealsViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(EmptyRecyclerView.ViewHolder holder, int position) {
        DealsViewHolder myHolder = (DealsViewHolder) holder;
        if (myHolder.thumb != null) {
            Utils.MyPicasso.with(context)
                    .load(products.get(position).getImage())
                    .placeholder(R.drawable.no_photo)
                    .into(myHolder.thumb);
        }

        if (myHolder.title != null) {
            myHolder.title.setText(products.get(position).getTitle());
        }

        if (products.get(position).getPrice() != null && myHolder.price != null) {
            if (products.get(position).getPromoted() &&  !products.get(position).getPrices().isEmpty()) {
                Double delta = products.get(position).getPrice() -
                        products.get(position).getPrices().get(0).getPrice();
                if (delta < 1) {
                    Animation anim = new AlphaAnimation(0.0f, 1.0f);
                    anim.setDuration(400);
                    anim.setStartOffset(10);
                    anim.setRepeatMode(Animation.REVERSE);
                    anim.setRepeatCount(Animation.INFINITE);
                    myHolder.price.startAnimation(anim);
                    myHolder.price.setVisibility(View.VISIBLE);
                    myHolder.price.setText(NumberFormat.getInstance(Locale.FRANCE).format(delta) + " Dhs");
                }
            } else {
                Double delta = products.get(position).getPrice();
                if (!products.get(position).getAlerts().isEmpty()) {
                    delta = products.get(position).getAlerts().get(0).getTargetPrice();
                }

                myHolder.price.setTextColor(context.getResources().getColor(R.color.green));
                myHolder.price.setVisibility(View.VISIBLE);
                myHolder.price.setText("Prix discount : " + NumberFormat.getInstance(Locale.FRANCE).format(delta) + " Dhs");
            }
        } else {
            myHolder.price.setText(
                    context.getResources()
                            .getString(R.string.negotiate_label));
        }

        if (myHolder.shop != null) {
            myHolder.shop.setImageResource(getDrawable(context, products.get(position).getShopName() + "_large"));
        }
    }

    public class DealsViewHolder extends EmptyRecyclerView.ViewHolder implements View.OnClickListener {
        CardView cv;
        TextView title;
        TextView price;
        ImageView thumb;
        ImageView shop;

        DealsViewHolder(View itemView){
            super(itemView);
            cv = itemView.findViewById(R.id.cv);
            title = itemView.findViewById(R.id.title);
            price = itemView.findViewById(R.id.price);
            thumb = itemView.findViewById(R.id.thumb);
            shop = itemView.findViewById(R.id.shop);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getPosition());
            }
        }
    }

    private int getDrawable(Context context, String name)
    {
        return context.getResources().getIdentifier(name,
                "drawable", context.getPackageName());
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener=mItemClickListener;
    }

    @Override
    public int getItemCount() {
        return products.size();
    }
}


