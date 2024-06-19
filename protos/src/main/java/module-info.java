module wonderland.protos {
  requires io.grpc;
  requires io.grpc.stub;
  requires io.grpc.protobuf;
  requires protobuf.java;
  requires com.google.common;
  requires jsr305;

  //make package content visible outside module.
  exports io.wonderland.protos;
}