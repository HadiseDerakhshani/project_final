package ir.maktab.project_final.data.dto.mapper;

import ir.maktab.project_final.data.dto.CustomerDto;
import ir.maktab.project_final.data.entity.user.Customer;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Data
@Component
@RequiredArgsConstructor
public class CustomerMap {

    private final OrderMap orderMap;


    public Customer createCustomer(CustomerDto customerDto) {

        Customer customer = Customer.builder()
                .firstName(customerDto.getFirstName())
                .lastName(customerDto.getLastName())
                .password(customerDto.getPassword())
                .phoneNumber(customerDto.getPhoneNumber())
                .credit(customerDto.getCredit())
                .email(customerDto.getEmail())
                .build();

        if (customerDto.getOrderList() != null) {
            customer.setOrderList(customerDto.getOrderList().stream().map(orderMap::createOrder).collect(Collectors.toList()));

        }
        return customer;
    }

    public CustomerDto createCustomerDto(Customer customer) {
        CustomerDto customerDto = CustomerDto.builder()
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .password(customer.getPassword())
                .phoneNumber(customer.getPhoneNumber())
                .credit(customer.getCredit())
                .email(customer.getEmail())
                .userStatus(customer.getUserStatus())
                .dateUpdate(customer.getDateUpdate())
                .build();

        if (customer.getOrderList() != null) {
            customerDto.setOrderList(customer.getOrderList().stream().map(orderMap::createOrderDto)
                    .collect(Collectors.toList()));

        }
        return customerDto;
    }
}
