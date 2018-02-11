
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
import org.nuvola.mobile.prixpascher.dto.ProductAnnonceVO;
import org.nuvola.mobile.prixpascher.models.AnnounceType;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class DealDevisAdapter extends EmptyRecyclerView.Adapter<EmptyRecyclerView.ViewHolder> {
    private Context context;
    private List<ProductAnnonceVO> annonces;
    OnItemClickListener mItemClickListener;

    public DealDevisAdapter(Context context, List<ProductAnnonceVO> annonces){
        this.context=context;
        this.annonces = annonces;
    }

    @Override
    public DealsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.deals_devis_layout, parent, false);
        DealsViewHolder pvh = new DealsViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(EmptyRecyclerView.ViewHolder holder, int position) {
        DealsViewHolder myHolder = (DealsViewHolder) holder;
        if (myHolder.thumb != null) {
            Utils.MyPicasso.with(context)
                    .load(annonces.get(position).getImage())
                    .placeholder(R.drawable.no_photo)
                    .into(myHolder.thumb);
        }

        if (myHolder.title != null) {
            myHolder.title.setText(annonces.get(position).getTitle());
        }

        if (annonces.get(position).getQty() != null && myHolder.price != null) {
            if (annonces.get(position).getQty() > 1) {
                Animation anim = new AlphaAnimation(0.0f, 1.0f);
                anim.setDuration(400);
                anim.setStartOffset(10);
                anim.setRepeatMode(Animation.REVERSE);
                anim.setRepeatCount(Animation.INFINITE);
                myHolder.qty.startAnimation(anim);
                myHolder.qty.setText("Qté : " + String.valueOf(annonces.get(position).getQty()));
            } else {
                myHolder.qty.setText("Qté : " + String.valueOf(annonces.get(position).getQty()));
            }
        }

        if (myHolder.price != null) {
            myHolder.price.setText("Prix vu : " +
                    NumberFormat.getInstance(Locale.FRANCE).format(annonces.get(position).getPrice()) + " Dhs");
        } else {
            myHolder.price.setText(
                    context.getResources()
                            .getString(R.string.negotiate_label));
        }

        if (myHolder.details != null) {
            final String details = annonces.get(position).getDetail();
            if (annonces.get(position).getType().equals(AnnounceType.OFFER_ASK)
                    && details != null && details.trim().length() > 5) {
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
    }

    public class DealsViewHolder extends EmptyRecyclerView.ViewHolder implements View.OnClickListener {
        CardView cv;
        TextView title;
        TextView price;
        TextView qty;
        ImageView thumb;
        ImageView details;

        DealsViewHolder(View itemView){
            super(itemView);
            cv = itemView.findViewById(R.id.cv);
            title = itemView.findViewById(R.id.title);
            details = itemView.findViewById(R.id.details);
            price = itemView.findViewById(R.id.price);
            qty = itemView.findViewById(R.id.qty);
            thumb = itemView.findViewById(R.id.thumb);
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
        return annonces.size();
    }
}


