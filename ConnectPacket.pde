class ConnectPacket extends NetworkPacket {
  int seed;
  static final int size = NetworkPacket.size + 4;

  ConnectPacket(int seed) {
    super(PacketType.CONNECT);
    this.seed = seed;
  }

  ConnectPacket(InputStream is) throws IOException {
    super(is);
  }

  void readFrom(ObjectInputStream stream) throws IOException {
    seed = stream.readInt();
  }

  void writeTo(ObjectOutputStream stream) throws IOException {
    stream.writeInt(seed);
  }
}
