package ir.maktab.project_final.web;

import ir.maktab.project_final.data.dto.*;
import ir.maktab.project_final.exception.DuplicateServiceException;
import ir.maktab.project_final.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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


    @GetMapping("/addService")
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

    @GetMapping("/addSubService/{name}")
    public Map<String, Object> showAddSubServicePage(@PathVariable("name") String name, HttpSession session) {
        Map<String, Object> model = new HashMap<>();
        ServiceDto serviceDto = service.findByName(name);
        session.setAttribute("service", serviceDto);
        Set<SubServiceDto> subServiceList = serviceDto.getSubServiceList();
        model.put("subServiceList", subServiceList);
        model.put("subService", new SubServiceDto());
        return model;
    }

    @PostMapping("/newSubService")
    public Set<SubServiceDto> addSubService(@ModelAttribute("subService") SubServiceDto subServiceDto,
                                            @SessionAttribute("service") ServiceDto serviceDto) {

        SubServiceDto saveSubService;
        try {
            saveSubService = subService.save(subServiceDto);
        } catch (DuplicateServiceException e) {
            System.out.println(e.getMessage());
            return serviceDto.getSubServiceList();
        }
        ServiceDto serviceUpdated = this.service.addSubService(serviceDto, saveSubService);
        return serviceUpdated.getSubServiceList();
    }

    @GetMapping("/selectExpert")
    public List<ExpertDto> showSelectExpert() {
        return expertService.findAll();

    }

    @PostMapping("/addExpert")
    public Map<String, Object> showAddExpert(@RequestParam("email") String email, HttpSession session) {
        Map<String, Object> model = new HashMap<>();
        ExpertDto expertDto = expertService.find(email);
        session.setAttribute("expertDto", expertDto);
        model.put("expertDto", expertDto);
        List<SubServiceDto> serviceList = expertDto.getServiceList();
        model.put("serviceList", serviceList);
        List<SubServiceDto> listAll = subService.findAll();
        model.put("listAll", listAll);
        return model;
    }

    @PostMapping("/expertAddToService")
    public Map<String, Object> addExpertToService(@RequestParam("name") String name,
                                                  @SessionAttribute("expertDto") ExpertDto expertDto) {
        Map<String, Object> model = new HashMap<>();
        expertService.addSubServiceToExpert(expertDto, name);
        model.put("message", "expert add to list service Successfully");
        ExpertDto expertDtoNew = expertService.find(expertDto.getEmail());
        model.put("expertDto", expertDtoNew);
        List<SubServiceDto> serviceList = expertDtoNew.getServiceList();
        model.put("serviceList", serviceList);
        List<SubServiceDto> listAll = subService.findAll();
        model.put("listAll", listAll);

        return model;
    }

    @GetMapping("/userPage")
    public Map<String, Object> showUserPage() {
        Map<String, Object> model = new HashMap<>();
        model.put("userCategory", new UserCategoryDto());
        List<SubServiceDto> list = subService.findAll();
        model.put("list", list);
        return model;
    }

    @PostMapping("/userFilter")
    public List<UserDto> showUser(@ModelAttribute("userCategory") UserCategoryDto categoryDto,
                                  @RequestParam(value = "name", required = false) String name) {

        if (name != null && !name.isEmpty())
            categoryDto.setService(name);
        return userService.filtering(categoryDto);

    }

    @GetMapping("/showOrderPage")
    public Map<String, Object> showOrderPage() {
        Map<String, Object> model = new HashMap<>();
        List<SubServiceDto> listSubServices = subService.findAll();
        List<ServiceDto> listServices = service.findAll();
        model.put("orderFilter", new OrderFilterDto());
        model.put("listServices", listServices);
        model.put("listSubServices", listSubServices);
        return model;
    }

    @PostMapping("/orderFilter")
    public List<OrderDto> orderFilter(@RequestBody OrderFilterDto orderFilterDto,
                                      @RequestBody String name, @RequestBody String nameSub) {
        if (name != null && !name.isEmpty())
            orderFilterDto.setService(name);
        if (nameSub != null && !nameSub.isEmpty())
            orderFilterDto.setSubService(nameSub);
        return orderService.filtering(orderFilterDto);
    }

}
