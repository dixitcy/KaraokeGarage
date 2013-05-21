package com.dcy.testapp;
//GridView adapter
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.ImageView;
import android.widget.TextView;

public class Exprmnt extends BaseAdapter {
	private List<Item> items = new ArrayList<Item>();
	private LayoutInflater inflater;

	public Exprmnt(Context context) {
		inflater = LayoutInflater.from(context);

		items.add(new Item("Challa", R.drawable.pic_1));
		items.add(new Item("Jab tak hai Jaan", R.drawable.pic_2));
		items.add(new Item("Ashiqui 2", R.drawable.pic_3));
		items.add(new Item("Gimme more", R.drawable.pic_4));
		items.add(new Item("Why dis Kolaveri di?", R.drawable.pic_5));
		items.add(new Item("Tumse Hi", R.drawable.pic_7));
		items.add(new Item("Challa", R.drawable.pic_1));
		items.add(new Item("Jab tak hai Jaan", R.drawable.pic_2));
		items.add(new Item("Ashiqui 2", R.drawable.pic_3));
		items.add(new Item("Gimme more", R.drawable.pic_4));
		items.add(new Item("Why dis Kolaveri di?", R.drawable.pic_5));
		items.add(new Item("Tumse Hi", R.drawable.pic_7));
		items.add(new Item("Challa", R.drawable.pic_1));
		items.add(new Item("Jab tak hai Jaan", R.drawable.pic_2));
		items.add(new Item("Ashiqui 2", R.drawable.pic_3));
		items.add(new Item("Gimme more", R.drawable.pic_4));
		items.add(new Item("Why dis Kolaveri di?", R.drawable.pic_5));
		items.add(new Item("Tumse Hi", R.drawable.pic_7));
	}

	@Override
	public int getCount() {

		return items.size();
	}

	@Override
	public Object getItem(int i) {
		return items.get(i);
	}

	@Override
	public long getItemId(int i) {
		return items.get(i).drawableId;
	}

	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {

		View v = view;
		ImageView picture;
		TextView name;

		if (v == null) {
			v = inflater.inflate(R.layout.squareimages, viewGroup, false);
			v.setTag(R.id.picture, v.findViewById(R.id.picture));
			v.setTag(R.id.text, v.findViewById(R.id.text));
		}

		picture = (ImageView) v.getTag(R.id.picture);

		name = (TextView) v.getTag(R.id.text);

		Item item = (Item) getItem(i);

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
