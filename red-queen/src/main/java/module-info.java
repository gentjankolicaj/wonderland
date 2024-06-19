module wonderland.redqueen {

  //wonderland modules
  requires wonderland.base;
  requires wonderland.grpc;
  requires wonderland.protos;
  requires wonderland.garden;
  requires wonderland.struct;

  //make package content visible outside module.
  exports io.wonderland.rq;
  exports io.wonderland.rq.api;
  exports io.wonderland.rq.cryptanalysis;
  exports io.wonderland.rq.ds;

  opens io.wonderland.rq.api;
}