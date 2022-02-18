package ir.maktab.project_final.service;

import ir.maktab.project_final.data.dto.*;
import ir.maktab.project_final.data.entity.enums.OrderStatus;
import ir.maktab.project_final.data.entity.order.Order;
import ir.maktab.project_final.data.entity.user.Customer;

import java.util.List;


public interface OrderService {


    OrderDto save(OrderDto orderDto);

    List<OrderDto> findOrderToSuggest();

    OrderDto find(long number);

    List<OrderDto> findOrderByCustomer(CustomerDto customer);

    void addCustomerToOrder(CustomerDto customer, OrderDto order);

    void addServiceToOrder(SubServiceDto subServiceDto, OrderDto order);

    Order giveReceptionNumber(Order order);

    Order findByReceptionNumber(long number);

    void updateStatus(OrderDto order, OrderStatus status);

    void updatePricePaid(OrderDto order, double amount);

    void addSuggestionToOrder(OrderDto order, SuggestionDto suggest);

    void addExpertToOrder(ExpertDto expert, OrderDto order);

    List<OrderDto> findAll();

    List<OrderDto> findOrderToSelectExpert(Customer customer);

    OrderDto update(Order order);

    List<OrderDto> findOrderByExpert(ExpertDto expertDto);

    OrderDto findOrderToPayment(CustomerDto customer);

    public OrderDto startAndEndOrder(long number);

    Double calculatePrice(OrderDto orderDto);

    List<OrderDto> filtering(OrderFilterDto orderFilterDto);
}