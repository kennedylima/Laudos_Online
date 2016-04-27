package br.com.metodoi.laudos_online;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.StrictMode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class LaudoOnline extends AsyncTask<String, Integer, Void> {

    private static final String DIRETORIO_SD_CARD = "/sdcard/LaudoOnline/laudoonline.pdf";
    private Context contexto;
    private ProgressDialog dialogoDeProgresso;
    HttpURLConnection conexaoHttp;
    private static final int HUM_MEGA_BYTE= 1024;
    private static final double TRINTA_E_CINCO_KILOBYTE = 35840;

    public LaudoOnline(Context contexto) {
        this.contexto = contexto;
        dialogoDeProgresso = new ProgressDialog(contexto);
    }

    public LaudoOnline() {
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialogoDeProgresso = new ProgressDialog(contexto);
        dialogoDeProgresso.setTitle("Carregando Laudo ...");
        dialogoDeProgresso.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialogoDeProgresso.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                conexaoHttp.disconnect();
                onCancelled();
                excluirArquivo();
                Laudo.cancelado =true;
            }
        });
        dialogoDeProgresso.setProgress(0);
        dialogoDeProgresso.show();
        desativarDeteccaoDeProblemas();
    }

    @Override
    protected Void doInBackground(String... parametros) {
        baixar(parametros[0].toString(), DIRETORIO_SD_CARD);
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        int progresso = (values[0]);
        dialogoDeProgresso.setProgress(progresso);

    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        dialogoDeProgresso.dismiss();
        ((Laudo) contexto).abrir();
    }

    public void baixar(String endereco,String diretorioOndeDeveSerSalvoOLaudo){

        try {

            URL url = new URL(endereco);
            conexaoHttp = (HttpURLConnection) url.openConnection();
            conexaoHttp.connect();
            FileOutputStream fileOutputStream = new FileOutputStream(diretorioOndeDeveSerSalvoOLaudo);
            InputStream inputStream = conexaoHttp.getInputStream();


            byte[] buffer = new byte[HUM_MEGA_BYTE];
            int tamanhoDoBuffer;
            int valorDoProgresso = 1;
            int quantidadeDeByteBaixado = 0;
            while ((tamanhoDoBuffer = inputStream.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, tamanhoDoBuffer);
                quantidadeDeByteBaixado += tamanhoDoBuffer;

                if(quantidadeDeByteBaixado > TRINTA_E_CINCO_KILOBYTE){
                    dialogoDeProgresso.setProgress(valorDoProgresso++);
                    quantidadeDeByteBaixado = 0;
                }
            }

            fileOutputStream.close();
            dialogoDeProgresso.setProgress(100);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void desativarDeteccaoDeProblemas(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    private void excluirArquivo() {
        File laudo = new File(DIRETORIO_SD_CARD);
        laudo.delete();
    }


}
