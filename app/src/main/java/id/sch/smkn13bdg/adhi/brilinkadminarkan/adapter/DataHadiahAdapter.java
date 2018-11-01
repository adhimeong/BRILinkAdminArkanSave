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
import id.sch.smkn13bdg.adhi.brilinkadminarkan.getset.DataHadiahController;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.volley.MySingleton;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.volley.Server;

/**
 * Created by adhi on 09/07/18.
 */

public class DataHadiahAdapter extends ArrayAdapter<DataHadiahController> implements View.OnClickListener {


    private List<DataHadiahController> data;


    ImageLoader mImageLoader;
    String url = Server.url_server +"app/hadiah/";
    String IMAGE_URL ;
    Context mContext;

    private static class ViewHolder {
        TextView idhadiah;
        TextView namahadiah;
        TextView jumlahpoint;
        TextView jumlahitems;
        NetworkImageView fotohadiah;
    }

    public DataHadiahAdapter(List<DataHadiahController> data, Context context) {
        super(context, R.layout.listdatahadiah, data);
        this.data = data;
        this.mContext=context;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        DataHadiahController data = getItem(position);

        ViewHolder viewHolder;

        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.listdatahadiah, parent, false);

            viewHolder.idhadiah = (TextView) convertView.findViewById(R.id.listidhadiah);
            viewHolder.namahadiah = (TextView) convertView.findViewById(R.id.listnamahadiah);
            viewHolder.fotohadiah = (NetworkImageView) convertView.findViewById(R.id.listfotohadiah);
            viewHolder.jumlahpoint = (TextView) convertView.findViewById(R.id.listpointadmin);
            viewHolder.jumlahitems = (TextView) convertView.findViewById(R.id.listpointwaktu);

            result = convertView;

            convertView.setTag(viewHolder);


        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;

        }

        viewHolder.idhadiah.setText(String.valueOf(data.getId_hadiah()));
        viewHolder.namahadiah.setText(String.valueOf(data.getNama_hadiah()));
        viewHolder.jumlahpoint.setText(String.valueOf(data.getJumlah_point()));
        viewHolder.jumlahitems.setText(String.valueOf(data.getJumlah_items() + " item"));

        mImageLoader = MySingleton.getInstance(getContext()).getImageLoader();
        IMAGE_URL = url + String.valueOf(data.getFoto_hadiah());
        viewHolder.fotohadiah.setImageUrl(IMAGE_URL, mImageLoader);

        return convertView;
    }

    @Override
    public void onClick(View view) {

    }
}

