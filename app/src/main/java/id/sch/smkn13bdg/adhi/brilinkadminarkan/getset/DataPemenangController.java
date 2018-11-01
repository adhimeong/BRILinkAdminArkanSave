package id.sch.smkn13bdg.adhi.brilinkadminarkan.getset;

/**
 * Created by adhi on 15/09/18.
 */

public class DataPemenangController {
    String nama;
    String admin;
    String tanggal;
    String hadiah;
    String no_kartu;
    String foto;
    String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getHadiah() {
        return hadiah;
    }

    public void setHadiah(String hadiah) {
        this.hadiah = hadiah;
    }

    public String getNo_kartu() {
        return no_kartu;
    }

    public void setNo_kartu(String no_kartu) {
        this.no_kartu = no_kartu;
    }
}
