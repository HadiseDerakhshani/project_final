package ir.maktab.project_final.data.repasitory;

import ir.maktab.project_final.data.entity.order.Suggestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SuggestionRepository extends JpaRepository<Suggestion, Integer> {

    Optional<Suggestion> findByReceptionNumber(long number);


}
