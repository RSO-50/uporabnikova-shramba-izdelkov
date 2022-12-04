package si.rsvo.uporabnikovashrambaizdelkov.models.converters;

import si.rsvo.uporabnikovashrambaizdelkov.lib.UporabnikoviIzdelkiMetadata;
import si.rsvo.uporabnikovashrambaizdelkov.models.entities.UporabnikoviIzdelkiMetadataEntity;

public class UporabnikoviIzdelkiMetadataConverter {

    public static UporabnikoviIzdelkiMetadata toDto(UporabnikoviIzdelkiMetadataEntity entity) {

        UporabnikoviIzdelkiMetadata dto = new UporabnikoviIzdelkiMetadata();
        dto.setId(entity.getId());
        dto.setUporabnikId(entity.getUporabnikId());
        dto.setIzdelekId(entity.getIzdelekId());

        return dto;

    }

    public static UporabnikoviIzdelkiMetadataEntity toEntity(UporabnikoviIzdelkiMetadata dto) {

        UporabnikoviIzdelkiMetadataEntity entity = new UporabnikoviIzdelkiMetadataEntity();
        entity.setId(dto.getId());
        entity.setUporabnikId(dto.getUporabnikId());
        entity.setIzdelekId(dto.getIzdelekId());

        return entity;

    }

}
