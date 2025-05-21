package ru.pin36bik.controller;

import org.springframework.security.test.context.support.WithMockUser;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithMockUser(username = "test@example.com", roles = "USER")
public @interface WithMockCustomUser {
}
