package io.github.emanual.java.app.widget;

import io.github.emanual.java.app.R;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class NewVersionDialog extends Dialog {
	Context context;
	TextView updaInfo;
	String description, url;

	public NewVersionDialog(Context context, String description, String url) {
		super(context, R.style.Dialog_Theme_BaseDialog);
		this.context = context;
		this.description = description;
		this.url = url;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dlg_newversion);
		updaInfo = (TextView) findViewById(R.id.et_body);
		updaInfo.setText(description);
		findViewById(R.id.btn_comfirm).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						Uri uri = Uri.parse(url);
						Intent intent = new Intent(Intent.ACTION_VIEW, uri);
						context.startActivity(intent);
						NewVersionDialog.this.dismiss();
					}
				});
		findViewById(R.id.btn_cancle).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						NewVersionDialog.this.dismiss();
					}
				});
	}
}
