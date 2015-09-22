# GELF Appender for Apache Log4j (1.x)

[![Build Status](https://travis-ci.org/JanLoebel/log4j-gelf.svg?branch=master)](https://travis-ci.org/JanLoebel/log4j-gelf)

This appender for [Apache Log4j (1.x)](https://logging.apache.org/log4j/1.x/) logs messages to a GELF server like [Graylog2](http://www.graylog2.org) or [logstash](http://logstash.net).

It's using the official [GELF Java client](https://graylog2.github.io/gelfclient/) to connect to a remote server.


## Options
You can specify the following parameters for the GELF appender in the `log4j.xml` or `log4.properties` configuration file:

* `Facility` (default: ``)
  * The facility of the logger
* `Server` (default: `localhost`)
  * The host name or IP address of the GELF server
* `Port` (default: `12201`)
  * The port the GELF server is listening on
* `Protocol` (default: `UDP`)
  * The transport protocol to use (UDP or TCP)
* `QueueSize` (default: `512`)
  * The size of the internally used queue
* `TlsEnabled` (default: `false`)
  * Should the connection use TLS for transportation


## Usage
Add the `log4j-gelf` dependecy to your `pom.xml`:
```
	<dependency>
		<groupId>net.codeoftheday.log4j</groupId>
		<artifactId>log4j-gelf</artifactId>
		<version>0.0.1-SNAPSHOT</version>
	</dependency>
```

The following ``log4.properties` configuration will send all log messages with a log-level higher than `INFO` via `UDP` to `localhost` on port `12201`:
```
log4j.rootLogger=INFO, gelf

log4j.appender.gelf=net.codeoftheday.log4j.gelf.GelfAppender
log4j.appender.gelf.Facility=My Gelf Facility
log4j.appender.gelf.Host=localhost
log4j.appender.gelf.Port=12201
log4j.appender.gelf.Protocol=udp
log4j.appender.gelf.TlsEnabled=false
log4j.appender.gelf.QueueSize=512
```

## GELF-Message-Fields
Resulting GELF-Message fields will be:

* `message` (GELF-standard)
  * The log message
* `fullmessage` (GELF-standard)
  * The full log message
* `timestamp` (GELF-standard)
  * The timestamp of the log message
* `level` (GELF-standard)
  * The numeric value of the log level/severity
* `facility` (Optional)
  * The value of the configured `facility` field
* `loggerName`
  * The name of the logger which triggered the logging  
* `sourceMethodName`
  * The name of the method which triggered the logging
* `sourceLineNumber`
  * The line number of the line which triggered the logging
* `sourceClassName`
  * The class name of the class which triggered the logging
* `threadName`
  * The thread which triggered the logging
* `severity`
  * The severity of the triggered log level
* `time`
  * Timestamp formatted as `yyyy-MM-ddTHH:mm:ss.SSSZ`
* `stackTrace` (Optional)
  * Stacktrace if there was an exception logged
* `{MDC-KEY}` (Optional)
  * All MDC key- and value-pairs will be added to the message
* `version`
  * Is the GELF-Version which is always 1.1

The project is based on [log4j2-gelf](https://github.com/Graylog2/log4j2-gelf).  

## Known limitations 
If you find any bug or if you have a feature-request, don't hesitate to use the [issue-tracker](https://github.com/JanLoebel/log4j-gelf/issues). **Push**-requests are always welcome.

* If your program ends before the [GELF Java client](https://graylog2.github.io/gelfclient/) has send the messages, these messages will be not be send.


## License

GELF Appender for Apache Log4j

Copyright (C) 2015 Jan Löbel

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
