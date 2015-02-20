package io.github.emanual.app.fragment;

import butterknife.ButterKnife;
import io.github.emanual.app.R;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Explore extends BaseFragment {
	
	@Override public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_explore, null);
		ButterKnife.inject(this, v);
		return v;
	}

}
