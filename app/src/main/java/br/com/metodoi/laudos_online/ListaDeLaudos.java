package br.com.metodoi.laudos_online;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
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
                File diretorioDoArquivoLaudoOnlinePDF = new File(diretorio.getPastaLaudoOnline()+item.getNomeDoArquivo());
                intent.setDataAndType(Uri.fromFile(diretorioDoArquivoLaudoOnlinePDF), "application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });
    }

    private void carregarLaudos(){
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



    public Bitmap getIcone(){
        return BitmapFactory.decodeResource(getResources(),R.drawable.iconepdf);
    }

}
