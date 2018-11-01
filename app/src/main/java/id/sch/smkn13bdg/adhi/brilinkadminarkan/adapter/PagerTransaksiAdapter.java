package id.sch.smkn13bdg.adhi.brilinkadminarkan.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import id.sch.smkn13bdg.adhi.brilinkadminarkan.modulbtmtransaksi.AntrianTransaksiFragment;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.modulbtmtransaksi.BatalTransaksiFragment;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.modulbtmtransaksi.BerhasilTransaksiFragment;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.modulbtmtransaksi.GagalTransaksiFragment;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.modulbtmtransaksi.KeepProsesTransaksiFragment;

/**
 * Created by adhi on 24/08/18.
 */

public class PagerTransaksiAdapter extends FragmentStatePagerAdapter {

    private int number_tabs;

    public PagerTransaksiAdapter(FragmentManager fm, int number_tabs) {
        super(fm);
        this.number_tabs = number_tabs;
    }

    //Mengembalikan Fragment yang terkait dengan posisi tertentu
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new AntrianTransaksiFragment();
            case 1:
                return new KeepProsesTransaksiFragment();
            case 2:
                return new GagalTransaksiFragment();
            case 3:
                return new BatalTransaksiFragment();
            case 4:
                return new BerhasilTransaksiFragment();
            default:
                return null;
        }
    }

    //Mengembalikan jumlah tampilan yang tersedia.
    @Override
    public int getCount() {
        return number_tabs;
    }
}
