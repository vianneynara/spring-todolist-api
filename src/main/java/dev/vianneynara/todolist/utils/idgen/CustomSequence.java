package dev.vianneynara.todolist.utils.idgen;

import org.hibernate.annotations.IdGeneratorType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@IdGeneratorType(SnowFlakeIdGenerator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
public @interface CustomSequence {
	String name();
}