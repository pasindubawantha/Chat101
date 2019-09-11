## Chat101
A simple Group Chat application.

### Requirements
 - [Openjdk 11.0.4](https://openjdk.java.net/projects/jdk/11/)
 - [Apache Maven](https://maven.apache.org/)

### Getting Started
 - Build using Apache Maven `mvn package` (It may take some time to download Maven plugins and Java dependencies).
 - On Linux distributions make `target/Server.jar` and `target/Client.jar` files executable.
   - `chmod +x target/Server.jar`.
   - `chmod +x target/Client.jar`.
 - Run `target/Server.jar` with `java -jar ./target/Server.jar`
 - Run as many `target/Client.jar` instance just by double clicking !