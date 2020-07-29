package web.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import web.dto.UserDto;
import web.service.ServiceAbstractInterface;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ServiceAbstractInterface<UserDto> serviceAbstractInterface;

    public AdminController(ServiceAbstractInterface<UserDto> serviceAbstractInterface) {
        this.serviceAbstractInterface = serviceAbstractInterface;
    }

    @GetMapping
    public String getMainPage(Authentication authentication, Model model) {

        model.addAttribute("user", serviceAbstractInterface.getEntityByName(authentication.getName()));
        return "table";
    }
}

//    @PostMapping("/userAdd")
//    public String addUser(UserDto userDto) {
//        serviceAbstractInterface.addEntity(userDto);
//        return "redirect:/admin";
//    }
//
//    @PostMapping("/delete")
//    public String deleteUser(@RequestParam("id") long id) {
//        serviceAbstractInterface.deleteEntity(id);
//        return "redirect:/admin";
//    }
//
//    @PostMapping("/editUser")
//    public String editUser(@ModelAttribute("user") UserDto userDto) {
//        serviceAbstractInterface.updateEntity(userDto);
//        return "redirect:/admin";
//    }
//}
