package net.codeoftheday.log4j.gelf.transformer.helper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class UtcDateFormatter {

   private final ThreadLocal<DateFormat> dateFormatter = new ThreadLocal<DateFormat>() {
      @Override
      protected DateFormat initialValue() {
         final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
         sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
         return sdf;
      }
   };

   public String convertDateToString(final Date date) {
      return dateFormatter.get().format(date);
   }
}
