class Logger {
  LOGGING_LEVEL level;
  String header = "";
  
  Logger(LOGGING_LEVEL level) {
    this.level = level;
  }
  
  Logger(LOGGING_LEVEL level, String header) {
    this.level = level;
    this.header = header;
  }
  
  void debug(Object msg){
    if (LOGGING_LEVEL.DEBUG.compareTo(level) > 0) return;
    println(header + "[DEBUG] " + msg);
  }
  
  void info(Object msg){
    if (LOGGING_LEVEL.INFO.compareTo(level) > 0) return;
    println(header + "[INFO] " + msg);
  }
  
  void warn(Object msg){
    if (LOGGING_LEVEL.WARN.compareTo(level) > 0) return;
    println(header + "[WARN] " + msg);
  }
  
  void error(Object msg){
    if (LOGGING_LEVEL.ERROR.compareTo(level) > 0) return;
    println(header + "[ERROR] " + msg);
  }
}

enum LOGGING_LEVEL {
  QUIET,
  ERROR,
  WARN,
  INFO,
  DEBUG,
  ALL
};
