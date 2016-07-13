package br.com.imoveisnomapa.imoveisnomapa;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.GoogleMap;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.maps.android.clustering.ClusterManager;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.imoveisnomapa.imoveisnomapa.adapter.ImovelAdapter;
import br.com.imoveisnomapa.imoveisnomapa.model.Bairro;
import br.com.imoveisnomapa.imoveisnomapa.model.Cidade;
import br.com.imoveisnomapa.imoveisnomapa.model.Estado;
import br.com.imoveisnomapa.imoveisnomapa.model.Imovel;
import br.com.imoveisnomapa.imoveisnomapa.model.Preco;
import br.com.imoveisnomapa.imoveisnomapa.model.Quarto;
import br.com.imoveisnomapa.imoveisnomapa.model.Tipo;
import br.com.imoveisnomapa.imoveisnomapa.service.CustomRequest;

public class FiltroActivity extends AppCompatActivity implements View.OnClickListener {

    private Spinner spinnerEstados;
    private Spinner spinnerCidades;
    private Spinner spinnerBairros;
    private Spinner spinnerTipos;
    private Spinner spinnerQuartos;
    private Spinner spinnerPrecos;

    ArrayList<Estado> listaestados;
    ArrayList<Cidade> listacidades;
    ArrayList<Bairro> listabairros;
    ArrayList<Tipo> listatipos;

    Estado estado;
    Cidade cidade;
    Bairro bairro;
    Tipo tipo;
    Quarto quarto;
    Preco preco;

    GoogleMap map;
    Bundle args;
    private ClusterManager<Imovel> mClusterManager;
    private SlidingUpPanelLayout slidingLayout;
    Button btnHide;
    ListView listImoveis;
    ImovelAdapter imovelAdapter;
    private String operacao;

    Button btnBuscar;

    FragmentManager fm;

