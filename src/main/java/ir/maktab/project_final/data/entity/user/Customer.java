package ir.maktab.project_final.data.entity.user;

import ir.maktab.project_final.data.entity.order.Order;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode
@NoArgsConstructor
@SuperBuilder
@Data
@Entity

public class Customer extends User {
    @Transient
    @OneToMany(mappedBy = "customer", fetch = FetchType.EAGER)
    private List<Order> orderList = new ArrayList<>();


}
