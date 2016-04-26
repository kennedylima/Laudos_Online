package br.com.metodoi.laudos_online;

import android.graphics.Bitmap;


public class ItensLaudo {
    public Bitmap icone;
    public String nomeDoArquivo;

    public ItensLaudo(Bitmap icone, String nomeDoArquivo) {
        this.icone = icone;
        this.nomeDoArquivo = nomeDoArquivo;
    }

    public Bitmap getIcone() {
        return icone;
    }

    public String getNomeDoArquivo() {
        return nomeDoArquivo;
    }
}
