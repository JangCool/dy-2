package kr.co.pionnet.dy;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD,ElementType.ANNOTATION_TYPE})
@Documented
public @interface TransactionalDynamic {
	String field() default "conntionName";
}
