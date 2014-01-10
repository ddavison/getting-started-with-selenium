package com.company.seleniumframework;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Config {
    public String url();
    public Browser browser() default Browser.FIREFOX;
    public String hub() default "";
}
