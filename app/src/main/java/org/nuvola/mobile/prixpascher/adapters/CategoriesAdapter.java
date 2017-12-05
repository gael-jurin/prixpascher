package org.nuvola.mobile.prixpascher.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.nuvola.mobile.prixpascher.R;
import org.nuvola.mobile.prixpascher.models.DrawerMenuItem;

import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<RecyclerViewHolders> {

	private List<DrawerMenuItem> itemList;
	private Context context;
	private OnItemClickListener mItemClickListener;

	public interface OnItemClickListener {
        void onItemClick(View view , int position);
    }

	public CategoriesAdapter(Context context, List<DrawerMenuItem> itemList) {
		this.itemList = itemList;
		this.context = context;
	}

	@Override
	public RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
		View layoutView = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.gridlistview_container_layout, null);
		RecyclerViewHolders rcv = new RecyclerViewHolders(layoutView, mItemClickListener);
		return rcv;
	}

	@Override
	public void onBindViewHolder(RecyclerViewHolders holder, int position) {
		holder.categoryName.setText(itemList.get(position).getAvt());
		holder.categoryPhoto.setImageResource(itemList.get(position).getIcon());
	}

	@Override
	public int getItemCount() {
		return this.itemList.size();
	}

	public void SetOnItemClickListener(OnItemClickListener onItemClickListener) {
		this.mItemClickListener = onItemClickListener;
	}
}
