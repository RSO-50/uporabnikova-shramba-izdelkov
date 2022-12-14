package si.rsvo.uporabnikovashrambaizdelkov.services.beans;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.UriInfo;
import java.net.http.HttpClient;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;

import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.metrics.annotation.Timed;
import si.rsvo.uporabnikovashrambaizdelkov.lib.UporabnikoviIzdelkiMetadata;
import si.rsvo.uporabnikovashrambaizdelkov.models.converters.UporabnikoviIzdelkiMetadataConverter;
import si.rsvo.uporabnikovashrambaizdelkov.models.entities.UporabnikoviIzdelkiMetadataEntity;

@RequestScoped
public class UporabnikoviIzdelkiMetadataBean {

    private Logger log = Logger.getLogger(UporabnikoviIzdelkiMetadataBean.class.getName());

    @Inject
    private EntityManager em;

    private Client httpClient;
    private String baseUrl;

    @PostConstruct
    private void init() {
        httpClient = ClientBuilder.newClient();
        baseUrl = "http://20.73.139.35/"; // ingress
    }

    @Timeout(value = 2, unit = ChronoUnit.SECONDS)
    @CircuitBreaker(requestVolumeThreshold = 3)
    @Fallback(fallbackMethod = "getUporabnikIdByUsernameFallback")
    public Integer getUporabnikIdByUsername(String username) {

        log.info("Calling users service: getting user's id.");

        try {
            return httpClient
                    .target(baseUrl + "uporabniki/v1/uporabniki/byUsername/" + username)
                    .request().get(new GenericType<Integer>() {
                    });
        }
        catch (WebApplicationException | ProcessingException e) {
            log.severe(e.getMessage());
            throw new InternalServerErrorException(e);
        }
    }

    public Integer getUporabnikIdByUsernameFallback(String username) {
        return null;
    }

    public List<UporabnikoviIzdelkiMetadata> getUporabnikovaShrambaByUsername(String username) {

        Integer uporabnikId = getUporabnikIdByUsername(username);

        return getIzdelkiByUporabnik(uporabnikId);
    }

    public List<UporabnikoviIzdelkiMetadata> getUporabnikoviIzdelkiMetadata() {

        TypedQuery<UporabnikoviIzdelkiMetadataEntity> query = em.createNamedQuery(
                "UporabnikoviIzdelkiMetadataEntity.getAll", UporabnikoviIzdelkiMetadataEntity.class);

        List<UporabnikoviIzdelkiMetadataEntity> resultList = query.getResultList();

        return resultList.stream().map(UporabnikoviIzdelkiMetadataConverter::toDto).collect(Collectors.toList());

    }

    @Timed
    public List<UporabnikoviIzdelkiMetadata> getIzdelkiByUporabnik(Integer uporabnikId) {

        TypedQuery<UporabnikoviIzdelkiMetadataEntity> query = em.createNamedQuery(
                "UporabnikoviIzdelkiMetadataEntity.getIzdelkiByUporabnik", UporabnikoviIzdelkiMetadataEntity.class);
        query.setParameter("uporabnikId", uporabnikId);

        List<UporabnikoviIzdelkiMetadataEntity> resultList = query.getResultList();

        return resultList.stream().map(UporabnikoviIzdelkiMetadataConverter::toDto).collect(Collectors.toList());
    }

    @Timed
    public UporabnikoviIzdelkiMetadata createUporabnikoviIzdelkiMetadata(UporabnikoviIzdelkiMetadata uporabnikoviIzdelkiMetadata) {

        UporabnikoviIzdelkiMetadataEntity uporabnikoviIzdelkiMetadataEntity = UporabnikoviIzdelkiMetadataConverter.toEntity(uporabnikoviIzdelkiMetadata);

        try {
            beginTx();
            em.persist(uporabnikoviIzdelkiMetadataEntity);
            commitTx();
        }
        catch (Exception e) {
            rollbackTx();
        }

        if (uporabnikoviIzdelkiMetadataEntity.getIzdelekId() == null || uporabnikoviIzdelkiMetadataEntity.getUporabnikId() == null) {
            throw new RuntimeException("Entity was not persisted");
        }

        return UporabnikoviIzdelkiMetadataConverter.toDto(uporabnikoviIzdelkiMetadataEntity);
    }

