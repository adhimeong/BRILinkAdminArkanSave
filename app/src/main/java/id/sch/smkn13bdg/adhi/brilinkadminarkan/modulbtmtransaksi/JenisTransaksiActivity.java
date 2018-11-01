package id.sch.smkn13bdg.adhi.brilinkadminarkan.modulbtmtransaksi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import id.sch.smkn13bdg.adhi.brilinkadminarkan.R;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.adapter.DataJenisTransaksiAdapter;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.getset.DataJenisTransaksiController;

public class JenisTransaksiActivity extends AppCompatActivity {

    ListView listView;
    List<DataJenisTransaksiController> listJenis = new ArrayList<DataJenisTransaksiController>();
    DataJenisTransaksiAdapter adapter;
    String namajenistransaksi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jenis_transaksi);

        listView = (ListView) findViewById(R.id.listview01);

        DataJenisTransaksiController jenis1 = new DataJenisTransaksiController();
        //jenis1.setGambarjenistransaksi(R.drawable.transaksi_sesama);
        jenis1.setNamajenistransaksi("Transfer BRI");
        listJenis.add(jenis1);

        DataJenisTransaksiController jenis2 = new DataJenisTransaksiController();
        //jenis2.setGambarjenistransaksi(R.drawable.transaksi_antar);
        jenis2.setNamajenistransaksi("Transfer Bank Lain");
        listJenis.add(jenis2);

        DataJenisTransaksiController jenis3 = new DataJenisTransaksiController();
        //jenis3.setGambarjenistransaksi(R.drawable.transaksi_tarik);
        jenis3.setNamajenistransaksi("Tarik Tunai");
        listJenis.add(jenis3);

        DataJenisTransaksiController jenis4 = new DataJenisTransaksiController();
        //jenis4.setGambarjenistransaksi(R.drawable.transaksi_pulsa);
        jenis4.setNamajenistransaksi("Pulsa & Paket Data");
        listJenis.add(jenis4);

        DataJenisTransaksiController jenis5 = new DataJenisTransaksiController();
        //jenis5.setGambarjenistransaksi(R.drawable.transaksi_bpjs);
        jenis5.setNamajenistransaksi("BPJS Kesehatan");
        listJenis.add(jenis5);

        DataJenisTransaksiController jenis6 = new DataJenisTransaksiController();
        //jenis6.setGambarjenistransaksi(R.drawable.transaksi_pln);
        jenis6.setNamajenistransaksi("PLN");
        listJenis.add(jenis6);

        DataJenisTransaksiController jenis7 = new DataJenisTransaksiController();
        //jenis7.setGambarjenistransaksi(R.drawable.transaksi_cicilan);
        jenis7.setNamajenistransaksi("Cicilan");
        listJenis.add(jenis7);

        DataJenisTransaksiController jenis8 = new DataJenisTransaksiController();
        //jenis8.setGambarjenistransaksi(R.drawable.transaksi_lain);
        jenis8.setNamajenistransaksi("Transaksi Lainnya");
        listJenis.add(jenis8);

        adapter = new DataJenisTransaksiAdapter(listJenis, this);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {

                namajenistransaksi = listJenis.get(position).getNamajenistransaksi();
                Intent i = new Intent(getApplicationContext(), TransaksiActivity.class);
                i.putExtra("jenistransaksi", namajenistransaksi);
                startActivity(i);
            }
        });
    }
}
