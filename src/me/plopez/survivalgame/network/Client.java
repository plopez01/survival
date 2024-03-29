package me.plopez.survivalgame.network;/* -*- mode: java; c-basic-offset: 2; indent-tabs-mode: nil -*- */

/*
  me.plopez.survivalgame.network.Client - basic network client implementation
  Part of the Processing project - http://processing.org

  Copyright (c) 2004-2007 Ben Fry and Casey Reas
  The previous version of this code was developed by Hernando Barragan

  This library is free software; you can redistribute it and/or
  modify it under the terms of the GNU Lesser General Public
  License as published by the Free Software Foundation; either
  version 2.1 of the License, or (at your option) any later version.

  This library is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
  Lesser General Public License for more details.

  You should have received a copy of the GNU Lesser General
  Public License along with this library; if not, write to the
  Free Software Foundation, Inc., 59 Temple Place, Suite 330,
  Boston, MA  02111-1307  USA
*/

import processing.core.*;

import java.io.*;
import java.lang.reflect.*;
import java.net.*;

/**
 * ( begin auto-generated from me.plopez.survivalgame.network.Client.xml )
 * <p>
 * A client connects to a server and sends data back and forth. If anything
 * goes wrong with the connection, for example the host is not there or is
 * listening on a different port, an exception is thrown.
 * <p>
 * ( end auto-generated )
 *
 * @webref net
 * @brief The client class is used to create client Objects which connect to a server to exchange data.
 * @instanceName client any variable of type me.plopez.survivalgame.network.Client
 * @usage Application
 * @see_external LIB_net/clientEvent
 */
public class Client implements Runnable {

    protected static final int MAX_BUFFER_SIZE = 1 << 27; // 128 MB

    PApplet parent;
    volatile Thread thread;
    Socket socket;
    int port;
    String host;

    public InputStream input;
    public OutputStream output;

    final Object bufferLock = new Object[0];

    byte buffer[] = null;
    int bufferIndex;
    int bufferLast;

    boolean disposeRegistered = false;


    /**
     * @param parent typically use "this"
     * @param host   address of the server
     * @param port   port to read/write from on the server
     */
    public Client(PApplet parent, String host, int port) throws IOException {
        this.parent = parent;
        this.host = host;
        this.port = port;

        socket = new Socket(this.host, this.port);
        input = socket.getInputStream();
        output = socket.getOutputStream();

        thread = new Thread(this);
        //thread.start();

        parent.registerMethod("dispose", this);
        disposeRegistered = true;

    }


    /**
     * @param socket any object of type Socket
     * @throws IOException
     */
    public Client(PApplet parent, Socket socket) throws IOException {
        this.parent = parent;
        this.socket = socket;

        input = socket.getInputStream();
        output = socket.getOutputStream();

        thread = new Thread(this);
        //thread.start();
    }


    /**
     * ( begin auto-generated from Client_stop.xml )
     * <p>
     * Disconnects from the server. Use to shut the connection when you're
     * finished with the me.plopez.survivalgame.network.Client.
     * <p>
     * ( end auto-generated )
     *
     * @webref client:client
     * @brief Disconnects from the server
     * @usage application
     */
    public void stop() {
        if (thread != null) {
            onDisconect();
        }
        if (disposeRegistered) {
            parent.unregisterMethod("dispose", this);
            disposeRegistered = false;
        }
        dispose();
    }

