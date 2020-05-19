/**
 *  Created by devgato on 19/05/2020.
 */
package com.example.uptostreamv2.Activity;

import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.uptostreamv2.Classe.FileHtml;
import com.example.uptostreamv2.Classe.Preference;
import com.example.uptostreamv2.R;

public class StreamingActivity extends AppCompatActivity {

    private WebView mWebView;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getApplicationContext();

        //full screen webview
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_web_view);

        //Recuperation du parametre
        Bundle param = getIntent().getExtras();
        String value = "";
        if (param != null)
            value = param.getString("code_film");

        //Creation du fichier html temporaire
        FileHtml.writeStringAsFile(value, value + ".html", this.getApplicationContext());

        //Recuperation de la vue web
        mWebView = (WebView) findViewById(R.id.web_view);

        String cheminHTML = this.getApplicationContext().getCacheDir().toString();
        cheminHTML += "/" + value + ".html";

        //Chargement du fichier html
        chargeHTML(cheminHTML);

        //Configuration du navigateur
        setConfNavigateur();

        //Configuration du cookie
        setCoookie();

        //Surcharge de la fonction pour empêcher l'ouverture de popup pour les comptes gratuits
        mWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //True if the host application wants to leave the current WebView and handle the url itself, otherwise return false.
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // TODO Auto-generated method stub

                Boolean autoPlay = Preference.getPreferenceBool("autoplay", context);

                if (autoPlay == true) {

                    simulateTouchScreen();

                }
            }
        });

    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        //Simulate spacekey for play/pause
        if (event.getAction() == KeyEvent.ACTION_UP && event.getKeyCode() == 23) {

            mWebView.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_SPACE));
        }
        //Simulate spacekey for nextquality
        else  if (event.getAction() == KeyEvent.ACTION_UP && event.getKeyCode() == 19) {

            mWebView.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_Q));
            Toast.makeText(context,"Next Quality", Toast.LENGTH_SHORT).show();
        }
        // Finish the activity
        else  if (event.getAction() == KeyEvent.ACTION_UP && event.getKeyCode() == 4) {

            finish();
        }

        return true;
    }

    //Fonction pour charger le chemin du fichier
    private void chargeHTML(String cheminHTML) {

        mWebView.loadUrl("file:///" + cheminHTML);
    }

    //Fonction pour charger les cookies dans le navigateur
    private void setCoookie() {

        //Récuperation des informations du token dans les preferences
        CookieManager.getInstance().setAcceptCookie(true);

        String cfduid = Preference.getPreference("cookie_cfduid", context);
        String xfss = Preference.getPreference("cookie_xfss", context);

        //Initialisation du token
        String cookieString = cfduid + "; path=/";
        CookieManager.getInstance().setCookie(".uptostream.com", cookieString);

        cookieString = xfss + "; path=/";
        CookieManager.getInstance().setCookie(".uptostream.com", cookieString);

        CookieManager.getInstance().setAcceptThirdPartyCookies(mWebView, true);

    }

    //Fonction pour configurer le navigateur
    private void setConfNavigateur() {

        mWebView.setWebChromeClient(new WebChromeClient());

        // Enable Javascript
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setMediaPlaybackRequiresUserGesture(false);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheEnabled(true);
    }


    //Fonction pour Simuler un click sur l'écran
    private void simulateTouchScreen() {

        // Autoclick to play automatically on video
        int x, y;
        x = 100;
        y = 100;

        long downTime = SystemClock.uptimeMillis() + (1000);
        long eventTime = SystemClock.uptimeMillis() + (1500);
        int metaState = 0;

        //DOWN
        MotionEvent me = MotionEvent.obtain(
                downTime,
                eventTime,
                MotionEvent.ACTION_DOWN,
                x,
                y,
                metaState
        );
        mWebView.dispatchTouchEvent(me);

        //UP
        me = MotionEvent.obtain(
                downTime,
                eventTime,
                MotionEvent.ACTION_UP,
                x,
                y,
                metaState
        );
        mWebView.dispatchTouchEvent(me);

    }
}