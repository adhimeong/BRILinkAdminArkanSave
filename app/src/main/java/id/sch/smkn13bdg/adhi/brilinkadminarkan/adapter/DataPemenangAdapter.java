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
import id.sch.smkn13bdg.adhi.brilinkadminarkan.getset.DataPemenangController;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.volley.MySingleton;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.volley.Server;

/**
 * Created by adhi on 15/09/18.
 */

public class DataPemenangAdapter extends ArrayAdapter<DataPemenangController> implements View.OnClickListener {

    private List<DataPemenangController> data;

    ImageLoader mImageLoader;
    String url = Server.url_server +"app/images/";
    String fotopelangganurl, namapelanggan, nokartupelanggan, admin, tanggal, namahadiah, statushadiah;
    String fotodefault = "defaultprofile.jpg";
    String IMAGE_URL ;
    Context mContext;


    private static class ViewHolder {
        TextView nokartu;
        TextView nama;
        TextView tanggalview;
        TextView adminview;
        TextView hadiahview;
        TextView status;
        NetworkImageView foto;
    }

    public DataPemenangAdapter(List<DataPemenangController> data, Context context) {
        super(context, R.layout.listdataklaimhadiah, data);
        this.data = data;
        this.mContext=context;
    }

    @Override
    public void onClick(View v) {
        int position=(Integer) v.getTag();
        Object object= getItem(position);
        DataPemenangController data =(DataPemenangController) object;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        DataPemenangController data = getItem(position);
        DataPemenangAdapter.ViewHolder viewHolder;

        final View result;

        if (convertView == null) {
            viewHolder = new DataPemenangAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.listdataklaimhadiah, parent, false);

            viewHolder.nama = (TextView) convertView.findViewById(R.id.listklaimnama);
            viewHolder.nokartu = (TextView) convertView.findViewById(R.id.listklaimokartu);
            viewHolder.tanggalview = (TextView) convertView.findViewById(R.id.listklaimtanggal);
            viewHolder.hadiahview = (TextView) convertView.findViewById(R.id.listklaimhadiah);
            viewHolder.adminview = (TextView) convertView.findViewById(R.id.listklaimadmin);
            viewHolder.status = (TextView) convertView.findViewById(R.id.listklaimstatushadiah);
            viewHolder.foto = (NetworkImageView) convertView.findViewById(R.id.listklaimfoto);

            result = convertView;

            convertView.setTag(viewHolder);


        }else{
            viewHolder = (DataPemenangAdapter.ViewHolder) convertView.getTag();
            result=convertView;

        }

        viewHolder.nokartu.setText(String.valueOf(data.getNo_kartu()));

        fotopelangganurl = data.getFoto();
        namapelanggan = data.getNama();
        nokartupelanggan = data.getNo_kartu();
        admin = data.getAdmin();
        tanggal = data.getTanggal();
        namahadiah = data.getHadiah();
        statushadiah = data.getStatus();

        viewHolder.nokartu.setText(nokartupelanggan);
        viewHolder.tanggalview.setText(tanggal);
        viewHolder.adminview.setText(admin);
        viewHolder.hadiahview.setText(namahadiah);
        viewHolder.status.setText(statushadiah);

        if (namapelanggan.equals("null")){
            viewHolder.nama.setText("Belum ada Nama");
        }else{
            viewHolder.nama.setText(namapelanggan);
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


