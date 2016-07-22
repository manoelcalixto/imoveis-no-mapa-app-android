package br.com.imoveisnomapa.imoveisnomapa.service;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;


import br.com.imoveisnomapa.imoveisnomapa.model.Estado;

/**
 * Created by calixto on 14/02/16.
 */
public class MapaService {

    ArrayList<Estado> estados;
    Gson gson = new Gson();

    public ArrayList<Estado> buscarEstados (Context contexto){

        String url = "http://45.55.214.198/buscarEstados";

        // Request a string response
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            estados = gson.fromJson(response, new TypeToken<ArrayList<Estado>>() {
                            }.getType());
                            String debug = "123";
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        // Result handling
                        System.out.println(response.substring(0,100));

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                // Error handling
                System.out.println("Something went wrong!");
                error.printStackTrace();

            }
        });

        Volley.newRequestQueue(contexto).add(stringRequest);


        return estados;
    }

}
