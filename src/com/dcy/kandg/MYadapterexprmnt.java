package com.dcy.kandg;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MYadapterexprmnt extends BaseAdapter {

	private List<Item> items = new ArrayList<Item>();
	private LayoutInflater inflater;

	public MYadapterexprmnt(Context context) {
		inflater = LayoutInflater.from(context);

		items.add(new Item("", R.drawable.pic_1));
		items.add(new Item("", R.drawable.pic_2));
		items.add(new Item("", R.drawable.pic_3));
		items.add(new Item("", R.drawable.pic_4));
		items.add(new Item("", R.drawable.pic_5));
		items.add(new Item("", R.drawable.pic_7));
		items.add(new Item("", R.drawable.pic_1));
		items.add(new Item("", R.drawable.pic_2));
		items.add(new Item("", R.drawable.pic_3));
		items.add(new Item("", R.drawable.pic_4));
		items.add(new Item("", R.drawable.pic_5));
		items.add(new Item("", R.drawable.pic_7));
		items.add(new Item("", R.drawable.pic_1));
		items.add(new Item("", R.drawable.pic_2));
		items.add(new Item("", R.drawable.pic_3));
		items.add(new Item("", R.drawable.pic_4));
		items.add(new Item("", R.drawable.pic_5));
		items.add(new Item("", R.drawable.pic_7));
	}

	@Override
	public int getCount() {

		return items.size();
	}

	@Override
	public Object getItem(int j) {
		return items.get(j);
	}

	@Override
	public long getItemId(int j) {
		return items.get(j).drawableId;
	}

	@Override
	public View getView(int j, View view, ViewGroup viewgroup) {

		View v = view;
		ImageView picture;
		TextView name;

		if (v == null) {
			v = inflater.inflate(R.layout.sqrd, viewgroup, false);
			v.setTag(R.id.picture, v.findViewById(R.id.picture));
			v.setTag(R.id.text, v.findViewById(R.id.text));
		}

		picture = (ImageView) v.getTag(R.id.picture);

		name = (TextView) v.getTag(R.id.text);

		Item item = (Item) getItem(j);

		picture.setImageResource(item.drawableId);
		name.setText(item.name);

		return v;
	}

	private class Item {
		final String name;
		final int drawableId;

		Item(String name, int drawableId) {
			this.name = name;
			this.drawableId = drawableId;
		}
	}

}
