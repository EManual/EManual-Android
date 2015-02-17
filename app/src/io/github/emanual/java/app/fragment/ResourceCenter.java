package io.github.emanual.java.app.fragment;

import io.github.emanual.java.app.R;
import io.github.emanual.java.app.api.EmanualAPI;
import io.github.emanual.java.app.ui.FileTree;
import io.github.emanual.java.app.utils.ZipUtils;
import io.github.emanual.java.app.widget.DownloadConfirmDialog;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.List;

import org.apache.http.Header;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectViews;
import butterknife.OnClick;
import butterknife.OnLongClick;

import com.loopj.android.http.FileAsyncHttpResponseHandler;

public class ResourceCenter extends BaseFragment {
	@InjectViews({ R.id.btn_java, R.id.btn_android, R.id.btn_php }) List<View> names;

	public String ROOT_PATH;
	public String DOWNLOAD_PATH;
	public String MD_PATH;
	// private List<Long> downloadIds = new ArrayList<Long>();
	private ProgressDialog mProgressDialog;
	private DownloadConfirmDialog mDownloadConfirmDialog;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_resource_center, null);
		ButterKnife.inject(this, v);

		ROOT_PATH = getActivity().getExternalFilesDir(null).getAbsolutePath();// /Android/data/包名/files
		MD_PATH = ROOT_PATH + File.separator + "md"; // /Android/data/包名/files/md
		DOWNLOAD_PATH = getActivity().getExternalFilesDir(     // /Android/data/包名/files/Download/
				Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
		Log.d("debug", DOWNLOAD_PATH);

		updateStatus();

		initDialog();
		
		return v;
	}

	private void initDialog() {
		mProgressDialog = new ProgressDialog(getActivity());
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		
		
		mDownloadConfirmDialog = new DownloadConfirmDialog(getActivity());
		mDownloadConfirmDialog.setConfirmClickListener(new OnClickListener() {
			
			@Override public void onClick(DialogInterface dialog, int which) {
				downloadLang(mDownloadConfirmDialog.getLang());
			}
		});
		
	}

	// 更新所有item的下载状态
	@SuppressLint("DefaultLocale") private void updateStatus() {
		File f = new File(MD_PATH);
		String[] _names = f.list(new FilenameFilter() {

			@Override public boolean accept(File dir, String filename) {
				if (new File(dir, filename).isDirectory()) {
					return true;
				}
				return false;
			}
		});
		for (View v : names) {
			// 初始化
			setDownloadVisibility(v, View.VISIBLE);
			v.setClickable(true);
		}
		if (_names != null) {
			for (String _n : _names) {
				for (View v : names) {
					TextView lang = (TextView) v.findViewWithTag("lang");
					if (lang.getText().toString().toLowerCase()
							.equals(_n.toLowerCase())) {
						// 有这个目录
						setDownloadVisibility(v, View.INVISIBLE);
					}
				}
			}
		}
	}

	/**
	 * 设置下载logo的显示性
	 * 
	 * @param btn
	 * @param visibility
	 */
	private void setDownloadVisibility(View btn, int visibility) {
		btn.findViewWithTag("img").setVisibility(visibility);
	}

	/**
	 * 下载的logo是否显示
	 * 
	 * @param btn
	 * @return
	 */
	private boolean downloadVisible(View btn) {
		return btn.findViewWithTag("img").getVisibility() == View.VISIBLE;
	}

	@OnClick({ R.id.btn_java, R.id.btn_android, R.id.btn_php }) public void click_lang(
			final View v) {
		String lang = "java";
		switch (v.getId()) {
		case R.id.btn_java:
			lang = "java";
			break;
		case R.id.btn_android:
			lang = "android";
			return;
			// break;
		case R.id.btn_php:
			lang = "php";
			return;
			// break;
		default:
			break;
		}
		if (!downloadVisible(v)) {
			// 已下载
			Intent intent = new Intent(getActivity(), FileTree.class);
			intent.putExtra("LANG_PATH", MD_PATH + File.separator + lang);
			getActivity().startActivity(intent);
		} else {
			// 未下载
			//downloadLang(lang);
			mDownloadConfirmDialog.show(lang);

		}
	}
	
	@OnLongClick({R.id.btn_java, R.id.btn_android, R.id.btn_php}) public boolean update_lang(View v){
		TextView tv =(TextView) v.findViewWithTag("lang");
		mDownloadConfirmDialog.show(tv.getText().toString().toLowerCase());
		return true;
	}
	
	private void downloadLang(String lang){
		EmanualAPI.downloadLang(lang, new FileAsyncHttpResponseHandler(
				new File(DOWNLOAD_PATH, lang + ".zip")) {

			@Override public void onStart() {
				mProgressDialog.setTitle("正在下载..");
				mProgressDialog.show();
			}

			@Override public void onSuccess(int arg0, Header[] arg1,
					File file) {
				// 解压
				new UnzipFileTask(file).execute();
			}

			@Override public void onFailure(int status_code, Header[] arg1,
					Throwable arg2, File file) {
				if (status_code == 404) {
					toast("找不到该资源");
				} else {
					toast("网络环境差，下载失败");
				}
			}

			@Override public void onProgress(int bytesWritten, int totalSize) {
				Log.d("debug", bytesWritten + "/" + totalSize);
				mProgressDialog.setMessage(String.format("大小:%.2f M",1.0*totalSize/1024/1024));
				mProgressDialog.setMax(totalSize);
				mProgressDialog.setProgress(bytesWritten);

			}
		});
	}

	/**
	 * 解压操作
	 */
	class UnzipFileTask extends AsyncTask<Void, Void, Boolean> {

		private File downloadFile;

		public UnzipFileTask(File downloadfile) {
			this.downloadFile = downloadfile;
		}

		@Override protected void onPreExecute() {
			mProgressDialog.setTitle("正在转换数据..");
		}

		@Override protected Boolean doInBackground(Void... params) {
			try {
				ZipUtils.unZipFiles(downloadFile.getAbsolutePath(), MD_PATH
						+ File.separator);
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			} finally {
				if (downloadFile.exists()) {
					downloadFile.delete();
				}
			}
			return true;
		}

		@Override protected void onPostExecute(Boolean result) {
			mProgressDialog.dismiss();
			if (result) {
				// 解压成功
				Toast.makeText(getActivity(), "操作完成，请点击打开", Toast.LENGTH_SHORT)
						.show();
				updateStatus();
				return;
			}
			// 解压失败,请求重试
			Toast.makeText(getActivity(), "数据转换失败，请重试!", Toast.LENGTH_SHORT)
					.show();
		}
	}
}
