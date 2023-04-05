package com.tugaskelompok.rumahsehat.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ConstructorPrivateTest {


    @Test()
    void testApiResponseKeyIsPrivate() throws NoSuchMethodException {
        Constructor<ApiResponseKey> constructor = ApiResponseKey.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
    }

    @Test()
    void testSsoWhitelistIsPrivate() throws NoSuchMethodException {
        Constructor<SsoWhitelist> constructor = SsoWhitelist.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
    }

    @Test()
    void testRedirectAttributesKeyIsPrivate() throws NoSuchMethodException {
        Constructor<RedirectAttributesKey> constructor = RedirectAttributesKey.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
    }
}
