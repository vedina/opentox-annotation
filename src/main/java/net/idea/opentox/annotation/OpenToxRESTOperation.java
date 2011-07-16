package net.idea.opentox.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface OpenToxRESTOperation {
	public enum OPENTOX_RESOURCE { none, algorithm, model, compound, feature, dataset, validation, report, task};
	public enum HTTP_METHOD { GET,PUT,POST,DELETE,OPTIONS,HEAD};
	public enum HTTP_STATUS { 
		OK {
			@Override
			public int getCode() { return 200;}
		},
		Accepted {
			@Override
			public int getCode() { return 202;}
		},
		BadRequest {
			@Override
			public int getCode() { return 400;}
		},
		NotFound {
			@Override
			public int getCode() { return 404;}
		};
		public abstract int getCode();
	};


	HTTP_METHOD hasHTTPMethod() default HTTP_METHOD.GET;
	HTTP_STATUS[] hasStatus();
	OPENTOX_RESOURCE resource() default OPENTOX_RESOURCE.none;
	OPENTOX_RESOURCE result() default OPENTOX_RESOURCE.none;
}

