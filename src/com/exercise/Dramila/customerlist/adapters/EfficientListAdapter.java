package com.exercise.Dramila.customerlist.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * List adapter that caches views
 * 
 * @author pfjodorovs
 * 
 * @param <E>
 *            list item type
 */
public abstract class EfficientListAdapter<E> extends BaseAdapter {
	/**
	 * List elements
	 */
	private List<E> data;
	/**
	 * Layout inflater
	 */
	private LayoutInflater inflater;
	/**
	 * Layout ID to use for list element views
	 */
	private int layout;

	/**
	 * Constructor
	 * 
	 * @param context
	 *            activity context
	 * @param layout
	 *            layout ID to use for list element views
	 */
	public EfficientListAdapter(Context context, int layout) {
		this.inflater = LayoutInflater.from(context);
		this.data = new ArrayList<E>();
		this.layout = layout;
	}

	/**
	 * @return data items
	 */
	public List<E> getData() {
		return data;
	}

	/**
	 * Sets data items
	 * 
	 * @param data
	 *            data items
	 */
	public void setData(List<E> data) {
		this.data = data;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return getData().size();
	}

	@Override
	public Object getItem(int index) {
		return getData().get(index);
	}

	@Override
	public long getItemId(int index) {
		return index;
	}

	/**
	 * Creates view holder object instance, fills it with view references
	 * 
	 * @param view
	 *            view to create holder for
	 * @return created view holder
	 */
	public abstract ViewHolder createViewHolder(View view);

	/**
	 * Sets view properties based on data item
	 * 
	 * @param viewHolder
	 *            holds references to views
	 * @param item
	 *            item to get values from
	 */
	public abstract void setViewHolderValues(ViewHolder viewHolder, E item);

	@Override
	public View getView(int index, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = inflater.inflate(layout, null);
			viewHolder = createViewHolder(convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		setViewHolderValues(viewHolder, data.get(index));
		return convertView;
	}

	protected abstract class ViewHolder {
	}
}
