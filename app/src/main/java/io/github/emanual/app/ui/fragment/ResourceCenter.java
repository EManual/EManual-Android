package io.github.emanual.app.ui.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.loopj.android.http.FileAsyncHttpResponseHandler;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import cz.msebera.android.httpclient.Header;
import io.github.emanual.app.R;
import io.github.emanual.app.api.EmanualAPI;
import io.github.emanual.app.ui.FileTree;
import io.github.emanual.app.utils.EManualUtils;
import io.github.emanual.app.utils.ZipUtils;
import io.github.emanual.app.widget.DownloadConfirmDialog;

public class ResourceCenter extends BaseFragment {
    @Bind({R.id.btn_java, R.id.btn_android, R.id.btn_php, R.id.btn_python, R.id.btn_javascript, R.id.btn_c}) List<View> names;

    public String ROOT_PATH;
    public String DOWNLOAD_PATH;
    public String MD_PATH;
    // private List<Long> downloadIds = new ArrayList<Long>();
    private ProgressDialog mProgressDialog;
    private DownloadConfirmDialog mDownloadConfirmDialog;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_resource_center, null);
        ButterKnife.bind(this, v);

        ROOT_PATH = EManualUtils.getRootPath(getActivity());// /Android/data/包名/files
        MD_PATH = EManualUtils.getMdPath(getActivity()); // /Android/data/包名/files/md
        DOWNLOAD_PATH = EManualUtils.getDownloadPath(getActivity());
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

    @OnClick({R.id.btn_java, R.id.btn_android, R.id.btn_php, R.id.btn_python, R.id.btn_javascript, R.id.btn_c}) public void click_lang(
            final View v) {
        String lang = "java";
        switch (v.getId()) {
            case R.id.btn_java:
                lang = "java";
                break;
            case R.id.btn_android:
                lang = "android";
                break;
            case R.id.btn_php:
                lang = "php";
                break;
            case R.id.btn_python:
                lang = "python";
                break;
            case R.id.btn_javascript:
                lang = "javascript";
                break;
            case R.id.btn_c:
                lang = "c";
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
            mDownloadConfirmDialog.show(lang);

        }
    }

    @OnLongClick({R.id.btn_java, R.id.btn_android, R.id.btn_php, R.id.btn_python}) public boolean update_lang(View v) {
        TextView tv = (TextView) v.findViewWithTag("lang");
        mDownloadConfirmDialog.show(tv.getText().toString().toLowerCase());
        return true;
    }

    private void downloadLang(String lang) {
        EmanualAPI.downloadLang(lang, new FileAsyncHttpResponseHandler(
                new File(DOWNLOAD_PATH, lang + ".zip")) {

            @Override public void onStart() {
                mProgressDialog.setTitle("正在下载..");
                mProgressDialog.setProgress(0);
                mProgressDialog.setMax(100);
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
                    toast("该模块未开放");
                } else {
                    toast("网络环境差，下载失败");
                }
                mProgressDialog.dismiss();
            }

            @Override public void onProgress(long bytesWritten, long totalSize) {
                super.onProgress(bytesWritten, totalSize);

                Log.d("debug", bytesWritten + "/" + totalSize);
                mProgressDialog.setMessage(String.format("大小:%.2f M", 1.0 * totalSize / 1024 / 1024));
                mProgressDialog.setMax((int)totalSize);
                mProgressDialog.setProgress((int)bytesWritten);
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
                toast("操作完成，请点击打开");
                updateStatus();
                return;
            }
            // 解压失败,请求重试
            toast("数据转换失败，请重试!");
        }
    }
}
