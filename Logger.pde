class Logger {
  LOGGING_LEVEL level;
  
  Logger(LOGGING_LEVEL level) {
    this.level = level;
  }
  
  void info(String msg){
    if (LOGGING_LEVEL.INFO.compareTo(level) > 0) return;
    println("[INFO] " + msg);
  }
  
  void warn(String msg){
    if (LOGGING_LEVEL.WARN.compareTo(level) > 0) return;
    println("[WARN] " + msg);
  }
  
  void error(String msg){
    if (LOGGING_LEVEL.ERROR.compareTo(level) > 0) return;
    println("[ERROR] " + msg);
  }
}

enum LOGGING_LEVEL {
  QUIET,
  ERROR,
  WARN,
  INFO,
  ALL
};
