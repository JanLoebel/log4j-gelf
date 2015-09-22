package net.codeoftheday.log4j.gelf.transformer.helper;

import org.apache.log4j.Level;
import org.graylog2.gelfclient.GelfMessageLevel;

public final class LevelHelper {

   private LevelHelper() {
   }

   public static GelfMessageLevel convert(final Level level) {
      switch (level.toInt()) {
         case Level.DEBUG_INT:
            return GelfMessageLevel.DEBUG;
         case Level.ERROR_INT:
            return GelfMessageLevel.ERROR;
         case Level.FATAL_INT:
            return GelfMessageLevel.EMERGENCY;
         case Level.INFO_INT:
            return GelfMessageLevel.INFO;
         case Level.WARN_INT:
            return GelfMessageLevel.WARNING;
         case Level.OFF_INT:
         default:
            throw new IllegalArgumentException("Given level '" + level + "' is not supported!");
      }
   }

}
