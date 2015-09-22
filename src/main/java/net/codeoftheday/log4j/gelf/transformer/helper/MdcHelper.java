package net.codeoftheday.log4j.gelf.transformer.helper;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.MDC;

public final class MdcHelper {

   private MdcHelper() {
   }

   public static Set<String> getAllMdcKeys() {
      final Set<String> mdcKeys = new HashSet<String>();

      @SuppressWarnings("unchecked")
      final Map<String, Object> context = MDC.getContext();

      if (context != null) {
         mdcKeys.addAll(context.keySet());
      }
      return mdcKeys;
   }

}
