package com.dcy.testapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

public class MainActivity extends Activity {
	Button fbbutton;
	Typeface face;
	TextView textView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gridder);
		
		//Button has been commented out. Hence the relavent code has been commented out too !
	//	TextView textView = (TextView) findViewById(R.id.fbbutton);
	//	Typeface typeFace = Typeface.createFromAsset(getAssets(), "droid.otf");
	//	textView.setTypeface(typeFace);
		
		

		

		GridView gridView = (GridView) findViewById(R.id.gridview);

		
		gridView.setAdapter(new Exprmnt(this));
		
		 gridView.setOnItemClickListener(new OnItemClickListener() {
	           
				@Override
				public void onItemClick(AdapterView<?> parent, View v,
	                    int position, long id) {
					
					 // Sending image id to FullScreenActivity
	                Intent i = new Intent(getApplicationContext(), ImgActivity.class);
	                // passing array index
	                i.putExtra("id", position);
	                startActivity(i);
					
					
				}
	        });

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
