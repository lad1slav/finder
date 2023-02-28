package en.ladislav.finderapi.repository;

import en.ladislav.finderapi.utility.Item;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends CrudRepository<Item, Integer> {
    @org.springframework.data.jdbc.repository.query.
        Query("SELECT * from item where query = :query")
    List<Item> getAllByQuery(Integer query);
}
