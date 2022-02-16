package ir.maktab.project_final.service;

import ir.maktab.project_final.data.dto.ServiceDto;
import ir.maktab.project_final.data.dto.SubServiceDto;
import ir.maktab.project_final.data.entity.serviceSystem.Service;

import java.util.List;
import java.util.Optional;


public interface ServiceService {


    ServiceDto save(String name);

    Optional<Service> find(String name);

    ServiceDto findByName(String name);

    void deleteService(String name);

    void update(ServiceDto serviceDto);

    ServiceDto addSubService(ServiceDto serviceDto, SubServiceDto subServiceDto);

    List<ServiceDto> findAll();
}
