package ir.maktab.project_final.service.implemention;

import ir.maktab.project_final.data.dto.AddSubServiceDto;
import ir.maktab.project_final.data.dto.SubServiceDto;
import ir.maktab.project_final.data.dto.mapper.ExpertMap;
import ir.maktab.project_final.data.dto.mapper.SubServiceMap;
import ir.maktab.project_final.data.entity.serviceSystem.SubService;
import ir.maktab.project_final.data.repasitory.SubServiceRepository;
import ir.maktab.project_final.exception.DuplicateServiceException;
import ir.maktab.project_final.exception.ObjectEntityNotFoundException;
import ir.maktab.project_final.service.SubServiceService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Service

public class SubServiceServiceImpl implements SubServiceService {

    private final SubServiceMap subServiceMap;

    private final ExpertMap expertMap;
    private final SubServiceRepository subServiceRepository;

    @Autowired
    public SubServiceServiceImpl(@Lazy SubServiceMap subServiceMap, @Lazy ExpertMap expertMap,
                                 SubServiceRepository subServiceRepository) {
        this.subServiceMap = subServiceMap;
        this.expertMap = expertMap;
        this.subServiceRepository = subServiceRepository;
    }


    @Override
    public List<SubServiceDto> findAll() {
        List<SubService> listSubService = subServiceRepository.findAll();
        if (listSubService != null) {
            List<SubServiceDto> listSubServiceDto = listSubService.stream().map(subServiceMap::createSubServiceDto)
                    .collect(Collectors.toList());
            return listSubServiceDto;
        } else
            throw new ObjectEntityNotFoundException(" --- SubService is null ---");
    }

    @Override
    public SubServiceDto findByName(String name) {

        return subServiceMap.createSubServiceDto(find(name));
    }

    @Override
    public SubService find(String name) {
        if (subServiceRepository.findByName(name).isPresent())
            return subServiceRepository.findByName(name).get();
        throw new ObjectEntityNotFoundException(" --- SubService is null ---");
    }

    @Override
    public SubServiceDto save(SubServiceDto subServiceDto) {
        if (find(subServiceDto.getName()) != null) {
            SubService subService = subServiceMap.createSubService(subServiceDto);
            SubService saveSubService = subServiceRepository.save(subService);
            return subServiceMap.createSubServiceDto(saveSubService);
        }
        throw new DuplicateServiceException(" --- SubService is exit for name ---");
    }

    @Override
    public void deleteSubService(String name) {
        subServiceRepository.delete(find(name));
    }

    @Override
    public SubServiceDto save(AddSubServiceDto addSubServiceDto) {
        return null;
    }

}
