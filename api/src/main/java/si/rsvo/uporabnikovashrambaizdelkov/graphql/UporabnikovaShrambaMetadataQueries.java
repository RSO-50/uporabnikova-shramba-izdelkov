package si.rsvo.uporabnikovashrambaizdelkov.graphql;

import com.kumuluz.ee.graphql.annotations.GraphQLClass;
import com.kumuluz.ee.graphql.classes.Filter;
import com.kumuluz.ee.graphql.classes.Pagination;
import com.kumuluz.ee.graphql.classes.PaginationWrapper;
import com.kumuluz.ee.graphql.classes.Sort;
import com.kumuluz.ee.graphql.utils.GraphQLUtils;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLQuery;
import si.rsvo.uporabnikovashrambaizdelkov.lib.UporabnikoviIzdelkiMetadata;
import si.rsvo.uporabnikovashrambaizdelkov.services.beans.UporabnikoviIzdelkiMetadataBean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

@GraphQLClass
@ApplicationScoped
public class UporabnikovaShrambaMetadataQueries {

    @Inject
    private UporabnikoviIzdelkiMetadataBean uporabnikoviIzdelkiMetadataBean;

    @GraphQLQuery
    public PaginationWrapper<UporabnikoviIzdelkiMetadata> allUporabnikovaShrambaMetadata(
             @GraphQLArgument(name = "pagination") Pagination pagination,
             @GraphQLArgument(name = "sort") Sort sort,
             @GraphQLArgument(name = "filter") Filter filter) {

        return GraphQLUtils.process(uporabnikoviIzdelkiMetadataBean.getUporabnikoviIzdelkiMetadata(), pagination, sort, filter);
    }

    @GraphQLQuery
    public List<UporabnikoviIzdelkiMetadata> getUporabnikoviIzdelkiMetadata(@GraphQLArgument(name = "id") Integer id) {
        return uporabnikoviIzdelkiMetadataBean.getIzdelkiByUporabnik(id);
    }

}
