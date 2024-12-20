# wonderland

- Project around cryptography.
- Warning !!! read all licenses before any action, by acting you have accepted all licenses.

[![License](https://img.shields.io/github/license/gentjankolicaj/wonderland)](https://github.com/gentjankolicaj/wonderland/blob/master/LICENSE)
[![License 2](https://img.shields.io/badge/Custom_License-blue)](https://github.com/gentjankolicaj/wonderland/blob/master/LICENSE_2)

## Modules:

- wonderland-alice
- wonderland-base
- wonderland-crypto
- wonderland-garden
- wonderland-garden-files
- wonderland-grpc
- wonderland-hatter
- wonderland-protos
- wonderland-rabbit-hole
- wonderland-red-queen

### What is wonderland-alice for ?

* Cryptographic Service Provider module
* Conforming to JAVA JCA , contains implementations about: ciphers,keys, encoders.
* Work in progress, not production ready !!!.

### What is wonderland-crypto for ?

- Cryptographic module

* High level wrapper/util implementations around already JAVA CSP.

- Work in progress

### What is wonderland-base for ?

- base module
- Contains implementations of utils & helper methods & classes.

### What is wonderland-grpc for ?

- grpc module
- Contains grpc implementations to be used on integration.

### What is wonderland-protos for ?

- protos module
- Contains service definitions in proto files.

### What is wonderland-hatter for ?

- hatter module
- TODO: A key-escrow application/server using spring.

### What is wonderland-garden for ?

- Externalized resource module
- Externalized resource module for resources letter frequency etc...
- Resource origin is supposed to be different like local file_system, redis, mysql

### What is wonderland-red-queen for ?

- Cryptanalysis module
- Contains implementations around cryptanalysis.
- Work in progress

### What is wonderland-rabbit-hole for ?

- Javafx gui application using wonderland modules.
- [Application manual](/docs/rh_manual.md)

[![Get it from the Snap Store](https://snapcraft.io/static/images/badges/en/snap-store-black.svg)](https://snapcraft.io/rabbit-hole)

<table>
<thead>Integrated cryptographic service providers :</thead>
<tr><td>Bouncy Castle</td></tr>
<tr><td>Sun</td></tr>
<tr><td>SunJCE</td></tr>
<tr><td>Conscrypt</td></tr>
<tr><td>AmazonCorretoCryptoProvider</td></tr>
<tr><td>Alice</td></tr>
</table>

- Screenshots of rabbit-hole app :
  ![](docs/img/cipher_0.png)
  <br>
  ![](docs/img/keygen_1.png)

## Licenses

Note: while wonderland project comes in below licenses,

- [GNU General Public License v3.0](https://github.com/gentjankolicaj/wonderland/blob/master/LICENSE)
- [Custom License 2](https://github.com/gentjankolicaj/wonderland/blob/master/LICENSE_2)

portions/parts/lines of this project include software dependencies licensed under different
licenses.
External cryptographic service providers have their licenses accordingly.
Please see licenses for below cryptographic service providers:

- https://github.com/bcgit/bc-java
- https://github.com/corretto/amazon-corretto-crypto-provider
- https://github.com/openjdk/jdk
- https://github.com/google/conscrypt