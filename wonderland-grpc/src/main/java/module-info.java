module wonderland.grpc {
  requires io.grpc;
  requires io.grpc.services;
  requires io.grpc.protobuf;

  //my dependencies
  requires wonderland.base;
  requires wonderland.protos;

  //make package content visible outside module.
  exports io.wonderland.grpc;
}