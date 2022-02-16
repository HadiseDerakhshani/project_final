package ir.maktab.project_final.data.dto.mapper;

import ir.maktab.project_final.data.dto.ExpertDto;
import ir.maktab.project_final.data.entity.user.Expert;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@Data
public class ExpertMap {

    private final SubServiceMap subServiceMap;
    private final OrderMap orderMap;
    private final CommentMap commentMap;

    @Autowired
    public ExpertMap(@Lazy SubServiceMap subServiceMap,
                     @Lazy OrderMap orderMap, @Lazy CommentMap commentMap) {

        this.subServiceMap = subServiceMap;
        this.orderMap = orderMap;
        this.commentMap = commentMap;
    }

    public Expert createExpert(ExpertDto expertDto) {
        Expert expert = Expert.builder()
                .firstName(expertDto.getFirstName())
                .lastName(expertDto.getLastName())
                .email(expertDto.getEmail())
                .password(expertDto.getPassword())
                .phoneNumber(expertDto.getPhoneNumber())
                .credit(expertDto.getCredit())
                .photo(expertDto.getPhoto())
                .userStatus(expertDto.getUserStatus())
                .dateUpdate(expertDto.getDateUpdate())
                .build();
        if (expertDto.getServiceList() != null) {
            expert.setServiceList(expertDto.getServiceList().stream().map(subServiceMap::createSubService)
                    .collect(Collectors.toSet()));
        }
        if (expertDto.getOrderList() != null) {
            expert.setOrderList(expertDto.getOrderList().stream().map(orderMap::createOrder)
                    .collect(Collectors.toList()));
        }

        if (expertDto.getCommentList() != null) {
            expert.setCommentList(expertDto.getCommentList().stream().map(commentMap::createComment)
                    .collect(Collectors.toSet()));
        }
        return expert;
    }

    public ExpertDto createExpertDto(Expert expert) {
        ExpertDto expertDto = ExpertDto.builder()
                .firstName(expert.getFirstName())
                .lastName(expert.getLastName())
                .email(expert.getEmail())
                .password(expert.getPassword())
                .phoneNumber(expert.getPhoneNumber())
                .credit(expert.getCredit())
                .photo(expert.getPhoto())
                .userStatus(expert.getUserStatus())
                .dateUpdate(expert.getDateUpdate())
                .build();
        if (expert.getServiceList() != null) {
            expertDto.setServiceList(expert.getServiceList().stream().map(subServiceMap::createSubServiceDto)
                    .collect(Collectors.toList()));
        }
        if (expert.getOrderList() != null) {
            expertDto.setOrderList(expert.getOrderList().stream().map(orderMap::createOrderDto)
                    .collect(Collectors.toList()));
        }

        if (expertDto.getCommentList() != null) {
            expertDto.setCommentList(expert.getCommentList().stream().map(commentMap::createCommentDto)
                    .collect(Collectors.toList()));
        }
        return expertDto;
    }
}
