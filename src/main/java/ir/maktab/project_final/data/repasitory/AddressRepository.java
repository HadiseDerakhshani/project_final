package ir.maktab.project_final.data.repasitory;

import ir.maktab.project_final.data.entity.order.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {

}
