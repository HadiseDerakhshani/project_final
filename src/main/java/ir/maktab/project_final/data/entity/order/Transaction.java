package ir.maktab.project_final.data.entity.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private long transactionNumber;
    private double amount;
    @OneToOne(cascade = CascadeType.ALL)
    private Order order;
    @CreationTimestamp
    @Temporal(TemporalType.DATE)
    private Date registerDate;


}
