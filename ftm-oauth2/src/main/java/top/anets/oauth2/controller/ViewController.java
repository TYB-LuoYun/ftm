package top.anets.oauth2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author ftm
 * @date 2023-08-18 14:27
 */
@Controller
@RequestMapping("/view")
public class ViewController {
    @RequestMapping("/login")
    public String loginView(Model model) {
        return "oauth-login";
    }
}
