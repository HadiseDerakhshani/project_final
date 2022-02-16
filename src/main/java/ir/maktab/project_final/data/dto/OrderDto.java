package ir.maktab.project_final.data.dto;

import ir.maktab.project_final.data.entity.enums.OrderStatus;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Data
public class OrderDto {

    private double proposedPrice;
    private String jobDescription;
    private Date registerDate;
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private Date doDate;
    private long receptionNumber;
    private double PricePaid;
    private SubServiceDto service;
    private OrderStatus status;
    private CustomerDto customer;
    private ExpertDto expert;
    private AddressDto address;
    private List<SuggestionDto> suggestion = new ArrayList<>();


}
