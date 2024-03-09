package me.plopez.survivalgame.log;

import me.plopez.survivalgame.log.LoggingLevel;

public class Logger {
  LoggingLevel level;
  String header = "";

  public static final String RED = "\u001b[31m";
  public static final String YELLOW = "\u001b[33m";
  public static final String GREEN = "\u001b[32m";
  public static final String CYAN = "\u001b[36m";
  public static final String PURPLE = "\u001b[35m";
  public static final String RESET = "\033[0m";

  Logger(LoggingLevel level) {
    this.level = level;
  }
  
  public Logger(LoggingLevel level, String header) {
    this.level = level;
    this.header = header;
  }
  
  void debug(Object msg){
    if (LoggingLevel.DEBUG.compareTo(level) > 0) return;
    System.out.println(header + GREEN + "[DEBUG] " + msg + RESET);
    //new Exception().printStackTrace();
  }
  
  public void info(Object msg){
    if (LoggingLevel.INFO.compareTo(level) > 0) return;
    System.out.println(header + "[INFO] " + msg + RESET);
  }
  
  public void warn(Object msg){
    if (LoggingLevel.WARN.compareTo(level) > 0) return;
    System.out.println(header + YELLOW + "[WARN] " + msg + RESET);
  }
  
  public void error(Object msg){
    if (LoggingLevel.ERROR.compareTo(level) > 0) return;
    System.out.println(header + RED + "[ERROR] " + msg + RESET);
  }
}

;
