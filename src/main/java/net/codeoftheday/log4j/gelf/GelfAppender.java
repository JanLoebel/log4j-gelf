package net.codeoftheday.log4j.gelf;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;
import org.graylog2.gelfclient.GelfMessage;

import net.codeoftheday.log4j.gelf.config.GelfAppenderConfig;
import net.codeoftheday.log4j.gelf.connection.GelfConnection;
import net.codeoftheday.log4j.gelf.transformer.Log4jTransformer;

public class GelfAppender extends AppenderSkeleton {

   private final GelfAppenderConfig config;
   private GelfConnection connection;

   private final Log4jTransformer transformer;

   public GelfAppender() {
      this(new Log4jTransformer(), new GelfAppenderConfig());
   }

   // Package private constructor for testing
   GelfAppender(final Log4jTransformer transformer, final GelfAppenderConfig config) {
      this.transformer = transformer;
      this.config = config;
   }

   @Override
   public void activateOptions() {
      super.activateOptions();

      connection = new GelfConnection(config);
      connection.open();
   }

   @Override
   protected void append(final LoggingEvent event) {
      if (!sendEvent(event)) {
         errorHandler.error("Could not send event! Properly the queue is full!");
      }
   }

   @Override
   public void close() {
      if (null != connection) {
         connection.close();
      }
   }

   @Override
   public boolean requiresLayout() {
      return false; // Pattern layout is not needed to be set
   }

   private boolean sendEvent(final LoggingEvent event) {
      final GelfMessage message = transformer.transform(config, event);
      return connection.sendMessage(message);
   }

   // ######### Appender Options #########

   public void setFacility(final String facility) {
      config.setFacility(facility);
   }

   public void setHost(final String host) {
      config.setHost(host);
   }

   public void setPort(final int port) {
      config.setPort(port);
   }

   public void setProtocol(final String protocol) {
      config.setProtocol(protocol);
   }

   public void setQueueSize(final int queueSize) {
      config.setQueueSize(queueSize);
   }

   public void setTlsEnabled(final boolean tlsEnabled) {
      config.setTlsEnabled(tlsEnabled);
   }

}
