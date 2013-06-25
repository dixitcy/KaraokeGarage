package com.dcy.kandg;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class Mahadaps extends BaseAdapter {
	private Context mContext;
	Bitmap bmImg;
	public ImageView imageView;

	public String[] mThumbIds = {
			"http://cdn.i.haymarket.net.au/Utils/ImageResizer.ashx?n=http%3A%2F%2Fi.haymarket.net.au%2FGalleries%2F20130524022727_dentsu_newoffice_00010.jpg&h=50&w=50&c=1",
			"http://cdn.i.haymarket.net.au/Utils/ImageResizer.ashx?n=http%3A%2F%2Fi.haymarket.net.au%2FGalleries%2F20130524022727_dentsu_newoffice_00010.jpg&h=50&w=50&c=1",
			"http://cdn.i.haymarket.net.au/Utils/ImageResizer.ashx?n=http%3A%2F%2Fi.haymarket.net.au%2FGalleries%2F20130524022727_dentsu_newoffice_00010.jpg&h=50&w=50&c=1",
			"http://cdn.i.haymarket.net.au/Utils/ImageResizer.ashx?n=http%3A%2F%2Fi.haymarket.net.au%2FGalleries%2F20130524022727_dentsu_newoffice_00010.jpg&h=50&w=50&c=1"

	};

	// Constructor
	public Mahadaps(Context c) {
		mContext = c;
	}

	@Override
	public int getCount() {
		return mThumbIds.length;
	}

	@Override
	public Object getItem(int position) {
		return mThumbIds[position];
	}

	@Override
	public long getItemId(int position) {

		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		imageView = new ImageView(mContext);
		downloadFile(imageView, mThumbIds[position]);
		imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		imageView.setLayoutParams(new GridView.LayoutParams(135, 135));
		imageView.setPadding(0, 0, 1, 0);
		return imageView;

	}

	public void downloadFile(final ImageView imageView, String fileUrl) {

		AsyncTask<String, Object, String> task = new AsyncTask<String, Object, String>() {

			@Override
			protected String doInBackground(String... Object) {
				URL myFileUrl = null;

				try {
					myFileUrl = new URL(Object[0]);
				} catch (MalformedURLException e) {

					e.printStackTrace();
				}
				try {
					HttpURLConnection conn = (HttpURLConnection) myFileUrl
							.openConnection();
					conn.setDoInput(true);
					conn.connect();
					InputStream is = conn.getInputStream();

					bmImg = BitmapFactory.decodeStream(is);
					Log.d("check bitmap", ">" + bmImg);

				} catch (IOException e) {

					e.printStackTrace();
				}

				return null;
			}

			protected void onPostExecute(String unused) {
				imageView.setImageBitmap(bmImg);
			}
		};
		task.execute(fileUrl);
	}
}
