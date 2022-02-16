package ir.maktab.project_final.service;

import ir.maktab.project_final.data.dto.ExpertDto;
import ir.maktab.project_final.data.dto.OrderDto;
import ir.maktab.project_final.data.entity.enums.UserStatus;
import ir.maktab.project_final.data.entity.user.Expert;

import java.util.List;
import java.util.Optional;


public interface ExpertService {


    Expert save(ExpertDto expert);

    ExpertDto update(Expert expert);

    void updatePassword(ExpertDto expertDto, String newPass);

    void updatePhoneNumber(ExpertDto expertDto, String newPhoneNumber);

    Optional<Expert> findByEmail(String email);

    ExpertDto addSubServiceToExpert(ExpertDto expertDto, String name);

    ExpertDto find(String email);


    void updateScore(int score, OrderDto orderDto);

    void updateStatus(UserStatus status, Expert expert);

    void updateCredit(double amount, Expert expert);

    List<ExpertDto> findAll();


    void deleteExpert(String email);

}
