package id.sch.smkn13bdg.adhi.brilinkadminarkan.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import id.sch.smkn13bdg.adhi.brilinkadminarkan.R;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.getset.DataTarifControler;

/**
 * Created by adhi on 15/09/18.
 */

public class DataTarifAdapter extends ArrayAdapter<DataTarifControler> implements View.OnClickListener {

    private List<DataTarifControler> data;

    Context mContext;

    private static class ViewHolder {
        TextView batasbawah;
        TextView batasatas;
        TextView tarif;
    }

    public DataTarifAdapter(List<DataTarifControler> data, Context context) {
        super(context, R.layout.listdatatarif, data);
        this.data = data;
        this.mContext=context;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        DataTarifControler data = getItem(position);

        DataTarifAdapter.ViewHolder viewHolder;

        final View result;

        if (convertView == null) {
            viewHolder = new DataTarifAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.listdatatarif, parent, false);

            viewHolder.batasbawah = (TextView) convertView.findViewById(R.id.listtarifbawah);
            viewHolder.batasatas = (TextView) convertView.findViewById(R.id.listtarifatas);
            viewHolder.tarif = (TextView) convertView.findViewById(R.id.listtariftarif);

            result = convertView;

            convertView.setTag(viewHolder);


        } else {
            viewHolder = (DataTarifAdapter.ViewHolder) convertView.getTag();
            result = convertView;

        }

        viewHolder.batasbawah.setText(String.valueOf(data.getBatasbawah()));
        viewHolder.batasatas.setText(String.valueOf(data.getBatasatas()));
        viewHolder.tarif.setText(String.valueOf(data.getTarif()));

        return convertView;
    }

    @Override
    public void onClick(View view) {

    }
}
