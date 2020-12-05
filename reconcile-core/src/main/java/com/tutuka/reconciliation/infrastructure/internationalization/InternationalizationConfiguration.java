package com.tutuka.reconciliation.infrastructure.internationalization;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Configuration
public class InternationalizationConfiguration extends AcceptHeaderLocaleResolver
             implements WebMvcConfigurer {

        List<Locale> LOCALES = Arrays.asList(
                new Locale("en"),
                new Locale("fa"));

        @Override
        public Locale resolveLocale(HttpServletRequest request) {
            String headerLang = request.getHeader("Accept-Language");
            return headerLang == null || headerLang.isEmpty()
                    ? Locale.getDefault()
                    : Locale.lookup(Locale.LanguageRange.parse(headerLang), LOCALES);
        }

        @Bean
        public ResourceBundleMessageSource messageSource() {
            ResourceBundleMessageSource rs = new ResourceBundleMessageSource();
            rs.setBasename("messages/messages");
            rs.setDefaultEncoding("UTF-8");
            rs.setUseCodeAsDefaultMessage(true);
            return rs;
        }

    @Bean
    public LocaleResolver localeResolver()
    {
        SessionLocaleResolver localeResolver = new SessionLocaleResolver();
        localeResolver.setDefaultLocale(Locale.US);
        return localeResolver;
    }
}
