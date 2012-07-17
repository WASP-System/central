package edu.yu.einstein.wasp;

import java.nio.charset.Charset;
import java.util.Arrays;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;

public class EncodingPostProcessor implements BeanPostProcessor {
    @Override
	public Object postProcessBeforeInitialization(Object bean, String name)
            throws BeansException {
        if (bean instanceof AnnotationMethodHandlerAdapter) {
            HttpMessageConverter<?>[] convs = ((AnnotationMethodHandlerAdapter) bean).getMessageConverters();
            for (HttpMessageConverter<?> conv: convs) {
                if (conv instanceof StringHttpMessageConverter) {
                    ((StringHttpMessageConverter) conv).setSupportedMediaTypes(
                        Arrays.asList(new MediaType("application", "json", 
                            Charset.forName("UTF-8"))));
                }
            }
        }
        return bean;
    }

    @Override
	public Object postProcessAfterInitialization(Object bean, String name)
            throws BeansException {
        return bean;
    }
}