package id.sch.smkn13bdg.adhi.brilinkadminarkan.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import id.sch.smkn13bdg.adhi.brilinkadminarkan.R;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.getset.DataPelangganController;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.volley.MySingleton;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.volley.Server;

/**
 * Created by adhi on 10/09/18.
 */

public class DataPelangganAdapter extends ArrayAdapter<DataPelangganController> implements View.OnClickListener {

    private List<DataPelangganController> data;

    ImageLoader mImageLoader;
    String url = Server.url_server +"app/images/";
    String fotopelangganurl, namapelanggan;
    String fotodefault = "defaultprofile.jpg";
    String IMAGE_URL ;
    Context mContext;


    private static class ViewHolder {
        TextView tglpasif;
        TextView namapelanggan;
        TextView jumlahpoint;
        NetworkImageView fotopelanggan;
    }

    public DataPelangganAdapter(List<DataPelangganController> data, Context context) {
        super(context, R.layout.listdatapelanggan, data);
        this.data = data;
        this.mContext=context;
    }

    @Override
    public void onClick(View v) {
        int position=(Integer) v.getTag();
        Object object= getItem(position);
        DataPelangganController data =(DataPelangganController)object;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        DataPelangganController data = getItem(position);
        ViewHolder viewHolder;

        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.listdatapelanggan, parent, false);

            viewHolder.tglpasif = (TextView) convertView.findViewById(R.id.listtglpasif);
            viewHolder.namapelanggan = (TextView) convertView.findViewById(R.id.listnamapelanggan);
            viewHolder.fotopelanggan = (NetworkImageView) convertView.findViewById(R.id.listpelangganfoto);
            viewHolder.jumlahpoint = (TextView) convertView.findViewById(R.id.listjumlah_pointpelanggan);

            result = convertView;

            convertView.setTag(viewHolder);


        }else{
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;

        }

        viewHolder.tglpasif.setText(String.valueOf(data.getTanggal_pasif()));
        viewHolder.jumlahpoint.setText(String.valueOf(data.getJumlah_point()));
        fotopelangganurl = data.getFoto_pelanggan();
        namapelanggan = data.getNama_pelanggan();

        if (namapelanggan.equals("null")){
            viewHolder.namapelanggan.setText("Belum ada Nama");
        }else{
            viewHolder.namapelanggan.setText(namapelanggan);
        }

        if (fotopelangganurl.equals("null")){
            mImageLoader = MySingleton.getInstance(getContext()).getImageLoader();
            IMAGE_URL = url + String.valueOf(fotodefault);
            viewHolder.fotopelanggan.setImageUrl(IMAGE_URL, mImageLoader);
        }else{
            mImageLoader = MySingleton.getInstance(getContext()).getImageLoader();
            IMAGE_URL = url + String.valueOf(fotopelangganurl);
            viewHolder.fotopelanggan.setImageUrl(IMAGE_URL, mImageLoader);
        }

        return convertView;
    }
}
