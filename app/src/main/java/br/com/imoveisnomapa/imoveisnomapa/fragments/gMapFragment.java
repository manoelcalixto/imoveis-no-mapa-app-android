package br.com.imoveisnomapa.imoveisnomapa.fragments;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import br.com.imoveisnomapa.imoveisnomapa.R;
import br.com.imoveisnomapa.imoveisnomapa.adapter.ImovelAdapter;
import br.com.imoveisnomapa.imoveisnomapa.model.Imovel;
import br.com.imoveisnomapa.imoveisnomapa.service.CustomRequest;

/**
 * Created by calixto on 13/02/16.
 */
public class gMapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap map;
    private ArrayList<Imovel> listaImoveis;
    private Gson gson = new Gson();
    private Bundle args;
    private ClusterManager<Imovel> mClusterManager;
    private SlidingUpPanelLayout slidingLayout;
    private Button btnHide;
    private ListView listImoveis;
    private ImovelAdapter imovelAdapter;
    private String operacao;
    private boolean filtro = false;

    private boolean filtroEstado;
    private boolean filtroCidade;
    private boolean filtroBairro;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gmaps, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listImoveis = (ListView)view.findViewById(R.id.list_imoveis);

        slidingLayout = (SlidingUpPanelLayout)view.findViewById(R.id.sliding_layout);

        btnHide = (Button) view.findViewById(R.id.btn_hide);

        operacao = getArguments().getString("operacao");

        btnHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
            }
        });

        slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);

        slidingLayout.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {

            }

            @Override
            public void onPanelCollapsed(View panel) {

            }

            @Override
            public void onPanelExpanded(View panel) {

            }

            @Override
            public void onPanelAnchored(View panel) {

            }

            @Override
            public void onPanelHidden(View panel) {

            }
        });

        Bundle args = getArguments();
        this.args = args;

        MapFragment fragment = (MapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        fragment.getMapAsync(this);
    }

    public void buscarImoveisPorGeoLocalizacao(double northeastLatitude, double northeastLongitude, double southwestLatitude, double southwestLongitude) {
        String url = "http://192.168.1.5:5000/buscarImoveisPorGeoLocalizacao";

        JSONObject requestJsonObject = new JSONObject();
        try {
            requestJsonObject.put("northeastLatitude", northeastLatitude);
            requestJsonObject.put("northeastLongitude", northeastLongitude);
            requestJsonObject.put("southwestLatitude", southwestLatitude);
            requestJsonObject.put("southwestLongitude", southwestLongitude);

            requestJsonObject.put("operacao", operacao);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CustomRequest jsonObjReq = new CustomRequest(Request.Method.POST, url, requestJsonObject, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    listaImoveis = gson.fromJson(response.toString(), new TypeToken<ArrayList<Imovel>>() {
                    }.getType());

                    mClusterManager.clearItems();
                    mClusterManager.setRenderer(new OwnIconRendered(getActivity(), map, mClusterManager));
                    mClusterManager.addItems(listaImoveis);

                    mClusterManager.cluster();

                            mClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<Imovel>() {
                                @Override
                                public boolean onClusterClick(Cluster<Imovel> cluster) {
                                    slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                                    imovelAdapter = new ImovelAdapter(getActivity(), (List) cluster.getItems());
                                    listImoveis.setAdapter(imovelAdapter);
                                    return false;
                                }
                            });

                    mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<Imovel>() {
                        @Override
                        public boolean onClusterItemClick(Imovel imovelItem) {
                            List<Imovel> imoveis = new ArrayList<Imovel>();
                            imoveis.add(imovelItem);
                            slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                            imovelAdapter = new ImovelAdapter(getActivity(), imoveis);
                            listImoveis.setAdapter(imovelAdapter);
                            return false;
                        }
                    });

                    //map.setOnCameraChangeListener(mClusterManager);
                    map.setOnMarkerClickListener(mClusterManager);

                    String debug = "123";
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

            }
        });

        Volley.newRequestQueue(getActivity()).add(jsonObjReq);

    }

    public void buscarImoveisPorFiltro(String operacao, String tipo, String preco, String quarto, String estado, String cidade, String bairro) {
        String url = "http://192.168.1.5:5000/buscarImoveisPorFiltro";

        JSONObject requestJsonObject = new JSONObject();
        try {
            mClusterManager = new ClusterManager<Imovel>(getActivity(), map);

            if (!operacao.equals("")){
                requestJsonObject.put("operacao", operacao);
            }
            if (!tipo.equals("")){
                requestJsonObject.put("tipo", tipo);
            }
            if (!preco.equals("")){
                requestJsonObject.put("preco", preco);
            }
            if (!quarto.equals("")){
                requestJsonObject.put("quarto", quarto);
            }
            if (!estado.equals("")){
                requestJsonObject.put("estado", estado);
                filtroEstado = true;
            }
            if (!cidade.equals("")){
                requestJsonObject.put("cidade", cidade);
                filtroCidade = true;
            }
            if (!bairro.equals("")){
                requestJsonObject.put("bairro", bairro);
                filtroBairro = true;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        CustomRequest jsonObjReq = new CustomRequest(Request.Method.POST, url, requestJsonObject, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {

                    listaImoveis = gson.fromJson(response.toString(), new TypeToken<ArrayList<Imovel>>() {
                    }.getType());

                    mClusterManager.clearItems();
                    mClusterManager.setRenderer(new OwnIconRendered(getActivity(), map, mClusterManager));
                    mClusterManager.addItems(listaImoveis);

                    mClusterManager.cluster();

                    mClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<Imovel>() {
                        @Override
                        public boolean onClusterClick(Cluster<Imovel> cluster) {
                            slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                            imovelAdapter = new ImovelAdapter(getActivity(), (List) cluster.getItems());
                            listImoveis.setAdapter(imovelAdapter);
                            return false;
                        }
                    });

                    mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<Imovel>() {
                        @Override
                        public boolean onClusterItemClick(Imovel imovelItem) {
                            List<Imovel> imoveis = new ArrayList<Imovel>();
                            imoveis.add(imovelItem);
                            slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                            imovelAdapter = new ImovelAdapter(getActivity(), imoveis);
                            listImoveis.setAdapter(imovelAdapter);
                            return false;
                        }
                    });

                    map.setOnMarkerClickListener(mClusterManager);

                    if (filtroBairro){
                        LatLng latLng = new LatLng(args.getDouble("latitudeBairro"), args.getDouble("longitudeBairro"));

                        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    } else if (filtroCidade){
                        LatLng latLng = new LatLng(args.getDouble("latitudeCidade"), args.getDouble("longitudeCidade"));

                        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    } else if (filtroEstado){
                        LatLng latLng = new LatLng(args.getDouble("latitudeEstado"), args.getDouble("longitudeEstado"));

                        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    }

                    filtro = true;

                    String debug = "123";
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

            }
        });

        Volley.newRequestQueue(getActivity()).add(jsonObjReq);

    }

    private class OwnIconRendered extends DefaultClusterRenderer<Imovel> {

        public OwnIconRendered(Context context, GoogleMap map, ClusterManager<Imovel> clusterManager) {
            super(context, map, clusterManager);
        }

        @Override
        protected boolean shouldRenderAsCluster(Cluster<Imovel> cluster) {
            //start clustering if at least 2 items overlap
            return cluster.getSize() > 1;
        }

        @Override
        protected void onBeforeClusterItemRendered(Imovel item,
                                                   MarkerOptions markerOptions) {


            IconGenerator iconFactory = new IconGenerator(getActivity().getApplicationContext());

            iconFactory.setStyle(IconGenerator.STYLE_GREEN);


            BitmapDescriptor icone2 = BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(item.getPreco()));

            //MarkerOptions tp = new MarkerOptions().position(item.getmPosition()).icon(BitmapDescriptorFactory.fromBitmap(icon2));
            Locale ptBr = new Locale("pt", "BR");
            NumberFormat format = NumberFormat.getCurrencyInstance(ptBr);
            Double preco = Double.parseDouble(item.getPreco());

            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(format.format(preco))));

            super.onBeforeClusterItemRendered(item, markerOptions);

        }


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;

        googleMap.clear();
        mClusterManager = new ClusterManager<Imovel>(getActivity(), map);

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        map.setMyLocationEnabled(true);

        filtro = getArguments().getBoolean("filtro");

        if (filtro == true){
            String operacao = args.getString("operacao");
            String tipo = args.getString("tipo");
            String preco = args.getString("preco");
            String quarto = args.getString("quarto");
            String estado = args.getString("estado");
            String cidade = args.getString("cidade");
            String bairro = args.getString("bairro");

            buscarImoveisPorFiltro(operacao, tipo, preco, quarto, estado, cidade, bairro);
        } else {
            LatLng latLng = new LatLng(args.getDouble("latitude"), args.getDouble("longitude"));
            map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        }

        //map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-33.8696, 151.2094), 10));

        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                if (!filtro) {
                    LatLngBounds latLngBounds = map.getProjection().getVisibleRegion().latLngBounds;
                    buscarImoveisPorGeoLocalizacao(latLngBounds.northeast.latitude, latLngBounds.northeast.longitude, latLngBounds.southwest.latitude, latLngBounds.southwest.longitude);
                }

                filtro = false;

            }
        });

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
            }
        });

        String debug = "123";
    }





}
