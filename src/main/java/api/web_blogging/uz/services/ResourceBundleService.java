package api.web_blogging.uz.services;

import api.web_blogging.uz.enums.AppLang;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class ResourceBundleService {

    @Autowired
    private ResourceBundleMessageSource messageSource;

    public String getMessage(String key, AppLang lang) {
        return messageSource.getMessage(key, null, new Locale(lang.name()));
    }

}
