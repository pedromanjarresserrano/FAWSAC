package com.gitlab.pedrioko.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.zkoss.web.util.resource.ClassWebResource;
import org.zkoss.zk.au.http.DHtmlUpdateServlet;
import org.zkoss.zk.ui.http.RichletFilter;

@EntityScan(basePackages = {"com.gitlab"})
@ComponentScan("com.gitlab")
public class ZKCEConfig {

    private static final String RICHLET_URI = "/richlet"; //optional
    private static final String ZUL_VIEW_RESOLVER_SUFFIX = ".zul";
    private static String UPDATE_URI = "/zkau"; //servlet mapping for ZK's update servlet
    private static String ZUL_VIEW_RESOLVER_PREFIX = UPDATE_URI + ClassWebResource.PATH_PREFIX + "/zul/";


    @Bean
    public ViewResolver zulViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver(ZUL_VIEW_RESOLVER_PREFIX, ZUL_VIEW_RESOLVER_SUFFIX);
        resolver.setOrder(InternalResourceViewResolver.LOWEST_PRECEDENCE);
        return resolver;
    }

    @Bean
    public FilterRegistrationBean richletFilter() {
        FilterRegistrationBean reg = new FilterRegistrationBean(new RichletFilter());
        reg.addUrlPatterns(RICHLET_URI + "/*");
        return reg;
    }

    @Bean
    @ConditionalOnClass(name = "org.zkoss.zats.mimic.Zats") //Zats doesn't support custom update URI.
    public ServletRegistrationBean defaultDHtmlUpdateServlet() {
        return new ServletRegistrationBean(new DHtmlUpdateServlet(), "/zkau/*");
    }

    @Bean
    @ConditionalOnMissingClass("org.zkoss.zats.mimic.Zats") //only allow custom update URI outside Zats testcases.
    public ServletRegistrationBean customizableDHtmlUpdateServlet() {
        return new ServletRegistrationBean(new DHtmlUpdateServlet(), UPDATE_URI + "/*");
    }


}


