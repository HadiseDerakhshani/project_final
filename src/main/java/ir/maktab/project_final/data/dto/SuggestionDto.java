package ir.maktab.project_final.data.dto;

import ir.maktab.project_final.data.entity.enums.SuggestionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SuggestionDto {

    private Date dateRegisterSuggest;

    private double proposedPrice;

    private int durationOfWork;

    @Min(value = 6, message = "min of start time is 6")
    @Max(value = 24, message = "max of start time is 24")
    private int startTime;

    private long receptionNumber;

    private SuggestionStatus status;

    private ExpertDto expert;

    private OrderDto order;


}
