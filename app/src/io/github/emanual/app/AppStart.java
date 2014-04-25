package io.github.emanual.app;

import io.github.emanual.app.ui.BaseActivity;
import io.github.emanual.app.ui.Main;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class AppStart extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acty_appstart);
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				 if(!isFinishing()){	
					 startActivity(new Intent(getContext(), Main.class));
					 finish();
				 }
			}
		},500);
	}

	@Override
	protected void initData() {
		
	}

	@Override
	protected void initLayout() {
		
	}
}
