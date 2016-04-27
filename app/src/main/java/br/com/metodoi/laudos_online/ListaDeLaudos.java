package br.com.metodoi.laudos_online;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ListaDeLaudos extends Activity {

    @Bind(R.id.listaDeLaudos)
    ListView listaDeLaudos;

    private static List<ItensLaudo> laudos = new ArrayList<ItensLaudo>();
    private Diretorio diretorio = new Diretorio();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laudos);
        ButterKnife.bind(this);
        carregarLaudos();

        listaDeLaudos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ItensLaudo item = (ItensLaudo) parent.getItemAtPosition(position);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                File diretorioDoArquivoLaudoOnlinePDF = new File(diretorio.getPastaLaudoOnline() + item.getNomeDoArquivo());
                intent.setDataAndType(Uri.fromFile(diretorioDoArquivoLaudoOnlinePDF), "application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });

        listaDeLaudos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final ItensLaudo item = (ItensLaudo) parent.getItemAtPosition(position);
                AlertDialog alert = new AlertDialog.Builder(ListaDeLaudos.this)
                        .setTitle("Excluir")
                        .setMessage("Deseja realmente excluir?")
                        .setNegativeButton("Não", null)
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, final int i) {
                                remover(item.getNomeDoArquivo());
                            }
                        }).show();
                return  true;
            }
        });
    }

    private void remover(String arquivo) {
        File laudo = new File(getDiretorioDo(arquivo));
        laudo.delete();
        carregarLaudos();

    }

    private void carregarLaudos(){
        laudos = new ArrayList<ItensLaudo>();
        File[] diretorioDosLaudos = new File("/sdcard/LaudoOnline/").listFiles();

        if(diretorioDosLaudos != null) {

            for (int i = 0; i < diretorioDosLaudos.length; i++) {
                String nomeDoArquivo = diretorioDosLaudos[i].getName().toString();
                ItensLaudo item = new ItensLaudo(getIcone(), nomeDoArquivo);
                laudos.add(item);
            }

            LaudoAdapter laudoAdapter = new LaudoAdapter(this, 0, laudos);
            laudoAdapter.notifyDataSetChanged();
            listaDeLaudos.setAdapter(laudoAdapter);
        }else{
            Toast.makeText(this, "Você não tem nenhum Laudo carregado ! ", Toast.LENGTH_SHORT).show();
        }

    }

    private  String getDiretorioDo(String arquivo){
        return "/sdcard/LaudoOnline/"+arquivo;
    }

    public Bitmap getIcone(){
        return BitmapFactory.decodeResource(getResources(),R.drawable.iconepdf);
    }

}
