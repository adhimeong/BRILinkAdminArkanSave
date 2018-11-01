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
import id.sch.smkn13bdg.adhi.brilinkadminarkan.getset.DataPointController;

/**
 * Created by adhi on 08/09/18.
 */

public class DataPointAdapter extends ArrayAdapter<DataPointController> implements View.OnClickListener {

    private List<DataPointController> data;

    Context mContext;

    private static class ViewHolder {
        TextView idpoint;
        TextView nokartu;
        TextView waktu;
        TextView tanggal;
        TextView admin;
    }

    public DataPointAdapter(List<DataPointController> data, Context context) {
        super(context, R.layout.listdatapoint, data);
        this.data = data;
        this.mContext=context;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        DataPointController data = getItem(position);

        DataPointAdapter.ViewHolder viewHolder;

        final View result;

        if (convertView == null) {
            viewHolder = new DataPointAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.listdatapoint, parent, false);

            viewHolder.idpoint = (TextView) convertView.findViewById(R.id.listpointid);
            viewHolder.nokartu = (TextView) convertView.findViewById(R.id.listpointnokartu);
            viewHolder.waktu = (TextView) convertView.findViewById(R.id.listpointwaktu);
            viewHolder.tanggal = (TextView) convertView.findViewById(R.id.listpointtanggal);
            viewHolder.admin = (TextView) convertView.findViewById(R.id.listpointadmin);

            result = convertView;

            convertView.setTag(viewHolder);


        } else {
            viewHolder = (DataPointAdapter.ViewHolder) convertView.getTag();
            result = convertView;

        }

        viewHolder.idpoint.setText(String.valueOf(data.getId_perolehan()));
        viewHolder.nokartu.setText(String.valueOf(data.getNo_kartu()));
        viewHolder.waktu.setText(String.valueOf(data.getWaktu()));
        viewHolder.tanggal.setText(String.valueOf(data.getTanggal_point()));
        viewHolder.admin.setText(String.valueOf(data.getAdmin()));

        return convertView;
    }

    @Override
    public void onClick(View view) {

    }
}
