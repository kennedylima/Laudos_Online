package br.com.metodoi.laudos_online;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.drakeet.materialdialog.MaterialDialog;


public class Laudo extends Activity {

    @Bind(R.id.codigo)
    EditText codigo;

    @Bind(R.id.chave)
    EditText chave;


    private static final int CODIGO_DO_PEDIDO_DE_PERMISSAO = 128;
    private MaterialDialog janelaDeDialogo;
    private Diretorio diretorio = new Diretorio();
    public static Boolean cancelado = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acessar);
        ButterKnife.bind(this);
    }

   @OnClick(R.id.botaoAcessar)
    public void acessar(){
       cancelado = false;
       solicitarPermissaoParaCriarPastaNoSdCard();
       solicitarPermissaoParaCriarArquivosNaPastaLaudoOnline();
       criarPastaLaudoOnline();
       verificarSeOsCamposForamPreenchidos();
   }

    private void verificarSeOsCamposForamPreenchidos() {
        if(codigo.getText().length() == 0 || chave.getText().length() == 0) {
            Toast.makeText(this, "Os campos 'Chave' e 'Codigo' devem ser preenchidos! ", Toast.LENGTH_SHORT).show();
        }else{
            if(arquivoExiste()){
                abrir();
            }else {
                baixar();
            }

        }
    }

    private void baixar()  {
        String [] parametros = {getURL()};
        new LaudoOnline(this).execute(parametros);

    }

    public void criarPastaLaudoOnline(){
        String sdCardMontado = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(sdCardMontado)) {
            File pastaLaudoOnline = new File(diretorio.getPastaLaudoOnline());
            if (!pastaLaudoOnline.exists()) {
                pastaLaudoOnline.mkdir();
            }
        }
    }

    public  void abrir(){

         if(cancelado == true){
             Toast.makeText(this, "Operação Cancelada! ", Toast.LENGTH_SHORT).show();

         }else if(!arquivoValido()){
            Toast.makeText(this, "Código ou Chave Inválido! ", Toast.LENGTH_SHORT).show();

         }else {
                 Intent intent = new Intent(Intent.ACTION_VIEW);
                 File diretorioDoArquivoLaudoOnlinePDF = new File(getDiretorioDoArquivoLaudoOnlinePDF());
                 intent.setDataAndType(Uri.fromFile(diretorioDoArquivoLaudoOnlinePDF), "application/pdf");
                 intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                 startActivity(intent);
                 limparCampos();
         }
    }

    private boolean arquivoValido() {
        renomear(diretorio.getPastaLaudoOnline(), codigo.getText().toString());
        File laudo = new File(getDiretorioDoArquivoLaudoOnlinePDF());
        final int HUM_MEGA_BYTE = 1024;
        if(laudo.length() > HUM_MEGA_BYTE){
            return true;
        }else{
            laudo.delete();
            limparCampos();
        }
        return false;
    }


    private void limparCampos() {
        codigo.setText("");
        chave.setText("");
    }

    private boolean arquivoExiste() {
        return new File(getDiretorioDoArquivoLaudoOnlinePDF()).exists();
    }

    public void solicitarPermissaoParaCriarPastaNoSdCard(){

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

    public void solicitarPermissaoParaCriarArquivosNaPastaLaudoOnline(){

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

    public void renomear(String diretorio, String codigo){
        File laudo = new File(diretorio,"laudoonline.pdf");
        File laudoRenomeado = new File(diretorio,"Laudo - "+codigo+".pdf");
        laudo.renameTo(laudoRenomeado);
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

    private String getURL(){
        String codigoInformado = codigo.getText().toString();
        String chaveInformada = chave.getText().toString();
        return getEndereco()+"codigo="+codigoInformado+"&chave="+chaveInformada;
    }

    private String getEndereco(){
        return "http://laudoonline.clinicascope.com.br/laudoonline.php?";
    }

    private String getDiretorioDoArquivoLaudoOnlinePDF(){
        return diretorio.getPastaLaudoOnline()+"Laudo - "+codigo.getText()+".pdf";
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
