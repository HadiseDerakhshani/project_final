package ir.maktab.project_final.web;

import ir.maktab.project_final.config.LastViewInterceptor;
import ir.maktab.project_final.data.dto.*;
import ir.maktab.project_final.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@SessionAttributes({"expert", "orderDto"})
@Controller
@RequestMapping("/expert")
public class ExpertController {

    private final ExpertService expertService;
    private final CommentService commentService;
    private final SubServiceService subServiceService;

    private final OrderService orderService;

    private final SuggestionService suggestionService;

    @Autowired
    public ExpertController(@Lazy ExpertService expertService, @Lazy SubServiceService subServiceService,
                            @Lazy OrderService orderService, @Lazy SuggestionService suggestionService,
                            @Lazy CommentService commentService) {
        this.expertService = expertService;
        this.subServiceService = subServiceService;
        this.orderService = orderService;
        this.suggestionService = suggestionService;
        this.commentService = commentService;
    }


    @GetMapping
    public String showRegisterPage(Model model) {
        List<SubServiceDto> subServiceDtoList = subServiceService.findAll();
        model.addAttribute("expert", new ExpertDto());
        model.addAttribute("subServiceDtoList", subServiceDtoList);
        return "expert/expert_register";
    }

    @PostMapping(value = "/registerExpert")
    public ModelAndView registerExpert(@RequestParam("image") CommonsMultipartFile image, @Validated @ModelAttribute("expert")
            ExpertDto expertDto, @RequestParam("name") String name, HttpSession session) {
        expertDto.setPhoto(image.getBytes());
        expertService.save(expertDto);
        expertService.addSubServiceToExpert(expertDto, name);
        session.setAttribute("expert", expertDto);
        return new ModelAndView("expert/success_register", "expert", expertDto);
    }

    @ExceptionHandler(value = BindException.class)
    public ModelAndView bindExceptionHandler(BindException ex, HttpServletRequest request) {
        String lastView = (String) request.getSession().getAttribute(LastViewInterceptor.LAST_VIEW_ATTRIBUTE);
        return new ModelAndView(lastView, ex.getBindingResult().getModel());
    }

    @GetMapping("/suggestion")

    public String showRegisterSuggestPage(Model model) {
        List<OrderDto> list = orderService.findOrderToSuggest();
        model.addAttribute("list", list);
        return "suggestion/choose_order";
    }

    @GetMapping("/selectOrder/{number}")
    public String selectOrder(@PathVariable long number, Model model, HttpSession session) {

        OrderDto orderDto = orderService.find(number);
        session.setAttribute("orderDto", orderDto);
        model.addAttribute("suggest", new SuggestionDto());
        model.addAttribute("orderDto", orderDto);
        return "suggestion/suggestion_register";
    }

    @PostMapping(value = "/registerSuggestion")
    public ModelAndView registerSuggestion(@Validated @ModelAttribute("suggest") SuggestionDto suggestionDto,
                                           @SessionAttribute("expert") ExpertDto expertDto,
                                           @SessionAttribute("orderDto") OrderDto orderDto) {
        SuggestionDto saveSuggest = suggestionService.save(suggestionDto, orderDto, expertDto);
        orderService.addSuggestionToOrder(orderDto, saveSuggest);
        return new ModelAndView("suggestion/success_register", "expert", expertDto);
    }

    @GetMapping("/selectOrderToSuggest/{number}")
    public String selectOrder(@PathVariable long number, Model model,
                              @SessionAttribute("customer") CustomerDto customerDto) {

        OrderDto orderDto = orderService.find(number);
        List<SuggestionDto> suggestionDtoList = suggestionService.findByOrder(number);
        System.out.println(suggestionDtoList);
        model.addAttribute("orderDto", orderDto);
        model.addAttribute("suggestList", suggestionDtoList);
        model.addAttribute("customer", customerDto);
        return "suggestion/choose_suggestion";
    }

    @GetMapping("/selectSuggest/{number}")
    public String selectSuggest(@PathVariable long number, Model model) {
        suggestionService.update(number);
        return "login";
    }

    @PostMapping("/registerScore")
    public ModelAndView registerScore(@RequestParam("score") String score, @RequestParam("comment") String comment,
                                      @SessionAttribute("order") OrderDto orderDto) {
        if (comment != null && comment.isEmpty())
            commentService.save(orderDto, comment);
        expertService.updateScore(Integer.parseInt(score), orderDto);
        return new ModelAndView("order/choose_type_payment", "message", "score register Successfully");
    }
}
