package net.codeoftheday.log4j.gelf.transformer.helper;

import java.net.InetAddress;
import java.net.UnknownHostException;

public final class HostNameHelper {

   private static String hostName;

   static {
      try {
         hostName = InetAddress.getLocalHost().getHostName();
      }
      catch (final UnknownHostException e) {
         hostName = "localhost";
      }
   }

   private HostNameHelper() {
   }

   public static String getHostName() {
      return hostName;
   }
}
