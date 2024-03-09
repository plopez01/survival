class ConnectPacket extends NetworkPacket {
  int seed;
  final static int size = 4;

  ConnectPacket(int seed) {
    super(PacketType.CONNECT);
    this.seed = seed;
  }

  ConnectPacket(byte[] data) throws IOException {
    super(data);
  }

  void readFrom(ObjectInputStream stream) throws IOException {
    seed = stream.readInt();
  }

  void writeTo(ObjectOutputStream stream) throws IOException {
    stream.writeInt(seed);
  }
}
