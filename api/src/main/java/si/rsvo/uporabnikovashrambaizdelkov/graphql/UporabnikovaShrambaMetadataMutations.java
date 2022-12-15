package si.rsvo.uporabnikovashrambaizdelkov.graphql;

import com.kumuluz.ee.graphql.annotations.GraphQLClass;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import si.rsvo.uporabnikovashrambaizdelkov.lib.UporabnikoviIzdelkiMetadata;
import si.rsvo.uporabnikovashrambaizdelkov.services.beans.UporabnikoviIzdelkiMetadataBean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@GraphQLClass
@ApplicationScoped
public class UporabnikovaShrambaMetadataMutations {
    @Inject
    private UporabnikoviIzdelkiMetadataBean uporabnikoviIzdelkiMetadataBean;

    @GraphQLMutation
    public UporabnikoviIzdelkiMetadata addUporabnikovaShrambaMetadata(@GraphQLArgument(name = "uporabnikovaShrambaMetadata") UporabnikoviIzdelkiMetadata uporabnikoviIzdelkiMetadata) {
        uporabnikoviIzdelkiMetadataBean.createUporabnikoviIzdelkiMetadata(uporabnikoviIzdelkiMetadata);
        return uporabnikoviIzdelkiMetadata;
    }

    @GraphQLMutation
    public DeleteResponse deleteUporabnikovaShrambaMetadata(@GraphQLArgument(name = "uporabnikId") Integer uporabnikId,
                                                            @GraphQLArgument(name = "izdelekId") Integer izdelekId) {
        return new DeleteResponse(uporabnikoviIzdelkiMetadataBean.deleteUporabnikoviIzdelkiMetadata(uporabnikId, izdelekId));
    }
}
