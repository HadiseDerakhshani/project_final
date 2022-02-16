package ir.maktab.project_final.web;

import ir.maktab.project_final.data.dto.*;
import ir.maktab.project_final.data.entity.enums.UserRole;
import ir.maktab.project_final.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;


@Controller
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final ExpertService expertService;
    private final CustomerService customerService;
    private final OrderService orderService;
    private final SuggestionService suggestionService;

    @Autowired
    public UserController(@Lazy UserService userService, @Lazy ExpertService expertService,
                          @Lazy CustomerService customerService, @Lazy OrderService orderService,
                          @Lazy SuggestionService suggestionService) {
        this.userService = userService;
        this.expertService = expertService;
        this.customerService = customerService;
        this.orderService = orderService;
        this.suggestionService = suggestionService;
    }

    @PostMapping("/login")
    public String showRegisterPage(@RequestParam("email") String email, @RequestParam("password") String password,
                                   Model model, HttpSession session) {
        UserRole role = userService.findByEmail(email, password);

        if (role.equals(UserRole.EXPERT)) {
            ExpertDto expertProfile = expertService.find(email);
            List<OrderDto> orderDtoList = orderService.findOrderByExpert(expertProfile);
            System.out.println(orderDtoList);//todo null check

            List<SuggestionDto> suggestionDtoList = suggestionService.findByExpert(expertProfile);
            List<SubServiceDto> serviceList = expertProfile.getServiceList();
            session.setAttribute("expert", expertProfile);
            model.addAttribute("expert", expertProfile);
            model.addAttribute("orderList", orderDtoList);
            model.addAttribute("suggestionDtoList", suggestionDtoList);
            model.addAttribute("serviceList", serviceList);
            return "expert/expert_profile";


        } else if (role.equals(UserRole.CUSTOMER)) {
            CustomerDto customerProfile = customerService.find(email);
            session.setAttribute("customer", customerProfile);
            List<OrderDto> orderList = orderService.findOrderByCustomer(customerProfile);
            model.addAttribute("customer", customerProfile);
            model.addAttribute("orderList", orderList);
            return "customer/customer_profile";
        }
        return "register";
    }


}
