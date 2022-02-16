package ir.maktab.project_final.data.repasitory;

import ir.maktab.project_final.data.entity.user.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, Integer> {


    Optional<Manager> findByUsernameAndPassword(String name, String pass);
}
