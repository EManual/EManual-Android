package io.github.emanual.app.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import io.github.emanual.app.event.EmptyEvent;

public abstract class BaseFragment extends Fragment {

    /**
     * EventBus 3必须要有一个@Subscribe
     */
    @Subscribe
    public void onEmpty(EmptyEvent event){

    }

    protected abstract void initData(Bundle savedInstanceState);
    protected abstract void initLayout(Bundle savedInstanceState);
    protected abstract int getContentViewId();

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.from(getActivity()).inflate(getContentViewId(), container, false);
        ButterKnife.bind(this, view);
        initData(savedInstanceState);
        initLayout(savedInstanceState);
        Log.d("debug", "BaseFragment onCreateView");
        return view;
    }

    @Override public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

    }

    public void toast(String content) {
        if (getActivity() != null) {
            Toast.makeText(getActivity(), content, Toast.LENGTH_SHORT).show();
        }
    }
}
