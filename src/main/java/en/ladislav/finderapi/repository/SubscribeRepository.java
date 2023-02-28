package en.ladislav.finderapi.repository;

import en.ladislav.finderapi.model.Subscribe;
import en.ladislav.finderapi.utility.Item;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscribeRepository extends CrudRepository<Subscribe, Integer> {

}
