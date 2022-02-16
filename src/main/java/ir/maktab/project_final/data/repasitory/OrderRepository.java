package ir.maktab.project_final.data.repasitory;

import ir.maktab.project_final.data.entity.enums.OrderStatus;
import ir.maktab.project_final.data.entity.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer>, JpaSpecificationExecutor<Order> {

    List<Order> findByStatusOrStatus(OrderStatus statusSelect, OrderStatus statusSuggest);

    Optional<Order> findByReceptionNumber(long number);


}

