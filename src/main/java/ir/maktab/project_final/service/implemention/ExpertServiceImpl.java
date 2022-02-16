package ir.maktab.project_final.service.implemention;

import ir.maktab.project_final.data.dto.ExpertDto;
import ir.maktab.project_final.data.dto.OrderDto;
import ir.maktab.project_final.data.dto.mapper.ExpertMap;
import ir.maktab.project_final.data.dto.mapper.SubServiceMap;
import ir.maktab.project_final.data.dto.mapper.SuggestionMap;
import ir.maktab.project_final.data.entity.enums.UserRole;
import ir.maktab.project_final.data.entity.enums.UserStatus;
import ir.maktab.project_final.data.entity.order.Order;
import ir.maktab.project_final.data.entity.serviceSystem.SubService;
import ir.maktab.project_final.data.entity.user.Expert;
import ir.maktab.project_final.data.repasitory.ExpertRepository;
import ir.maktab.project_final.data.repasitory.SubServiceRepository;
import ir.maktab.project_final.exception.DuplicateServiceException;
import ir.maktab.project_final.exception.ObjectEntityNotFoundException;
import ir.maktab.project_final.service.*;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@Service

public class ExpertServiceImpl implements ExpertService {

    private final ExpertMap expertMap;
    private final ExpertRepository expertRepository;
    private final SubServiceMap subServiceMap;
    private final SubServiceRepository subServiceRepository;
    private final SuggestionMap suggestionMap;

    private final SubServiceService subServiceService;

    private final OrderService orderService;
    private final UserService userService;
    private final SuggestionService suggestionService;

    @Autowired
    public ExpertServiceImpl(@Lazy ExpertMap expertMap, ExpertRepository expertRepository, @Lazy SubServiceService subServiceService,
                             @Lazy OrderService orderService, @Lazy SuggestionMap suggestionMap,
                             @Lazy SuggestionService suggestionService, @Lazy SubServiceMap subServiceMap,
                             @Lazy SubServiceRepository subServiceRepository, @Lazy UserService userService) {
        this.expertMap = expertMap;
        this.expertRepository = expertRepository;
        this.subServiceService = subServiceService;
        this.orderService = orderService;
        this.suggestionService = suggestionService;
        this.suggestionMap = suggestionMap;
        this.subServiceMap = subServiceMap;
        this.subServiceRepository = subServiceRepository;
        this.userService = userService;
    }

    @Override
    public Expert save(ExpertDto expertDto) {
        if (!findByEmail(expertDto.getEmail()).isPresent()) {
            Expert expert = expertMap.createExpert(expertDto);
            expert.setUserStatus(UserStatus.WAITING_CONFIRM);
            expert.setUserRole(UserRole.EXPERT);
            return expertRepository.save(expert);
        } else
            throw new DuplicateServiceException("-- Expert is exit for this email --");
    }


    @Override
    public void updatePassword(ExpertDto expertDto, String newPass) {
        Expert expert = findByEmail(expertDto.getEmail()).get();
        expert.setPassword(newPass);
        expertRepository.save(expert);
    }

    @Override
    public void updatePhoneNumber(ExpertDto expertDto, String newPhoneNumber) {
        Expert expert = findByEmail(expertDto.getEmail()).get();
        expert.setPhoneNumber(newPhoneNumber);
        expertRepository.save(expert);
    }

    @Override
    public Optional<Expert> findByEmail(String email) {
        return expertRepository.findByEmail(email);
    }

    @Override
    public ExpertDto find(String email) {
        return expertMap.createExpertDto(findByEmail(email).get());
    }

    @Override
    public ExpertDto addSubServiceToExpert(ExpertDto expertDto, String name) {
        SubService subService = subServiceService.find(name);
        Expert expert = findByEmail(expertDto.getEmail()).get();
        expert.getServiceList().add(subService);
        expertRepository.save(expert);
        return expertMap.createExpertDto(expert);
    }


    @Override
    public void updateScore(int score, OrderDto orderDto) {
        Order order = orderService.findByReceptionNumber(orderDto.getReceptionNumber());
        Expert expertFound = order.getExpert();
        expertFound.setScore(score);
        expertRepository.save(expertFound);

    }

    @Override
    public void updateStatus(UserStatus status, Expert expert) {
        Expert expertFound = findByEmail(expert.getEmail()).get();
        expertFound.setUserStatus(status);
        expertRepository.save(expertFound);
    }

    @Override
    public void updateCredit(double amount, Expert expert) {

        double credit = expert.getCredit();
        expert.setCredit(credit + (amount * 0.70));
        expertRepository.save(expert);
    }


    @Override
    public void deleteExpert(String email) {
        expertRepository.delete(expertRepository.findByEmail(email).get());
    }

    @Override
    public ExpertDto update(Expert expert) {
        Expert saveExpert = expertRepository.save(expert);
        return expertMap.createExpertDto(saveExpert);
    }

    @Override
    public List<ExpertDto> findAll() {
        List<Expert> list = expertRepository.findAll();
        if (list != null)
            return list.stream().map(expertMap::createExpertDto).collect(Collectors.toList());
        else
            throw new ObjectEntityNotFoundException(" --- list of expert is null ---");
    }
}
