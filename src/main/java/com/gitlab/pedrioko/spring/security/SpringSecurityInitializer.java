package com.gitlab.pedrioko.spring.security;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

/**
 * The Class SpringSecurityInitializer.
 */
public class SpringSecurityInitializer extends AbstractSecurityWebApplicationInitializer {

    /**
     * Instantiates a new spring security initializer.
     */
    public SpringSecurityInitializer() {
        super(WebSecurityConfig.class);
    }

}
