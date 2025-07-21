# Simple Speedtest-Client

A Java library and command-line interface (CLI) for testing internet bandwidth using [speedtest.net](https://www.speedtest.net/).
This is a simplified Java implementation of [Matt Martz's speedtest-cli](https://github.com/sivel/speedtest-cli).

## ✅ Requirements
- Java 11+
- Maven

## ⚙️ Building the Project
To build the JAR using Maven:
```bash
mvn clean install
```

## 🚀 Usage

### 💻 CLI Mode
To run the CLI client:
```bash
java -jar simple-speedtest-client-2.1.0.jar
```

Available CLI Parameters:
```bash
usage: Optional parameters:
 -h,--dedicatedServerHost <HOST>   Dedicated server host to run the tests
                                   against
 -l,--listServerHosts              Provide a list of server hosts to run
                                   the tests against
 -nd,--noDownload                  Do not perform download test
 -nu,--noUpload                    Do not perform upload test
 -s,--share                        Generate and provide an URL to the
                                   speedtest.net share results image
```

Example CLI Output:
```bash
$ java -jar simple-speedtest-client-2.1.0.jar 
Retrieving speedtest.net configuration...
Testing from M247 Ltd (91.132.139.76)...
Retrieving speedtest.net server list...
Selecting best server based on ping...
Hosted by AMA netwoRX GmbH (Vienna) [10,54 km]: 17,33 ms
Testing download speed........................................
Download: 50,26 Mbits/s
Testing upload speed...................................................
Upload: 19,24 Mbits/s
```

### 📚 Java Library Usage
Use the library directly in Java:
```java
// Note: Full test may take up to 25 seconds
try {
  SpeedtestResult result = SpeedtestController.runSpeedTest();
} catch (SpeedtestException e) {
  e.printStackTrace();
}
```

## 📦 Dependency (Maven / Gradle)
Add the dependency via JitPack:
https://jitpack.io/private#BernhardAngerer/simple-speedtest-client/2.1.0
