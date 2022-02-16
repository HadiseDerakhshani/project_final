package ir.maktab.project_final.service;

import ir.maktab.project_final.data.dto.ExpertDto;
import ir.maktab.project_final.data.dto.OrderDto;
import ir.maktab.project_final.data.dto.SuggestionDto;
import ir.maktab.project_final.data.entity.enums.SuggestionStatus;
import ir.maktab.project_final.data.entity.order.Suggestion;

import java.util.List;


public interface SuggestionService {


    SuggestionDto save(SuggestionDto suggestion, OrderDto orderDto, ExpertDto expertDto);

    Suggestion findByReceptionNumber(long number);

    List<SuggestionDto> findByExpert(ExpertDto expertDto);

    Suggestion giveReceptionNumber(Suggestion suggestion);

    void updateStatus(Suggestion suggestion, SuggestionStatus status);

    List<SuggestionDto> findByOrder(long number);

    void update(long number);
}
