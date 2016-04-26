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

    private static final String DIRETORIO_SD_CARD = "/sdcard/LaudoOnline/laudoonline.pdf";
    private Context contexto;
    private ProgressDialog dialogoDeProgresso;


    public LaudoOnline(Context contexto) {
        this.contexto = contexto;
        dialogoDeProgresso = new ProgressDialog(contexto);
    }

    public LaudoOnline() {
    }

    private static void desativarDeteccaoDeProblemas(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialogoDeProgresso = ProgressDialog.show(contexto, "Aguarde ...", "Enquanto o Laudo é carregado.", true);
        dialogoDeProgresso.setCancelable(true);
        dialogoDeProgresso.show();
        desativarDeteccaoDeProblemas();
    }

    @Override
    protected Void doInBackground(String... parametros) {
        baixar(parametros[0].toString(), DIRETORIO_SD_CARD);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        dialogoDeProgresso.dismiss();
        ((Laudo)contexto).abrir();

    }

    public void baixar(String endereco,String diretorioOndeDeveSerSalvoOLaudo){
        try {

            URL url = new URL(endereco);
            HttpURLConnection conexaoHttp = (HttpURLConnection) url.openConnection();
            conexaoHttp.connect();
            FileOutputStream fileOutputStream = new FileOutputStream(diretorioOndeDeveSerSalvoOLaudo);
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


}
