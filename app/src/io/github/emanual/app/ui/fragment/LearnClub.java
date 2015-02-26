package io.github.emanual.app.ui.fragment;

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
		v.findViewById(R.id.btn_advance).setOnClickListener(this);
		v.findViewById(R.id.btn_pattern).setOnClickListener(this);
		v.findViewById(R.id.btn_database).setOnClickListener(this);
		v.findViewById(R.id.btn_web).setOnClickListener(this);
		v.findViewById(R.id.btn_webframework).setOnClickListener(this);
		v.findViewById(R.id.btn_arithmetic).setOnClickListener(this);
		v.findViewById(R.id.btn_puzzle).setOnClickListener(this);
		return v;
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(getActivity(), TopicList.class);
		switch (v.getId()) {
		case R.id.btn_basic:
			intent.putExtra("kind", "basic");
			intent.putExtra("title", "Java基础");
			break;
		case R.id.btn_advance:
			intent.putExtra("kind", "advance");
			intent.putExtra("title", "Java进阶");
			break;
		case R.id.btn_pattern:
			intent.putExtra("kind", "pattern");
			intent.putExtra("title", "设计模式");
			break;

		case R.id.btn_database:
			intent.putExtra("kind", "database");
			intent.putExtra("title", "数据库");
			break;
			
		case R.id.btn_web:
			intent.putExtra("kind", "java_web");
			intent.putExtra("title", "Java Web");
			break;
		case R.id.btn_webframework:
			intent.putExtra("kind", "framework");
			intent.putExtra("title", "Web框架");
			break;
		case R.id.btn_arithmetic:
			intent.putExtra("kind", "arithmetic");
			intent.putExtra("title", "算法实践");
			break;
			
		case R.id.btn_puzzle:
			intent.putExtra("kind", "puzzle");
			intent.putExtra("title", "谜题");
			break;
		default:
			toast("It's comming soon.");
			break;
		}
		startActivity(intent);
	}
}
