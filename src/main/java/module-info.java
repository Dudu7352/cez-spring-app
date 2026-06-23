import org.jspecify.annotations.NullMarked;

@NullMarked
module cez.spring.app {
    requires static lombok;
    requires org.jspecify;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
}