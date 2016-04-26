package br.com.metodoi.laudos_online;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity {

    @Bind(android.R.id.tabhost)
    TabHost tabHost;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        TabHost.TabSpec primeiraAba = tabHost.newTabSpec("Primeira Aba");
        primeiraAba.setIndicator("Acessar");
        primeiraAba.setContent(new Intent(this, Laudo.class));

        TabHost.TabSpec segundaAba = tabHost.newTabSpec("Segunda Aba");
        segundaAba.setIndicator("Laudos");
        segundaAba.setContent(new Intent(this, ListaDeLaudos.class));

        tabHost.addTab(primeiraAba);
        tabHost.addTab(segundaAba);

        for(int i =0 ; i<2;i++) {
            TextView textView = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            textView.setTextColor(Color.WHITE);
            textView.setTextSize(14);
            textView.setTypeface(Typeface.DEFAULT_BOLD);
        }

    }
}
