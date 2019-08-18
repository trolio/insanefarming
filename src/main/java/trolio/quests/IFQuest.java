package trolio.quests;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention (RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface IFQuest 
{
	String value() default "";
	
	String mod() default "insanefarming";
}
