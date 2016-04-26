package br.com.metodoi.laudos_online;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Kennedy on 25/04/2016.
 */
public class LaudoAdapter extends ArrayAdapter<ItensLaudo> {

    private final Context context;
    private final int resource;
    private final List<ItensLaudo> nomeDosArquivos;

    public LaudoAdapter(Context context, int resource,List<ItensLaudo> nomeDosArquivos) {
        super(context, resource,nomeDosArquivos);
        this.context = context;
        this.resource = resource;
        this.nomeDosArquivos = nomeDosArquivos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = View.inflate(getContext(),R.layout.layout_lista_laudos,null);
            ButterKnife.bind(viewHolder,convertView);
            convertView.setTag(viewHolder);

        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final ItensLaudo item = getItem(position);

        if(item != null) {
            viewHolder.nomeDoArquivo.setTextSize(16);
            viewHolder.nomeDoArquivo.setTypeface(Typeface.DEFAULT_BOLD);

            viewHolder.nomeDoArquivo.setText(item.getNomeDoArquivo());
            viewHolder.iconeDoArquivo.setImageBitmap(item.getIcone());
        }

        return convertView;
    }

    public class ViewHolder{

        @Bind(R.id.nomeDoArquivo)
        TextView nomeDoArquivo;

        @Bind(R.id.iconeDoarquivo)
        ImageView iconeDoArquivo;



    }
}
