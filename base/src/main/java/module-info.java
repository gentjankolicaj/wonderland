open module wonderland.base {
  //commons dep
  requires transitive org.apache.commons.lang3;
  requires transitive org.apache.commons.collections4;
  requires transitive org.apache.commons.io;
  requires transitive org.apache.commons.codec;

  //jackson dep
  requires transitive com.fasterxml.jackson.core;
  requires transitive com.fasterxml.jackson.dataformat.yaml;
  requires transitive com.fasterxml.jackson.databind;
  requires transitive com.fasterxml.jackson.annotation;
  requires transitive com.fasterxml.jackson.module.paramnames;
  requires transitive com.fasterxml.jackson.datatype.jdk8;
  requires transitive com.fasterxml.jackson.datatype.jsr310;

  requires transitive lombok;
  requires transitive org.slf4j;
  requires transitive org.reflections;
  requires transitive fugue;
  requires org.apache.commons.text;

  exports io.wonderland.base;

}