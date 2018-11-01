package id.sch.smkn13bdg.adhi.brilinkadminarkan.getset;

/**
 * Created by adhi on 24/08/18.
 */

public class DataTransaksiController {

    String id_tansaksi;
    String tanggaltransaksi;
    String no_kartu;
    String rek_tujuan;
    String nominal;
    String bank;
    String jenis;
    String namaadmin;
    String penerima;

    public String getTanggaltransaksi() {
        return tanggaltransaksi;
    }

    public void setTanggaltransaksi(String tanggaltransaksi) {
        this.tanggaltransaksi = tanggaltransaksi;
    }

    public String getNamaadmin() {
        return namaadmin;
    }

    public void setNamaadmin(String namaadmin) {
        this.namaadmin = namaadmin;
    }

    public String getPenerima() {
        return penerima;
    }

    public void setPenerima(String penerima) {
        this.penerima = penerima;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public String getId_tansaksi() {
        return id_tansaksi;
    }

    public void setId_tansaksi(String id_tansaksi) {
        this.id_tansaksi = id_tansaksi;
    }

    public String getNo_kartu() {
        return no_kartu;
    }

    public void setNo_kartu(String no_kartu) {
        this.no_kartu = no_kartu;
    }

    public String getRek_tujuan() {
        return rek_tujuan;
    }

    public void setRek_tujuan(String rek_tujuan) {
        this.rek_tujuan = rek_tujuan;
    }

    public String getNominal() {
        return nominal;
    }

    public void setNominal(String nominal) {
        this.nominal = nominal;
    }
}
