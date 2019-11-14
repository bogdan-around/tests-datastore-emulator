package com.prompto.api;


import com.google.cloud.datastore.DatastoreOptions;
import com.google.common.base.Strings;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyFilter;
import com.googlecode.objectify.ObjectifyService;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@Configuration
public class ObjectifyConfig {

    @Bean
    public FilterRegistrationBean<ObjectifyFilter> objectifyFilterRegistration() {
        final FilterRegistrationBean<ObjectifyFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new ObjectifyFilter());
        registration.addUrlPatterns("/*");
        registration.setOrder(1);
        return registration;
    }

    @Bean
    public ServletListenerRegistrationBean<ObjectifyListener> listenerRegistrationBean() {
        ServletListenerRegistrationBean<ObjectifyListener> bean =
                new ServletListenerRegistrationBean<>();
        bean.setListener(new ObjectifyListener());
        return bean;
    }

    @WebListener
    public class ObjectifyListener implements ServletContextListener {

        @Override
        public void contextInitialized(ServletContextEvent sce) {
            String environment = System.getenv("SPRING_PROFILES_ACTIVE");
            if (Strings.isNullOrEmpty(environment)) {
                // on appengine
                ObjectifyService.init(new ObjectifyFactory(
                        DatastoreOptions.getDefaultInstance().getService(),
                        new AppEngineMemcacheClientService()
                ));
            } else if ("localhost-no-memcache".equals(environment)) {
                //local without memcache gradle appengineRun
                ObjectifyService.init(new ObjectifyFactory(
                        DatastoreOptions.newBuilder()
                                .setHost("http://localhost:8484")
                                .setProjectId("bogdanplayground01")
                                .build()
                                .getService()
                ));
            } else if ("localhost-memcache".equals(environment)) {
                // local with memcache (gradle appengineRun)
                ObjectifyService.init(new ObjectifyFactory(
                        DatastoreOptions.newBuilder()
                                .setHost("http://localhost:8484")
                                .setProjectId("bogdanplayground01")
                                .build()
                                .getService(),
                        new AppEngineMemcacheClientService()
                ));
            } else {
                throw new IllegalStateException("No valid environment set");
            }
            ObjectifyService.register(Item.class);
        }

        @Override
        public void contextDestroyed(ServletContextEvent sce) {

        }
    }

}