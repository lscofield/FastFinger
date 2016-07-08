package es.tudir.fastfinger.ui.actividades;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;

import java.text.DecimalFormat;

import cz.msebera.android.httpclient.Header;
import es.tudir.fastfinger.R;
import es.tudir.fastfinger.web.MyAdMob;

public class Main extends AppCompatActivity {

    Button finger;
    int puntos = 0;
    TextView toques;
    int tiempo = 10000;
    String speed;
    Toolbar toolbar;
    MediaPlayer tick;
    InterstitialAd mInterstitialAd;
    CountDownTimer t;
    String id;
    MyAdMob myAdMob = new MyAdMob(id);
    String speedChallenger;
    private static final String host = "http://perfil.atwebpages.com/fastfinger/GetData.php";
    Main myActivity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        id = getString(R.string.id_an);

        AdView mAdView = (AdView) findViewById(R.id.adView);
        MyAdMob adb = new MyAdMob();
        adb.adBannerRequest(mAdView);

        //get data from server (max speed)
        ObtDatos();


        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(id);

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                myAdMob.requestNewInterstitial(mInterstitialAd);
            }
        });

        myAdMob.requestNewInterstitial(mInterstitialAd);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tick = MediaPlayer.create(this, R.raw.tick);
        tick.setLooping(false);

        finger = (Button) findViewById(R.id.btnAsir1);
        toques = (TextView) findViewById(R.id.toques);

        finger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tick.start();
                ++puntos;
                toques.setText(Integer.toString(puntos));
                if (puntos % 2 == 0) {
                    toques.setTextColor(Color.rgb(3, 168, 244));
                } else {
                    toques.setTextColor(Color.rgb(244, 67, 54));
                }
                if (puntos == 1) {
                    esperar();
                }
            }
        });

    }


    private void esperar(){
        t = new CountDownTimer(tiempo, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String info = getString(R.string.titPunt)+" "+(millisUntilFinished/1000)
                        +" "+getString(R.string.velMaxim)+" "+speedChallenger;
                toolbar.setTitle(info);
            }

            @Override
            public void onFinish() {
                toolbar.setTitle(getString(R.string.ob) + " "
                        +puntos+" "+getString(R.string.pt));
                finger.setEnabled(false);
                dialogo();
                myAdMob.time(mInterstitialAd);
            }
        }.start();
    }

    private void dialogo(){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton(getString(R.string.jugar), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                toolbar.setTitle(getString(R.string.app_name));
                puntos = 0;
                toques.setText("");
                finger.setEnabled(true);
                myAdMob.time(mInterstitialAd);
            }
            //onClick last line
        });
        alertDialogBuilder.setNegativeButton(getString(R.string.salir), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                sendSpeed();
            }
        });

        // Creamos un AlertDialog y lo mostramos
        double veloz = (double) puntos/(tiempo / 1000);
        DecimalFormat df = new DecimalFormat("0.00");
        String vel = String.format(df.format(veloz)+" fingers/seg");

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setTitle(getString(R.string.info)+
                " " + puntos + " " + getString(R.string.info2) + " " +
                getString(R.string.a) + " " +
                vel + "\n"+
                getString(R.string.velMaxim)+" "+speedChallenger);
        alertDialog.setMessage(getString(R.string.info3));
        speed = vel;
        alertDialog.show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tick.isPlaying()){
            tick.stop();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ObtDatos();
        myAdMob.time(mInterstitialAd);
        play();
    }

    @Override
    protected void onPause() {
        super.onPause();
        tick.stop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void  elegirTiempo(){

        // Rescatamos el layout creado para el prompt
        LayoutInflater li = LayoutInflater.from(this);
        View prompt = li.inflate(R.layout.prompt, null);

        // Creamos un constructor de Alert Dialog y le añadimos nuestro layout al cuadro de dialogo
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(prompt);

        final EditText nombreUsuario = (EditText) prompt.findViewById(R.id.nombre_usuario);

        // Mostramos el mensaje del cuadro de dialogo
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                toolbar.setTitle(getString(R.string.velMaxim));
                int punt = 0;

                //tiempo(1);
                // Rescatamos el nombre del EditText y lo mostramos por pantalla
                try {
                    punt = Integer.parseInt(nombreUsuario.getText().toString());
                    tiempo = punt*1000;
                } catch (Exception e) {
                    Toast.makeText(Main.this,getString(R.string.toast), Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(Main.this, tiempo/1000+" seg", Toast.LENGTH_SHORT).show();
            }

            //ultima linea de onclick
        });
        alertDialogBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                toolbar.setTitle(getString(R.string.velMaxim));
                dialog.cancel();
                myAdMob.time(mInterstitialAd);
            }
        });

        // Creamos un AlertDialog y lo mostramos

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    
    private void playCheck(){
        if(speed == null){
            Toast.makeText(Main.this, getString(R.string.playTest), Toast.LENGTH_SHORT).show();
        }else {
            sendSpeed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            playCheck();
            return true;
        }else if (id == R.id.action_time){
            elegirTiempo();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void play(){
        finger.setEnabled(true);
        toques.setText("");
        puntos = 0;
    }

    public void sendSpeed(){
        Intent intent = new Intent(myActivity, ActivityEventos_ver_add.class);
        intent.putExtra("id_speed",speed);
        startActivity(intent);
        myAdMob.time(mInterstitialAd);
        Toast.makeText(Main.this, getString(R.string.load), Toast.LENGTH_SHORT).show();
    }

    public void ObtDatos(){
        AsyncHttpClient client = new AsyncHttpClient();
        String url = host;

        client.post(url, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    obtDatosJSON(new String(responseBody), 1);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            }
        });
    }


    public void obtDatosJSON(String response, int cod){

        try{
            JSONArray jsonArray = new JSONArray(response);
            speedChallenger = jsonArray.getJSONObject(0).getString("speed");

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
