package net.codeoftheday.log4j.gelf.transformer;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

import org.apache.log4j.spi.LocationInfo;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.ThrowableInformation;
import org.graylog2.gelfclient.GelfMessage;
import org.graylog2.gelfclient.GelfMessageBuilder;

import net.codeoftheday.log4j.gelf.config.GelfAppenderConfig;
import net.codeoftheday.log4j.gelf.config.GelfFieldNames;
import net.codeoftheday.log4j.gelf.transformer.helper.HostNameHelper;
import net.codeoftheday.log4j.gelf.transformer.helper.LevelHelper;
import net.codeoftheday.log4j.gelf.transformer.helper.MdcHelper;
import net.codeoftheday.log4j.gelf.transformer.helper.TimeStampHelper;
import net.codeoftheday.log4j.gelf.transformer.helper.UtcDateFormatter;

public class Log4jTransformer {

   private static final UtcDateFormatter DATE_FORMATTER = new UtcDateFormatter();

   public GelfMessage transform(final GelfAppenderConfig config, final LoggingEvent message) {
      GelfMessageBuilder messageBuilder = new GelfMessageBuilder("", HostNameHelper.getHostName());

      messageBuilder = parseLevel(messageBuilder, message);
      messageBuilder = parseTimeStamp(messageBuilder, message);
      messageBuilder = parseThread(messageBuilder, message);
      messageBuilder = parseMessage(messageBuilder, message);
      messageBuilder = parseFullMessage(messageBuilder, message);

      messageBuilder = parseStacktrace(messageBuilder, message);
      messageBuilder = parseMdc(messageBuilder, message);
      messageBuilder = parseLoggerName(messageBuilder, message);
      messageBuilder = parseSourceInformation(messageBuilder, message);
      messageBuilder = parseFacility(messageBuilder, config);

      return messageBuilder.build();
   }

   private GelfMessageBuilder parseFacility(final GelfMessageBuilder gelfMessage, final GelfAppenderConfig config) {
      if (config.getFacility() != null && config.getFacility().trim().length() > 0) {
         gelfMessage.additionalField(GelfFieldNames.FACILITY, config.getFacility());
      }
      return gelfMessage;
   }

   private GelfMessageBuilder parseFullMessage(final GelfMessageBuilder builder, final LoggingEvent message) {
      return builder.fullMessage(message.getRenderedMessage());
   }

   private GelfMessageBuilder parseLevel(final GelfMessageBuilder gelfMessage, final LoggingEvent message) {
      gelfMessage.level(LevelHelper.convert(message.getLevel()));
      gelfMessage.additionalField(GelfFieldNames.SEVERITY, message.getLevel().toString());
      return gelfMessage;
   }

   private GelfMessageBuilder parseLoggerName(final GelfMessageBuilder builder, final LoggingEvent message) {
      builder.additionalField(GelfFieldNames.LOGGER_NAME, message.getLoggerName());
      return builder;
   }

   private GelfMessageBuilder parseMdc(final GelfMessageBuilder builder, final LoggingEvent message) {
      for (final String mdcName : MdcHelper.getAllMdcKeys()) {
         builder.additionalField(mdcName, message.getMDC(mdcName));
      }

      return builder;
   }

   private GelfMessageBuilder parseMessage(final GelfMessageBuilder builder, final LoggingEvent message) {
      if (message.getMessage() != null) {
         return builder.message(message.getMessage().toString());
      }
      return builder;
   }

   private GelfMessageBuilder parseSourceInformation(final GelfMessageBuilder builder, final LoggingEvent message) {
      final LocationInfo locationInformation = message.getLocationInformation();
      if (null != locationInformation) {
         // Class name
         builder.additionalField(GelfFieldNames.CLASS_NAME, locationInformation.getClassName());

         // Method name
         builder.additionalField(GelfFieldNames.METHOD_NAME, locationInformation.getMethodName());

         // Line number
         builder.additionalField(GelfFieldNames.LINE_NUMBER, locationInformation.getLineNumber());
      }

      return builder;
   }

   private GelfMessageBuilder parseStacktrace(final GelfMessageBuilder builder, final LoggingEvent message) {
      final ThrowableInformation throwableInformation = message.getThrowableInformation();
      if (null != throwableInformation && null != throwableInformation.getThrowable()) {
         try (final StringWriter writer = new StringWriter();) {
            throwableInformation.getThrowable().printStackTrace(new PrintWriter(writer));
            builder.additionalField(GelfFieldNames.STACKTRACE, writer.toString());
         }
         catch (final IOException e) {
            throw new RuntimeException("Error while writing Stacktrace to StringWriter.", e);
         }
      }
      return builder;
   }

   private GelfMessageBuilder parseThread(final GelfMessageBuilder builder, final LoggingEvent message) {
      return builder.additionalField(GelfFieldNames.THREAD_NAME, message.getThreadName());
   }

   private GelfMessageBuilder parseTimeStamp(final GelfMessageBuilder gelfMessage, final LoggingEvent message) {
      final long timeStamp = TimeStampHelper.getTimeStamp(message);
      gelfMessage.timestamp(timeStamp / 1000d);
      gelfMessage.additionalField(GelfFieldNames.TIME, DATE_FORMATTER.convertDateToString(new Date(timeStamp)));
      return gelfMessage;
   }

}
