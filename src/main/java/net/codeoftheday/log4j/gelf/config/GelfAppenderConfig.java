package net.codeoftheday.log4j.gelf.config;

public class GelfAppenderConfig {

   private String facility = "";
   private String host = "localhost";
   private int port = 12201;
   private String protocol = "udp";
   private int queueSize = 512;
   private boolean tlsEnabled = false;

   public String getFacility() {
      return facility;
   }

   public String getHost() {
      return host;
   }

   public int getPort() {
      return port;
   }

   public String getProtocol() {
      return protocol;
   }

   public int getQueueSize() {
      return queueSize;
   }

   public boolean isTlsEnabled() {
      return tlsEnabled;
   }

   public void setFacility(final String facility) {
      this.facility = facility;
   }

   public void setHost(final String host) {
      this.host = host;
   }

   public void setPort(final int port) {
      this.port = port;
   }

   public void setProtocol(final String protocol) {
      if ("udp".equalsIgnoreCase(protocol) || "tcp".equalsIgnoreCase(protocol)) {
         this.protocol = protocol;
         return;
      }

      throw new IllegalArgumentException("Only 'udp' or 'tcp' are supported!");
   }

   public void setQueueSize(final int queueSize) {
      this.queueSize = queueSize;
   }

   public void setTlsEnabled(final boolean tlsEnabled) {
      this.tlsEnabled = tlsEnabled;
   }

}
