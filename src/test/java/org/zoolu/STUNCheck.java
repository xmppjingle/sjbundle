package org.zoolu;

import junit.framework.TestCase;

import java.io.IOException;
import java.net.*;

public class STUNCheck extends TestCase{

    final static byte BINDING_REQUEST_ID = 0x0001;
    final static int MAPPED_ADDRESS = 0x0001;
    final static byte CHANGE_REQUEST_NO_CHANGE[] = {0, 3, 0, 4, 0, 0, 0, 0};


    private static byte[] getHeader(final int contentLenght) {

        final byte header[] = new byte[20];

        final long random = System.nanoTime();

        header[0] = 0;
        header[1] = BINDING_REQUEST_ID;
        header[2] = 0;
        header[3] = (byte) contentLenght;
        header[4] = (byte) (random % 9);
        header[5] = (byte) (random % 8);
        header[6] = (byte) (random % 7);
        header[7] = (byte) (random % 6);

        return header;
    }

    public void testSTUN(){
        try {
            DatagramSocket clientSocket = new DatagramSocket();
            final DatagramPacket p = createSTUNChangeRequest();
            p.setAddress(InetAddress.getByName("stun.xten.net"));
            p.setPort(30000);
            clientSocket.send(p);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static DatagramPacket createSTUNChangeRequest() {

        final byte header[] = getHeader(CHANGE_REQUEST_NO_CHANGE.length);

        final byte data[] = new byte[header.length + CHANGE_REQUEST_NO_CHANGE.length];

        System.arraycopy(header, 0, data, 0, header.length);
        System.arraycopy(CHANGE_REQUEST_NO_CHANGE, 0, data, header.length, CHANGE_REQUEST_NO_CHANGE.length);

        return new DatagramPacket(data, data.length);
    }

    public static Response parseResponse(byte[] data) {
        byte[] lengthArray = new byte[2];
        System.arraycopy(data, 2, lengthArray, 0, 2);
        int length = unsignedShortToInt(lengthArray);
        byte[] cuttedData;
        int offset = 20;

        final Response r = new Response();

        while (length > 0) {
            cuttedData = new byte[length];
            System.arraycopy(data, offset, cuttedData, 0, length);
            Header h = parseHeader(cuttedData);

            if (h.getType() == MAPPED_ADDRESS) {
                r.setMapped(h);
            }

            length -= h.getLength();
            offset += h.getLength();
        }

        return r;
    }

    private static Header parseHeader(byte[] data) {
        byte[] typeArray = new byte[2];
        System.arraycopy(data, 0, typeArray, 0, 2);
        int type = unsignedShortToInt(typeArray);
        byte[] lengthArray = new byte[2];
        System.arraycopy(data, 2, lengthArray, 0, 2);
        int lengthValue = unsignedShortToInt(lengthArray);
        byte[] valueArray = new byte[lengthValue];
        System.arraycopy(data, 4, valueArray, 0, lengthValue);
        if (data.length >= 8) {
            int family = unsignedByteToInt(valueArray[1]);
            if (family == 1) {
                byte[] portArray = new byte[2];
                System.arraycopy(valueArray, 2, portArray, 0, 2);
                int port = unsignedShortToInt(portArray);
                int firstOctet = unsignedByteToInt(valueArray[4]);
                int secondOctet = unsignedByteToInt(valueArray[5]);
                int thirdOctet = unsignedByteToInt(valueArray[6]);
                int fourthOctet = unsignedByteToInt(valueArray[7]);
                final StringBuilder ip = new StringBuilder().append(firstOctet).append(".").append(secondOctet).append(".").append(thirdOctet).append(".").append(fourthOctet);
                return new Header(ip.toString(), port, type, lengthValue + 4);
            }
        }
        return new Header(null, -1, -1, lengthValue + 4);
    }

    public static int unsignedShortToInt(final byte[] b) {
        int a = b[0] & 0xFF;
        int aa = b[1] & 0xFF;
        return ((a << 8) + aa);
    }

    public static int unsignedByteToInt(byte b) {
        return (int) b & 0xFF;
    }

    public static class Response {

        Header mapped=null;

        public Header getMapped() {
            return mapped;
        }

        public void setMapped(Header mapped) {
            this.mapped = mapped;
        }
    }

    public static class Header {
        String ip;
        int port;
        int type;
        int length;

        public Header(String ip, int port, int type, int length) {
            this.ip = ip;
            this.port = port;
            this.type = type;
            this.length = length;
        }

        public int getType() {
            return type;
        }

        public String getIp() {
            return ip;
        }

        public int getPort() {
            return port;
        }

        public int getLength() {
            return length;
        }
    }
}