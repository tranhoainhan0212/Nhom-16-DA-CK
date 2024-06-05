package com.buoi1.webbanhang.controller;

import com.buoi1.webbanhang.model.User;
import com.buoi1.webbanhang.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/login")
    public String login() {
        return "users/login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        return "users/register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") User user,
                               BindingResult bindingResult,
                               Model model,
                               @RequestParam("confirmPassword") String confirmPassword) {

        // Kiểm tra xác nhận mật khẩu
        if (!confirmPassword.equals(user.getPassword())) {
            bindingResult.rejectValue("confirmPassword", "error.user", "Mật khẩu xác nhận không khớp");
        }

        // Kiểm tra lỗi validate
        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toArray(String[]::new);
            model.addAttribute("errors", errors);
            return "users/register";
        }

        userService.save(user);
        userService.setDefaultRole(user.getUsername());

        return "redirect:/login";
    }
}
