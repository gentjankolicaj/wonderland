module red.queen {
  requires java.se;
  requires org.apache.commons.lang3;
  requires org.apache.commons.collections4;
  requires org.apache.commons.io;
  requires lombok;
  requires org.slf4j;
  requires common;


  //make public classes visible outside module.
  exports io.wonderland.rq.cryptanalysis;
  exports io.wonderland.rq.ds;
  exports io.wonderland.rq.type;
  exports io.wonderland.rq.resource;
}