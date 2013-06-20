package com.dcy.kandg;

import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.PlayerStyle;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;


public class PlayerViewActivity extends YouTubeFailureAcitivty {
	String youtube_id =null;
	String songnamenow = null;
	TextView tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.utubeplayer);
		
	
		Intent ikill = getIntent();
       youtube_id = ikill.getStringExtra("id");
       songnamenow = ikill.getStringExtra("songer");
       tv =(TextView) findViewById(R.id.songu);
		 tv.setText(songnamenow);
        Log.d("check youtube",">"+songnamenow);

		YouTubePlayerView youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
		youTubeView.initialize(DeveloperKey.DEVELOPER_KEY, this);
	}

	@Override
	public void onInitializationSuccess(YouTubePlayer.Provider provider,
			YouTubePlayer player, boolean wasRestored) {
		if (!wasRestored) {
			player.cueVideo(youtube_id);
			 player.setPlayerStyle(PlayerStyle.CHROMELESS);
			
		}

	}

	@Override
	protected Provider getYouTubePlayerProvider() {
		return (YouTubePlayerView) findViewById(R.id.youtube_view);
	}

}