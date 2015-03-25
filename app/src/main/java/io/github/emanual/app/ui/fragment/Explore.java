package io.github.emanual.app.ui.fragment;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.emanual.app.R;
import io.github.emanual.app.ui.About;
import io.github.emanual.app.ui.Browser;
import io.github.emanual.app.ui.Feedback;
import io.github.emanual.app.utils.EManualUtils;

public class Explore extends BaseFragment {

    ProgressDialog mProgressDialog;

    @Override public View onCreateView(LayoutInflater inflater,
                                       @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_explore, null);
        ButterKnife.inject(this, v);

        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setTitle("检查更新");
        mProgressDialog.setMessage("正在检查更新....");

        return v;
    }

    @OnClick(R.id.btn_rate_app) public void rate_app() {
        String packageName = getActivity().getPackageName();
        try {
            getActivity().startActivity(
                    new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="
                            + packageName)));
        } catch (ActivityNotFoundException e) {
            toast("你手机没有装应用中心，建议下载豌豆荚客户端");
        }
    }

    @OnClick(R.id.btn_about) public void about() {
        startActivity(new Intent(getActivity(), About.class));
    }

    @OnClick(R.id.btn_feedback) public void feedback() {
        Intent intent = new Intent(getActivity(), Feedback.class);
        intent.putExtra(Feedback.EXTRA_TYPE, Feedback.TYPE_ADVICE);
        startActivity(intent);
    }

    @OnClick(R.id.btn_update) public void update() {
        UmengUpdateAgent.setUpdateAutoPopup(false);
        UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
            @Override
            public void onUpdateReturned(int updateStatus,UpdateResponse updateInfo) {
                mProgressDialog.dismiss();
                switch (updateStatus) {
                    case UpdateStatus.Yes: // has update
                        UmengUpdateAgent.showUpdateDialog(getActivity(), updateInfo);
                        break;
                    case UpdateStatus.No: // has no update
                        toast("没有更新");
                        break;
                }
            }
        });
        UmengUpdateAgent.update(getActivity());
        mProgressDialog.show();
    }

    @OnClick(R.id.btn_usage) public void usage() {
        Intent intent = new Intent(getActivity(), Browser.class);
        intent.putExtra(Browser.EXTRA_URL, EManualUtils.URL_USAGE);
        startActivity(intent);
    }

    @OnClick(R.id.btn_sponsor) public void sponsor() {
        Intent intent = new Intent(getActivity(), Browser.class);
        intent.putExtra(Browser.EXTRA_URL, EManualUtils.URL_SPONSOR);
        startActivity(intent);
    }

    @OnClick(R.id.btn_opensource) public void opensource() {
        Intent intent = new Intent(getActivity(), Browser.class);
        intent.putExtra(Browser.EXTRA_URL, EManualUtils.URL_OPENSOURCE);
        startActivity(intent);
    }


}
