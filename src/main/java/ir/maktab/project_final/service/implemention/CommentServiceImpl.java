package ir.maktab.project_final.service.implemention;

import ir.maktab.project_final.data.dto.CommentDto;
import ir.maktab.project_final.data.dto.OrderDto;
import ir.maktab.project_final.data.dto.mapper.CommentMap;
import ir.maktab.project_final.data.entity.order.Comment;
import ir.maktab.project_final.data.entity.order.Order;
import ir.maktab.project_final.data.repasitory.CommentRepository;
import ir.maktab.project_final.service.CommentService;
import ir.maktab.project_final.service.OrderService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Getter
@Service

public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentMap commentMap;
    private final OrderService orderService;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, @Lazy CommentMap commentMap, @Lazy OrderService orderService) {
        this.commentRepository = commentRepository;
        this.commentMap = commentMap;
        this.orderService = orderService;
    }

    @Override
    public CommentDto save(OrderDto orderDto, String text) {
        Order order = orderService.findByReceptionNumber(orderDto.getReceptionNumber());
        Comment comment = Comment.builder()
                .commentText(text)
                .customer(order.getCustomer())
                .expert(order.getExpert()).build();

        return commentMap.createCommentDto(comment);

    }
}
