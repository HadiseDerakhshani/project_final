package ir.maktab.project_final.service;

import ir.maktab.project_final.data.dto.CommentDto;
import ir.maktab.project_final.data.dto.OrderDto;


public interface CommentService {


    CommentDto save(OrderDto orderDto, String text);
}
