package org.nuvola.mobile.prixpascher.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
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
import org.nuvola.mobile.prixpascher.dto.OfferVO;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class DealOffersAdapter extends EmptyRecyclerView.Adapter<EmptyRecyclerView.ViewHolder> {
    private Context context;
    private List<OfferVO> offers;
    OnItemClickListener mItemClickListener;

    public DealOffersAdapter(Context context, List<OfferVO> offers){
        this.context=context;
        this.offers = offers;
    }

    @Override
    public DealsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.deals_offers_layout, parent, false);
        DealsViewHolder pvh = new DealsViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(EmptyRecyclerView.ViewHolder holder, int position) {
        DealsViewHolder myHolder = (DealsViewHolder) holder;
        if (myHolder.thumb != null) {
            Utils.MyPicasso.with(context)
                    .load(offers.get(position).getProductAnnonce().getImage())
                    .placeholder(R.drawable.no_photo)
                    .into(myHolder.thumb);
        }

        if (myHolder.title != null) {
            myHolder.title.setText(offers.get(position).getProductAnnonce().getTitle());
        }

        if (myHolder.qty != null) {
            myHolder.qty.setText("En stock : " + offers.get(position).getStock().toString());
        }

        if (myHolder.details != null) {
            final String details = offers.get(position).getInfos();
            if (details != null && details.trim().length() > 5) {
                myHolder.details.setVisibility(View.VISIBLE);
                myHolder.details.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder buidler = new AlertDialog.Builder(context);
                        buidler.setMessage(details);
                        buidler.setPositiveButton(context.getResources().getString(R.string.ok_label),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        AlertDialog dialog = buidler.create();
                        dialog.show();
                    }
                });
            } else {
                myHolder.details.setVisibility(View.INVISIBLE);
            }
        }

        if (offers.get(position).getTargetPrice() != null && myHolder.price != null) {
            double delta = offers.get(position).getProductAnnonce().getPrice() - Double.valueOf(offers.get(position).getTargetPrice());
            Double remise = (delta * 100) / offers.get(position).getProductAnnonce().getPrice();
            if (delta > 1) {
                Animation anim = new AlphaAnimation(0.0f, 1.0f);
                anim.setDuration(400);
                anim.setStartOffset(10);
                anim.setRepeatMode(Animation.REVERSE);
                anim.setRepeatCount(Animation.INFINITE);
                myHolder.price.startAnimation(anim);
                myHolder.price.setText("- " + NumberFormat.getInstance(Locale.FRANCE).format(remise) + " %");
            } else {
                myHolder.price.setTextColor(context.getResources().getColor(R.color.green));
                myHolder.price.setText(NumberFormat.getInstance(Locale.FRANCE)
                        .format(offers.get(position).getTargetPrice()) + " Dhs");
            }
        } else {
            myHolder.price.setText(
                    context.getResources()
                            .getString(R.string.negotiate_label));
        }

        if (myHolder.shop != null) {
            int drawable = getDrawable(context,
                    offers.get(position).getShop().getName().toLowerCase() + "_large");
            if (drawable != 0) {
                myHolder.shop.setImageResource(drawable);
                myHolder.shopText.setVisibility(View.GONE);
                myHolder.shop.setVisibility(View.VISIBLE);
            } else {
                /*Utils.MyPicasso.with(context)
                        .load(context.getResources().getString(R.string.images_thumb_url)
                                + offers.get(position).getShop().getName().toLowerCase() + "_large")
                        .resize(200, 200).centerCrop()
                        .placeholder(R.drawable.ic_small_avatar)
                        .into(myHolder.shop);*/
                if (myHolder.shopText != null) {
                    myHolder.shop.setVisibility(View.GONE);
                    myHolder.shopText.setVisibility(View.VISIBLE);
                    myHolder.shopText.setText("Boutique : " + offers.get(position).getShop().getName());
                }
            }
        }
    }

    public class DealsViewHolder extends EmptyRecyclerView.ViewHolder implements View.OnClickListener {
        CardView cv;
        TextView title;
        TextView price;
        TextView qty;
        TextView shopText;
        ImageView thumb;
        ImageView shop;
        ImageView details;

        DealsViewHolder(View itemView){
            super(itemView);
            cv = itemView.findViewById(R.id.cv);
            title = itemView.findViewById(R.id.title);
            price = itemView.findViewById(R.id.price);
            shopText = itemView.findViewById(R.id.shopText);
            qty = itemView.findViewById(R.id.qty);
            thumb = itemView.findViewById(R.id.thumb);
            details = itemView.findViewById(R.id.details);
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
        return offers.size();
    }
}


