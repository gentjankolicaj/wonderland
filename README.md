# wonderland

- Project around cryptography.
- Warning !!! read all licenses before any action, by acting you have accepted all licenses.

[![License](https://img.shields.io/github/license/gentjankolicaj/wonderland)](https://github.com/gentjankolicaj/wonderland/blob/master/LICENSE)
[![License 2](https://img.shields.io/badge/Custom_License-blue)](https://github.com/gentjankolicaj/wonderland/blob/master/LICENSE_2)

## Modules:

- alice
- base
- crypto
- garden
- garden-files
- grpc
- hatter
- protos
- rabbit-hole
- red-queen

### What is alice for ?

- Cryptographic Service Provider module
- Contains implementations about: ciphers, keygen, hash

### What is crypto for ?

- Cryptographic module
- High level wrapper/util implementations around protocols & libs
- Work In Progress

### What is base for ?

- base module
- Contains implementations of utils & helper methods & classes.

### What is grpc for ?

- grpc module
- Contains grpc implementations to be used on integration.

### What is protos for ?

- protos module
- Contains service definitions in proto files.

### What is hatter for ?

- hatter module
- TODO: A key-escrow application/server using spring.

### What is garden for ?

- Externalized resource module
- Externalized resource module for resources letter frequency etc...
- Resource origin is supposed to be different like local file_system, redis, mysql

### What is red-queen for ?

- Cryptanalysis module
- Contains implementations around cryptanalysis.

### What is rabbit-hole for ?

- Javafx gui application using modules like: alice, base, crypto, garden, red-queen etc.

<table>
<thead>Integrated cryptographic security providers :</thead>
<tr><td>Bouncy Castle</td></tr>
<tr><td>Sun</td></tr>
<tr><td>SunJCE</td></tr>
<tr><td>Conscrypt</td></tr>
<tr><td>Alice</td></tr>
</table>

- Screenshots of rabbit-hole app :
  ![](img/cipher_0.png)
  <br>
  ![](img/analysis_lf.png)

## License

[GNU General Public License v3.0](https://github.com/gentjankolicaj/wonderland/blob/master/LICENSE)

[Custom License 2](https://github.com/gentjankolicaj/wonderland/blob/master/LICENSE_2)
