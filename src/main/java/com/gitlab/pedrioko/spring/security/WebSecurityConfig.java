package com.gitlab.pedrioko.spring.security;

import com.gitlab.pedrioko.core.api.StaticResouceLocation;
import com.gitlab.pedrioko.core.view.util.ApplicationContextUtils;
import com.gitlab.pedrioko.domain.enumdomain.TipoUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The Class WebSecurityConfig.
 */
@Configuration
@Order(200)
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService customUserDetailsService;

    /**
     * The auth provider.
     */
    //   @Autowired
    // private JdbcAuthenticationProvider authProvider;

    /*
     * (non-Javadoc)
     *
     * @see org.springframework.security.config.annotation.web.configuration.
     * WebSecurityConfigurerAdapter#configure(org.springframework.security.
     * config.annotation.authentication.builders.AuthenticationManagerBuilder)
     */
    @Override
    @Autowired
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //   auth.authenticationProvider(authProvider);
        auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());

    }

    /*
     * (non-Javadoc)
     *
     * @see org.springframework.security.config.annotation.web.configuration.
     * WebSecurityConfigurerAdapter#configure(org.springframework.security.
     * config.annotation.web.builders.HttpSecurity)
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        List<StaticResouceLocation> beans = ApplicationContextUtils.getBeans(StaticResouceLocation.class);
        String[] strings = beans.stream().map(e -> e.getPath()).collect(Collectors.toSet()).toArray(new String[]{});
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry and = http.authorizeRequests().antMatchers("/ws/**").permitAll().and().authorizeRequests()
                .antMatchers("/videos", "/css/**", "/signaling", "/components/**", "/bootstrap/**", "/fonts/**", "/js/**",
                        "/images/**", "/zkau/**", "/login", "/recovery", "/register")
                .permitAll().and().authorizeRequests();
        for (String e : Arrays.asList(strings)) {
            and = and.antMatchers(e).permitAll();
        }
        and.antMatchers("/content/admin/**").hasAuthority(TipoUsuario.ROLE_ADMIN.name())
                .antMatchers("/content/usercrue/**")
                .hasAnyAuthority(TipoUsuario.ROLE_ADMIN.name(), TipoUsuario.ROLE_ENTIDAD.name(),
                        TipoUsuario.ROLE_ENTIDADCONTROL.name(), TipoUsuario.ROLE_ENTIDADESTATAL.name())
                .antMatchers("/**")
                .hasAnyAuthority(TipoUsuario.ROLE_ADMIN.name(), TipoUsuario.ROLE_ENTIDAD.name(),
                        TipoUsuario.ROLE_ENTIDADCONTROL.name(), TipoUsuario.ROLE_ENTIDADESTATAL.name(),
                        TipoUsuario.ROLE_ENTIDADCONTROL.name(), TipoUsuario.ROLE_TURISTA.name())
                .and().formLogin().loginPage("/login").permitAll().and().logout().permitAll().and().csrf().disable()
                .headers().frameOptions().sameOrigin().httpStrictTransportSecurity().disable();
    }


    /**
     * Password encoder.
     *
     * @return the password encoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
}