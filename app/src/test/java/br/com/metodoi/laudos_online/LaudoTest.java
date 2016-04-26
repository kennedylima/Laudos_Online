package br.com.metodoi.laudos_online;


import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;

public class LaudoTest {

    private static final String DIRETORIO_ONDE_DEVE_SER_SALVO_O_LAUDO_NA_MAQUINA = "/C:/Users/Kennedy/Downloads/TesteLaudoOnline/";
    private static final String DIRETORIO_LAUDO_ONLINE =  "/C:/Users/Kennedy/Downloads/TesteLaudoOnline/laudoonline.pdf";

    @Test
    public void deve_baixar_um_laudo() throws FileNotFoundException {
        LaudoOnline laudoOnline = new LaudoOnline();
        String codigo = "78985";
        String chave = "e864e";
        String url = getEndereco()+"codigo="+codigo+"&chave="+chave;
        File laudo = new File(DIRETORIO_LAUDO_ONLINE);

        laudoOnline.baixar(url,DIRETORIO_LAUDO_ONLINE);

        Assert.assertTrue(laudo.exists());
    }

    @Test
    public void deve_renomear_o_laudo(){
        String codigo = "78985";
        Laudo laudo = new Laudo();
        File laudoOnline = new File(DIRETORIO_ONDE_DEVE_SER_SALVO_O_LAUDO_NA_MAQUINA+"laudo"+codigo+".pdf");
        laudo.renomear(DIRETORIO_ONDE_DEVE_SER_SALVO_O_LAUDO_NA_MAQUINA, codigo);

        Assert.assertTrue(laudoOnline.exists());

    }

    private String getEndereco(){
        return "http://laudoonline.clinicascope.com.br/laudoonline.php?";
    }
}