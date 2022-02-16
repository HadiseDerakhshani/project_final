package ir.maktab.project_final.data.dto.mapper;

import ir.maktab.project_final.data.dto.SubServiceDto;
import ir.maktab.project_final.data.entity.serviceSystem.SubService;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component

public class SubServiceMap {

    public SubService createSubService(SubServiceDto subServiceDto) {
        SubService service = SubService.builder()
                .name(subServiceDto.getName())
                .price(subServiceDto.getPrice())
                .description(subServiceDto.getDescription())
                .build();

        return service;

    }

    public SubServiceDto createSubServiceDto(SubService subService) {
        SubServiceDto serviceDto = SubServiceDto.builder()
                .name(subService.getName())
                .price(subService.getPrice())
                .description(subService.getDescription())
                .build();
        return serviceDto;

    }
}
