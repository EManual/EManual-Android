package io.github.emanual.app.ui.fragment;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.emanual.app.CoreService;
import io.github.emanual.app.R;
import io.github.emanual.app.ui.About;
import io.github.emanual.app.ui.Browser;
import io.github.emanual.app.ui.Feedback;
import io.github.emanual.app.utils.AndroidUtils;
import io.github.emanual.app.utils.EManualUtils;

public class Explore extends BaseFragment {

    UpdateBroadcastReceiver mReceiver;
    ProgressDialog mProgressDialog;

    @Override public View onCreateView(LayoutInflater inflater,
                                       @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_explore, null);
        ButterKnife.inject(this, v);

        mReceiver = new UpdateBroadcastReceiver();

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
        IntentFilter filter = new IntentFilter();
        filter.addAction(CoreService.Action_CheckVersion);
        getActivity().registerReceiver(mReceiver, filter);

        mProgressDialog.show();

        Intent service = new Intent(getActivity(), CoreService.class);
        service.setAction(CoreService.Action_CheckVersion);
        getActivity().startService(service);
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

    @Override public void onPause() {
        super.onPause();
        try {
            getActivity().unregisterReceiver(mReceiver);
        } catch (Exception e) {
            // mReceiver 未注册
        }
    }

    class UpdateBroadcastReceiver extends BroadcastReceiver {

        @Override public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(CoreService.Action_CheckVersion)) {
                int version_code = intent.getIntExtra("version_code", 1);

                if (version_code <= AndroidUtils
                        .getAppVersionCode(getActivity())) {
                    toast("没有更新");
                }
                mProgressDialog.dismiss();
            }
        }

    }
}
