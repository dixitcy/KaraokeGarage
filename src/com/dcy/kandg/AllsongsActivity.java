package com.dcy.kandg;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.L;
import com.nostra13.universalimageloader.utils.StorageUtils;

////////////////////////////////     ALLSONGS ACTIVITY   ////////////////////

public class AllsongsActivity extends FragmentActivity implements
		ActionBar.TabListener {

	// //////////////////////////////// GLOBAL DECLARATIONS ////////////////////

	static ImageLoader imageLoader;
	static DisplayImageOptions options;

	public static int k;

	ImageView imageView;

	public static String url = "http://karaokegarage.com/songs/allsongs.json/?category=new";

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
	TabHost tab_host;

	static ArrayList<HashMap<String, String>> contactList;

	JSONObject song;
	ListView listview;
	private ViewSwitcher switcher;
	static String[] imgs = new String[250];
	static String[] utubeid = new String[250];
	static String[] albumid = new String[250];
	static String[] songname = new String[250];
	static String[] lyricsid = new String[250];
	Context mContext;
	int i, j;

	static String[] myStringArray = new String[250];

	private static final String TEST_FILE_NAME = "Universal Image Loader @#&=+-_.,!()~'%20.png";

	SectionsPagerAdapter mSectionsPagerAdapter;

	ViewPager mViewPager;

	// ///////////////////// ON CREATE /////////////////////////

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		File testImageOnSdCard = new File("/mnt/sdcard", TEST_FILE_NAME);
		if (!testImageOnSdCard.exists()) {
			copyTestImageToSdCard(testImageOnSdCard);
		}

		contactList = new ArrayList<HashMap<String, String>>();

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);

		actionBar.getDisplayOptions();

		// Set up the ViewPager with the sections adapter.

		mViewPager = new ViewPager(this);
		mViewPager.setId(R.id.pager);

		setContentView(mViewPager);

		mViewPager.setAdapter(mSectionsPagerAdapter);

		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						// When swiping between different app sections, select
						// the corresponding tab.
						// We can also use ActionBar.Tab#select() to do this if
						// we have a reference to the
						// Tab.
						actionBar.setSelectedNavigationItem(position);

					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < 3; i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter.
			// Also specify this Activity object, which implements the
			// TabListener interface, as the
			// listener for when this tab is selected.
			actionBar.addTab(actionBar.newTab()

			.setText(mSectionsPagerAdapter.getPageTitle(i))

			.setTabListener(this));

		}

	}

	// ///////////////////// ON CREATE END /////////////////////////

	// ///////////////////////// LOAD ALBUMS /////////////////////

	// //////////////////////////////// LOAD ALBUMS END
	// /////////////////////////////////

	// ////////////////////////////////////// COPYING IMAGES TO SD CARD
	// //////////////////////////////////

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

	// /////////////////////////////////////// COPYING IMAGES TO SD CARD END
	// /////////////////////////////////

	// ////////// JSON PARSER //////////////

	public static class JSONParser {
		static InputStream is = null;
		static JSONObject jObj = null;
		static String json = "";

		// constructor
		public JSONParser() {

		}

		// function get json from url
		// by making HTTP POST or GET mehtod
		public static String makeHttpRequest(String url, String method,
				List<NameValuePair> params) {

			// Making HTTP request
			try {

				// check for request method
				if (method == "POST") {
					// request method is POST
					// defaultHttpClient
					DefaultHttpClient httpClient = new DefaultHttpClient();

					HttpPost httpPost = new HttpPost(url);
					// Log.d("URL",url);
					httpPost.setEntity(new UrlEncodedFormEntity(params));

					HttpResponse httpResponse = httpClient.execute(httpPost);
					HttpEntity httpEntity = httpResponse.getEntity();
					is = httpEntity.getContent();

				} else if (method == "GET") {
					// request method is GET
					DefaultHttpClient httpClient = new DefaultHttpClient();
					String paramString = URLEncodedUtils.format(params,
							"iso-8859-1");
					Log.d("ParamString", paramString);

					Log.d("URL", url);
					HttpGet httpGet = new HttpGet(url);

					HttpResponse httpResponse = httpClient.execute(httpGet);
					HttpEntity httpEntity = httpResponse.getEntity();
					is = httpEntity.getContent();
				}

			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is, "iso-8859-1"), 8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				is.close();
				json = sb.toString();
			} catch (Exception e) {
				Log.e("Buffer Error", "Error converting result " + e.toString());
			}

			// return JSON String
			return json;

		}
	}

	// /////////////////////////////////////// JSON PARSER END
	// //////////////////////////////////////////////////

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {

	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		mViewPager.setCurrentItem(tab.getPosition());

	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {

	}

	public class SectionsPagerAdapter extends FragmentPagerAdapter {
		final int NUM_ITEMS = 3; // number of tabs

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int i) {

			Fragment fragment = new TabFragment();
			Bundle args = new Bundle();
			args.putStringArray("chaching", myStringArray);
			args.putStringArray("bling", songname);
			args.putStringArray("chinger", lyricsid);
			args.putStringArray("youtube_id", utubeid);
			args.putInt("object", i);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public float getPageWidth(int position) {

			return super.getPageWidth(position);
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return NUM_ITEMS;
		}

		@Override
		public CharSequence getPageTitle(int position) {

			String tabLabel = null;

			switch (position) {
			case 0:
				tabLabel = getString(R.string.label1);

				break;
			case 1:
				tabLabel = getString(R.string.label2);
				break;
			case 2:
				tabLabel = getString(R.string.label3);
				break;
			}

			return tabLabel;
		}
	}

	// ////////////////////////// TAB FRAGMENT
	// //////////////////////////////////

	public static class TabFragment extends Fragment {

		String[] imageUrls;
		String[] tagalb;
		String[] tagutube;
		String[] tagtitle;
		String[] taglyric;
		String[] tagcategory = { "telugu", "tamil", "kannada", "new",
				"english", "hindi" };
		String Kgtit;
		int tabLayout;
		int positions;

		public static final String ARG_OBJECT = "object";

		public class ImageAdapter extends BaseAdapter {
			private Context mContext;

			public ImageAdapter(Context c) {
				mContext = c;
			}

			public int getCount() {
				return mThumbIds.length;
			}

			public Object getItem(int position) {
				return null;
			}

			public long getItemId(int position) {
				return 0;
			}

			// create a new ImageView for each item referenced by the Adapter
			public View getView(int position, View convertView, ViewGroup parent) {
				ImageView imageView;
				if (convertView == null) { // if it's not recycled, initialize
											// some attributes
					imageView = new ImageView(mContext);
					imageView.setLayoutParams(new GridView.LayoutParams(450,
							300));

					imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
					imageView.setPadding(0, 0, 0, 0);
				} else {
					imageView = (ImageView) convertView;
				}

				imageView.setImageResource(mThumbIds[position]);
				return imageView;
			}

			// references to our images
			private Integer[] mThumbIds = { R.drawable.ic_launcher,
					R.drawable.ic_launcher, R.drawable.ic_launcher,
					R.drawable.ic_launcher, R.drawable.ic_launcher,
					R.drawable.ic_launcher

			};
		}

		public class Imgadapter extends BaseAdapter {

			private LayoutInflater inflater;

			ImageLoader imageLoader;
			DisplayImageOptions options;
			Context mContext;

			public Imgadapter(Context context) {
				mContext = context;
				inflater = LayoutInflater.from(context);

				Bundle args = getArguments();
				if (args != null) {

					tagtitle = args.getStringArray("bling");

					taglyric = args.getStringArray("chinger");

					imageUrls = args.getStringArray("chaching");

				}

				if (imageLoader == null) {

					File cacheDir = StorageUtils.getOwnCacheDirectory(
							getActivity(), "/mnt/sdcard");

					// Get singletone instance of ImageLoader
					imageLoader = ImageLoader.getInstance();
					// Create configuration for ImageLoader (all options are
					// optional)
					ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
							context)
							// You can pass your own memory cache implementation
							.discCache(new UnlimitedDiscCache(cacheDir))
							// You can pass your own disc cache implementation
							.discCacheFileNameGenerator(
									new HashCodeFileNameGenerator())
							.enableLogging().build();
					imageLoader.init(config);

					options = new DisplayImageOptions.Builder()
							.showStubImage(R.drawable.i3l69yvi)
							.showImageForEmptyUri(R.drawable.i3l69yvi)
							.showImageOnFail(R.drawable.i3l69yvi)
							.cacheInMemory().cacheOnDisc()
							// .displayer(new RoundedBitmapDisplayer(10))
							.bitmapConfig(Bitmap.Config.RGB_565).build();
					// imageLoader.init(ImageLoaderConfiguration
					// .createDefault(mContext));

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

				View view = convertView;

				final ViewHolder holder;
				if (view == null) {
					view = inflater.inflate(R.layout.grid_item, parent, false);
					holder = new ViewHolder();
					holder.text1 = (TextView) view.findViewById(R.id.SONG_NAME);

					holder.image = (ImageView) view.findViewById(R.id.image);
					holder.text1.setText(tagtitle[position]);

					view.setTag(holder);
				} else {

					holder = (ViewHolder) view.getTag();
					holder.text1.setText(tagtitle[position]);
				}

				holder.text1.setText(tagtitle[position]);
				if (imageUrls[position] != null) {

					imageLoader.displayImage(imageUrls[position], holder.image,
							options);
					Log.d("Check this out", ">" + imageUrls[5]);
				}
				Log.d("And this", "?" + tagtitle[5]);

				return view;
			}

		}

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {

			super.onActivityCreated(savedInstanceState);

		}

		@Override
		public void onDestroyView() {
			// TODO Auto-generated method stub
			super.onDestroyView();
			tagutube = null;
			tagtitle = null;

			taglyric = null;

			imageUrls = null;

		}

		@Override
		public void onDetach() {
			// TODO Auto-generated method stub
			super.onDetach();
			tagutube = null;
			tagtitle = null;

			taglyric = null;

			imageUrls = null;

		}

		@Override
		public void onPause() {
			// TODO Auto-generated method stub
			super.onPause();
			tagutube = null;
			tagtitle = null;

			taglyric = null;

			imageUrls = null;

		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			// Bundle bundle = getIntent().getExtras();
			// imageUrls = bundle.getStringArray(Extra.IMAGES);

			Bundle args = getArguments();
			tagutube = args.getStringArray("youtube_id");
			tagtitle = args.getStringArray("bling");

			taglyric = args.getStringArray("chinger");

			imageUrls = args.getStringArray("chaching");

			positions = args.getInt("object");
			if (positions == 2) {
				new LoadAlbums().execute();
			} else if (positions == 1) {
				new LoadAlbums().execute();
			}
			Log.d("CHECK POSITION", ">" + positions);

			tabLayout = 0;
			switch (positions) {
			case 1:
				tabLayout = R.layout.ac_image_grid;
				break;
			case 2:
				tabLayout = R.layout.tabs2;
				break;
			case 0:
				tabLayout = R.layout.tabs1;
				break;
			}

			View rootView = inflater.inflate(tabLayout, container, false);

			if (positions == 0) {
				GridView fridView = (GridView) rootView
						.findViewById(R.id.buttonsgridview);

				fridView.setAdapter(new ImageAdapter(rootView.getContext()));

				fridView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View v,
							int position, long id) {

						// Sending image id to FullScreenActivity
						Intent ikillded = new Intent(getActivity()
								.getBaseContext(), ImageGridActivity.class);
						// passing array index

						ikillded.putExtra("cat", tagcategory[position]);

						startActivity(ikillded);

					}
				});

			} else if (positions == 2) {

				GridView gridView = (GridView) rootView
						.findViewById(R.id.gridview);

				gridView.setAdapter(new Imgadapter(rootView.getContext()));

				gridView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View v,
							int position, long id) {

						// Sending image id to FullScreenActivity
						Intent ikill = new Intent(getActivity()
								.getBaseContext(), PlayerViewActivity.class);
						// passing array index
						ikill.putExtra("id", tagutube[position]);
						ikill.putExtra("songer", tagtitle[position]);
						ikill.putExtra("lyricer", taglyric[position]);

						startActivity(ikill);

					}
				});
			} else if (positions == 1) {
				GridView gridView = (GridView) rootView
						.findViewById(R.id.gridview2);

				gridView.setAdapter(new Imgadapter(rootView.getContext()));

				gridView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View v,
							int position, long id) {

						// Sending image id to FullScreenActivity
						Intent ikill = new Intent(getActivity()
								.getBaseContext(), PlayerViewActivity.class);
						// passing array index
						ikill.putExtra("id", tagutube[position]);
						ikill.putExtra("songer", tagtitle[position]);
						ikill.putExtra("lyricer", taglyric[position]);

						startActivity(ikill);

					}
				});

			}

			return rootView;

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
				if (positions == 2) {
					url = "http://karaokegarage.com/songs/allsongs.json/?category=new";
				} else if (positions == 1) {
					url = "http://karaokegarage.com/songs/allsongs.json/?category=telugu";
				}

			}

			protected String doInBackground(String... args) {

				List<NameValuePair> params = new ArrayList<NameValuePair>();

				// getting JSON string from URL
				String json = JSONParser.makeHttpRequest(url, "GET", params);

				// Check your log cat for JSON response
				Log.d("Albums JSON: ", "> " + json);

				try {
					JSONObject first = new JSONObject(json);

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
				if (positions == 1) {

					GridView gridView = (GridView) getActivity().findViewById(
							R.id.gridview2);

					gridView.setAdapter(new Imgadapter(getActivity()
							.getBaseContext()));
				} else if (positions == 2) {

					GridView gridView = (GridView) getActivity().findViewById(
							R.id.gridview);

					gridView.setAdapter(new Imgadapter(getActivity()
							.getBaseContext()));

				}
			}

		}

	}

	// //////////////////////// TAB FRAGMENT END ///////////////////
}
