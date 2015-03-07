package com.wandoujia.ads.sdk.sample;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.wandoujia.ads.sdk.AdListener;
import com.wandoujia.ads.sdk.Ads;
import com.wandoujia.ads.sdk.InterstitialAd;
import com.wandoujia.ads.sdk.loader.Fetcher;
import com.wandoujia.ads.sdk.widget.AdBanner;

public class MainActivity extends FragmentActivity {

  private static final String TAG = "Ads-Sample";

  private static final String ADS_APP_ID = "100020115";
  private static final String ADS_SECRET_KEY = "1fa2aef2b4ec33b43c267c64d0166755";

  static final String TAG_LIST = "66caff24c98802b40dbb014bbf39f0be";
  private static final String TAG_INTERSTITIAL_WIDGET = "2d1ccfaaab9a09d4be8eec7d86ccca77";
  private static final String TAG_BANNER = "30d7fd51a4c3ede0c01c8b1c7c84fea7";

  private Button showAppWallButton;

  private AdBanner adBanner;
  private View adBannerView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    showAppWallButton = (Button) findViewById(R.id.show_apps_button);

    findViewById(R.id.show_apps_button).setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        Ads.showAppWall(MainActivity.this, TAG_LIST);

      }

    });

    findViewById(R.id.show_app_wall).setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(new Intent(MainActivity.this, AppWallFragmentActivity.class));
      }
    });

    final InterstitialAd interstitialAd = new InterstitialAd(this, TAG_INTERSTITIAL_WIDGET);
    interstitialAd.setAdListener(new AdListener() {
      @Override
      public void onAdReady() {
        interstitialAd.show();
      }

      @Override
      public void onLoadFailure() {

      }

      @Override
      public void onAdPresent() {

      }

      @Override
      public void onAdDismiss() {

      }
    });

    // 插屏广告
    findViewById(R.id.show_app_widget_button).setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        interstitialAd.load();
      }

    });



    try {
      Ads.init(this, ADS_APP_ID, ADS_SECRET_KEY);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    Ads.preLoad(this, Fetcher.AdFormat.appwall, "APP", TAG_LIST, new AdListener() {

      @Override
      public void onAdReady() {
        if (Ads.getUpdateAdCount("APP") > 0) {
          drawUpdateIndicator(Color.RED, true);
        }
      }

      @Override
      public void onLoadFailure() {

      }

      @Override
      public void onAdPresent() {

      }

      @Override
      public void onAdDismiss() {

      }
    });

    Ads.preLoad(this, Fetcher.AdFormat.appwall, "GAME", TAG_LIST, new AdListener() {

      @Override
      public void onAdReady() {
        if (Ads.getUpdateAdCount("GAME") > 0) {
          drawUpdateIndicator(Color.GREEN, false);
        }
      }

      @Override
      public void onLoadFailure() {
        Toast.makeText(MainActivity.this, "网络异常，广告加载失败！", Toast.LENGTH_SHORT).show();
      }

      @Override
      public void onAdPresent() {

      }

      @Override
      public void onAdDismiss() {

      }
    });

    final ViewGroup adsWidgetContainer = (ViewGroup) findViewById(R.id.ads_widget_container);

    final Fetcher.AdFormat adFormat = Fetcher.AdFormat.interstitial;
    if (Ads.isLoaded(adFormat, TAG_INTERSTITIAL_WIDGET)) {
      showAppWidget(adsWidgetContainer);
    } else {
      adsWidgetContainer.setVisibility(View.GONE);
      Log.d(TAG, "Preload data for interstitial Ads.");
      Ads.preLoad(this, adFormat, TAG_INTERSTITIAL_WIDGET);
      new Thread() {
        @Override
        public void run() {
          try {
            while (!Ads.isLoaded(adFormat, TAG_INTERSTITIAL_WIDGET)) {
              Log.d(TAG, "Wait loading for a while...");
              Thread.sleep(2000);
            }
            Log.d(TAG, "Ads data had been loaded.");
            new Handler(Looper.getMainLooper()).post(new Runnable() {
              @Override
              public void run() {
                adsWidgetContainer.setVisibility(View.VISIBLE);
                showAppWidget(adsWidgetContainer);
              }
            });
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      }.start();
    }

    showBannerAd();
  }

  @Override
  protected void onStart() {
    // adBanner.startAutoScroll();
    super.onStart();
  }

  @Override
  protected void onStop() {
    // adBanner.stopAutoScroll();
    super.onStop();
  }

  private void drawUpdateIndicator(int color, boolean drawLeftOrRight) {
    ShapeDrawable smallerCircle = new ShapeDrawable(new OvalShape());
    smallerCircle.setIntrinsicHeight(60);
    smallerCircle.setIntrinsicWidth(60);
    smallerCircle.setBounds(new Rect(0, 0, 60, 60));
    smallerCircle.getPaint().setColor(color);
    smallerCircle.setPadding(50, 50, 50, 100);

    Drawable drawableleft = null;
    Drawable drawableRight = null;
    if (drawLeftOrRight) {
      drawableleft = smallerCircle;
    } else {
      drawableRight = smallerCircle;
    }
    showAppWallButton.setCompoundDrawables(drawableleft, null, drawableRight, null);
  }
  // 插屏广告，非全屏
  void showAppWidget(final ViewGroup container) {
    container.addView(Ads.showAppWidget(this, TAG_INTERSTITIAL_WIDGET, null, Ads.ShowMode.WIDGET,
        new OnClickListener() {
          @Override
          public void onClick(View v) {
            container.setVisibility(View.GONE);
          }
        }));
  }
  // 横条广告
  void showBannerAd() {
    ViewGroup containerView = (ViewGroup) findViewById(R.id.banner_ad_container);
    if (adBannerView != null && containerView.indexOfChild(adBannerView) >= 0) {
      containerView.removeView(adBannerView);
    }
    adBanner = Ads.showBannerAd(this, (ViewGroup) findViewById(R.id.banner_ad_container),
        TAG_BANNER);
    adBannerView = adBanner.getView();
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

}
