package com.dcy.testapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.ImageView;

public class Myadapter extends BaseAdapter {
	Context mContext;
	View grid;

	public Integer[] mThumbIds = { R.drawable.rsz_2pic_3,
			R.drawable.rsz_2pic_3, R.drawable.rsz_2pic_3,
			R.drawable.rsz_2pic_3, R.drawable.rsz_2pic_3,
			R.drawable.rsz_2pic_3, R.drawable.rsz_2pic_3,
			R.drawable.rsz_2pic_3, R.drawable.rsz_2pic_3,
			R.drawable.rsz_2pic_3, R.drawable.pic_2, R.drawable.pic_2,
			R.drawable.pic_2, R.drawable.pic_2, R.drawable.pic_2,
			R.drawable.pic_2, R.drawable.pic_2, R.drawable.pic_2,
			R.drawable.pic_2, R.drawable.pic_2, R.drawable.pic_2,
			R.drawable.pic_2, R.drawable.pic_2, R.drawable.pic_2,
			R.drawable.pic_2, R.drawable.pic_2, R.drawable.pic_2,
			R.drawable.pic_2,

	};
	public Myadapter(Context c) {
		mContext = c;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			grid = new View(mContext);
			// LayoutInflater inflater=getLayoutInflater();
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			grid = inflater.inflate(R.layout.mygrid_layout, parent, false);
		} else {
			grid = (View) convertView;
		}

		ImageView imageView = (ImageView) grid.findViewById(R.id.image);

		imageView.setImageResource(mThumbIds[position]);

		return grid;

	}

}
