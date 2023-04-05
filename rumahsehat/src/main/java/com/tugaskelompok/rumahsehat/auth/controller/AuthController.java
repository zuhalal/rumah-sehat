package com.tugaskelompok.rumahsehat.auth.controller;

import com.tugaskelompok.rumahsehat.admin.model.AdminModel;
import com.tugaskelompok.rumahsehat.admin.service.AdminService;
import com.tugaskelompok.rumahsehat.config.RedirectAttributesKey;
import com.tugaskelompok.rumahsehat.config.SsoEndpoint;
import com.tugaskelompok.rumahsehat.config.security.xml.ServiceResponse;
import com.tugaskelompok.rumahsehat.user.model.UserModel;
import com.tugaskelompok.rumahsehat.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.time.LocalDateTime;

@Controller
public class AuthController {
    @Autowired
    UserService userService;

    @Autowired
    AdminService adminService;

    @Autowired
    private SsoEndpoint ssoEndpoint;

    private WebClient webClient = WebClient.builder().build();
    private Logger logger = LoggerFactory.getLogger(AuthController.class);
    private String redirectLogin = "redirect:/login";

    @GetMapping("/")
    public String home(HttpServletRequest request) {
        var now = LocalDateTime.now().toString();
        logger.info("Webpage Request in {} at {}", request.getRequestURI(), now);
        return "home/home-web";
    }

    @GetMapping("/login")
    public String login(Model model) {
        var now = LocalDateTime.now().toString();
        logger.info("Webpage Request in /login at {}", now);
        return "auth/form-login";
    }

    @GetMapping("/validate-ticket")
    public ModelAndView adminLoginSSO(
            @RequestParam(value = "ticket", required = false) String ticket,
            HttpServletRequest request,
            RedirectAttributes redirectAttrs
    ) {
       try {
           var now = LocalDateTime.now().toString();
           logger.info("Redirect Request to SSO validate ticket at {}", now);

           var serviceResponse = this.webClient.get().uri(
                   String.format(
                           ssoEndpoint.getServerValidateTicket(),
                           ticket,
                           ssoEndpoint.getClientLogin()
                   )
           ).retrieve().bodyToMono(ServiceResponse.class).block();

           if (serviceResponse != null) {
               var attributes = serviceResponse.getAuthenticationSuccess().getAttributes();
               String username = serviceResponse.getAuthenticationSuccess().getUser();

               UserModel user = userService.getUserByUsername(username);

               if (user != null) {
                   boolean isAdmin = adminService.whiteListAdminCheck(username);

                   if (!isAdmin) {
                       redirectAttrs.addFlashAttribute(RedirectAttributesKey.ERROR, "Anda belum terdaftar sebagai admin");
                       return new ModelAndView(redirectLogin);
                   }
               }

               if (user == null) {
                   var admin = new AdminModel();
                   admin.setEmail(username + "@ui.ac.id");
                   admin.setNama(attributes.getNama());
                   admin.setPassword("rumahsehat");
                   admin.setUsername(username);
                   admin.setIsSso(true);

                   adminService.addAdmin(admin);
               }

               Authentication authentication = new UsernamePasswordAuthenticationToken(username, "rumahsehat");

               var securityContext = SecurityContextHolder.getContext();
               securityContext.setAuthentication(authentication);

               var httpSession = request.getSession(true);
               httpSession.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);
               return new ModelAndView("redirect:/");
           } else {
               throw new NullPointerException();
           }
       } catch (NullPointerException e) {
           var now = LocalDateTime.now().toString();
           logger.error("SSO Login error at {}", now);
           redirectAttrs.addFlashAttribute(RedirectAttributesKey.ERROR, "Sistem SSO sedang mengalami gangguan. Silahkan coba lagi nanti");
           return new ModelAndView(redirectLogin);
       } catch (Exception e) {
           var now = LocalDateTime.now().toString();
           logger.error("Login error at {}", now);
           redirectAttrs.addFlashAttribute(RedirectAttributesKey.ERROR, "Sistem Login mengalami masalah. Silahkan coba lagi nanti");
           return new ModelAndView(redirectLogin);
       }
    }

    @GetMapping("/login-sso")
    public ModelAndView loginSSO() {
        var now = LocalDateTime.now().toString();
        logger.info("Redirect Request to SSO login service at {}", now);
        return new ModelAndView("redirect:" + ssoEndpoint.getServerLogin() + ssoEndpoint.getClientLogin());
    }

    @GetMapping("/logout-sso")
    public ModelAndView logoutSSO(Principal principal){
        var now = LocalDateTime.now().toString();
        logger.info("Redirect Request to SSO logout service at {}", now);
        UserModel user = userService.getUserByUsername(principal.getName());
        if (!user.getIsSso()){
            return new ModelAndView("redirect:/logout");
        }
        return new ModelAndView("redirect:" + ssoEndpoint.getServerLogout() + ssoEndpoint.getClientLogout());
    }
}
