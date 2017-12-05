package org.nuvola.mobile.prixpascher.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.nuvola.mobile.prixpascher.R;

public class RecyclerViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView categoryName;
    public ImageView categoryPhoto;

    CategoriesAdapter.OnItemClickListener mItemClickListener;

    public RecyclerViewHolders(View itemView, CategoriesAdapter.OnItemClickListener itemClickListener) {
        super(itemView);
        itemView.setOnClickListener(this);
        categoryName = (TextView)itemView.findViewById(R.id.category_name);
        categoryPhoto = (ImageView)itemView.findViewById(R.id.category_photo);
        mItemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        if (mItemClickListener != null) {
            mItemClickListener.onItemClick(view, getLayoutPosition());
        }
    }
}
