package ir.maktab.project_final.service;

import ir.maktab.project_final.data.dto.AddSubServiceDto;
import ir.maktab.project_final.data.dto.SubServiceDto;
import ir.maktab.project_final.data.entity.serviceSystem.SubService;

import java.util.List;


public interface SubServiceService {


    List<SubServiceDto> findAll();

    SubService find(String name);

    SubServiceDto findByName(String name);

    SubServiceDto save(SubServiceDto subServiceDto);

    void deleteSubService(String name);

    SubServiceDto save(AddSubServiceDto addSubServiceDto);
}
