package net.codeoftheday.log4j.gelf.connection;

import java.net.InetSocketAddress;

import org.graylog2.gelfclient.GelfConfiguration;
import org.graylog2.gelfclient.GelfMessage;
import org.graylog2.gelfclient.GelfTransports;
import org.graylog2.gelfclient.transport.GelfTransport;

import net.codeoftheday.log4j.gelf.config.GelfAppenderConfig;

public class GelfConnection {

   private static final int BUFFER_SIZE = -1;
   private static final int CONNECT_TIMEOUT = 3000;
   private static final int RECONNECT_DELAY = 500;
   private static final boolean TCP_KEEP_ALIVE = true;
   private static final boolean TCP_NO_DELAY = true;

   private final GelfConfiguration config;
   private GelfTransport transport;

   public GelfConnection(final GelfAppenderConfig appenderConfig) {
      this.config = new GelfConfiguration(new InetSocketAddress(appenderConfig.getHost(), appenderConfig.getPort()))
         .transport(appenderConfig.getProtocol().equalsIgnoreCase("udp") ? GelfTransports.UDP : GelfTransports.TCP)
         .queueSize(appenderConfig.getQueueSize())
         .connectTimeout(CONNECT_TIMEOUT)
         .tcpKeepAlive(TCP_KEEP_ALIVE)
         .reconnectDelay(RECONNECT_DELAY)
         .tcpNoDelay(TCP_NO_DELAY)
         .sendBufferSize(BUFFER_SIZE);

      if (appenderConfig.isTlsEnabled()) {
         config.enableTls();
      }
   }

   public void close() {
      if (null != transport) {
         transport.stop();
         transport = null;
      }
   }

   public void open() {
      if (null != transport) {
         throw new IllegalStateException("Transport is already open.");
      }

      transport = GelfTransports.create(config);
   }

   public boolean sendMessage(final GelfMessage message) {
      // Async send
      return transport.trySend(message);
   }

}
