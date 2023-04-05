package com.tugaskelompok.rumahsehat.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class SsoEndpoint {
    private String clientBaseUrl;
    private String clientLogin;
    private String clientLogout;
    private String serverBaseUrl = "https://sso.ui.ac.id/cas2";
    private String serverLogin = serverBaseUrl + "/login?service=";
    private String serverLogout = serverBaseUrl + "/logout?url=";
    private String serverValidateTicket = serverBaseUrl + "/serviceValidate?ticket=%s&service=%s";

    public SsoEndpoint(@Value("${sso.url}") String ssoUrl) {
        this.clientBaseUrl = ssoUrl;
        this.clientLogin = clientBaseUrl + "/validate-ticket";
        this.clientLogout = clientBaseUrl + "/logout";
    }
}
