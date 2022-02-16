package ir.maktab.project_final.data.dto.mapper;

import ir.maktab.project_final.data.dto.CommentDto;
import ir.maktab.project_final.data.entity.order.Comment;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Data
@Component
@RequiredArgsConstructor
public class CommentMap {

    @Lazy
    private final CustomerMap customerMap;
    @Lazy
    private final ExpertMap expertMap;

    public Comment createComment(CommentDto commentDto) {
        Comment comment = Comment.builder()
                .commentText(commentDto.getCommentText())
                .registerComment(commentDto.getRegisterComment())
                .build();
        if (commentDto.getCustomer() != null)
            comment.setCustomer(customerMap.createCustomer(commentDto.getCustomer()));
        if (commentDto.getExpert() != null)
            comment.setExpert(expertMap.createExpert(commentDto.getExpert()));
        return comment;
    }

    public CommentDto createCommentDto(Comment comment) {
        CommentDto commentDto = CommentDto.builder()
                .commentText(comment.getCommentText())
                .registerComment(comment.getRegisterComment())
                .build();
        if (comment.getCustomer() != null)
            commentDto.setCustomer(customerMap.createCustomerDto(comment.getCustomer()));
        if (comment.getExpert() != null)
            commentDto.setExpert(expertMap.createExpertDto(comment.getExpert()));
        return commentDto;
    }
}
