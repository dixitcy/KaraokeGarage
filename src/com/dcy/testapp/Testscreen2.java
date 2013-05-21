package com.dcy.testapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Testscreen2 extends Activity implements OnClickListener{
	Button buttonrequest;
	EditText editsn;
	EditText editan;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.testscreen2);
		
		editsn = (EditText) findViewById(R.id.editsn);
		editan = (EditText) findViewById(R.id.editan);
		buttonrequest= (Button) findViewById(R.id.buttonrequest);
		
		buttonrequest.setOnClickListener(this);
		
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

}
