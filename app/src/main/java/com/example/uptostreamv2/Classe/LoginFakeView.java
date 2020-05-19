/**
 *  Created by devgato on 19/05/2020.
 */
package com.example.uptostreamv2.Classe;

import android.content.SharedPreferences;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.uptostreamv2.Activity.LoginActivity;
import com.example.uptostreamv2.Activity.MainEmptyActivity;

public class LoginFakeView {

    private WebView fakeWebView;
    private String urlLogin;
    private int nbrOpen;
    private String cookie;
    private String token;
    private SharedPreferences mPreferences;

    public LoginFakeView(SharedPreferences mPreferences) {

        urlLogin = "https://uptobox.com/login?referer=homepage";
        nbrOpen = 0;
        cookie = "";
        fakeWebView = new WebView(LoginActivity.getInstance().getApplicationContext());
        this.mPreferences = mPreferences;

        fakeWebView.setWebChromeClient(new WebChromeClient());
        fakeWebView.clearCache(true);

        // Enable Javascript
        WebSettings webSettings = fakeWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheEnabled(true);

    }

    //Fonction pour authentifiier l'utilisateur via la page web
    public void getCookieLoginFakeView(final String login, final String password) {

        nbrOpen = 0;
        fakeWebView.loadUrl(urlLogin);

        //Surcharge de la fonction pour empêcher l'ouverture de popup pour les comptes gratuits
        fakeWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                if (url.contains("uptobox.com")) {
                    fakeWebView.loadUrl(url);
                }

                //True if the host application wants to leave the current WebView and handle the url itself, otherwise return false.
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // TODO Auto-generated method stub
                // Log.d("TAG", "nbrOpen "+login);
                // Log.d("TAG", "nbrOpen "+password);
                //Ouverture page login
                if (nbrOpen == 0) {
                    //injection javascript pour lancer l'authentification
                    fakeWebView.loadUrl(
                            "javascript:(function() { " +
                                    "document.getElementById('login-form').login.value='" + login + "';" +
                                    "document.getElementById('login-form').password.value='" + password + "';" +
                                    "document.getElementById('login-form').submit();" +
                                    "})()");

                }
                //ouveture resultat submit le le cookie xss est charge
                else if (nbrOpen == 1) {

                    cookie = CookieManager.getInstance().getCookie(url);
                    Log.d("TAG", "cookie " + cookie);
                    if (cookie.contains("xfss")) {
                        getToken();
                    } else {
                        LoginActivity.getInstance().connexionFailed();

                    }

                }

                nbrOpen++;
            }
        });
    }


    public void getToken() {

        fakeWebView.loadUrl("https://uptobox.com/my_account");

        //Surcharge de la fonction pour empêcher l'ouverture de popup pour les comptes gratuits
        fakeWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                if (url.contains("uptobox.com")) {
                    fakeWebView.loadUrl(url);
                }

                //True if the host application wants to leave the current WebView and handle the url itself, otherwise return false.
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // TODO Auto-generated method stub
                // System.out.println("page loading finished 1");
                //Log.d("myTag", " 1");
                fakeWebView.evaluateJavascript(
                        "(function() { return (document.getElementsByClassName('none')[2].innerHTML); })();",
                        new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String html) {

                                token = html;
                                //Log.d("TAG", "token " + token);

                                if (token != "") {
                                    //Suppression du caractère "
                                    token = token.replace("\"", "");
                                    //Log.d("TAG", "new token " + token);

                                    //Sauvegarde du token
                                    save();

                                    //Lancer activity principal
                                    MainEmptyActivity.getInstance().startActivityMain();
                                } else {
                                    Toast.makeText(LoginActivity.getInstance(), "Erreur TOKEN FAILED!",
                                            Toast.LENGTH_LONG).show();
                                }

                            }
                        });
            }
        });
    }

    //Fonction pour sauvegarder les preferences
    public boolean save() {

        //Recupération des valeurs des edits texts
        String[] tabCookie = cookie.split(";");

        //Enregistrement cookie cfuid
        String scookie_cfduid = tabCookie[0];

        //Enregistrement cookie xfss
        String scookie_xfss = "";

        for (String element : tabCookie) {
            if (element.contains("xfss=")) {
                scookie_xfss = element;
            }
        }

        Preference.save(mPreferences, token, scookie_cfduid, scookie_xfss, "");

        return true;
    }
}
