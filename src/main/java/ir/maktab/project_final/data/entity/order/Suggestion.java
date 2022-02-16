package ir.maktab.project_final.data.entity.order;

import ir.maktab.project_final.data.entity.enums.SuggestionStatus;
import ir.maktab.project_final.data.entity.user.Expert;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@EqualsAndHashCode
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Suggestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @CreationTimestamp
    @Temporal(TemporalType.DATE)
    private Date dateRegisterSuggest;
    private double ProposedPrice;
    private int durationOfWork;
    private int startTime;
    private long receptionNumber;
    @Enumerated(EnumType.STRING)
    private SuggestionStatus status;
    @ManyToOne
    private Expert expert;
    @ManyToOne
    private Order order;

}
