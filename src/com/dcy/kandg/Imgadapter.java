package com.dcy.kandg;


import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Imgadapter extends BaseAdapter {



	private LayoutInflater inflater;
	String[] imageUrls;
	String[] tagalb;
	String[] tagutube;
	String[] tagtitle;
	static ImageLoader imageLoader;
	static DisplayImageOptions options;
	Context mContext;

	public Imgadapter(Context context) {
		mContext = context;
		inflater = LayoutInflater.from(context);
		imageUrls = Constants.IMAGES;

		if (imageLoader == null) {

			imageLoader = ImageLoader.getInstance();

			options = new DisplayImageOptions.Builder()
					.showStubImage(R.drawable.ic_launcher)
					.showImageForEmptyUri(R.drawable.ic_launcher)
					.showImageOnFail(R.drawable.ic_launcher).cacheInMemory()
					.cacheOnDisc().displayer(new RoundedBitmapDisplayer(18))
					.bitmapConfig(Bitmap.Config.RGB_565).build();
			imageLoader.init(ImageLoaderConfiguration.createDefault(mContext));
			
			
			
		}
	}

	private class ViewHolder {
		public TextView text;
		public ImageView image;
		public TextView text1;
	}

	@Override
	public int getCount() {
		return imageUrls.length;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// final Imfinal ImageView imageView;
		View view = convertView;

		final ViewHolder holder;
		if (convertView == null) {
			view = inflater.inflate(R.layout.grid_item, parent, false);
			holder = new ViewHolder();
			holder.image = (ImageView) view.findViewById(R.id.image);

			view.setTag(holder);
		} else {

			holder = (ViewHolder) view.getTag();
		}

		imageLoader.displayImage(imageUrls[position], holder.image, options);

		return view;
	}

}
