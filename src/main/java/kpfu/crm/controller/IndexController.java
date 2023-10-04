package kpfu.crm.controller;

import kpfu.crm.config.security.UserDetailImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class IndexController {

    @GetMapping
    public String getPage(@AuthenticationPrincipal UserDetailImpl user, Model model) {
        model.addAttribute("user", user);

        return "index";
    }
}
