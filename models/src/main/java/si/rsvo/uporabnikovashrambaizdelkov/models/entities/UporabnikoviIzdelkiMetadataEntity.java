package si.rsvo.uporabnikovashrambaizdelkov.models.entities;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "uporabnikoviIzdelki_metadata")
@NamedQueries(value =
        {
                @NamedQuery(name = "UporabnikoviIzdelkiMetadataEntity.getAll",
                        query = "SELECT im FROM UporabnikoviIzdelkiMetadataEntity im"),
                @NamedQuery(name = "UporabnikoviIzdelkiMetadataEntity.getIzdelkiByUporabnik",
                        query = "SELECT t FROM UporabnikoviIzdelkiMetadataEntity t WHERE t.uporabnikId = :uporabnikId"),
                @NamedQuery(name = "UporabnikoviIzdelkiMetadataEntity.getUporabnikovIzdelek",
                        query = "SELECT t FROM UporabnikoviIzdelkiMetadataEntity t WHERE t.uporabnikId = :uporabnikId AND t.izdelekId = :izdelekId")
        })

public class UporabnikoviIzdelkiMetadataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "uporabnikId")
    private Integer uporabnikId;

    @Column(name = "izdelekId")
    private Integer izdelekId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

}