[![Maven Package](https://github.com/BernhardAngerer/simple-speedtest-client/actions/workflows/maven-publish.yml/badge.svg)](https://github.com/BernhardAngerer/simple-speedtest-client/actions/workflows/maven-publish.yml)
[![Maven Package](https://github.com/BernhardAngerer/simple-speedtest-client/actions/workflows/maven-verify.yml/badge.svg)](https://github.com/BernhardAngerer/simple-speedtest-client/actions/workflows/maven-verify.yml)

# ⏱️ Simple Speedtest-Client

A **Java library and CLI** tool for measuring internet bandwidth using [speedtest.net](https://www.speedtest.net/).  
This is a lightweight Java-based implementation inspired by [Matt Martz’s speedtest-cli](https://github.com/sivel/speedtest-cli).

## ✅ Features

- Perform download, upload, and latency tests
- (Auto-) Select best server based on ping
- Share result as a Speedtest.net image
- Use as a CLI or embed as a Java library

## 🧰 Requirements
- Java 17+
- Maven

## ⚙️ Build Instructions
To build the project and create a JAR file:
```bash
mvn clean install
```
The JAR will be located in target/, e.g.:
```bash
target/simple-speedtest-client-3.0.1.jar
```

## 🚀 Usage

### 💻 CLI Usage
To run the CLI client:
```bash
java -jar simple-speedtest-client-3.0.1.jar
```

#### 🔧 CLI Options
```bash
usage: Optional parameters:
 -h,--dedicatedServerHost <HOST>   Dedicated server host to run the tests
                                   against
 -l,--listServerHosts              Provide a list of server hosts to run
                                   the tests against
 -nd,--noDownload                  Do not perform download test
 -nu,--noUpload                    Do not perform upload test
 -of,--outputFormat <FORMAT>       Output format of results (default:
                                   console)
                                   Available formats:
                                   console — human-readable output to the
                                   console
                                   json    — machine-readable JSON format
                                   xml     — machine-readable XML format
                                   csv     — comma-separated values format
 -s,--share                        Generate and provide an URL to the
                                   speedtest.net share results image

```

#### 📈 Example Output
```bash
$ java -jar simple-speedtest-client-3.0.1.jar 

Retrieving speedtest.net configuration...
Testing from Telekom Austria (193.81.52.87, AT)...
Retrieving speedtest.net server list...
Selecting best server based on ping...
Hosted by COSYS DATA GmbH (Vienna, AT) [9,46 km]: 18,00 ms
Testing download speed........................................
Download: 54,26 Mbits/s
Testing upload speed...................................................
Upload: 19,19 Mbits/s
```

### 📚 Java Library Usage
Use the library directly in Java:
```java
try {
    // ⚠️ Note: This is a blocking call and may take up to 25 seconds 
    // depending on network latency, server selection, and transfer speed.
    // Consider running it in a background thread if used in a GUI or server application.
    SpeedtestResult result = SpeedtestController.runSpeedTest();

    System.out.printf("Download: %.2f Mbps%n", result.getDownload().getRateInMbps());
    System.out.printf("Upload: %.2f Mbps%n", result.getUpload().getRateInMbps());
    System.out.printf("Latency: %.2f ms%n", result.getLatency().getLatency());
    System.out.printf("Server: %s (%s, %s)%n",
                        result.getServer().getSponsor(),
                        result.getServer().getCity(),
                        result.getServer().getIsoAlpha2CountryCode());
} catch (SpeedtestException e) {
    e.printStackTrace();
}
```

#### 📈 Example Output
```bash
Download: 156,19 Mbps
Upload: 16,33 Mbps
Latency: 21,33 ms
Server: Timewarp IT Consulting GmbH (Vienna, AT)
```

## 📦 Dependency (Maven / Gradle)
Add the dependency via JitPack:
https://jitpack.io/private#BernhardAngerer/simple-speedtest-client/3.0.1
