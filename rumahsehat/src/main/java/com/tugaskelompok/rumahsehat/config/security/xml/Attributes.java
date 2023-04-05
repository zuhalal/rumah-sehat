package com.tugaskelompok.rumahsehat.config.security.xml;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@Setter
@Getter
@XmlAccessorType(XmlAccessType.FIELD)
public class Attributes {
    @XmlElement(namespace = "http://www.yale.edu/tp/cas", name = "ldap_cn")
    private String ldapCn;

    @XmlElement(namespace = "http://www.yale.edu/tp/cas", name = "kd_org")
    private String kdOrg;

    @XmlElement(namespace = "http://www.yale.edu/tp/cas", name = "peran_user")
    private String peranUser;

    @XmlElement(namespace = "http://www.yale.edu/tp/cas")
    private String nama;

    @XmlElement(namespace = "http://www.yale.edu/tp/cas")
    private String npm;
}
