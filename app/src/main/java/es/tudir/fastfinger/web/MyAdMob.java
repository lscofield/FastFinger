package es.tudir.fastfinger.web;

import android.os.CountDownTimer;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

/**
 * Created by lscofield on 29/05/2016.
 */
public class MyAdMob {

    private CountDownTimer t;

    public MyAdMob(String id_ad){
        //construct for Intersticial Ads
    }

    public MyAdMob(){
        //construct for banner Ads
    }

    public void adBannerRequest(AdView mAdView){
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    public void requestNewInterstitial(InterstitialAd mInterstitialAd) {
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mInterstitialAd.loadAd(adRequest);
    }

    public void time(final InterstitialAd mInterstitialAd){
        t = new CountDownTimer(1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                requestNewInterstitial(mInterstitialAd);
            }

            @Override
            public void onFinish() {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
            }
        }.start();
    }
}
