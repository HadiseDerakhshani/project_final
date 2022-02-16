package ir.maktab.project_final.data.dto.mapper;

import ir.maktab.project_final.data.dto.ServiceDto;
import ir.maktab.project_final.data.entity.serviceSystem.Service;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Data
@Component

public class ServiceMap {

    private final SubServiceMap subServiceMap;

    @Autowired
    public ServiceMap(@Lazy SubServiceMap subServiceMap) {

        this.subServiceMap = subServiceMap;
    }

    public Service createService(ServiceDto serviceDto) {
        Service service = Service.builder()
                .name(serviceDto.getName())
                .build();
        if (serviceDto.getSubServiceList() != null) {
            service.setSubServiceList(serviceDto.getSubServiceList().stream().map(subServiceMap::createSubService)
                    .collect(Collectors.toSet()));
        }
        return service;
    }

    public ServiceDto createServiceDto(Service service) {
        ServiceDto serviceDto = ServiceDto.builder()
                .name(service.getName())
                .build();
        if (service.getSubServiceList() != null) {
            serviceDto.setSubServiceList(service.getSubServiceList().stream().map(subServiceMap::createSubServiceDto)
                    .collect(Collectors.toSet()));
        }
        return serviceDto;
    }
}
