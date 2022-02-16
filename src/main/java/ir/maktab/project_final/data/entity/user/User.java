package ir.maktab.project_final.data.entity.user;

import ir.maktab.project_final.data.entity.enums.UserRole;
import ir.maktab.project_final.data.entity.enums.UserStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@SuperBuilder
@NoArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    private String firstName;
    private String lastName;
    @Column(unique = true, nullable = false)
    private String email;
    private String password;
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;
    @CreationTimestamp
    @Temporal(TemporalType.DATE)
    private Date dateRegister;
    @UpdateTimestamp
    private Date dateUpdate;
    @Enumerated(EnumType.STRING)
    private UserRole userRole;
    private double credit;
}
