package ir.maktab.project_final.data.repasitory;


import ir.maktab.project_final.data.entity.user.Expert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExpertRepository extends JpaRepository<Expert, Integer> {


    Optional<Expert> findByEmail(String email);


}