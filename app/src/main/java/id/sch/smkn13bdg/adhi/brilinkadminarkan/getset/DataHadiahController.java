package id.sch.smkn13bdg.adhi.brilinkadminarkan.getset;

/**
 * Created by adhi on 09/07/18.
 */

public class DataHadiahController {

    public String id_hadiah;
    public String nama_hadiah;
    public String foto_hadiah;
    public String jumlah_point;
    public String jumlah_items;

    public String getJumlah_items() {
        return jumlah_items;
    }

    public void setJumlah_items(String jumlah_items) {
        this.jumlah_items = jumlah_items;
    }

    public String getId_hadiah() {
        return id_hadiah;
    }

    public void setId_hadiah(String id_hadiah) {
        this.id_hadiah = id_hadiah;
    }

    public String getNama_hadiah() {
        return nama_hadiah;
    }

    public void setNama_hadiah(String nama_hadiah) {
        this.nama_hadiah = nama_hadiah;
    }

    public String getFoto_hadiah() {
        return foto_hadiah;
    }

    public void setFoto_hadiah(String foto_hadiah) {
        this.foto_hadiah = foto_hadiah;
    }

    public String getJumlah_point() {
        return jumlah_point;
    }

    public void setJumlah_point(String jumlah_point) {
        this.jumlah_point = jumlah_point;
    }

}

