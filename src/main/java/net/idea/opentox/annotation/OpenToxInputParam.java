package net.idea.opentox.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import net.idea.opentox.annotation.OpenToxRESTOperation.OPENTOX_RESOURCE;

@Retention(RetentionPolicy.RUNTIME)
public @interface OpenToxInputParam {
	OPENTOX_RESOURCE content() default OPENTOX_RESOURCE.none;
	String paramName();
	boolean paramOptional() default true;
}
