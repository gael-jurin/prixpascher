
package org.nuvola.mobile.prixpascher.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import org.nuvola.mobile.prixpascher.R;
import org.nuvola.mobile.prixpascher.models.Products;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductsViewHolder> {
    private Context context;
    private ArrayList<Products> products;
    OnItemClickListener mItemClickListener;

    public ProductsAdapter(Context context, ArrayList<Products> products){
        this.context=context;
        this.products=products;
    }

    @Override
    public ProductsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.products_list_item_layout, parent, false);
        ProductsViewHolder pvh = new ProductsViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(ProductsViewHolder holder, int position) {
      Ion.with(
              context,
              products.get(position).getImage()).withBitmap()
              .resize(250, 250)
              .centerCrop().error(R.drawable.no_photo)
              .placeholder(R.drawable.no_photo).intoImageView(holder.thumb);

        if (holder.title != null) {
            holder.title.setText(" " + products.get(position).getTitle());
        }

        if (products.get(position).getPrice() != null
                && !products.get(position).getPrice().equalsIgnoreCase("")) {
            holder.price.setText(products.get(position).getPrice() + " Dhs");
        } else {
            holder.price.setText(
                    context.getResources()
                    .getString(R.string.negotiate_label));
        };

        if (holder.shop != null) {
            holder.shop.setImageResource(getDrawable(context, products.get(position).getShopName()));
        }
    }

    public class ProductsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
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

    public int getDrawable(Context context, String name)
    {
        return context.getResources().getIdentifier(name,
                "drawable", context.getPackageName());
    }

    public interface OnItemClickListener {
        public void onItemClick(View view , int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener=mItemClickListener;
    }

    @Override
    public int getItemCount() {
        return products.size();
    }
}