    public boolean deleteUporabnikoviIzdelkiMetadata(Integer uporabnikId, Integer izdelekId) {

        TypedQuery<UporabnikoviIzdelkiMetadataEntity> query = em.createNamedQuery(
                "UporabnikoviIzdelkiMetadataEntity.getUporabnikovIzdelek", UporabnikoviIzdelkiMetadataEntity.class);
        query.setParameter("uporabnikId", uporabnikId);
        query.setParameter("izdelekId", izdelekId);

        UporabnikoviIzdelkiMetadataEntity uporabnikoviIzdelkiMetadata = query.getSingleResult();

        if (uporabnikoviIzdelkiMetadata != null) {
            try {
                beginTx();
                em.remove(uporabnikoviIzdelkiMetadata);
                commitTx();
            }
            catch (Exception e) {
                rollbackTx();
            }
        }
        else {
            return false;
        }

        return true;
    }

    /*
    @Timed
    public List<UporabnikoviIzdelkiMetadata> getUporabnikoviIzdelkiMetadataFilter(UriInfo uriInfo) {

        QueryParameters queryParameters = QueryParameters.query(uriInfo.getRequestUri().getQuery()).defaultOffset(0)
                .build();

        return JPAUtils.queryEntities(em, UporabnikoviIzdelkiMetadataEntity.class, queryParameters).stream()
                .map(UporabnikoviIzdelkiMetadataConverter::toDto).collect(Collectors.toList());
    }

    public UporabnikoviIzdelkiMetadata getUporabnikoviIzdelkiMetadata(Integer id) {

        UporabnikoviIzdelkiMetadataEntity uporabnikoviIzdelkiMetadataEntity = em.find(UporabnikoviIzdelkiMetadataEntity.class, id);

        if (uporabnikoviIzdelkiMetadataEntity == null) {
            throw new NotFoundException();
        }

        UporabnikoviIzdelkiMetadata uporabnikoviIzdelkiMetadata = UporabnikoviIzdelkiMetadataConverter.toDto(uporabnikoviIzdelkiMetadataEntity);

        return uporabnikoviIzdelkiMetadata;
    }

    public UporabnikoviIzdelkiMetadata createUporabnikoviIzdelkiMetadata(UporabnikoviIzdelkiMetadata uporabnikoviIzdelkiMetadata) {

        UporabnikoviIzdelkiMetadataEntity uporabnikoviIzdelkiMetadataEntity = UporabnikoviIzdelkiMetadataConverter.toEntity(uporabnikoviIzdelkiMetadata);

        try {
            beginTx();
            em.persist(uporabnikoviIzdelkiMetadataEntity);
            commitTx();
        }
        catch (Exception e) {
            rollbackTx();
        }

        if (uporabnikoviIzdelkiMetadataEntity.getIzdelekId() == null || uporabnikoviIzdelkiMetadataEntity.getUporabnikId() == null) {
            throw new RuntimeException("Entity was not persisted");
        }

        return UporabnikoviIzdelkiMetadataConverter.toDto(uporabnikoviIzdelkiMetadataEntity);
    }

    public UporabnikoviIzdelkiMetadata putUporabnikoviIzdelkiMetadata(Integer id, UporabnikoviIzdelkiMetadata uporabnikoviIzdelkiMetadata) {

        UporabnikoviIzdelkiMetadataEntity c = em.find(UporabnikoviIzdelkiMetadataEntity.class, id);

        if (c == null) {
            return null;
        }

        UporabnikoviIzdelkiMetadataEntity updatedUporabnikoviIzdelkiMetadataEntity = UporabnikoviIzdelkiMetadataConverter.toEntity(uporabnikoviIzdelkiMetadata);

        try {
            beginTx();
            updatedUporabnikoviIzdelkiMetadataEntity.setIzdelekId(c.getIzdelekId());
            updatedUporabnikoviIzdelkiMetadataEntity.setUporabnikId(c.getUporabnikId());
            updatedUporabnikoviIzdelkiMetadataEntity = em.merge(updatedUporabnikoviIzdelkiMetadataEntity);
            commitTx();
        }
        catch (Exception e) {
            rollbackTx();
        }

        return UporabnikoviIzdelkiMetadataConverter.toDto(updatedUporabnikoviIzdelkiMetadataEntity);
    }

    public boolean deleteUporabnikoviIzdelkiMetadata(Integer id) {

        UporabnikoviIzdelkiMetadataEntity uporabnikoviIzdelkiMetadata = em.find(UporabnikoviIzdelkiMetadataEntity.class, id);

        if (uporabnikoviIzdelkiMetadata != null) {
            try {
                beginTx();
                em.remove(uporabnikoviIzdelkiMetadata);
                commitTx();
            }
            catch (Exception e) {
                rollbackTx();
            }
        }
        else {
            return false;
        }

        return true;
    }

     */

    private void beginTx() {
        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }
    }

    private void commitTx() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().commit();
        }
    }

    private void rollbackTx() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
    }
}
