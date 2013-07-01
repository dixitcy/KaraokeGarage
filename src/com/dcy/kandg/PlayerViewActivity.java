package com.dcy.kandg;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Timer;
import java.util.TimerTask;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.dcy.kandg.SongParser;
import com.dcy.kandg.Song;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;

public class PlayerViewActivity extends YouTubeFailureAcitivty {
	String youtube_id = null;
	String songnamenow = null;
	String mylyrics = null;
	TextView tv;
	TextView tvlyric;
	String resulthere;
	String str;
	String mahstr;
	InputStream in;
	SongParser parser;

	byte[] output = null;
	byte[] myoutput;
	String decryptresult;

	private YouTubePlayer player;
	Timer mytimer;
	int p;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.utubeplayer);

		Intent ikill = getIntent();
		youtube_id = ikill.getStringExtra("id");
		mylyrics = ikill.getStringExtra("lyricer");
		songnamenow = ikill.getStringExtra("songer");
		tv = (TextView) findViewById(R.id.songu);
		tv.setText(songnamenow);
		Log.d("check youtube", ">" + songnamenow);
		Log.d("check taglyric", ">" + mylyrics);

		YouTubePlayerView youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
		youTubeView.initialize(DeveloperKey.DEVELOPER_KEY, this);

		Thread th = new Thread(new Runnable() {

			public void run() {

				URL url = null;
				try {
					url = new URL(
							"https://s3-ap-southeast-1.amazonaws.com/kgassets/kglyrics/"
									+ mylyrics + ".txt");
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				URLConnection urlConnection = null;
				try {
					urlConnection = url.openConnection();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					in = new BufferedInputStream(urlConnection.getInputStream());
					resulthere = getStringFromInputStream(in);
					if (resulthere.startsWith("$", 0)) {

					}
					Log.d("CHECK UTUBE", resulthere);

					str = resulthere.substring(1);

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				byte[] output = null;

				byte[] kv = { (byte) 0x06, (byte) 0xa9, (byte) 0x21,
						(byte) 0x40, (byte) 0x36, (byte) 0xb8, (byte) 0xa1,
						(byte) 0x5b, (byte) 0x51, (byte) 0x2e, (byte) 0x03,
						(byte) 0xd5, (byte) 0x34, (byte) 0x12, (byte) 0x00,
						(byte) 0x06 };

				byte[] iv = { (byte) 0x3d, (byte) 0xaf, (byte) 0xba,
						(byte) 0x42, (byte) 0x9d, (byte) 0x9e, (byte) 0xb4,
						(byte) 0x30, (byte) 0xb4, (byte) 0x22, (byte) 0xda,
						(byte) 0x80, (byte) 0x2c, (byte) 0x9f, (byte) 0xac,
						(byte) 0x41 };

				SecretKeySpec skey = new SecretKeySpec(kv, "AES");
				IvParameterSpec ips = new IvParameterSpec(iv);
				Cipher cipher = null;
				try {
					cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
				} catch (NoSuchAlgorithmException e2) {

					e2.printStackTrace();
				} catch (NoSuchPaddingException e2) {

					e2.printStackTrace();
				}
				try {
					cipher.init(Cipher.DECRYPT_MODE, skey, ips);
				} catch (InvalidKeyException e1) {

					e1.printStackTrace();
				} catch (InvalidAlgorithmParameterException e1) {

					e1.printStackTrace();
				}

				try {
					output = cipher.doFinal(Hex.decodeHex(str.toCharArray()));

					InputStream is = new ByteArrayInputStream(output);
					resulthere = getStringFromInputStream(is);

					// System.out.println(resulthere);
					int myint = 0;
					Log.d("CHECK DECRYPTED OUTPUT", ">" + new String(output));

					// Log.d("Check decryption", ">" + new String(output));

				} catch (IllegalBlockSizeException e) {

					e.printStackTrace();
				} catch (BadPaddingException e) {

					e.printStackTrace();
				} catch (DecoderException e) {

					e.printStackTrace();
				}

				Song mine = new Song(new String(output));

				try {
					parser = new SongParser(mine);
				} catch (KaraokeEvent e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						tvlyric = (TextView) findViewById(R.id.mylyricer);
						tvlyric.setText(parser.myString);

					}
				});

			}

		})

		;
		th.start();

	}

	@Override
	public void onInitializationSuccess(YouTubePlayer.Provider provider,
			YouTubePlayer player, boolean wasRestored) {
		if (!wasRestored) {
			this.player = player;
			player.cueVideo(youtube_id);

		}

		mytimer = new Timer();
		mytimer.scheduleAtFixedRate(new myTimertask(), 20000, 1000);

	}

	@Override
	protected Provider getYouTubePlayerProvider() {
		return (YouTubePlayerView) findViewById(R.id.youtube_view);
	}

	class myTimertask extends TimerTask {

		@Override
		public void run() {
			p = player.getCurrentTimeMillis();
			Log.d("show time", formatTime(p));

			int j = player.getDurationMillis();

		}
	}

	private String formatTime(int millis) {
		int seconds = millis / 1000;
		int minutes = seconds / 60;
		int hours = minutes / 60;

		return (hours == 0 ? "" : hours + ":")
				+ String.format("%02d:%02d", minutes % 60, seconds % 60);
	}

	@Override
	public void onBackPressed() {

		super.onBackPressed();
		mytimer.cancel();
		mytimer.purge();
		str = null;
		resulthere = null;
		output = null;
		mylyrics = null;

	}

	private static String getStringFromInputStream(InputStream is) {

		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();

		String line;
		try {

			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return sb.toString();

	}

}