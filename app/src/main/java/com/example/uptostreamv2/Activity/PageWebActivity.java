/**
 *  Created by devgato on 19/05/2020.
 */
package com.example.uptostreamv2.Activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.example.uptostreamv2.Classe.Preference;
import com.example.uptostreamv2.R;

public class PageWebActivity extends AppCompatActivity {

    private WebView mWebViewPageWeb;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;

        //full screen webview
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_web_view);

        //Recuperation de la vue web
        mWebViewPageWeb = (WebView) findViewById(R.id.web_view);

        setConfNavigateur();

        setCoookie();

        //Chargement de page login
        mWebViewPageWeb.loadUrl("https://uptobox.com");

        //Surcharge de la fonction pour empêcher l'ouverture de popup pour les comptes gratuits
        mWebViewPageWeb.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                if (url.contains("uptobox.com")) {
                    mWebViewPageWeb.loadUrl(url);
                }

                //True if the host application wants to leave the current WebView and handle the url itself, otherwise return false.
                return true;
            }

        });
    }

    //Fonction pour charger les cookies dans le navigateur
    private void setCoookie() {
        //Récuperation des informations du token dans les preferences
        CookieManager.getInstance().setAcceptCookie(true);

        String cfduid = Preference.getPreference("cookie_cfduid", context);
        String xfss = Preference.getPreference("cookie_xfss", context);

        //Initialisation du cookie
        String cookieString = cfduid + "; path=/";
        CookieManager.getInstance().setCookie(".uptobox.com", cookieString);

        cookieString = xfss + "; path=/";
        CookieManager.getInstance().setCookie(".uptobox.com", cookieString);

        CookieManager.getInstance().setAcceptThirdPartyCookies(mWebViewPageWeb, true);

    }

    //Fonction pour configurer le navigateur
    private void setConfNavigateur() {
        mWebViewPageWeb.setWebChromeClient(new WebChromeClient());
        // Enable Javascript
        WebSettings webSettings = mWebViewPageWeb.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheEnabled(true);

    }
}
