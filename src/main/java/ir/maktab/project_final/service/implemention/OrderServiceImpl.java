package ir.maktab.project_final.service.implemention;

import ir.maktab.project_final.data.dto.*;
import ir.maktab.project_final.data.dto.mapper.OrderMap;
import ir.maktab.project_final.data.dto.mapper.SubServiceMap;
import ir.maktab.project_final.data.entity.enums.OrderStatus;
import ir.maktab.project_final.data.entity.enums.SuggestionStatus;
import ir.maktab.project_final.data.entity.order.Order;
import ir.maktab.project_final.data.entity.order.Suggestion;
import ir.maktab.project_final.data.entity.serviceSystem.SubService;
import ir.maktab.project_final.data.entity.user.Customer;
import ir.maktab.project_final.data.entity.user.Expert;
import ir.maktab.project_final.data.repasitory.OrderRepository;
import ir.maktab.project_final.data.repasitory.OrderSpecifications;
import ir.maktab.project_final.exception.ObjectEntityNotFoundException;
import ir.maktab.project_final.service.*;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Service

public class OrderServiceImpl implements OrderService {

    private final OrderMap orderMap;

    private final OrderRepository orderRepository;
    private final SubServiceMap subServiceMap;
    private final SubServiceService subServiceService;
    private final SuggestionService suggestionService;
    private final ExpertService expertService;
    private final TransactionService transactionService;
    private final ServiceService service;
    private final CustomerService customerService;

    @Autowired
    public OrderServiceImpl(@Lazy OrderMap orderMap, OrderRepository orderRepository, @Lazy SubServiceServiceImpl subServiceService,
                            @Lazy ExpertServiceImpl expertService, @Lazy CustomerServiceImpl customerService,
                            @Lazy SubServiceMap subServiceMap, @Lazy SuggestionServiceImpl suggestionService,
                            @Lazy TransactionServiceImpl transactionService, @Lazy ServiceServiceImpl service) {
        this.orderMap = orderMap;
        this.orderRepository = orderRepository;
        this.subServiceService = subServiceService;
        this.expertService = expertService;
        this.customerService = customerService;
        this.subServiceMap = subServiceMap;
        this.suggestionService = suggestionService;
        this.transactionService = transactionService;
        this.service = service;
    }


    @Override
    public List<OrderDto> findOrderToSuggest() {
        List<Order> list = orderRepository.findByStatusOrStatus(OrderStatus.WAITING_FOR_EXPERT_SUGGESTION,
                OrderStatus.WAITING_FOR_EXPERT_SELECTION);
        if (list != null) {
            return list.stream().map(orderMap::createOrderDto).collect(Collectors.toList());
        } else
            throw new ObjectEntityNotFoundException("---order is not for given Suggestion---");
    }

    @Override
    public Order giveReceptionNumber(Order order) {
        order.setReceptionNumber(1000 + order.getId());
        return order;
    }

    @Override
    public OrderDto find(long number) {
        return orderMap.createOrderDto(findByReceptionNumber(number));
    }

    @Override
    public Order findByReceptionNumber(long number) {
        return orderRepository.findByReceptionNumber(number).get();
    }

    @Override
    public void updateStatus(OrderDto order, OrderStatus status) {
        Order orderFound = findByReceptionNumber(order.getReceptionNumber());
        orderFound.setStatus(status);
        orderRepository.save(orderFound);
    }

    @Override
    public void updatePricePaid(OrderDto order, double amount) {
        Order orderFound = findByReceptionNumber(order.getReceptionNumber());
        orderFound.setPricePaid(amount);
        Expert expert = orderFound.getExpert();
        expertService.updateCredit(amount, expert);
        orderFound.setStatus(OrderStatus.PAID);
        Order saveOrder = orderRepository.save(orderFound);
        OrderDto orderDto = orderMap.createOrderDto(saveOrder);
        transactionService.save(orderDto, amount);
    }

    @Override
    public void addSuggestionToOrder(OrderDto order, SuggestionDto suggest) {
        Order orderFound = findByReceptionNumber(order.getReceptionNumber());
        if (order.getStatus().equals(OrderStatus.WAITING_FOR_EXPERT_SUGGESTION))
            orderFound.setStatus(OrderStatus.WAITING_FOR_EXPERT_SELECTION);
        Suggestion suggestion = suggestionService.findByReceptionNumber(suggest.getReceptionNumber());
        orderFound.getSuggestion().add(suggestion);
        orderRepository.save(orderFound);
    }

    @Override
    public void addExpertToOrder(ExpertDto expert, OrderDto order) {

        Order orderFound = findByReceptionNumber(order.getReceptionNumber());
        orderFound.setExpert(expertService.findByEmail(expert.getEmail()).get());
        orderRepository.save(orderFound);
    }

    @Override
    public void addCustomerToOrder(CustomerDto customer, OrderDto order) {

        Order orderFound = findByReceptionNumber(order.getReceptionNumber());
        orderFound.setCustomer(customerService.findByEmail(customer.getEmail()).get());
        orderRepository.save(orderFound);
    }

    @Override
    public void addServiceToOrder(SubServiceDto subServiceDto, OrderDto order) {

        Order orderFound = findByReceptionNumber(order.getReceptionNumber());
        orderFound.setService(subServiceService.find(subServiceDto.getName()));
        orderRepository.save(orderFound);
    }

