package com.example.application.it;

import com.example.application.it.elements.LoginViewElement;
import com.vaadin.flow.component.login.testbench.LoginFormElement;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.support.AbstractTestContextBootstrapper;

public class LoginIT extends LoginE2ETest {
    public LoginIT() {
        super("");
    }

    @Test
    public void loginAsValidUserSucceeds(){
        LoginViewElement loginView = $(LoginViewElement.class).onPage().first();
        Assertions.assertTrue(loginView.login("user", "password"));
    }

    @Test
    public void loginAsInvalidUserFails(){
        LoginViewElement loginView = $(LoginViewElement.class).onPage().first();
        Assertions.assertFalse(loginView.login("user", "wrongpassword"));
    }
}
