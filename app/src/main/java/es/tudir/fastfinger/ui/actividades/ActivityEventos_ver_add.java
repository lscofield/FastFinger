package es.tudir.fastfinger.ui.actividades;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.InterstitialAd;

import es.tudir.fastfinger.R;
import es.tudir.fastfinger.tools.Constantes;
import es.tudir.fastfinger.ui.fragmentos.FragmentPrincEvent;
import es.tudir.fastfinger.web.MyAdMob;
/**
 * Created by lscofield on 29/05/2016.
 */
public class ActivityEventos_ver_add extends AppCompatActivity {

    InterstitialAd mInterstitialAd;
    String speed;
    String id;
    MyAdMob myAdMob = new MyAdMob(id);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_ver_eventos);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        speed = getIntent().getExtras().getString("id_speed");
        id = getString(R.string.id_an);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(id);

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                myAdMob.requestNewInterstitial(mInterstitialAd);
            }
        });

        myAdMob.requestNewInterstitial(mInterstitialAd);

        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            // Poner ícono del drawer toggle
            ab.setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
            ab.setDisplayHomeAsUpEnabled(true);
        }

        // Creación del fragmento principal
        if (savedInstanceState == null) {
            Bundle b = new Bundle();
            b.putString("id_speed", speed);
            FragmentPrincEvent f = new FragmentPrincEvent();
            f.setArguments(b);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, f, "FragmentPrincEvent")
                    .commit();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constantes.CODIGO_DETALLE || requestCode == 3) {
            if (resultCode == RESULT_OK || resultCode == 203) {
                FragmentPrincEvent fragment = (FragmentPrincEvent) getSupportFragmentManager().
                        findFragmentByTag("FragmentPrincEvent");
                fragment.cargarAdaptador();
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home){
           // tiempo();
            Log.i("", "");
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        myAdMob.time(mInterstitialAd);
    }

}
