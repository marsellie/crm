package kpfu.crm.controller;

import kpfu.crm.model.User;
import kpfu.crm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class IndexController {
    private final UserService userService;

    @GetMapping
    public String getPage(
            @AuthenticationPrincipal User user,
            Model model
    ) {
        if (user != null)
            model.addAttribute(
                    userService.loadUserByUsername(user.getUsername())
            );

        return "index";
    }
}
