package ir.maktab.project_final.service.implemention;

import ir.maktab.project_final.data.dto.CustomerDto;
import ir.maktab.project_final.data.dto.OrderDto;
import ir.maktab.project_final.data.dto.mapper.CustomerMap;
import ir.maktab.project_final.data.dto.mapper.OrderMap;
import ir.maktab.project_final.data.entity.enums.UserRole;
import ir.maktab.project_final.data.entity.enums.UserStatus;
import ir.maktab.project_final.data.entity.user.Customer;
import ir.maktab.project_final.data.repasitory.CustomerRepository;
import ir.maktab.project_final.exception.DuplicateServiceException;
import ir.maktab.project_final.exception.ObjectEntityNotFoundException;
import ir.maktab.project_final.service.*;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    private final SuggestionService suggestService;

    private final OrderService orderService;

    private final ExpertService expertService;

    private final CommentService commentService;
    private final UserService userService;
    private final CustomerMap customerMap;
    private final OrderMap orderMap;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository, @Lazy SuggestionService suggestService,
                               @Lazy OrderService orderService, @Lazy ExpertService expertService,
                               @Lazy CommentService commentService, @Lazy OrderMap orderMap,
                               @Lazy CustomerMap customerMap, @Lazy UserService userService) {
        this.customerRepository = customerRepository;
        this.suggestService = suggestService;
        this.orderService = orderService;
        this.expertService = expertService;
        this.commentService = commentService;
        this.customerMap = customerMap;
        this.orderMap = orderMap;
        this.userService = userService;
    }

    @Override
    public Customer save(CustomerDto customerDto) {
        if (!findByEmail(customerDto.getEmail()).isPresent()) {
            Customer customer = customerMap.createCustomer(customerDto);
            customer.setUserStatus(UserStatus.WAITING_CONFIRM);
            customer.setUserRole(UserRole.CUSTOMER);
            return customerRepository.save(customer);

        } else
            throw new DuplicateServiceException("-- Customer is exit for this email --");
    }


    @Override
    public List<CustomerDto> findByUserStatus(UserStatus status) {

        List<Customer> list = customerRepository.findByUserStatus(status);

        String nameStatus = status.name();
        List<CustomerDto> listDto = new ArrayList<>();
        if (list.size() != 0) {
            for (Customer customer : list) {
                listDto.add(customerMap.createCustomerDto(customer));
            }
            return listDto;
        } else
            throw new ObjectEntityNotFoundException(" --- customer is not by userStatus " + nameStatus + " ---");
    }

    @Override
    public void deleteCustomer(String email) {
        customerRepository.delete(customerRepository.findByEmail(email).get());
    }

    @Override
    public void changePassword(CustomerDto customer, String newPass) {
        Customer customerFound = findByEmail(customer.getEmail()).get();
        customerFound.setPassword(newPass);
        customerRepository.save(customerFound);
    }

    @Override
    public void updateOrder(CustomerDto customerDto, OrderDto orderDto) {
        Customer customerFound = findByEmail(customerDto.getEmail()).get();
        customerDto.getOrderList().add(orderDto);
        customerFound.setOrderList(customerDto.getOrderList().stream().map(orderMap::createOrder)
                .collect(Collectors.toList()));
        customerRepository.save(customerFound);
    }

    @Override
    public void updateStatus(String email, UserStatus status) {
        Customer customerFound = findByEmail(email).get();
        customerFound.setUserStatus(status);
        customerRepository.save(customerFound);

    }

    @Override
    public void changePhoneNumber(CustomerDto customer, String newPhoneNumber) {
        Customer customerFound = findByEmail(customer.getEmail()).get();
        customerFound.setPhoneNumber(newPhoneNumber);
        customerRepository.save(customerFound);

    }

    @Override
    public Optional<Customer> findByEmail(String email) {

        return customerRepository.findByEmail(email);

    }

    @Override
    public CustomerDto find(String email) {

        if (findByEmail(email).isPresent())
            return customerMap.createCustomerDto(findByEmail(email).get());
        else {
            throw new ObjectEntityNotFoundException(" --- customer is not by email  ---");
        }
    }

    @Override
    public void increaseCredit(Customer customer, double amount) {
        Customer customerFound = findByEmail(customer.getEmail()).get();
        double credit = customerFound.getCredit();
        customerFound.setCredit(credit + amount);
        customerRepository.save(customerFound);
    }

    @Override
    public void decreaseCredit(CustomerDto customer, double amount) {
        Customer customerFound = findByEmail(customer.getEmail()).get();
        double credit = customerFound.getCredit();
        customerFound.setCredit(credit - amount);
        customerRepository.save(customerFound);
    }

    @Override
    public List<CustomerDto> findAll(int pageNumber, int pageSize) {
        Page<Customer> pageList = customerRepository.findAll(PageRequest.of(pageNumber, pageSize));
        List<Customer> list = pageList.toList();
        List<CustomerDto> listDto = new ArrayList<>();
        if (list != null) {
            for (Customer customer : list) {
                listDto.add(customerMap.createCustomerDto(customer));
            }
            return listDto;
        } else
            throw new ObjectEntityNotFoundException(" --- list of customer is null ---");
    }

    @Override
    public List<CustomerDto> findAll() {
        return customerRepository.findAll().stream().map(customerMap::createCustomerDto).collect(Collectors.toList());
    }


    @Override
    public void payment(CustomerDto customerDto, OrderDto orderDto, double amount) {
        orderService.updatePricePaid(orderDto, amount);
        decreaseCredit(customerDto, amount);
    }


}
