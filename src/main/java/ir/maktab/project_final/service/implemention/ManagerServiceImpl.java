package ir.maktab.project_final.service.implemention;

import ir.maktab.project_final.data.dto.CustomerDto;
import ir.maktab.project_final.data.dto.mapper.ManagerMap;
import ir.maktab.project_final.data.entity.enums.UserStatus;
import ir.maktab.project_final.data.entity.order.Order;
import ir.maktab.project_final.data.entity.user.Expert;
import ir.maktab.project_final.data.entity.user.Manager;
import ir.maktab.project_final.data.repasitory.ManagerRepository;
import ir.maktab.project_final.exception.ObjectEntityNotFoundException;
import ir.maktab.project_final.service.CustomerService;
import ir.maktab.project_final.service.ExpertService;
import ir.maktab.project_final.service.ManagerService;
import ir.maktab.project_final.service.OrderService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Getter
@Service

public class ManagerServiceImpl implements ManagerService {

    private final CustomerService customerService;

    private final OrderService orderService;

    private final ExpertService expertService;
    private final ManagerRepository managerRepository;

    private final ManagerMap managerMap;

    @Autowired
    public ManagerServiceImpl(@Lazy CustomerService customerService, @Lazy OrderService orderService,
                              @Lazy ExpertService expertService, ManagerRepository managerRepository, @Lazy ManagerMap managerMap) {
        this.customerService = customerService;
        this.orderService = orderService;
        this.expertService = expertService;
        this.managerRepository = managerRepository;
        this.managerMap = managerMap;
    }

    @Override
    public Manager createManager(String userName, String pass) {
        Manager manager = Manager.builder()
                .username(userName)
                .password(pass).build();
        if (manager != null)
            throw new ObjectEntityNotFoundException("-- Manager is null --");
        return manager;
    }

    @Override
    public void Save(Manager manager) {
        if (checkManager(manager) == null) {
            managerRepository.save(manager);
        }
        throw new ObjectEntityNotFoundException("--- manager is exit ----");
    }

    @Override
    public Manager checkManager(Manager manager) {

        return managerRepository.findByUsernameAndPassword(manager.getUsername(), manager.getPassword()).get();
    }

    @Override
    public void customerConfirmation() {

        List<CustomerDto> byStatus = customerService.findByUserStatus(UserStatus.WAITING_CONFIRM);
        List<String> customerListEmail = new ArrayList<>();
        for (CustomerDto customer : byStatus) {
            if (customer.getDateRegister().compareTo(customer.getDateUpdate()) == -1)
                customerListEmail.add(customer.getEmail());
        }
        for (String email : customerListEmail) {
            customerService.updateStatus(email, UserStatus.CONFIRMED);
        }
    }

    @Override
    public void payment(int number, double amount, int score) {

        Order order = orderService.findByReceptionNumber(number);

        if (order != null) {
            Expert expert = order.getExpert();
            //  orderServiceImpl.updatePricePaid(order, amount);
            // orderServiceImpl.updateStatus(order, OrderStatus.PAID);//todo
            //   expertServiceImpl.updateScore(score, expert);
            expertService.updateCredit((0.80 * amount), expert);

        } else
            throw new ObjectEntityNotFoundException(" order is not exit");
    }
}
