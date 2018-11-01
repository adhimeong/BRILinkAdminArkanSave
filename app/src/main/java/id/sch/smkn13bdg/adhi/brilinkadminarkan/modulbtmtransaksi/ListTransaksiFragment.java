package id.sch.smkn13bdg.adhi.brilinkadminarkan.modulbtmtransaksi;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import id.sch.smkn13bdg.adhi.brilinkadminarkan.R;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.adapter.PagerTransaksiAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListTransaksiFragment extends Fragment {

    public ListTransaksiFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_transaksi_list, container, false);
        //Toolbar toolbar = view.findViewById(R.id.toolbar); //Inisialisasi dan Implementasi id Toolbar
        //setSupportActionBar(toolbar); // Memasang Toolbar pada Aplikasi

        //Menerapkan TabLayout dan ViewPager pada Activity
        final TabLayout tabLayout = view.findViewById(R.id.tab_layout2);
        final ViewPager viewPager = view.findViewById(R.id.pager2);

        //Memanggil dan Memasukan Value pada Class PagerAdapter(FragmentManager dan JumlahTab)
        PagerTransaksiAdapter pagerAdapter = new PagerTransaksiAdapter(getActivity().getSupportFragmentManager(), tabLayout.getTabCount());

        //Memasang Adapter pada ViewPager
        viewPager.setAdapter(pagerAdapter);

        /*
         Menambahkan Listener yang akan dipanggil kapan pun halaman berubah atau
         bergulir secara bertahap, sehingga posisi tab tetap singkron
         */
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        //Callback Interface dipanggil saat status pilihan tab berubah.
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //Dipanggil ketika tab memasuki state/keadaan yang dipilih.
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //Dipanggil saat tab keluar dari keadaan yang dipilih.
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //Dipanggil ketika tab yang sudah dipilih, dipilih lagi oleh user.
            }
        });

        return view;
    }

}
