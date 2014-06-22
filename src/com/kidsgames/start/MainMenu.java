package com.kidsgames.start;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.drawingfun.R;

public class MainMenu extends Activity implements OnClickListener{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu);
		
		findViewById(R.id.pack1_button).setOnClickListener(this);
		findViewById(R.id.pack2_button).setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		
		switch(v.getId())
		{
		case R.id.pack1_button:
			Intent intent = new Intent(this, DrawerSliderActivity.class);
			intent.putExtra("pack", "pack1");
			startActivity(intent);
			break;
		case R.id.pack2_button:
			intent = new Intent(this, DrawerSliderActivity.class);
			intent.putExtra("pack", "pack2");
			startActivity(intent);
			break;
		}
	}
}
