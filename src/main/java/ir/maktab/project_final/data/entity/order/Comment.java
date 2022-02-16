package ir.maktab.project_final.data.entity.order;

import ir.maktab.project_final.data.entity.user.Customer;
import ir.maktab.project_final.data.entity.user.Expert;
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
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String commentText;
    @OneToOne(cascade = CascadeType.ALL)
    private Customer customer;
    @OneToOne(cascade = CascadeType.ALL)
    private Expert expert;
    @CreationTimestamp
    @Temporal(TemporalType.DATE)
    private Date registerComment;
}
