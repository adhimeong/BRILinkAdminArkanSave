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
import id.sch.smkn13bdg.adhi.brilinkadminarkan.getset.DataTransaksiController;

/**
 * Created by adhi on 24/08/18.
 */

public class DataTransaksiAdapter extends ArrayAdapter<DataTransaksiController> implements View.OnClickListener {

    private List<DataTransaksiController> data;

    String txtidtransaksi, txtnokartu, txtrektujuan, txtnominal, txtjenis, txtbank, txttanggal, txtpenerima, txtadmin;

    Context mContext;

    private static class ViewHolder {
        TextView idtransaksi;
        TextView nokartu;
        TextView rektujuan;
        TextView nominal;
        TextView jenis;
        TextView bank;
        TextView tanggal;
        TextView penerima;
        TextView admin;
        ImageView gambar;
    }

    public DataTransaksiAdapter(List<DataTransaksiController> data, Context context) {
        super(context, R.layout.listdatatransaksi, data);
        this.data = data;
        this.mContext=context;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        DataTransaksiController data = getItem(position);

        ViewHolder viewHolder;

        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.listdatatransaksi, parent, false);

            viewHolder.idtransaksi = (TextView) convertView.findViewById(R.id.listtransid);
            viewHolder.nokartu = (TextView) convertView.findViewById(R.id.listtransnokartu);
            viewHolder.nominal = (TextView) convertView.findViewById(R.id.listtransnominal);
            viewHolder.rektujuan = (TextView) convertView.findViewById(R.id.listtransrek);
            viewHolder.bank = (TextView) convertView.findViewById(R.id.listtransbank);
            viewHolder.jenis = (TextView) convertView.findViewById(R.id.listtransjenis);
            viewHolder.tanggal = (TextView) convertView.findViewById(R.id.listtranstanggal);
            viewHolder.penerima = (TextView) convertView.findViewById(R.id.listtranspenerima);
            viewHolder.admin = (TextView) convertView.findViewById(R.id.listtransadmin);
            viewHolder.gambar = (ImageView) convertView.findViewById(R.id.listtrangambar);

            result = convertView;

            convertView.setTag(viewHolder);


        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;

        }

        txtidtransaksi = data.getId_tansaksi();
        txtnokartu = data.getNo_kartu();
        txtnominal = data.getNominal();
        txtrektujuan = data.getRek_tujuan();
        txtbank = data.getBank();
        txtjenis = data.getJenis();
        txttanggal = data.getTanggaltransaksi();
        txtpenerima = data.getPenerima();
        txtadmin = data.getNamaadmin();

        if (txtnominal.equals("null")){
            txtnominal = "";
        }

        if (txtbank.equals("null")){
            txtbank = "";
        }

        if (txtrektujuan.equals("null")){
            txtrektujuan = "";
        }

        if (txtpenerima.equals("null")){
            txtpenerima = "";
        }

        if (txtadmin.equals("null")){
            txtadmin = "belum diproses";
        }


        if (txtjenis.equals("point")){
            viewHolder.gambar.setImageResource(R.drawable.ic_stars_black_24dp);
        }else if (txtjenis.equals("Transfer BRI")){
            viewHolder.gambar.setImageResource(R.drawable.ic_stars_black_24dp);
        }else if (txtjenis.equals("Transfer Bank Lain")){
            viewHolder.gambar.setImageResource(R.drawable.ic_stars_black_24dp);
        }else if (txtjenis.equals("Tarik Tunai")){
            viewHolder.gambar.setImageResource(R.drawable.ic_stars_black_24dp);
        }else if (txtjenis.equals("Pulsa & Paket Data")){
            viewHolder.gambar.setImageResource(R.drawable.ic_stars_black_24dp);
        }else if (txtjenis.equals("BPJS Kesehatan")){
            viewHolder.gambar.setImageResource(R.drawable.ic_stars_black_24dp);
        }else if (txtjenis.equals("PLN")){
            viewHolder.gambar.setImageResource(R.drawable.ic_stars_black_24dp);
        }else if (txtjenis.equals("Cicilan")){
            viewHolder.gambar.setImageResource(R.drawable.ic_stars_black_24dp);
        }else if (txtjenis.equals("Transaksi Lainnya")){
            viewHolder.gambar.setImageResource(R.drawable.ic_stars_black_24dp);
        }


        viewHolder.idtransaksi.setText(String.valueOf(data.getId_tansaksi()));
        viewHolder.nokartu.setText(String.valueOf(data.getNo_kartu()));
        viewHolder.nominal.setText(String.valueOf(txtnominal));
        viewHolder.rektujuan.setText(String.valueOf(txtrektujuan));
        viewHolder.bank.setText(String.valueOf(txtbank));
        viewHolder.jenis.setText(String.valueOf(data.getJenis()));
        viewHolder.tanggal.setText(String.valueOf(data.getTanggaltransaksi()));
        viewHolder.penerima.setText(String.valueOf(txtpenerima));
        viewHolder.admin.setText(String.valueOf(txtadmin));

        return convertView;
    }

    @Override
    public void onClick(View view) {

    }
}
