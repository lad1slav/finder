package en.ladislav.finderapi.repository;

import en.ladislav.finderapi.model.Query;
import en.ladislav.finderapi.utility.Item;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QueryRepository extends CrudRepository<Query, Integer> {
    List<Query> findByQueryName(String queryName);
//    @org.springframework.data.jdbc.repository.query.
//            Query("SELECT * from query where id = :queryId")
//    List<Query> findByQueryId(Integer queryName);
}
