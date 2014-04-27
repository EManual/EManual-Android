package io.github.emanual.app.fragment;

import io.github.emanual.app.R;
import io.github.emanual.app.ui.TopicList;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class LearnClub extends BaseFragment implements OnClickListener {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater
				.inflate(R.layout.fragment_learnclub, container, false);
		v.findViewById(R.id.btn_basic).setOnClickListener(this);
		return v;
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(getActivity(), TopicList.class);
		switch (v.getId()) {
		case R.id.btn_basic:
			intent.putExtra("kind", "basic");
			intent.putExtra("title", "基础");
			break;
		default:
			toast("It's comming soon.");
			break;
		}
		startActivity(intent);
	}
}
