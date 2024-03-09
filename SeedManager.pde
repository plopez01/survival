class SeedManager {
  int seed;
  
  SeedManager(){
    generateSeed();
  }
  
  SeedManager(int seed) {
    setSeed(seed);
  }
  
  void setSeed(int newSeed) {
    seed = newSeed;
    noiseSeed(newSeed);
    randomSeed(newSeed);
  }
  
  void generateSeed(){
    setSeed(int(random(Integer.MIN_VALUE, Integer.MAX_VALUE)));
  }
}
