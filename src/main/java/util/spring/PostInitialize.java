package util.spring;

import java.lang.annotation.Documented;
import static java.lang.annotation.ElementType.METHOD;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;

/**
 * User: AlphaCSP
 *
 * @since: Aug 7, 2008
 */
@Documented
@Retention(RUNTIME)
@Target(METHOD)
public @interface PostInitialize {
    int order() default 0;
}
