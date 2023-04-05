package com.tugaskelompok.rumahsehat.config.security.xml;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@Setter
@Getter
@XmlAccessorType(XmlAccessType.FIELD)
public class AuthenticationSuccess {
    @XmlElement(namespace = "http://www.yale.edu/tp/cas")
    private String user;

    @XmlElement(namespace = "http://www.yale.edu/tp/cas")
    private Attributes attributes;

}
