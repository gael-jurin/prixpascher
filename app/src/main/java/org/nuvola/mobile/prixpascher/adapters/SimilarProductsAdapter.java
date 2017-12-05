
package org.nuvola.mobile.prixpascher.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import org.nuvola.mobile.prixpascher.R;
import org.nuvola.mobile.prixpascher.dto.ProductVO;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class SimilarProductsAdapter extends RecyclerView.Adapter<SimilarProductsAdapter.ProductsViewHolder> {
    private Context context;
    private List<ProductVO> products;
    OnItemClickListener mItemClickListener;

    public SimilarProductsAdapter(Context context, List<ProductVO> products){
        this.context=context;
        this.products=products;
    }

    @Override
    public ProductsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.products_similar_layout, parent, false);
        ProductsViewHolder pvh = new ProductsViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(ProductsViewHolder holder, int position) {
        if (holder.thumb != null) {
            Picasso.with(context)
                    .load(products.get(position).getImage())
                    .placeholder(R.drawable.no_photo)
                    .into(holder.thumb);
        }

        if (holder.title != null) {
            holder.title.setText(products.get(position).getTitle());
        }

        if (products.get(position).getPrice() != null
                && !products.get(position).getPrice().toString().equalsIgnoreCase("")) {
            holder.price.setText(
                    NumberFormat.getInstance(Locale.FRANCE).format(products.get(position).getPrice()) + " Dhs");
        } else {
            holder.price.setText(
                    context.getResources()
                    .getString(R.string.negotiate_label));
        }

        if (holder.shop != null) {
            holder.shop.setImageResource(getDrawable(context, products.get(position).getShopName() + "_large"));
        }
    }

    public class ProductsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
           CardView cv;
           TextView title;
           TextView price;
           ImageView thumb;
           ImageView shop;

            ProductsViewHolder(View itemView){
               super(itemView);
               cv = (CardView)itemView.findViewById(R.id.cv);
               title = (TextView)itemView.findViewById(R.id.title);
               price=(TextView)itemView.findViewById(R.id.price);
               thumb = (ImageView)itemView.findViewById(R.id.thumb);
               shop = (ImageView)itemView.findViewById(R.id.shop);
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
        public void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener=mItemClickListener;
    }

    @Override
    public int getItemCount() {
        return products.size();
    }
}


