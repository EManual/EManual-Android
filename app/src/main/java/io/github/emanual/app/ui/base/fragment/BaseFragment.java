package io.github.emanual.app.ui.base.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import io.github.emanual.app.R;
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
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.from(getActivity()).inflate(getContentViewId(), container, false);
        ButterKnife.bind(this, view);
        initData(savedInstanceState);
        initLayout(savedInstanceState);
        return view;
    }

    @Override public void onDestroy() {
        super.onDestroy();
        if(EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }

    public void toast(String content) {
        if (getActivity() != null) {
            try{
                Snackbar.make(getActivity().findViewById(R.id.layout_container), content, Snackbar.LENGTH_LONG).show();
            }catch(Exception e){
                Toast.makeText(getActivity(), content, Toast.LENGTH_LONG).show();
            }

        }
    }
}