    /**
     * Disconnect from the server: internal use only.
     * <p>
     * This should only be called by the internal functions in PApplet,
     * use stop() instead from within your own applets.
     */
    public void dispose() {
        onDisconnect();
        thread = null;
        try {
            if (input != null) {
                input.close();
                input = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (output != null) {
                output.close();
                output = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (socket != null) {
                socket.close();
                socket = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void onDisconnect(){}

    @Override
    public void run() {
        byte[] readBuffer;
        { // make the read buffer same size as socket receive buffer so that
            // we don't waste cycles calling listeners when there is more data waiting
            int readBufferSize = 1 << 16; // 64 KB (default socket receive buffer size)
            try {
                readBufferSize = socket.getReceiveBufferSize();
            } catch (SocketException ignore) {
            }
            readBuffer = new byte[readBufferSize];
        }
        while (Thread.currentThread() == thread) {
            try {
                while (input != null) {
                    int readCount;

                    // try to read a byte using a blocking read.
                    // An exception will occur when the sketch is exits.
                    try {
                        readCount = input.read(readBuffer, 0, readBuffer.length);
                    } catch (SocketException e) {
                        System.err.println("me.plopez.survivalgame.network.Client SocketException: " + e.getMessage());
                        // the socket had a problem reading so don't try to read from it again.
                        stop();
                        return;
                    }

                    // read returns -1 if end-of-stream occurs (for example if the host disappears)
                    if (readCount == -1) {
                        System.err.println("me.plopez.survivalgame.network.Client got end-of-stream.");
                        stop();
                        return;
                    }

                    synchronized (bufferLock) {
                        int freeBack = buffer.length - bufferLast;
                        if (readCount > freeBack) {
                            // not enough space at the back
                            int bufferLength = bufferLast - bufferIndex;
                            byte[] targetBuffer = buffer;
                            if (bufferLength + readCount > buffer.length) {
                                // can't fit even after compacting, resize the buffer
                                // find the next power of two which can fit everything in
                                int newSize = Integer.highestOneBit(bufferLength + readCount - 1) << 1;
                                if (newSize > MAX_BUFFER_SIZE) {
                                    // buffer is full because client is not reading (fast enough)
                                    System.err.println("me.plopez.survivalgame.network.Client: can't receive more data, buffer is full. " +
                                            "Make sure you read the data from the client.");
                                    stop();
                                    return;
                                }
                                targetBuffer = new byte[newSize];
                            }
                            // compact the buffer (either in-place or into the new bigger buffer)
                            System.arraycopy(buffer, bufferIndex, targetBuffer, 0, bufferLength);
                            bufferLast -= bufferIndex;
                            bufferIndex = 0;
                            buffer = targetBuffer;
                        }
                        // copy all newly read bytes into the buffer
                        System.arraycopy(readBuffer, 0, buffer, bufferLast, readCount);
                        bufferLast += readCount;
                    }
                }
            } catch (IOException e) {
                //errorMessage("run", e);
                e.printStackTrace();
            }
        }
    }

    protected void onDisconect() {}

    /**
     * ( begin auto-generated from Client_active.xml )
     * <p>
     * Returns true if this client is still active and hasn't run
     * into any trouble.
     * <p>
     * ( end auto-generated )
     *
     * @webref client:client
     * @brief Returns true if this client is still active
     * @usage application
     */
    public boolean active() {
        return (thread != null);
    }


    /**
     * ( begin auto-generated from Client_ip.xml )
     * <p>
     * Returns the IP address of the computer to which the me.plopez.survivalgame.network.Client is attached.
     * <p>
     * ( end auto-generated )
     *
     * @webref client:client
     * @usage application
     * @brief Returns the IP address of the machine as a String
     */
    public String ip() {
        if (socket != null) {
            return socket.getInetAddress().getHostAddress();
        }
        return null;
    }

    /**
     * ( begin auto-generated from Client_clear.xml )
     * <p>
     * Empty the buffer, removes all the data stored there.
     * <p>
     * ( end auto-generated )
     *
     * @webref client:client
     * @usage application
     * @brief Clears the buffer
     */
    public void clear() {
        synchronized (bufferLock) {
            bufferLast = 0;
            bufferIndex = 0;
        }
    }


    /**
     * ( begin auto-generated from Client_read.xml )
     * <p>
     * Returns a number between 0 and 255 for the next byte that's waiting in
     * the buffer. Returns -1 if there is no byte, although this should be
     * avoided by first cheacking <b>available()</b> to see if any data is available.
     * <p>
     * ( end auto-generated )
     *
     * @webref client:client
     * @usage application
     * @brief Returns a value from the buffer
     */
    public int read() {
        synchronized (bufferLock) {
            if (bufferIndex == bufferLast) return -1;

            int outgoing = buffer[bufferIndex++] & 0xff;
            if (bufferIndex == bufferLast) {  // rewind
                bufferIndex = 0;
                bufferLast = 0;
            }
            return outgoing;
        }
    }


    /**
     * ( begin auto-generated from Client_readChar.xml )
     * <p>
     * Returns the next byte in the buffer as a char. Returns -1 or 0xffff if
     * nothing is there.
     * <p>
     * ( end auto-generated )
     *
     * @webref client:client
     * @usage application
     * @brief Returns the next byte in the buffer as a char
     */
    public char readChar() {
        synchronized (bufferLock) {
            if (bufferIndex == bufferLast) return (char) (-1);
            return (char) read();
        }
    }


    /**
     * ( begin auto-generated from Client_readBytes.xml )
     * <p>
     * Reads a group of bytes from the buffer. The version with no parameters
     * returns a byte array of all data in the buffer. This is not efficient,
     * but is easy to use. The version with the <b>byteBuffer</b> parameter is
     * more memory and time efficient. It grabs the data in the buffer and puts
     * it into the byte array passed in and returns an int value for the number
     * of bytes read. If more bytes are available than can fit into the
     * <b>byteBuffer</b>, only those that fit are read.
     * <p>
     * ( end auto-generated )
     * <h3>Advanced</h3>
     * Return a byte array of anything that's in the serial buffer.
     * Not particularly memory/speed efficient, because it creates
     * a byte array on each read, but it's easier to use than
     * readBytes(byte b[]) (see below).
     *
     * @webref client:client
     * @usage application
     * @brief Reads everything in the buffer
     */
    public byte[] readBytes() {
        synchronized (bufferLock) {
            if (bufferIndex == bufferLast) return null;

            int length = bufferLast - bufferIndex;
            byte outgoing[] = new byte[length];
            System.arraycopy(buffer, bufferIndex, outgoing, 0, length);

            bufferIndex = 0;  // rewind
            bufferLast = 0;
            return outgoing;
        }
    }


    /**
     * <h3>Advanced</h3>
     * Return a byte array of anything that's in the serial buffer
     * up to the specified maximum number of bytes.
     * Not particularly memory/speed efficient, because it creates
     * a byte array on each read, but it's easier to use than
     * readBytes(byte b[]) (see below).
     *
     * @param max the maximum number of bytes to read
     */
    public byte[] readBytes(int max) {
        synchronized (bufferLock) {
            if (bufferIndex == bufferLast) return null;

            int length = bufferLast - bufferIndex;
            if (length > max) length = max;
            byte outgoing[] = new byte[length];
            System.arraycopy(buffer, bufferIndex, outgoing, 0, length);

            bufferIndex += length;
            if (bufferIndex == bufferLast) {
                bufferIndex = 0;  // rewind
                bufferLast = 0;
            }

            return outgoing;
        }
    }


    /**
     * <h3>Advanced</h3>
     * Grab whatever is in the serial buffer, and stuff it into a
     * byte buffer passed in by the user. This is more memory/time
     * efficient than readBytes() returning a byte[] array.
     * <p>
     * Returns an int for how many bytes were read. If more bytes
     * are available than can fit into the byte array, only those
     * that will fit are read.
     *
     * @param bytebuffer passed in byte array to be altered
     */
    public int readBytes(byte bytebuffer[]) {
        synchronized (bufferLock) {
            if (bufferIndex == bufferLast) return 0;

            int length = bufferLast - bufferIndex;
            if (length > bytebuffer.length) length = bytebuffer.length;
            System.arraycopy(buffer, bufferIndex, bytebuffer, 0, length);

            bufferIndex += length;
            if (bufferIndex == bufferLast) {
                bufferIndex = 0;  // rewind
                bufferLast = 0;
            }
            return length;
        }
    }


    /**
     * ( begin auto-generated from Client_readBytesUntil.xml )
     * <p>
     * Reads from the port into a buffer of bytes up to and including a
     * particular character. If the character isn't in the buffer, 'null' is
     * returned. The version with no <b>byteBuffer</b> parameter returns a byte
     * array of all data up to and including the <b>interesting</b> byte. This
     * is not efficient, but is easy to use. The version with the
     * <b>byteBuffer</b> parameter is more memory and time efficient. It grabs
     * the data in the buffer and puts it into the byte array passed in and
     * returns an int value for the number of bytes read. If the byte buffer is
     * not large enough, -1 is returned and an error is printed to the message
     * area. If nothing is in the buffer, 0 is returned.
     * <p>
     * ( end auto-generated )
     *
     * @param interesting character designated to mark the end of the data
     * @webref client:client
     * @usage application
     * @brief Reads from the buffer of bytes up to and including a particular character
     */
    public byte[] readBytesUntil(int interesting) {
        byte what = (byte) interesting;

        synchronized (bufferLock) {
            if (bufferIndex == bufferLast) return null;

            int found = -1;
            for (int k = bufferIndex; k < bufferLast; k++) {
                if (buffer[k] == what) {
                    found = k;
                    break;
                }
            }
            if (found == -1) return null;

            int length = found - bufferIndex + 1;
            byte outgoing[] = new byte[length];
            System.arraycopy(buffer, bufferIndex, outgoing, 0, length);

            bufferIndex += length;
            if (bufferIndex == bufferLast) {
                bufferIndex = 0; // rewind
                bufferLast = 0;
            }
            return outgoing;
        }
    }


    /**
     * <h3>Advanced</h3>
     * Reads from the serial port into a buffer of bytes until a
     * particular character. If the character isn't in the serial
     * buffer, then 'null' is returned.
     * <p>
     * If outgoing[] is not big enough, then -1 is returned,
     * and an error message is printed on the console.
     * If nothing is in the buffer, zero is returned.
     * If 'interesting' byte is not in the buffer, then 0 is returned.
     *
     * @param byteBuffer passed in byte array to be altered
     */
    public int readBytesUntil(int interesting, byte byteBuffer[]) {
        byte what = (byte) interesting;

        synchronized (bufferLock) {
            if (bufferIndex == bufferLast) return 0;

            int found = -1;
            for (int k = bufferIndex; k < bufferLast; k++) {
                if (buffer[k] == what) {
                    found = k;
                    break;
                }
            }
            if (found == -1) return 0;

            int length = found - bufferIndex + 1;
            if (length > byteBuffer.length) {
                System.err.println("readBytesUntil() byte buffer is" +
                        " too small for the " + length +
                        " bytes up to and including char " + interesting);
                return -1;
            }
            //byte outgoing[] = new byte[length];
            System.arraycopy(buffer, bufferIndex, byteBuffer, 0, length);

            bufferIndex += length;
            if (bufferIndex == bufferLast) {
                bufferIndex = 0;  // rewind
                bufferLast = 0;
            }
            return length;
        }
    }


    /**
     * ( begin auto-generated from Client_readString.xml )
     * <p>
     * Returns the all the data from the buffer as a String. This method
     * assumes the incoming characters are ASCII. If you want to transfer
     * Unicode data, first convert the String to a byte stream in the
     * representation of your choice (i.e. UTF8 or two-byte Unicode data), and
     * send it as a byte array.
     * <p>
     * ( end auto-generated )
     *
     * @webref client:client
     * @usage application
     * @brief Returns the buffer as a String
     */
    public String readString() {
        byte b[] = readBytes();
        if (b == null) return null;
        return new String(b);
    }


    /**
     * ( begin auto-generated from Client_readStringUntil.xml )
     * <p>
     * Combination of <b>readBytesUntil()</b> and <b>readString()</b>. Returns
     * <b>null</b> if it doesn't find what you're looking for.
     * <p>
     * ( end auto-generated )
     * <h3>Advanced</h3>
     * <p/>
     * If you want to move Unicode data, you can first convert the
     * String to a byte stream in the representation of your choice
     * (i.e. UTF8 or two-byte Unicode data), and send it as a byte array.
     *
     * @param interesting character designated to mark the end of the data
     * @webref client:client
     * @usage application
     * @brief Returns the buffer as a String up to and including a particular character
     */
    public String readStringUntil(int interesting) {
        byte b[] = readBytesUntil(interesting);
        if (b == null) return null;
        return new String(b);
    }





    /**
     * Handle disconnect due to an Exception being thrown.
     */
  /*
    protected void disconnect(Exception e) {
    dispose();
    if (e != null) {
    e.printStackTrace();
    }
    }
  */


    /**
     * General error reporting, all corralled here just in case
     * I think of something slightly more intelligent to do.
     */
    //public void errorMessage(String where, Exception e) {
    //parent.die("Error inside me.plopez.survivalgame.network.Client." + where + "()", e);
    //e.printStackTrace(System.err);
    //}
}
