package br.com.metodoi.laudos_online.ApiWeb;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by Kennedy on 15/04/2016.
 */
public class ApiWeb {
    public static final String BASE_URL = "http://laudoonline.clinicascope.com.br/laudoonline.php";


    public static Rotas rotasApi;

    public static Rotas getRotas() {
        if (rotasApi == null) {

            //Serializador Client  Json
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                    .create();

            //Inicializa o Rest Client
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .setConverter(new GsonConverter(gson))
                    .setEndpoint(String.format("%s", BASE_URL))
                    .build();

            //Objeto rest
            rotasApi = restAdapter.create(Rotas.class);
        }

        return rotasApi;
    }

    public interface Rotas{


        @GET("{codigo}/{chave}")
        public void acessar(@Path("codigo")String codigo, @Path("chave")String chave);




    }
}
