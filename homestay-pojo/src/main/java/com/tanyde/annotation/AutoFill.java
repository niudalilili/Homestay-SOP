package com.tanyde.annotation;


import com.tanyde.enumeration.OperationType;
import nonapi.io.github.classgraph.utils.VersionFinder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFill {
    //数据库操纵类型 UPDATE INSET
    OperationType value();

}
