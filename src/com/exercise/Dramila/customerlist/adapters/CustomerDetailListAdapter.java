package com.exercise.Dramila.customerlist.adapters;

import com.exercise.Dramila.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Cashing adapter for business partner views
 * 
 * @author sandesh
 */
public class CustomerDetailListAdapter extends EfficientListAdapter<String> {
	
	/**
	 * @param context
	 *            activity context
	 * @param layout
	 *            layout ID to use for list element views
	 */
	public CustomerDetailListAdapter(Context context, int layout) {
		super(context, layout);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ViewHolder createViewHolder(View view) {
		UserViewHolder holder = new UserViewHolder();
		holder.name = (TextView) view.findViewById(R.id.name);
		return holder;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setViewHolderValues(ViewHolder viewHolder, String item) {
		((UserViewHolder) viewHolder).name.setText(item);
	}

	private class UserViewHolder extends ViewHolder {
		TextView name;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean areAllItemsEnabled() {
		// to disable list item selection
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEnabled(int position) {
		// to disable list item selection
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public View getView(int index, View convertView, ViewGroup parent) {
		View view = super.getView(index, convertView, parent);
		// set different background color for even and odd list items
		view.setBackgroundResource(index % 2 == 0 ? R.color.darker_blue
				: R.color.dark_blue);
		return view;
	}

}
