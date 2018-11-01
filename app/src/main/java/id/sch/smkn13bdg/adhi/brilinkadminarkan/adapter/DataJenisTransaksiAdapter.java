package id.sch.smkn13bdg.adhi.brilinkadminarkan.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import id.sch.smkn13bdg.adhi.brilinkadminarkan.R;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.getset.DataJenisTransaksiController;

/**
 * Created by adhi on 27/09/18.
 */

public class DataJenisTransaksiAdapter extends ArrayAdapter<DataJenisTransaksiController> implements View.OnClickListener {

    private List<DataJenisTransaksiController> data;
    Context mContext;

    private static class ViewHolder{
        TextView namajenistransaksi;
        ImageView gambarjenistransaksi;
    }

    public DataJenisTransaksiAdapter(List<DataJenisTransaksiController> data, Context context) {
        super(context, R.layout.listdatajenistransaksi, data);
        this.data = data;
        this.mContext=context;
    }

    @Override
    public void onClick(View v) {
        int position=(Integer) v.getTag();
        Object object= getItem(position);
        DataJenisTransaksiController data = (DataJenisTransaksiController) object;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        DataJenisTransaksiController data = getItem(position);
        DataJenisTransaksiAdapter.ViewHolder viewHolder;

        final View result;

        if (convertView == null){
            viewHolder = new DataJenisTransaksiAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.listdatajenistransaksi, parent, false);

            //viewHolder.gambarjenistransaksi =(ImageView) convertView.findViewById(R.id.jenistransaksigambar);
            viewHolder.namajenistransaksi = (TextView) convertView.findViewById(R.id.jenistransaksinama);

            result = convertView;

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (DataJenisTransaksiAdapter.ViewHolder) convertView.getTag();
            result = convertView;
        }

        viewHolder.gambarjenistransaksi.setImageResource(data.getGambarjenistransaksi());
        viewHolder.namajenistransaksi.setText(data.getNamajenistransaksi());

        return convertView;
    }
}
