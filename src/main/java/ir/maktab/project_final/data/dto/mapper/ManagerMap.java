package ir.maktab.project_final.data.dto.mapper;

import ir.maktab.project_final.data.dto.ManagerDto;
import ir.maktab.project_final.data.entity.user.Manager;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component

public class ManagerMap {


    private Manager createManager(ManagerDto managerDto) {
        Manager manager = Manager.builder()
                .password(managerDto.getPassword())
                .username(managerDto.getUsername())
                .userRole(managerDto.getUserRole()).build();

        return manager;
    }

    private ManagerDto createManagerDto(Manager manager) {

        ManagerDto managerDto = ManagerDto.builder()
                .password(manager.getPassword())
                .username(manager.getUsername())
                .userRole(manager.getUserRole()).build();

        return managerDto;
    }
}
