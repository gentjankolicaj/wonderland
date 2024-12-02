module wonderland.garden {
  requires lettuce.core;
  requires io.netty.buffer;
  requires io.netty.common;
  requires io.grpc;
  requires io.grpc.stub;
  requires protobuf.java;

  //wonderland modules
  requires wonderland.base;
  requires wonderland.grpc;
  requires wonderland.protos;
  requires wonderland.garden.files;

  //make package content visible outside module.
  exports io.wonderland.garden;
  exports io.wonderland.garden.api;
  exports io.wonderland.garden.domain;
  exports io.wonderland.garden.resource;
  exports io.wonderland.garden.exception;
  exports io.wonderland.garden.redis;

}