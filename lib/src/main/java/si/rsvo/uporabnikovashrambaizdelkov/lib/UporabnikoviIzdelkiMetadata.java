package si.rsvo.uporabnikovashrambaizdelkov.lib;

import java.util.List;

public class UporabnikoviIzdelkiMetadata {

    private Integer id;
    private Integer uporabnikId;
    private Integer izdelekId;

    public Integer getUporabnikId() {
        return uporabnikId;
    }

    public void setUporabnikId(Integer uporabnikId) {
        this.uporabnikId = uporabnikId;
    }

    public Integer getIzdelekId() {
        return izdelekId;
    }

    public void setIzdelekId(Integer izdelekId) {
        this.izdelekId = izdelekId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
