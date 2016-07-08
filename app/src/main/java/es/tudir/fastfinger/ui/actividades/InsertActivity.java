package es.tudir.fastfinger.ui.actividades;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

import es.tudir.fastfinger.R;
import es.tudir.fastfinger.ui.fragmentos.ConfirmDialogFragment;
import es.tudir.fastfinger.ui.fragmentos.InsertFragment;

/**
 * Created by lscofield on 29/05/2016.
 */

public class InsertActivity extends AppCompatActivity  implements ConfirmDialogFragment.ConfirmDialogListener{

    private String speed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_ver_eventos);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        speed = getIntent().getExtras().getString("id_speed");

        if (getSupportActionBar() != null)
            getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_done);

        // Creación del fragmento de inserción
        if (savedInstanceState == null) {
            Bundle b = new Bundle();
            b.putString("id_speed", speed);
            InsertFragment f = new InsertFragment();
            f.setArguments(b);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, f, "InsertFragment")
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_form, menu);
        return true;
    }



    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {

        InsertFragment insertFragment = (InsertFragment)
                getSupportFragmentManager().findFragmentByTag("InsertFragment");

        if (insertFragment != null) {
            finish(); // Finalizar actividad descartando cambios
        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        InsertFragment insertFragment = (InsertFragment)
                getSupportFragmentManager().findFragmentByTag("InsertFragment");

        if (insertFragment != null) {
            // Nada por el momento
        }
    }
}
