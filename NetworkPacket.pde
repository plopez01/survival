import java.io.*;

abstract class NetworkPacket {
  PacketType type;
  static final int size = 7;
  
  
  NetworkPacket(PacketType type) {
    this.type = type;
  }
  
  NetworkPacket(InputStream is) throws IOException {
    ObjectInputStream ois = new ObjectInputStream(is);
    log.debug("OIS:" + ois.available());
    type = PacketType.values()[ois.readByte()];
    //log.debug("Type: " + ois.readByte());
    readFrom(ois);
  }
  
  abstract void writeTo(ObjectOutputStream stream) throws IOException;
  abstract void readFrom(ObjectInputStream stream) throws IOException;
  
  byte[] serialize() throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    
    try (ObjectOutputStream os = new ObjectOutputStream(baos)) {
      os.writeByte((byte) type.ordinal());
      writeTo(os);
    }
    
    return baos.toByteArray();
  }
  
  
}

enum PacketType {
  CONNECT
}
