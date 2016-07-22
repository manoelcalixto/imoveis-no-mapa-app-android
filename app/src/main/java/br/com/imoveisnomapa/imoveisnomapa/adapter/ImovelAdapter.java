package br.com.imoveisnomapa.imoveisnomapa.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import br.com.imoveisnomapa.imoveisnomapa.R;
import br.com.imoveisnomapa.imoveisnomapa.model.Imovel;
import br.com.imoveisnomapa.imoveisnomapa.service.VolleySingleton;

/**
 * Created by calixto on 21/02/16.
 */
public class ImovelAdapter extends BaseAdapter {

    Context ctx;
    List<Imovel> imoveis;
    ImageLoader imageLoader;
    NetworkImageView networkImageView;
    String enderecoImagemProducao = "http://45.55.214.198:8080/img_upload/thumbs/big/";
    String enderecoImagemDesenvolvimento = "http://192.168.1.5:5000/data/images/";

    public ImovelAdapter(Context ctx, List<Imovel> imoveis){
        this.ctx = ctx;
        this.imoveis = imoveis;
    }

    @Override
    public int getCount() {
        return imoveis.size();
    }

    @Override
    public Object getItem(int position) {
        return imoveis.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Imovel imovel = imoveis.get(position);

        final View linha = LayoutInflater.from(ctx).inflate(R.layout.item_imovel, null);

        TextView txtTipoApartamento = (TextView)linha.findViewById(R.id.txtTipoApartamento);
        TextView txtPreco = (TextView)linha.findViewById(R.id.txtPreco);
        TextView txtEndereco = (TextView)linha.findViewById(R.id.txtEndereco);
        Button btnLink = (Button)linha.findViewById(R.id.btnLinkImovel);

        final BitmapDrawable[] ob = new BitmapDrawable[1];

        Locale ptBr = new Locale("pt", "BR");
        NumberFormat format = NumberFormat.getCurrencyInstance(ptBr);
        Double preco = Double.parseDouble(imovel.getPreco());



        txtTipoApartamento.setText("Tipo: " + imovel.getTipo());
        txtPreco.setText("Preço: " + format.format(preco));
        txtEndereco.setText("Endereço: " + imovel.getEndereco());
        btnLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(imovel.getUrl());

                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                ctx.startActivity(intent);
            }
        });

        final String url = enderecoImagemProducao + imovel.getImages()[0].getPath().replaceAll("full/", "");

        imageLoader = VolleySingleton.getInstance(ctx).getImageLoader();
        imageLoader.get(url, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                ob[0] = new BitmapDrawable(ctx.getResources(), response.getBitmap());
                linha.setBackground(ob[0]);

                linha.invalidate();
            }

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        return linha;
    }
}


