/*******************************************************************************
 * Copyright 2011-2013 Sergey Tarasevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.dcy.kandg;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dcy.kandg.Constants.Extra;
import com.dcy.kandg.AbsListViewBaseActivity;
import com.dcy.kandg.JSONParser;
import com.dcy.kandg.PlayerViewActivity;
import com.dcy.kandg.R;
import com.dcy.kandg.ImageGridActivity.ImageAdapter;
import com.dcy.kandg.ImageGridActivity.LoadAlbums;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.utils.L;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
public class ImageGridActivity extends AbsListViewBaseActivity {

	String[] imageUrls;
	String[] tagalb;
	String[] tagutube;
	String[] tagtitle;
	String[] taglyric;
	public int k;
	Bitmap bmImg;
	ImageView imageView;

	public String url = "http://karaokegarage.com/songs/allsongs.json/?category=telugu";

	// JSON Node names
	private static final String TAG_SONGS = "songs";
	private static final String TAG_ID = "id";
	private static final String TAG_ALBUM = "album";
	private static final String TAG_TITLE = "title";
	private static final String TAG_SINGER = "singer";
	private static final String TAG_LANG = "language";
	private static final String TAG_LYRICS_URL = "lyrics_url";
	private static final String TAG_UTUBE_ID = "youtube_id";
	private static final String TAG_IMG_URL = "img_url";
	final static String imgloc = "";

	ArrayList<HashMap<String, String>> contactList;

	JSONObject song;
	ListView listview;
	WebView webView;

	String[] imgs = new String[250];
	String[] utubeid = new String[250];
	String[] albumid = new String[250];
	String[] songname = new String[250];
	String[] lyricsid = new String[250];
	Context mContext;

	String[] myStringArray = new String[250];

	private static final String TEST_FILE_NAME = "Universal Image Loader @#&=+-_.,!()~'%20.png";

	DisplayImageOptions options;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_image_grid);

		Intent ikillded = getIntent();
		url = "http://karaokegarage.com/songs/allsongs.json/?category="
				+ ikillded.getStringExtra("cat");
		Log.d("THISSSSSS", url);

		contactList = new ArrayList<HashMap<String, String>>();
		new LoadAlbums().execute();

		File testImageOnSdCard = new File("/mnt/sdcard", TEST_FILE_NAME);
		if (!testImageOnSdCard.exists()) {
			copyTestImageToSdCard(testImageOnSdCard);
		}

		imageUrls = myStringArray;
		tagalb = imgs;
		tagutube = utubeid;
		tagtitle = songname;
		taglyric = lyricsid;

		k = imageUrls.length;
		Log.d("WTF", ">" + imageUrls[2]);

		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.i3l69yvi)

				// .showImageForEmptyUri(R.drawable.ic_empty)
				// .showImageOnFail(R.drawable.ic_error)
				.cacheInMemory().displayer(new RoundedBitmapDisplayer(7))
				.cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565)

				.build();

		listView = (GridView) findViewById(R.id.gridview);
		((GridView) listView).setAdapter(new ImageAdapter());

		listView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// Sending image id to FullScreenActivity
				Intent ikill = new Intent(getBaseContext(),
						PlayerViewActivity.class);
				// passing array index
				ikill.putExtra("id", tagutube[position]);
				ikill.putExtra("songer", tagtitle[position]);
				ikill.putExtra("lyricer", taglyric[position]);

				startActivity(ikill);
			}
		});

	}

	private void copyTestImageToSdCard(final File testImageOnSdCard) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					InputStream is = getAssets().open(TEST_FILE_NAME);
					FileOutputStream fos = new FileOutputStream(
							testImageOnSdCard);
					byte[] buffer = new byte[8192];
					int read;
					try {
						while ((read = is.read(buffer)) != -1) {
							fos.write(buffer, 0, read);
						}
					} finally {
						fos.flush();
						fos.close();
						is.close();
					}
				} catch (IOException e) {
					L.w("Can't copy test image onto SD card");
				}
			}
		}).start();
	}

	class LoadAlbums extends AsyncTask<String, String, String> {

		/*
		 * @Override protected void onPreExecute() { super.onPreExecute();
		 * pDialog = new ProgressDialog(HomeActivity.this);
		 * pDialog.setMessage("Getting Albums ...");
		 * pDialog.setIndeterminate(false); pDialog.setCancelable(false);
		 * pDialog.show(); }
		 */

		@Override
		protected void onPreExecute() {

			super.onPreExecute();

		}

		protected String doInBackground(String... args) {

			List<NameValuePair> params = new ArrayList<NameValuePair>();

			// getting JSON string from URL
			String json2 = JSONParser.makeHttpRequest(url, "GET", params);

			// Check your log cat for JSON response
			Log.d("Albums JSON: ajnfb'ondonbadoignb", "> " + json2);

			try {
				JSONObject first = new JSONObject(json2);

				JSONArray song = first.getJSONArray(TAG_SONGS);

				if (song != null) {
					// looping through All albums
					for (int i = 0; i < song.length(); i++) {
						k = song.length();
						Log.d("LENGTH", ">" + k);
						JSONObject c = song.getJSONObject(i);

						// Storing each json item values in variable
						String id = c.getString(TAG_ID);
						String album = c.getString(TAG_ALBUM);
						String singer = c.getString(TAG_SINGER);
						String youtube_id = c.getString(TAG_UTUBE_ID);
						String img_url = c.getString(TAG_IMG_URL);
						String title = c.getString(TAG_TITLE);
						String lyrics_url = c.getString(TAG_LYRICS_URL);

						myStringArray[i] = "https://s3-ap-southeast-1.amazonaws.com/kgassets/posters/"
								+ img_url;
						Log.d("STRING CHECK", myStringArray[i]);
						imgs[i] = album;

						utubeid[i] = youtube_id;
						albumid[i] = id;
						songname[i] = title;
						lyricsid[i] = lyrics_url;

						// creating new HashMap
						HashMap<String, String> map = new HashMap<String, String>();

						// adding each child node to HashMap key => value
						map.put(TAG_ID, id);
						map.put(TAG_ALBUM, album);
						map.put(TAG_SINGER, singer);
						map.put(TAG_IMG_URL, img_url);
						map.put(TAG_UTUBE_ID, youtube_id);
						map.put(TAG_LYRICS_URL, lyrics_url);
						map.put(TAG_TITLE, title);

						// adding HashList to ArrayList
						contactList.add(map);
						Log.d("check TAGALBUM", album);

					}
				} else {
					Log.d("songs: ", "null");
				}
			}

			catch (JSONException e) {
				e.printStackTrace();
			}

			return null;

		}

		@Override
		protected void onPostExecute(String result) {

			super.onPostExecute(result);
			listView = (GridView) findViewById(R.id.gridview);
			((GridView) listView).setAdapter(new ImageAdapter());

		}

	}

	/*
	 * private void startImagePagerActivity(int position) { Intent intent = new
	 * Intent(this, ImagePagerActivity.class); intent.putExtra(Extra.IMAGES,
	 * imageUrls); intent.putExtra(Extra.IMAGE_POSITION, position);
	 * startActivity(intent); }
	 */

	public class ImageAdapter extends BaseAdapter {

		private class ViewHolder {
			public TextView text;
			public ImageView image;
			public TextView text1;
		}

		@Override
		public int getCount() {
			return 300;
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
			// final ImageView imageView;
			View view = convertView;
			final ViewHolder holder;
			if (convertView == null) {
				LayoutInflater inflater = getLayoutInflater();
				view = inflater.inflate(R.layout.grid_item, parent, false);
				holder = new ViewHolder();
				holder.text1 = (TextView) view.findViewById(R.id.SONG_NAME);
				holder.text = (TextView) view.findViewById(R.id.ALBUM_NAME);
				holder.text1.setText(tagtitle[position]);
				holder.text.setText(tagalb[position]);

				holder.image = (ImageView) view.findViewById(R.id.image);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}

			holder.text1.setText(tagtitle[position]);
			holder.text.setText(tagalb[position]);

			imageLoader
					.displayImage(imageUrls[position], holder.image, options);

			return view;

		}
	}

}