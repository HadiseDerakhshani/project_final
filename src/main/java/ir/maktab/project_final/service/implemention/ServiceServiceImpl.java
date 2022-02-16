package ir.maktab.project_final.service.implemention;

import ir.maktab.project_final.data.dto.ServiceDto;
import ir.maktab.project_final.data.dto.SubServiceDto;
import ir.maktab.project_final.data.dto.mapper.ServiceMap;
import ir.maktab.project_final.data.entity.serviceSystem.Service;
import ir.maktab.project_final.data.entity.serviceSystem.SubService;
import ir.maktab.project_final.data.repasitory.ServiceRepository;
import ir.maktab.project_final.exception.DuplicateServiceException;
import ir.maktab.project_final.exception.ObjectEntityNotFoundException;
import ir.maktab.project_final.service.ServiceService;
import ir.maktab.project_final.service.SubServiceService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@org.springframework.stereotype.Service

public class ServiceServiceImpl implements ServiceService {
    private final ServiceRepository serviceRepository;

    private final ServiceMap serviceMap;
    private final SubServiceService subServiceService;


    @Autowired
    public ServiceServiceImpl(ServiceRepository serviceRepository, @Lazy ServiceMap serviceMap,
                              @Lazy SubServiceService subServiceService) {
        this.serviceRepository = serviceRepository;
        this.serviceMap = serviceMap;
        this.subServiceService = subServiceService;
    }

    @Override
    public ServiceDto save(String name) {
        if (!find(name).isPresent()) {
            Service service = Service.builder()
                    .name(name).build();
            Service saveService = serviceRepository.save(service);
            return serviceMap.createServiceDto(saveService);
        } else
            throw new DuplicateServiceException("--- exit Service ---");
    }


    @Override
    public ServiceDto findByName(String name) {
        if (serviceRepository.findByName(name).isPresent())
            return serviceMap.createServiceDto(serviceRepository.findByName(name).get());
        else
            throw new ObjectEntityNotFoundException("Service not found");
    }

    @Override
    public Optional<Service> find(String name) {
        return serviceRepository.findByName(name);
    }

    @Override
    public void deleteService(String name) {

        serviceRepository.delete(serviceMap.createService(findByName(name)));
    }

    @Override
    public void update(ServiceDto serviceDto) {
        Service service = serviceMap.createService(serviceDto);
        serviceRepository.save(service);
    }

    @Override
    public List<ServiceDto> findAll() {
        List<Service> serviceList = serviceRepository.findAll();
        return serviceList.stream().map(serviceMap::createServiceDto).collect(Collectors.toList());
    }

    @Override
    public ServiceDto addSubService(ServiceDto serviceDto, SubServiceDto subServiceDto) {
        Service service = find(serviceDto.getName()).get();
        SubService subService = subServiceService.find(subServiceDto.getName());
        service.getSubServiceList().add(subService);
        Service save = serviceRepository.save(service);
        return serviceMap.createServiceDto(save);
    }
}
