import org.jspecify.annotations.NullMarked;

@NullMarked
open module cez.spring.app {
    requires static lombok;
    requires org.jspecify;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.beans;
    requires spring.web;
    requires java.sql;
    requires com.fasterxml.jackson.annotation;
    requires jakarta.validation;
    requires jakarta.annotation;
}