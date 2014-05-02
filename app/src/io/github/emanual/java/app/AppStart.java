package io.github.emanual.java.app;

import io.github.emanual.java.app.ui.Main;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class AppStart  extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_appstart);
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				 if(!isFinishing()){	
					 startActivity(new Intent(AppStart.this, Main.class));
					 finish();
				 }
			}
		},500);
	}

 
}