    Gson gson = new Gson();
    private boolean userIsInteracting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_filter);
        spinnerEstados = (Spinner)findViewById(R.id.spinnerEstados);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        operacao = bundle.getString("operacao");

        fm = getFragmentManager();

        buscarEstados();
        try {
            buscarTipos();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        buscarPrecos();
        buscarQuartos();

        btnBuscar = (Button) findViewById(R.id.btnBuscar);

        btnBuscar.setOnClickListener(this);


    }

    private void buscarTipos() throws JSONException {

        spinnerTipos = (Spinner)findViewById(R.id.spinnerTipo);

        String url = "http://192.168.1.5:5000/buscarTipos";


        JSONObject requestJsonObject = new JSONObject();
        requestJsonObject.put("operacao", operacao);

        CustomRequest jsonObjReq = new CustomRequest(Request.Method.POST, url, requestJsonObject, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    listatipos = gson.fromJson(response.toString(), new TypeToken<ArrayList<Tipo>>() {
                    }.getType());

                    Tipo tipoVazio = new Tipo();
                    tipoVazio.setTipo("");

                    listatipos.add(0, tipoVazio);

                    spinnerTipos.setAdapter(new ArrayAdapter<Tipo>(FiltroActivity.this, android.R.layout.simple_spinner_item, listatipos));

                    spinnerTipos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            tipo = (Tipo) spinnerTipos.getSelectedItem();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    String debug = "123";
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        Volley.newRequestQueue(this).add(jsonObjReq);

    }

    private void buscarPrecos() {

        spinnerPrecos = (Spinner)findViewById(R.id.spinnerPreco);

        List<Preco> precos = new ArrayList<Preco>();

        if(operacao.equals("Aluguel")){
            precos.add(new Preco(""));
            precos.add(new Preco("250"));
            precos.add(new Preco("500"));
            precos.add(new Preco("1000"));
            precos.add(new Preco("2000"));
            precos.add(new Preco("4000"));
        } else {
            precos.add(new Preco(""));
            precos.add(new Preco("10000"));
            precos.add(new Preco("20000"));
            precos.add(new Preco("40000"));
            precos.add(new Preco("80000"));
            precos.add(new Preco("160000"));
            precos.add(new Preco("320000"));
            precos.add(new Preco("640000"));
            precos.add(new Preco("1280000"));
        }



        spinnerPrecos.setAdapter(new ArrayAdapter<Preco>(FiltroActivity.this, android.R.layout.simple_spinner_item, precos));

        spinnerPrecos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                preco = (Preco) spinnerPrecos.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void buscarQuartos() {

        spinnerQuartos = (Spinner)findViewById(R.id.spinnerQuartos);

        List<Quarto> quartos = new ArrayList<Quarto>();

        quartos.add(new Quarto(""));
        quartos.add(new Quarto("1"));
        quartos.add(new Quarto("2"));
        quartos.add(new Quarto("3"));
        quartos.add(new Quarto("4"));

        spinnerQuartos.setAdapter(new ArrayAdapter<Quarto>(FiltroActivity.this, android.R.layout.simple_spinner_item, quartos));

        spinnerQuartos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                quarto = (Quarto) spinnerQuartos.getSelectedItem();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("posicaoEstado", spinnerEstados.getSelectedItemPosition());
        outState.putInt("posicaoCidade", spinnerCidades.getSelectedItemPosition());
        outState.putInt("posicaoBairro", spinnerBairros.getSelectedItemPosition());
        outState.putInt("posicaoTipo", spinnerTipos.getSelectedItemPosition());
        outState.putInt("posicaoQuarto", spinnerQuartos.getSelectedItemPosition());
        outState.putInt("posicaoPreco", spinnerPrecos.getSelectedItemPosition());

        super.onSaveInstanceState(outState);

    }

    private void buscarEstados (){


        String url = "http://192.168.1.5:5000/buscarEstados";

        CustomRequest jsonObjReq = new CustomRequest(Request.Method.POST, url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    listaestados = gson.fromJson(response.toString(), new TypeToken<ArrayList<Estado>>() {
                    }.getType());

                    Estado estadoVazio = new Estado();
                    estadoVazio.setEstado("");

                    listaestados.add(0, estadoVazio);

                    spinnerEstados.setAdapter(new ArrayAdapter<Estado>(FiltroActivity.this, android.R.layout.simple_spinner_item, listaestados));

                    spinnerEstados.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                            if (spinnerEstados.getSelectedItem() != null) {
                                estado = (Estado) spinnerEstados.getSelectedItem();
                                try {
                                    buscarCidades(estado.getEstado());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    String debug = "123";
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        Volley.newRequestQueue(this).add(jsonObjReq);
    }

    private void buscarCidades(String estado) throws JSONException {
        spinnerCidades = (Spinner)findViewById(R.id.spinnerCidades);

        String url = "http://192.168.1.5:5000/buscarCidadesPorEstado";

        JSONObject requestJsonObject = new JSONObject();
        requestJsonObject.put("estado", estado);


        final HashMap<String, String> params = new HashMap<String, String>();
        params.put("estado", estado);

        CustomRequest jsonObjReq = new CustomRequest(Request.Method.POST, url, requestJsonObject, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    listacidades = gson.fromJson(response.toString(), new TypeToken<ArrayList<Cidade>>() {
                    }.getType());

                    Cidade cidadeVazia = new Cidade();
                    cidadeVazia.setCidade("");

                    listacidades.add(0, cidadeVazia);

                    spinnerCidades.setAdapter(new ArrayAdapter<Cidade>(FiltroActivity.this, android.R.layout.simple_spinner_item, listacidades));

                    spinnerCidades.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                            if (spinnerCidades.getSelectedItem() != null) {
                                cidade = (Cidade) spinnerCidades.getSelectedItem();
                                try {
                                    userIsInteracting = true;
                                    buscarBairros(cidade.getCidade());

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    String debug = "123";
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        Volley.newRequestQueue(this).add(jsonObjReq);

    }

    private void buscarBairros(String cidade) throws JSONException {
        spinnerBairros = (Spinner) findViewById(R.id.spinnerBairros);

        String url = "http://192.168.1.5:5000/buscarBairrosPorEstadoECidade";

        JSONObject requestJsonObject = new JSONObject();
        requestJsonObject.put("cidade", cidade);

        CustomRequest jsonObjReq = new CustomRequest(Request.Method.POST, url, requestJsonObject, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    listabairros = gson.fromJson(response.toString(), new TypeToken<ArrayList<Bairro>>() {
                    }.getType());

                    Bairro bairroVazio = new Bairro();
                    bairroVazio.setBairro("");

                    listabairros.add(0, bairroVazio);
                    spinnerBairros.setAdapter(new ArrayAdapter<Bairro>(FiltroActivity.this, android.R.layout.simple_spinner_item, listabairros));

                    userIsInteracting = true;

                    spinnerBairros.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                            if (spinnerBairros.getSelectedItem() != null) {
                                bairro = (Bairro) spinnerBairros.getSelectedItem();

                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        Volley.newRequestQueue(this).add(jsonObjReq);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        spinnerEstados.setSelection(savedInstanceState.getInt("posicaoEstado", 0));
        spinnerCidades.setSelection(savedInstanceState.getInt("posicaoCidade", 0));
        spinnerBairros.setSelection(savedInstanceState.getInt("posicaoBairro", 0));
        spinnerTipos.setSelection(savedInstanceState.getInt("posicaoTipo", 0));
        spinnerQuartos.setSelection(savedInstanceState.getInt("posicaoQuarto", 0));
        spinnerPrecos.setSelection(savedInstanceState.getInt("posicaoPreco", 0));
    }


    @Override
    public void onClick(View v) {

        View estadoSelectedView = spinnerEstados.getSelectedView();
        View cidadeSelectedView = spinnerCidades.getSelectedView();
        if (spinnerEstados.getSelectedItem().toString().trim().equals(""))
        {
            TextView estadoSelectedTextView = (TextView) estadoSelectedView;
            estadoSelectedTextView.setError("Escolha um estado");
        } else if(spinnerCidades.getSelectedItem().toString().trim().equals("")) {
            TextView estadoSelectedTextView = (TextView) cidadeSelectedView;
            estadoSelectedTextView.setError("Escolha uma cidade");
        }else {
            Intent intent = new Intent(this, MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putBoolean("filtro", true);
            bundle.putString("operacao", operacao);
            bundle.putString("tipo", tipo.getTipo());
            bundle.putString("quarto", quarto.getNome());
            bundle.putString("bairro", bairro.getBairro());
            bundle.putString("cidade", cidade.getCidade());
            bundle.putString("preco", preco.getNome());
            bundle.putString("estado", estado.getEstado());

            if (!estado.getEstado().equals("")){
                bundle.putDouble("latitudeEstado", Double.parseDouble(estado.getLatitude()));
                bundle.putDouble("longitudeEstado", Double.parseDouble(estado.getLongitude()));
            }
            if (!cidade.getCidade().equals("")){
                bundle.putDouble("latitudeCidade", Double.parseDouble(cidade.getLatitude()));
                bundle.putDouble("longitudeCidade", Double.parseDouble(cidade.getLongitude()));
            }
            if (!bairro.getBairro().equals("")){
                bundle.putDouble("latitudeBairro", Double.parseDouble(bairro.getLatitude()));
                bundle.putDouble("longitudeBairro", Double.parseDouble(bairro.getLongitude()));
            }

            intent.putExtras(bundle);

            startActivity(intent);
            finish();

        }
    }

    @Override
    public void onBackPressed() {

        this.finish();
    }
}


