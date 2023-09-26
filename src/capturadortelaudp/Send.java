package capturadortelaudp;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 *
 * @author Douglas
 * @author Bernardo
 * 
 */
public class Send {

    public static void main(String[] args) {
        new Send().sendScreenshot();
    }

    public void sendScreenshot() {
        while (true) {
            try {
                Robot r = new Robot();
                DatagramSocket clientSocket = new DatagramSocket();
                InetAddress IpServer = InetAddress.getByName("127.0.0.1");

                BufferedImage img = r.createScreenCapture(new Rectangle(Util.RESOLUTION_X, Util.RESOLUTION_Y)); 

                byte buffer[] = new byte[Util.BLOCK_X * Util.BLOCK_Y * 4 + 4 + 4];

                for (int posX = 0; posX < (Util.RESOLUTION_X - Util.BLOCK_X); posX = posX + Util.BLOCK_X) {
                    for (int posY = 0; posY < (Util.RESOLUTION_Y - Util.BLOCK_Y); posY = posY + Util.BLOCK_Y) {

                        int aux = 0;
                        for (int y = 0; y < Util.BLOCK_Y; y++) {
                            for (int x = 0; x < Util.BLOCK_X; x++) {

                                int cor = img.getRGB(posX + x, posY + y); 

                                byte auxBuffer[] = Util.integerToBytes(cor);

                                for (int j = 0; j < auxBuffer.length; j++) {
                                    buffer[aux++] = auxBuffer[j];
                                }
                            }
                        }

                        byte auxBufferPosX[] = Util.integerToBytes(posX);
                        for (int j = 0; j < auxBufferPosX.length; j++) {
                            buffer[aux++] = auxBufferPosX[j];
                        }

                        byte auxBufferPosY[] = Util.integerToBytes(posY);
                        for (int j = 0; j < auxBufferPosY.length; j++) {
                            buffer[aux++] = auxBufferPosY[j];
                        }

                        DatagramPacket sendPacket = new DatagramPacket(buffer, buffer.length, IpServer, 8000);
                        clientSocket.send(sendPacket);

                        //System.out.println("posX = " + posX + " posY = " + posY + " Enviou " + buffer.length);
                        System.out.println("Send Screenshot...");                        
                    }
                }

                Thread.sleep(10);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
 
}