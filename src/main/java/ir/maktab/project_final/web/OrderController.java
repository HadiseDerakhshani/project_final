package ir.maktab.project_final.web;

import ir.maktab.project_final.config.LastViewInterceptor;
import ir.maktab.project_final.data.dto.CustomerDto;
import ir.maktab.project_final.data.dto.OrderDto;
import ir.maktab.project_final.data.dto.ServiceDto;
import ir.maktab.project_final.data.dto.SubServiceDto;
import ir.maktab.project_final.data.entity.enums.OrderStatus;
import ir.maktab.project_final.service.CustomerService;
import ir.maktab.project_final.service.OrderService;
import ir.maktab.project_final.service.ServiceService;
import ir.maktab.project_final.service.SubServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Set;


@Controller
@RequestMapping("/order")
public class OrderController {

    private final CustomerService customerService;

    private final OrderService orderService;

    private final SubServiceService subServiceService;

    private final ServiceService service;


    @Autowired
    public OrderController(@Lazy CustomerService customerService, @Lazy OrderService orderService,
                           @Lazy SubServiceService subServiceService, @Lazy ServiceService service) {
        this.customerService = customerService;
        this.orderService = orderService;
        this.subServiceService = subServiceService;
        this.service = service;


    }


    @GetMapping
    public ModelAndView showRegisterOrder() {

        List<ServiceDto> serviceDtoList = service.findAll();

        return new ModelAndView("services/choose_service", "serviceList", serviceDtoList);
    }

    @PostMapping("/registerOrder")
    public ModelAndView registerOrder(@Validated @ModelAttribute("order") OrderDto orderDto,
                                      @SessionAttribute("customer") CustomerDto customerDto,
                                      @RequestParam("name") String name) {

        SubServiceDto serviceDto = subServiceService.findByName(name);
        orderDto.setStatus(OrderStatus.WAITING_FOR_EXPERT_SUGGESTION);
        OrderDto saveOrder = orderService.save(orderDto);
        orderService.addCustomerToOrder(customerDto, saveOrder);
        orderService.addServiceToOrder(serviceDto, saveOrder);
        customerService.updateOrder(customerDto, saveOrder);
        return new ModelAndView("order/success_register", "message",
                "success register order for customer");
    }

    @ExceptionHandler(value = BindException.class)
    public ModelAndView bindExceptionHandler(BindException ex, HttpServletRequest request) {
        String lastView = (String) request.getSession().getAttribute(LastViewInterceptor.LAST_VIEW_ATTRIBUTE);
        return new ModelAndView(lastView, ex.getBindingResult().getModel());
    }

    @GetMapping("/selectService/{name}")
    public String selectService(@PathVariable String name, Model model) {
        ServiceDto serviceDto = service.findByName(name);
        Set<SubServiceDto> subServiceList = serviceDto.getSubServiceList();
        model.addAttribute("subServiceList", subServiceList);
        model.addAttribute("order", new OrderDto());
        return "order/order_register";
    }

    @GetMapping("/findOrder")
    public String findOrder(Model model, @SessionAttribute("customer") CustomerDto customerDto) {
        List<OrderDto> orderDtoList = orderService.findOrderByCustomer(customerDto);
        model.addAttribute("orderDtoList", orderDtoList);
        return "order/choose_order";
    }

    @GetMapping("/payment")
    public String payment(@SessionAttribute("customer") CustomerDto customerDto, Model model,
                          HttpSession session) {
        Double amount;
        OrderDto order = orderService.findOrderToPayment(customerDto);
        if (order != null) {
            amount = orderService.calculatePrice(order);
            if (amount != null) {
                model.addAttribute("order", order);
                model.addAttribute("customer", customerDto);
                model.addAttribute("amount", amount);
                session.setAttribute("amount", amount);
                session.setAttribute("order", order);
                return "order/choose_type_payment";//page select type of payment
            }
        }
        return "customer/customer_profile";//todo
    }


    @GetMapping("/paymentPage_online")
    public String showPaymentPage(@SessionAttribute("customer") CustomerDto customerDto, Model model,
                                  @SessionAttribute("order") OrderDto orderDto, @SessionAttribute("amount") Double amount) {
        model.addAttribute("order", orderDto);
        model.addAttribute("customer", customerDto);
        model.addAttribute("amount", amount);
        return "order/payment_online";
    }

    @PostMapping("/payment_online")
    public ModelAndView paymentOnline(@RequestParam("price") String amount,
                                      @SessionAttribute("order") OrderDto orderDto) {
        orderService.updatePricePaid(orderDto, Double.parseDouble(amount));
        return new ModelAndView("order/choose_type_payment", "message", "payment successfully");
    }

    @GetMapping("/paymentPage_cash")
    public ModelAndView showPaymentCashPage(@SessionAttribute("customer") CustomerDto customerDto, Model model,
                                            @SessionAttribute("order") OrderDto orderDto, @SessionAttribute("amount") Double amount) {
        customerService.payment(customerDto, orderDto, amount);
        model.addAttribute("customer", customerDto);
        return new ModelAndView("order/choose_type_payment", "message", "payment successfully");
    }


}
