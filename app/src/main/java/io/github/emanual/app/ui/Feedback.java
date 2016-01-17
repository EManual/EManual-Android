package io.github.emanual.app.ui;

import android.app.ProgressDialog;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;

import butterknife.Bind;
import butterknife.OnClick;
import io.github.emanual.app.R;
import io.github.emanual.app.utils.AndroidUtils;

public class Feedback extends SwipeBackActivity {
    public static final String EXTRA_CONTENT = "content";//反馈内容
    public static final String EXTRA_TYPE = "type"; // 反馈类型
    public static final String TYPE_REPORT = "report"; // 报告错误
    public static final String TYPE_ADVICE = "advice";// 建议

    public static final int MAX_CONTENT_LENGTH = 400; //反馈内容最长为400


    @Bind(R.id.et_content) EditText et_content;
    @Bind(R.id.et_user_contact) EditText et_user_contact;
    @Bind(R.id.rg_type) RadioGroup rg_type;

    ProgressDialog mProgressDialog;
    String _content;
    String _type;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override protected void initData() {
        super.initData();
        _type = getStringExtra(EXTRA_TYPE);
        _content = getStringExtra(EXTRA_CONTENT);
    }

    @Override protected void initLayout() {
        super.initLayout();
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.acty_feedback);

        if (!TextUtils.isEmpty(_content)) {
            et_content.setText(_content);
            et_content.setSelection(_content.length());
        }
        if (!TextUtils.isEmpty(_type)) {
            if (_type.equals(TYPE_REPORT)) {
                rg_type.check(R.id.rb_type_report);
            } else {
                rg_type.check(R.id.rb_type_advice);
            }
        }

        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setTitle("提示");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("正在发送请求.....");

    }

    @Override protected int getContentViewId() {
        return R.layout.acty_feedback;
    }

    @OnClick(R.id.btn_submit) public void submit() {
        String content = et_content.getText().toString();
        String user_contact = et_user_contact.getText().toString(); //optional
        String type = rg_type.getCheckedRadioButtonId() == R.id.rb_type_advice ? TYPE_ADVICE
                : TYPE_REPORT;
        String app_version = "";
        String system_version = Build.VERSION.RELEASE + "-" + Build.VERSION.SDK_INT;
        String model = Build.BRAND + " " + Build.MODEL;
        try {
            app_version = AndroidUtils.getAppVersionName(getContext());
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        if (content.length() < 5) {
            toast("多写点东西吧~");
            return;
        } else if (content.length() > MAX_CONTENT_LENGTH) {
            toast("内容太多了,文字数应小于" + MAX_CONTENT_LENGTH);
            return;
        }

        AVObject fb = new AVObject("FeedBack");
        fb.put("content", content);
        fb.put("user_contact", user_contact);
        fb.put("type", type);
        fb.put("app_version", app_version);
        fb.put("system_version", system_version);
        fb.put("model", model);

        mProgressDialog.show();
        fb.saveInBackground(new SaveCallback() {

            @Override public void done(AVException e) {
                if (e == null) {
                    toast("已发送,感谢你的反馈!");
                    finish();
                } else {
                    toast("发送失败,请重试");
                }
                mProgressDialog.dismiss();
            }
        });
    }


}
