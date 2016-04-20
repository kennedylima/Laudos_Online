package br.com.metodoi.laudos_online;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.view.WindowManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Kennedy on 20/04/2016.
 */
public class LaudoOnline extends AsyncTask<String, Void, Void> {

    private Context contexto;
    private ProgressDialog dialogoDeProgresso;


    public LaudoOnline(Context contexto) {
        this.contexto = contexto;
        dialogoDeProgresso = new ProgressDialog(contexto);
    }

    private static void desativarDeteccaoDeProblemas(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }


    public static void baixar(String endereco){
        try {
            desativarDeteccaoDeProblemas();
            URL url = new URL(endereco);
            HttpURLConnection conexaoHttp = (HttpURLConnection) url.openConnection();
            conexaoHttp.connect();
            FileOutputStream fileOutputStream = new FileOutputStream("/sdcard/LaudoOnline/laudoonline.pdf");
            InputStream inputStream = conexaoHttp.getInputStream();

            byte[] buffer = new byte[1024];
            int tamanhoDoBuffer;
            while ((tamanhoDoBuffer = inputStream.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, tamanhoDoBuffer);
            }

            fileOutputStream.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Void doInBackground(String... params) {
        baixar(params[0].toString());
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialogoDeProgresso = ProgressDialog.show(contexto, "Aguarde ...", "Enquanto o Laudo é carregado.", true);
        dialogoDeProgresso.setCancelable(true);
        dialogoDeProgresso.show();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        dialogoDeProgresso.dismiss();
        ((Laudo)contexto).abrir();
    }


}
