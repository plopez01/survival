class RangeConstrain {
  float min, max;
  
  RangeConstrain(float min, float max) {
    this.min = min;
    this.max = max;
  }
  
  float enforce(float value) {
    if (value > max) return max;
    else if (value < min) return min;
    else return value;
  }
  
  boolean inBounds(float value) {
    return value <= max && value >= min;
  }
  
  float getPosition(float value) {
    return (value-min)/(max-min);
  }
}
