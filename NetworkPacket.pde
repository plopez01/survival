import java.io.*;

abstract class NetworkPacket {
  PacketType type;
  
  NetworkPacket(PacketType type) {
    this.type = type;
  }
  
  NetworkPacket(byte[] data) throws IOException {
        log.debug(data);
    ByteArrayInputStream bais = new ByteArrayInputStream(data);
    
    ObjectInputStream is = new ObjectInputStream(bais);
    type = PacketType.values()[is.readInt()];
    readFrom(is);
  }
  
  abstract void writeTo(ObjectOutputStream stream) throws IOException;
  abstract void readFrom(ObjectInputStream stream) throws IOException;
  
  byte[] serialize() throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    
    ObjectOutputStream os = new ObjectOutputStream(baos);
    os.write((byte) type.ordinal());
    writeTo(os);
    
    return baos.toByteArray();
  }
  
  
}

enum PacketType {
  CONNECT
}
