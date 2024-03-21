module commons {
  requires java.se;
  requires org.apache.commons.lang3;
  requires org.apache.commons.collections4;
  requires org.apache.commons.io;
  requires lombok;
  requires org.slf4j;

  //make public classes visible outside module.
  exports io.wonderland.commons;
}