package io.github.emanual.java.app.utils;

import android.support.v4.widget.SwipeRefreshLayout;

public class SwipeRefreshLayoutUtils {

	public static void setRefreshing(
			final SwipeRefreshLayout swipeRefreshLayout,
			final boolean isRefreshing) {
		if(swipeRefreshLayout == null)
			return;
		swipeRefreshLayout.post(new Runnable() {
			
			@Override public void run() {
				swipeRefreshLayout.setRefreshing(isRefreshing);
			}
		});
	}

}
