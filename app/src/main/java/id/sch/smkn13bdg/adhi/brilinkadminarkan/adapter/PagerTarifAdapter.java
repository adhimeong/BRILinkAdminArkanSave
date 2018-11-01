package id.sch.smkn13bdg.adhi.brilinkadminarkan.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import id.sch.smkn13bdg.adhi.brilinkadminarkan.modulnavtarif.TarifAntarFragment;
import id.sch.smkn13bdg.adhi.brilinkadminarkan.modulnavtarif.TarifSesamaFragment;

/**
 * Created by adhi on 15/09/18.
 */

public class PagerTarifAdapter extends FragmentStatePagerAdapter {

    private int number_tabs;

    public PagerTarifAdapter(FragmentManager fm, int number_tabs) {
        super(fm);
        this.number_tabs = number_tabs;
    }

    //Mengembalikan Fragment yang terkait dengan posisi tertentu
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new TarifSesamaFragment();
            case 1:
                return new TarifAntarFragment();
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
