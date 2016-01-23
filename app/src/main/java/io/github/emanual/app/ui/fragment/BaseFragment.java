package io.github.emanual.app.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import io.github.emanual.app.event.EmptyEvent;

public class BaseFragment extends Fragment {
    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    /**
     * EventBus 3必须要有一个@Subscribe
     */
    @Subscribe
    public void onEmpty(EmptyEvent event){

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
