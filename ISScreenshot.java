package positronws;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import javax.swing.JComponent;

public class ISScreenshot {

    public static void saveComponentAsPng(JComponent component, String filePath) {
        Dimension size = component.getSize();
        int width = size.width;
        int height = size.height;

        if (width <= 0 || height <= 0) {
            System.err.println("Component must be visible and laid out with a valid size.");
            return;
        }

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        
        Graphics g = image.getGraphics();
        component.paint(g);
        g.dispose();
        
        int[] pixels = new int[width * height];
        PixelGrabber pg = new PixelGrabber(image, 0, 0, width, height, pixels, 0, width);
        try {
            if (!pg.grabPixels()) {
                System.err.println("Failed to grab pixels from the component.");
                return;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            return;
        }

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(filePath);
            writePng(fos, pixels, width, height);
            System.out.println("Screenshot successfully saved to: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try { fos.close(); } catch (IOException ignored) {}
            }
        }
    }

    private static void writePng(FileOutputStream out, int[] pixels, int width, int height) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        
        dos.write(new byte[]{(byte)137, 80, 78, 71, 13, 10, 26, 10}); //png signature

        // IHDR Chunk
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        DataOutputStream chunk = new DataOutputStream(baos);
        chunk.writeInt(width);
        chunk.writeInt(height);
        chunk.writeByte(8);  // 8 bits per col channel
        chunk.writeByte(6);  // 6 bits col ype
        chunk.writeByte(0);  // deflate compression
        chunk.writeByte(0);  // no filters
        chunk.writeByte(0);  // no interlacing
        writeChunk(dos, "IHDR", baos.toByteArray());

        baos.reset(); //compressed pixel data
        DeflaterOutputStream dfos = new DeflaterOutputStream(baos, new Deflater(Deflater.BEST_COMPRESSION));
        
        for (int y = 0; y < height; y++) {
            dfos.write(0);
            for (int x = 0; x < width; x++) {
                int pixel = pixels[y * width + x];
                int a = (pixel >> 24) & 0xFF;
                int r = (pixel >> 16) & 0xFF;
                int g = (pixel >> 8) & 0xFF;
                int b = pixel & 0xFF;
                dfos.write(r);
                dfos.write(g);
                dfos.write(b);
                dfos.write(a);
            }
        }
        dfos.finish();
        writeChunk(dos, "IDAT", baos.toByteArray());

        writeChunk(dos, "IEND", new byte[0]);
        dos.flush();
    }

    private static void writeChunk(DataOutputStream dos, String type, byte[] data) throws IOException {
        dos.writeInt(data.length);
        byte[] typeBytes = type.getBytes("US-ASCII");
        dos.write(typeBytes);
        dos.write(data);
        
        java.util.zip.CRC32 crc = new java.util.zip.CRC32();
        crc.update(typeBytes);
        crc.update(data);
        dos.writeInt((int) crc.getValue());
    }
}
