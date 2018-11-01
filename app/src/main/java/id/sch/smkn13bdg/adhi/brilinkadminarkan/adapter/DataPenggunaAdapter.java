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
import id.sch.smkn13bdg.adhi.brilinkadminarkan.getset.DataPenggunaController;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.volley.MySingleton;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.volley.Server;

/**
 * Created by adhi on 12/09/18.
 */

public class DataPenggunaAdapter extends ArrayAdapter<DataPenggunaController> implements View.OnClickListener {

    private List<DataPenggunaController> data;

    ImageLoader mImageLoader;
    String url = Server.url_server +"app/images/";
    String fotopelangganurl, namatxt, kontaktxt, emailtxt, alamattxt;
    String fotodefault = "defaultprofile.jpg";
    String IMAGE_URL ;
    Context mContext;


    private static class ViewHolder {
        TextView nokartu;
        TextView nama;
        TextView kontak;
        TextView email;
        TextView alamat;
        NetworkImageView foto;
    }

    public DataPenggunaAdapter(List<DataPenggunaController> data, Context context) {
        super(context, R.layout.listdatapengguna, data);
        this.data = data;
        this.mContext=context;
    }

    @Override
    public void onClick(View v) {
        int position=(Integer) v.getTag();
        Object object= getItem(position);
        DataPenggunaController data =(DataPenggunaController)object;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        DataPenggunaController data = getItem(position);
        DataPenggunaAdapter.ViewHolder viewHolder;

        final View result;

        if (convertView == null) {
            viewHolder = new DataPenggunaAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.listdatapengguna, parent, false);

            viewHolder.nama = (TextView) convertView.findViewById(R.id.listpenggunanama);
            viewHolder.nokartu = (TextView) convertView.findViewById(R.id.listpenggunanokartu);
            viewHolder.kontak = (TextView) convertView.findViewById(R.id.listpenggunakontak);
            viewHolder.email = (TextView) convertView.findViewById(R.id.listpenggunaemail);
            viewHolder.alamat = (TextView) convertView.findViewById(R.id.listpenggunaalamat);
            viewHolder.foto = (NetworkImageView) convertView.findViewById(R.id.listpengggunafoto);

            result = convertView;

            convertView.setTag(viewHolder);


        }else{
            viewHolder = (DataPenggunaAdapter.ViewHolder) convertView.getTag();
            result=convertView;

        }

        viewHolder.nokartu.setText(String.valueOf(data.getNokartu()));

        fotopelangganurl = data.getFoto();
        namatxt = data.getNama();
        kontaktxt = data.getKontak();
        emailtxt = data.getEmail();
        alamattxt = data.getAlamat();

        if (namatxt.equals("null")){
            viewHolder.nama.setText("Belum ada Nama");
        }else{
            viewHolder.nama.setText(namatxt);
        }

        if (kontaktxt.equals("null")){
            viewHolder.kontak.setText("Belum ada Nama");
        }else{
            viewHolder.kontak.setText(kontaktxt);
        }

        if (emailtxt.equals("null")){
            viewHolder.email.setText("Belum ada Nama");
        }else{
            viewHolder.email.setText(emailtxt);
        }

        if (alamattxt.equals("null")){
            viewHolder.alamat.setText("Belum ada Nama");
        }else{
            viewHolder.alamat.setText(alamattxt);
        }

        if (fotopelangganurl.equals("null")){
            mImageLoader = MySingleton.getInstance(getContext()).getImageLoader();
            IMAGE_URL = url + String.valueOf(fotodefault);
            viewHolder.foto.setImageUrl(IMAGE_URL, mImageLoader);
        }else{
            mImageLoader = MySingleton.getInstance(getContext()).getImageLoader();
            IMAGE_URL = url + String.valueOf(fotopelangganurl);
            viewHolder.foto.setImageUrl(IMAGE_URL, mImageLoader);
        }

        return convertView;
    }
}

