package br.com.metodoi.laudos_online;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.drakeet.materialdialog.MaterialDialog;


public class Laudo extends AppCompatActivity  {

    @Bind(R.id.codigo)
    EditText codigo;

    @Bind(R.id.chave)
    EditText chave;

    private static final int CODIGO_DO_PEDIDO_DE_PERMISSAO = 128;
    private MaterialDialog janelaDeDialogo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laudo_online);
        ButterKnife.bind(this);
    }

   @OnClick(R.id.botaoAcessar)
    public void acessar(){
       verificarPermissaoParaCriarPastaNoSdCard();
       verificarPermissaoParaCriarArquivosNaPastaLaudoOnline();
       criarPastaLaudoOnline();

       if(verificarSeOsCamposForamPreenchidos() == true) {
           baixar();
       }
}

    private boolean verificarSeOsCamposForamPreenchidos() {
        if(codigo.getText().length() == 0 || chave.getText().length() == 0){
            Toast.makeText(this,"Os campos 'Chave' e 'Codigo' devem ser preenchidos! ",Toast.LENGTH_SHORT).show();
            return false;
        }else{
            return true;
        }

    }

    private void baixar()  {
        String [] parametros = {getURL()};
        new LaudoOnline(this).execute(parametros);

    }

    private String getURL(){
        String codigoInformado = codigo.getText().toString();
        String chaveInformada = chave.getText().toString();
        return getEndereco()+"codigo="+codigoInformado+"&chave="+chaveInformada;
    }

    private String getEndereco(){
        return "http://laudoonline.clinicascope.com.br/laudoonline.php?";
    }

    public void criarPastaLaudoOnline(){
        String sdCardMontado = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(sdCardMontado)) {
            File pastaLaudoOnline = new File(getDiretorioDaPastaLaudoOnline());
            if (!pastaLaudoOnline.exists()) {
                pastaLaudoOnline.mkdir();
            }
        }
    }


    public  void abrir(){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        File diretorioDoArquivoLaudoOnlinePDF = new File(getDiretorioDoArquivoLaudoOnlinePDF());
        intent.setDataAndType(Uri.fromFile(diretorioDoArquivoLaudoOnlinePDF), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    private  String getDiretorioDaPastaLaudoOnline(){
        return "/sdcard/LaudoOnline/";
    }

    private String getDiretorioDoArquivoLaudoOnlinePDF(){
        return getDiretorioDaPastaLaudoOnline()+"laudoonline.pdf";
    }

    private void verificarPermissaoParaCriarPastaNoSdCard(){

        if( ContextCompat.checkSelfPermission( this, Manifest.permission.WRITE_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED ){

            if( ActivityCompat.shouldShowRequestPermissionRationale( this, Manifest.permission.WRITE_EXTERNAL_STORAGE ) ){
                janelaDeDialogo(" WRITE_EXTERNAL_STORAGE - É necessário autorização para criar a pasta Laudo Online .",
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE});
            }
            else{
                ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CODIGO_DO_PEDIDO_DE_PERMISSAO );
            }
        }
        else{
            criarPastaLaudoOnline();
        }

    }

    private void verificarPermissaoParaCriarArquivosNaPastaLaudoOnline(){

        if( ContextCompat.checkSelfPermission( this, Manifest.permission.READ_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED ){
            if( ActivityCompat.shouldShowRequestPermissionRationale( this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                janelaDeDialogo("READ_EXTERNAL_STORAGE- É necessário autorização para ler os arquivos da pasta Laudo Online.",
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE} );
            }
            else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CODIGO_DO_PEDIDO_DE_PERMISSAO);
            }
        }
        else {
            criarPastaLaudoOnline();
        }
    }

    private void janelaDeDialogo( String mensagem, final String[] permissoes ){
        janelaDeDialogo = new MaterialDialog(this)
                .setTitle("Permisao")
                .setMessage( mensagem )
                .setPositiveButton("Ok", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ActivityCompat.requestPermissions(Laudo.this, permissoes, CODIGO_DO_PEDIDO_DE_PERMISSAO);
                        janelaDeDialogo.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        janelaDeDialogo.dismiss();
                    }
                });
        janelaDeDialogo.show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode){
            case CODIGO_DO_PEDIDO_DE_PERMISSAO:
                for (int i =0; i< permissions.length;i++) {
                    if (permissions[i].equalsIgnoreCase(Manifest.permission.WRITE_EXTERNAL_STORAGE) &&
                            grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        criarPastaLaudoOnline();

                    }else if( permissions[i].equalsIgnoreCase( Manifest.permission.READ_EXTERNAL_STORAGE)
                            && grantResults[i] == PackageManager.PERMISSION_GRANTED ){
                        criarPastaLaudoOnline();
                    }
                }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}