package ir.maktab.project_final.web;

import ir.maktab.project_final.data.dto.AddSubServiceDto;
import ir.maktab.project_final.data.dto.ExpertDto;
import ir.maktab.project_final.data.dto.ServiceDto;
import ir.maktab.project_final.data.dto.SubServiceDto;
import ir.maktab.project_final.exception.DuplicateServiceException;
import ir.maktab.project_final.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class ManagerRestController {
    private final ServiceService service;
    private final SubServiceService subService;
    private final ExpertService expertService;
    private final UserService userService;
    private final OrderService orderService;

    @Autowired
    public ManagerRestController(@Lazy ServiceService service, @Lazy SubServiceService subService,
                                 @Lazy ExpertService expertService, @Lazy UserService userService,
                                 @Lazy OrderService orderService) {
        this.service = service;
        this.subService = subService;
        this.expertService = expertService;
        this.userService = userService;
        this.orderService = orderService;
    }


    @GetMapping("/getService")
    public List<ServiceDto> showAddServicePage() {
        List<ServiceDto> serviceList = service.findAll();
        return serviceList;
    }

    @PostMapping("/newService")
    public void addService(@RequestBody String newName) {
        try {
            service.save(newName);
        } catch (DuplicateServiceException e) {

            System.out.println(e.getMessage());
        }

        System.out.println("success add service");
    }


    @PostMapping("/addSubService")
    public SubServiceDto addSubService(@RequestBody AddSubServiceDto addSubServiceDto) {

        return subService.save(addSubServiceDto);

    }

    @GetMapping("/getExpert")
    public List<ExpertDto> showSelectExpert() {
        return expertService.findAll();

    }

    @PostMapping("/expertAddToService")
    public ExpertDto addExpertToService(@RequestParam("name") String name,
                                        @RequestBody ExpertDto expertDto) {
        ExpertDto expertDto1 = expertService.addSubServiceToExpert(expertDto, name);

        return expertDto1;
    }

}
