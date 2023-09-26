package capturadortelaudp;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 *
 * @author Douglas
 * @author Bernardo
 * 
 */
public class Receive extends JFrame implements Runnable {

    DatagramSocket socket;

    private BufferedImage bi = new BufferedImage(Util.RESOLUTION_X, Util.RESOLUTION_Y, BufferedImage.TYPE_INT_ARGB);
    private byte buffer[] = new byte[Util.BLOCK_X * Util.BLOCK_Y * 4 + 4 + 4]; // pegar R, G, B, e alfa + 4 pois quero informar o posX e + 4 posY (posicão sorteada)

    public static void main(String[] args) {
        new Receive();
    } 
    
    public Receive() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setVisible(true);
        setTitle("Screen share");
        setSize(1024, 768);
        
        try {
            socket = new DatagramSocket(8000);

            Thread t = new Thread(this);
            t.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
        
    @Override
    public void run() {
        while (true) {
            try {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                
                System.out.println("Receiving...");

                byte auxPosX[] = new byte[4];
                for (int i = 0; i < 4; i++) {
                    auxPosX[i] = buffer[buffer.length - 8 + i]; 
                }
                int posX = Util.bytesToInteger(auxPosX);

                byte auxPosY[] = new byte[4];
                for (int i = 0; i < 4; i++) {
                    auxPosY[i] = buffer[buffer.length - 4 + i]; 
                }
                int posY = Util.bytesToInteger(auxPosY);

                int aux = 0;
                for (int y = 0; y < Util.BLOCK_Y; y++) {
                    for (int x = 0; x < Util.BLOCK_X; x++) {

                        byte auxCor[] = new byte[4];
                        for (int i = 0; i < 4; i++) {
                            auxCor[i] = buffer[aux++]; 
                        }
                        int cor = Util.bytesToInteger(auxCor);

                        bi.setRGB(posX + x, posY + y, cor);
                        
                    }
                }

                //Thread.sleep(10);
                repaint();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    
    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.drawImage(bi, 0, 0, this);
    }
    
}