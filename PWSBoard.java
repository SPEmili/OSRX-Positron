package positronws;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

public class PWSBoard extends JPanel implements MouseListener, MouseMotionListener {
    private static final long serialVersionUID = 1L;
	private BufferedImage canvas;
    private Graphics2D g2dCanvas;
    private int lastX, lastY;

    private float lineThickness = 3.0f;
    private Color currentColor = Color.WHITE;

    public PWSBoard() {
        setBackground(Color.BLACK);
        
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    private void initCanvas(int w, int h) {
        if (canvas == null || canvas.getWidth() < w || canvas.getHeight() < h) {
            int newW = Math.max(canvas != null ? canvas.getWidth() : 1, w);
            int newH = Math.max(canvas != null ? canvas.getHeight() : 1, h);

            BufferedImage newCanvas = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = newCanvas.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, newW, newH);

            if (canvas != null) { //resize canvas deletion prevention
                g.drawImage(canvas, 0, 0, null);
            }
            g.dispose();
            
            canvas = newCanvas;
            g2dCanvas = canvas.createGraphics();
            g2dCanvas.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        initCanvas(getWidth(), getHeight());
        if (canvas != null) {
            g.drawImage(canvas, 0, 0, this);
        }
    }

    
    public static BufferedImage loadBufferedImage(String filePath) {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image rawImage = toolkit.getImage(filePath);

        JFrame dummyComponent = new JFrame(); 
        MediaTracker tracker = new MediaTracker(dummyComponent);
        tracker.addImage(rawImage, 0);

        try {
            tracker.waitForID(0); 
        } catch (InterruptedException e) {
            System.err.println("Image loading interrupted.");
            return null;
        }

        if (tracker.isErrorID(0)) {
            System.err.println("Error: Toolkit could not decode the image file: " + filePath);
            return null;
        }

        int width = rawImage.getWidth(null);
        int height = rawImage.getHeight(null);

        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics g = bufferedImage.getGraphics();
        g.drawImage(rawImage, 0, 0, null);
        g.dispose();

        return bufferedImage;
    }
    
    
    public void loadPng(String dir) {
    	BufferedImage img = loadBufferedImage(dir);
        if (img == null) {
            return;
        }

        int imgW = img.getWidth();
        int imgH = img.getHeight();

        int targetW = Math.max(getWidth(), imgW);
        int targetH = Math.max(getHeight(), imgH);

        if (canvas == null || canvas.getWidth() < targetW || canvas.getHeight() < targetH) {
            int newW = Math.max(canvas != null ? canvas.getWidth() : 0, targetW);
            int newH = Math.max(canvas != null ? canvas.getHeight() : 0, targetH);

            BufferedImage newCanvas = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = newCanvas.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, newW, newH);

            if (canvas != null) {
                g.drawImage(canvas, 0, 0, null);
            }
            g.dispose();

            canvas = newCanvas;
            g2dCanvas = canvas.createGraphics();
            g2dCanvas.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            setPreferredSize(new Dimension(newW, newH));
            revalidate();
        }

        if (g2dCanvas != null) {
            g2dCanvas.drawImage(img, 0, 0, null);
            repaint();
        }
    }

    public void mousePressed(MouseEvent e) {
        lastX = e.getX();
        lastY = e.getY();
    }

    public void mouseReleased(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}


    public void mouseDragged(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        if (g2dCanvas != null) {
            g2dCanvas.setColor(currentColor);
            g2dCanvas.setStroke(new BasicStroke(lineThickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2dCanvas.drawLine(lastX, lastY, x, y);
        }

        lastX = x;
        lastY = y;
        
        repaint();
    }

    public void mouseMoved(MouseEvent e) {}

    public void setLineThickness(float thickness) {
        if (thickness < 0.5f) {
            this.lineThickness = 0.5f;
        } else {
            this.lineThickness = thickness;
        }
    }

    public void setLineColor(Color color) {
        if (color != null) {
            this.currentColor = color;
        }
    }

    public void clearCanvas() {
        if (g2dCanvas != null && canvas != null) {
            g2dCanvas.setColor(Color.BLACK);
           
            g2dCanvas.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
            repaint();
        }
    }
}