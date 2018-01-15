
package org.nuvola.mobile.prixpascher.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.nuvola.mobile.prixpascher.R;
import org.nuvola.mobile.prixpascher.business.Utils;
import org.nuvola.mobile.prixpascher.dto.OfferVO;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class AnnonceOffersAdapter extends RecyclerView.Adapter<AnnonceOffersAdapter.OffersViewHolder> {
    private Context context;
    private List<OfferVO> offerVOs;
    OnItemClickListener mItemClickListener;

    public AnnonceOffersAdapter(Context context, List<OfferVO> offerVOs){
        this.context=context;
        this.offerVOs=offerVOs;
    }

    @Override
    public OffersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.offers_item_layout, parent, false);
        OffersViewHolder pvh = new OffersViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(OffersViewHolder holder, int position) {
        if (holder.thumb != null) {
            Utils.MyPicasso.with(context)
                    .load(Utils.buildImageUri(offerVOs.get(position).getProductAnnonce().getImage()))
                    .placeholder(R.drawable.ic_devis)
                    .into(holder.thumb);
        }

        boolean discount = (offerVOs.get(position).getTargetPrice() -
                offerVOs.get(position).getProductAnnonce().getPrice()) < 0;
        holder.promoFlag.setVisibility(discount ? View.VISIBLE : View.INVISIBLE);

        if (holder.merchantAvt != null) {
            holder.merchantAvt.setImageResource(getDrawable(context, offerVOs.get(position)
                    .getShop().getClasse().name().toLowerCase() + "_large"));
        }

        if (holder.title != null) {
            holder.title.setText(offerVOs.get(position).getProductAnnonce().getTitle());
        }

        if (holder.details != null) {
            final String details = offerVOs.get(position).getInfos();
            if (details != null && details.trim().length() > 5) {
                holder.details.setVisibility(View.VISIBLE);
                holder.details.setOnClickListener(new View.OnClickListener() {
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
                holder.details.setVisibility(View.INVISIBLE);
            }
        }

        if (holder.date != null) {
            String date = "Posté " + Utils.formatToYesterdayOrToday(
                    offerVOs.get(position).getPosted());
            holder.date.setText(date);
        }

        if (holder.qty != null && offerVOs.get(position).getStock() != null) {
            holder.qty.setText("Qté : " + String.valueOf(offerVOs.get(position).getStock()));
            holder.qty.setVisibility(View.VISIBLE);
        }

        if (offerVOs.get(position).getTargetPrice() != null
                && !offerVOs.get(position).getTargetPrice().toString().equalsIgnoreCase("")) {
            holder.price.setText("Prix : " +
                    NumberFormat.getInstance(Locale.FRANCE).format(offerVOs.get(position).getTargetPrice()) + " Dhs");
        } else {
            holder.price.setText(context.getResources().getString(R.string.negotiate_label));
        }
    }

    public class OffersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cv;
        TextView date;
        TextView title;
        TextView price;
        TextView qty;
        ImageView details;
        ImageView thumb;
        ImageView merchantAvt;
        ImageView promoFlag;

        OffersViewHolder(View itemView){
            super(itemView);
            cv = itemView.findViewById(R.id.cv);
            date = itemView.findViewById(R.id.date);
            title = itemView.findViewById(R.id.title);
            details = itemView.findViewById(R.id.details);
            price = itemView.findViewById(R.id.price);
            qty = itemView.findViewById(R.id.qty);
            thumb = itemView.findViewById(R.id.thumb);
            merchantAvt = itemView.findViewById(R.id.merchantAvt);
            promoFlag = itemView.findViewById(R.id.flag);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getLayoutPosition());
            }
        }
    }

    private int getDrawable(Context context, String name)
    {
        return context.getResources().getIdentifier(name,
                "drawable", context.getPackageName());
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener=mItemClickListener;
    }

    @Override
    public int getItemCount() {
        return offerVOs.size();
    }
}


