package com.dcy.kandg;

import java.io.BufferedReader;
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

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

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
import android.webkit.WebView;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class AllsongsActivity extends FragmentActivity implements
		ActionBar.TabListener {

	static ImageLoader imageLoader;
	static DisplayImageOptions options;

	public int k;
	Bitmap bmImg;
	ImageView imageView;

	public String url = "http://karaokegarage.com/songs/allsongs.json/?category=new";

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
	Context mContext;

	String[] myStringArray = new String[250];

	private static final String TEST_FILE_NAME = "Universal Image Loader @#&=+-_.,!()~'%20.png";

	SectionsPagerAdapter mSectionsPagerAdapter;

	ViewPager mViewPager;

	private ActionBar mActionBar;
	private LayoutInflater mInflater;
	private View mCustomView;
	private TextView mTitleTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_allsongs);

		contactList = new ArrayList<HashMap<String, String>>();
		new LoadAlbums().execute();

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
		mViewPager = (ViewPager) findViewById(R.id.pager);

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
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
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

	// /////////////////// LOAD ALBUMS ////////////////////////

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

						myStringArray[i] = "https://s3-ap-southeast-1.amazonaws.com/kgassets/posters/"
								+ img_url;
						Log.d("STRING CHECK", myStringArray[i]);
						imgs[i] = album;

						utubeid[i] = youtube_id;
						albumid[i] = id;
						songname[i] = title;

						// creating new HashMap
						HashMap<String, String> map = new HashMap<String, String>();

						// adding each child node to HashMap key => value
						map.put(TAG_ID, id);
						map.put(TAG_ALBUM, album);
						map.put(TAG_SINGER, singer);
						map.put(TAG_IMG_URL, img_url);
						map.put(TAG_UTUBE_ID, youtube_id);
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

		}

	}

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
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			// Fragment fragment = new DummySectionFragment();
			// Bundle args = new Bundle();
			// args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position +
			// 1);
			// fragment.setArguments(args);
			// return fragment;
			Fragment fragment = new TabFragment();
			Bundle args = new Bundle();
			args.putStringArray("chaching", myStringArray);
			args.putStringArray("bling", songname);
			args.putStringArray("youtube_id", utubeid);
			args.putInt(TabFragment.ARG_OBJECT, i);
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
		String Kgtit;

		public static final String ARG_OBJECT = "object";

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

					imageUrls = args.getStringArray("chaching");

				}

				if (imageLoader == null) {

					imageLoader = ImageLoader.getInstance();

					options = new DisplayImageOptions.Builder()
							.showStubImage(R.drawable.i3l69yvi)
							.showImageForEmptyUri(R.drawable.i3l69yvi)
							.showImageOnFail(R.drawable.i3l69yvi)
							.cacheInMemory().cacheOnDisc()
							.displayer(new RoundedBitmapDisplayer(10))
							.bitmapConfig(Bitmap.Config.RGB_565).build();
					imageLoader.init(ImageLoaderConfiguration
							.createDefault(mContext));

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
				if (view == null) {
					view = inflater.inflate(R.layout.grid_item, parent, false);
					holder = new ViewHolder();
					holder.text1 = (TextView) view.findViewById(R.id.SONG_NAME);
					// Typeface typeFace =
					// Typeface.createFromAsset(getActivity()
					// .getAssets(), "Quicksand_Bold.otf");
					// holder.text1.setTypeface(typeFace);
					holder.image = (ImageView) view.findViewById(R.id.image);
					holder.text1.setText(tagtitle[position]);

					view.setTag(holder);
				} else {

					holder = (ViewHolder) view.getTag();
					holder.text1.setText(tagtitle[position]);
				}

				holder.text1.setText(tagtitle[position]);
				imageLoader.displayImage(imageUrls[position], holder.image,
						options);
				Log.d("Check this out", ">" + imageUrls[5]);
				Log.d("And this", "?" + tagtitle[5]);

				return view;
			}

		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			// Bundle bundle = getIntent().getExtras();
			// imageUrls = bundle.getStringArray(Extra.IMAGES);

			Bundle args = getArguments();
			tagutube = args.getStringArray("youtube_id");
			int position = args.getInt(ARG_OBJECT);
			// imageUrls =args.getStringArray("chaching");

			int tabLayout = 0;
			switch (position) {
			case 0:
				tabLayout = R.layout.ac_image_grid;
				break;
			case 1:
				tabLayout = R.layout.ac_image_grid;
				break;
			case 2:
				tabLayout = R.layout.ac_image_grid;
				break;
			}

			View rootView = inflater.inflate(tabLayout, container, false);

			GridView gridView = (GridView) rootView.findViewById(R.id.gridview);

			gridView.setAdapter(new Imgadapter(rootView.getContext()));

			gridView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View v,
						int position, long id) {

					// Sending image id to FullScreenActivity
					Intent ikill = new Intent(getActivity().getBaseContext(),
							PlayerViewActivity.class);
					// passing array index
					ikill.putExtra("id", tagutube[position]);
					ikill.putExtra("songer", tagtitle[position]);
					startActivity(ikill);

				}
			});

			return rootView;

		}

	}

}
