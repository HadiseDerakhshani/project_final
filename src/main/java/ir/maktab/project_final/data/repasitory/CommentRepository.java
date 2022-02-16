package ir.maktab.project_final.data.repasitory;

import ir.maktab.project_final.data.entity.order.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

}
