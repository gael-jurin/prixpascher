package org.nuvola.mobile.prixpascher.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class AnnouncesAdapter extends EmptyRecyclerView.Adapter<EmptyRecyclerView.ViewHolder> {
    private Context context;
    private List<ProductAnnonceVO> annonceVOs;
    OnItemClickListener mItemClickListener;

    public AnnouncesAdapter(Context context, List<ProductAnnonceVO> annonceVOs) {
        this.context = context;
        this.annonceVOs = annonceVOs;
    }

    @Override
    public AnnouncesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.announces_list_item_layout, parent, false);
        AnnouncesViewHolder pvh = new AnnouncesViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(EmptyRecyclerView.ViewHolder myHolder, int position) {
        AnnouncesViewHolder holder = (AnnouncesViewHolder) myHolder;
        if (holder.thumb != null) {
            Utils.MyPicasso.with(context)
                    .load(Utils.buildImageUri(annonceVOs.get(position).getImage()))
                    .placeholder(R.drawable.no_photo)
                    .into(holder.thumb);
        }

        if (holder.thumb != null && annonceVOs.get(position).getType().equals(AnnounceType.OFFER_ASK)) {
            Utils.MyPicasso.with(context)
                    .load(Utils.buildImageUri(annonceVOs.get(position).getImage()))
                    .placeholder(R.drawable.ic_devis)
                    .into(holder.thumb);
        }

        if (holder.merchantAvt != null) {
            if (!annonceVOs.get(position).getType().equals(AnnounceType.OFFER_ASK)) {
                Utils.MyPicasso.with(context)
                        .load(annonceVOs.get(position).getUser().getPhoto())
                        .resize(90, 90).centerCrop()
                        .placeholder(R.drawable.ic_avatar)
                        .into(holder.merchantAvt);
                holder.merchantAvt.setVisibility(View.VISIBLE);
            } else {
                holder.merchantAvt.setVisibility(View.INVISIBLE);
            }
        }

        if (holder.title != null) {
            holder.title.setText(annonceVOs.get(position).getTitle());
        }

        if (holder.details != null) {
            final String details = annonceVOs.get(position).getDetail();
            if (annonceVOs.get(position).getType().equals(AnnounceType.OFFER_ASK)
                    && details != null && details.trim().length() > 5) {
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
                    annonceVOs.get(position).getTrackingDate());
            holder.date.setText(date);
        }

        if (holder.qty != null && annonceVOs.get(position).getQty() != null) {
            holder.qty.setText("Qté : " + String.valueOf(annonceVOs.get(position).getQty()));
            holder.qty.setVisibility(View.VISIBLE);
        } else {
            holder.qty.setVisibility(View.GONE);
        }

        if (annonceVOs.get(position).getPrice() != null
                && !annonceVOs.get(position).getPrice().toString().equalsIgnoreCase("")) {
            if (annonceVOs.get(position).getType().equals(AnnounceType.OFFER_ASK)) {
                holder.price.setText("Prix vu : " +
                        NumberFormat.getInstance(Locale.FRANCE).format(annonceVOs.get(position).getPrice()) + " Dhs");
            } else {
                holder.price.setText(
                        NumberFormat.getInstance(Locale.FRANCE).format(annonceVOs.get(position).getPrice()) + " Dhs");
            }
        } else {
            holder.price.setText(
                    context.getResources()
                    .getString(R.string.negotiate_label));
        }
    }

    public class AnnouncesViewHolder extends EmptyRecyclerView.ViewHolder implements View.OnClickListener {
        CardView cv;
        TextView date;
        TextView title;
        TextView price;
        TextView qty;
        ImageView details;
        ImageView thumb;
        ImageView merchantAvt;

            AnnouncesViewHolder(View itemView){
                super(itemView);
                cv = itemView.findViewById(R.id.cv);
                date = itemView.findViewById(R.id.date);
                title = itemView.findViewById(R.id.title);
                details = itemView.findViewById(R.id.details);
                price = itemView.findViewById(R.id.price);
                qty = itemView.findViewById(R.id.qty);
                thumb = itemView.findViewById(R.id.thumb);
                merchantAvt = itemView.findViewById(R.id.merchantAvt);
                itemView.setOnClickListener(this);
           }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getLayoutPosition());
            }
        }
    }

    public void clear() {
        annonceVOs.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<ProductAnnonceVO> list) {
        annonceVOs.addAll(list);
        notifyDataSetChanged();
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
        return annonceVOs.size();
    }
}


