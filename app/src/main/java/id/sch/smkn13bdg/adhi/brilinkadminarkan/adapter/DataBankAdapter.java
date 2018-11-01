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
import id.sch.smkn13bdg.adhi.brilinkadminarkan.getset.DataBankController;


/**
 * Created by adhi on 16/09/18.
 */

public class DataBankAdapter extends ArrayAdapter<DataBankController> implements View.OnClickListener {

    private List<DataBankController> data;

    Context mContext;

    private static class ViewHolder {
        TextView namabank;
        TextView kodebank;
    }

    public DataBankAdapter(List<DataBankController> data, Context context) {
        super(context, R.layout.listdatabank, data);
        this.data = data;
        this.mContext=context;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        DataBankController data = getItem(position);

        DataBankAdapter.ViewHolder viewHolder;

        final View result;

        if (convertView == null) {
            viewHolder = new DataBankAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.listdatabank, parent, false);

            viewHolder.namabank = (TextView) convertView.findViewById(R.id.listbanknama);
            viewHolder.kodebank = (TextView) convertView.findViewById(R.id.listbankkode);


            result = convertView;

            convertView.setTag(viewHolder);


        } else {
            viewHolder = (DataBankAdapter.ViewHolder) convertView.getTag();
            result = convertView;

        }

        viewHolder.namabank.setText(String.valueOf(data.getNamabank()));
        viewHolder.kodebank.setText(String.valueOf(data.getKodebank()));

        return convertView;
    }

    @Override
    public void onClick(View view) {

    }
}