    @Override
    public List<OrderDto> findAll() {
        List<Order> list = orderRepository.findAll();
        List<OrderDto> listDto = new ArrayList<>();
        if (list != null) {
            for (Order order : list) {
                listDto.add(orderMap.createOrderDto(order));
            }
            return listDto;
        } else
            throw new ObjectEntityNotFoundException(" --- Order is not exit ---");
    }

    @Override
    public List<OrderDto> findOrderToSelectExpert(Customer customer) {

        List<Order> list = orderRepository.findAll();
        List<OrderDto> listFind = findAll();
        if (list != null) {
            List<Order> orderList = list.stream().filter(o -> o.getCustomer().getEmail().equals(customer.getEmail()))
                    .filter(o -> o.getStatus().equals(OrderStatus.WAITING_FOR_EXPERT_SELECTION))
                    .collect(Collectors.toList());
            if (orderList != null) {
                return listFind = orderList.stream().map(orderMap::createOrderDto).collect(Collectors.toList());
            } else
                throw new ObjectEntityNotFoundException(" --- Order is not exit for select expert ---");
        } else
            throw new ObjectEntityNotFoundException(" --- Order is not exit yet ---");
    }

    @Override
    public OrderDto findOrderToPayment(CustomerDto customer) {

        List<OrderDto> list = findOrderByCustomer(customer);
        if (list != null) {
            return list.stream().filter(o -> o.getCustomer().getEmail().equals(customer.getEmail()))
                    .filter(o -> o.getStatus().equals(OrderStatus.DONE)).findFirst().orElse(null);

        } else
            throw new ObjectEntityNotFoundException(" --- Order is not exit yet ---");
    }

    @Override
    public void startAndEndOrder(int number, int chose, Expert expert) {
      /*  Order order = findByReceptionNumber(number);
        if (order != null) {
            if (chose == 6)
                updateStatus(order, OrderStatus.STARTED);
            else if (chose == 7) {
                updateStatus(order, OrderStatus.DONE);
                expertServiceImpl.updateStatus(UserStatus.WAITING_CONFIRM, expert);
            }
        } else
            throw new ObjectEntityNotFoundException(" order is not exit");*/
    }

    public OrderDto save(OrderDto orderDto) {
        Order save = orderRepository.save(orderMap.createOrder(orderDto));
        Order order = giveReceptionNumber(save);
        orderRepository.save(order);
        return orderMap.createOrderDto(order);
    }


    @Override
    public List<OrderDto> findOrderByCustomer(CustomerDto customer) {
        Customer customerFound = customerService.findByEmail(customer.getEmail()).get();
        List<Order> list = orderRepository.findAll();
        if (list != null) {
            List<Order> orderList = list.stream().filter(o -> o.getCustomer().getId() == customerFound.getId())
                    .collect(Collectors.toList());
            if (orderList != null) {
                return orderList.stream().map(orderMap::createOrderDto)
                        .sorted(Comparator.comparing(o -> o.getRegisterDate()))
                        .collect(Collectors.toList());
            } else
                throw new ObjectEntityNotFoundException(" --- Order is not exit for select expert ---");
        } else
            throw new ObjectEntityNotFoundException(" --- Order is not exit yet ---");
    }

    @Override
    public OrderDto update(Order order) {
        Order saveOrder = orderRepository.save(order);
        return orderMap.createOrderDto(saveOrder);
    }

    @Override
    public List<OrderDto> findOrderByExpert(ExpertDto expertDto) {
        Expert expert = expertService.findByEmail(expertDto.getEmail()).get();
        List<Order> list = orderRepository.findAll();
        if (list != null) {
            List<Order> orderList = list.stream().filter(o -> o.getExpert() != null && o.getExpert().getId() == expert.getId()).collect(Collectors.toList());
            if (orderList != null) {
                return orderList.stream().map(orderMap::createOrderDto)
                        .collect(Collectors.toList());
            } else
                throw new ObjectEntityNotFoundException(" --- Order is not exit for select expert ---");
        } else
            throw new ObjectEntityNotFoundException(" --- Order is not exit yet ---");
    }

    @Override
    public Double calculatePrice(OrderDto order) {
        Double amount, priceSuggest, priceService, proposedPrice;
        priceService = order.getService().getPrice();
        proposedPrice = order.getProposedPrice();
        priceSuggest = order.getSuggestion().stream().filter(s -> s.getStatus().equals(SuggestionStatus.CONFIRMED))
                .map(s -> s.getProposedPrice()).findAny().orElse(null);
        amount = proposedPrice <= priceService ? priceService : proposedPrice;
        if (priceSuggest != null)
            amount = priceSuggest > amount ? priceService : amount;

        return amount;
    }

    @Override
    public List<OrderDto> filtering(OrderFilterDto orderFilterDto) {
        Set<SubService> subServiceList = new HashSet<>();
        if (orderFilterDto.getService() != null && !orderFilterDto.getService().isEmpty()) {
            ir.maktab.project_final.data.entity.serviceSystem.Service serviceFound = service.find(orderFilterDto.getService()).get();
            subServiceList = serviceFound.getSubServiceList();
        }
        Pageable pageable = PageRequest.of(orderFilterDto.getPageNumber(), orderFilterDto.getPageSize());
        Specification<Order> specification = OrderSpecifications.filterByCriteria(orderFilterDto, subServiceList);
        return orderRepository
                .findAll(specification, pageable)
                .stream()
                .map(orderMap::createOrderDto)
                .collect(Collectors.toList());

    }
}