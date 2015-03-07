package io.github.emanual.app.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

public class NewVersionDialog extends AlertDialog {

	Context context;
	String updaInfo, url;

	public NewVersionDialog(Context context) {
		super(context);
		this.context = context;
		init();
	}

	private void init() {
		this.setTitle("发现新版本");
		this.setButton(BUTTON_NEGATIVE, "下次更新", new OnClickListener() {

			@Override public void onClick(DialogInterface dialog, int which) {
				dismiss();
			}
		});
		this.setButton(BUTTON_POSITIVE, "立即更新", new OnClickListener() {

			@Override public void onClick(DialogInterface dialog, int which) {
				Uri uri = Uri.parse(url);
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				context.startActivity(intent);
				NewVersionDialog.this.dismiss();
			}
		});
	}

	public void show(String updateInfo, String url) {
		this.updaInfo = updateInfo;
		this.url = url;
		this.setMessage(updateInfo);
		
		super.show();
	}
}
