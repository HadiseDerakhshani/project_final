package ir.maktab.project_final.web;


import ir.maktab.project_final.data.dto.UserDto;
import ir.maktab.project_final.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {
    private final UserService userService;

    @Autowired
    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/")
    public String indexView() {

        return "index";
    }

    @GetMapping(value = "/error")
    public String errorView() {

        return "errorPage";
    }

    @GetMapping(value = "/login")
    public String loginView(Model model) {
        List<UserDto> userDtoList = userService.findAll();
        model.addAttribute("list", userDtoList);
        return "login";
    }

    @GetMapping(value = "/register")
    public String registerView() {

        return "register";
    }
}

