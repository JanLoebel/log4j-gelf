package net.codeoftheday.log4j.gelf.transformer.helper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.log4j.spi.LoggingEvent;

public final class TimeStampHelper {

   private static Method getTimeStamp;

   static {
      final Method[] declaredMethods = LoggingEvent.class.getDeclaredMethods();
      for (final Method declaredMethod : declaredMethods) {
         if (declaredMethod.getName().equals("getTimeStamp")) {
            getTimeStamp = declaredMethod;
            break;
         }
      }
   }

   private TimeStampHelper() {
   }

   public static synchronized long getTimeStamp(final LoggingEvent event) {
      if (null != getTimeStamp) {
         try {
            return (Long) getTimeStamp.invoke(event);
         }
         catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            // Ignore
         }
      }

      return System.currentTimeMillis();
   }
}
