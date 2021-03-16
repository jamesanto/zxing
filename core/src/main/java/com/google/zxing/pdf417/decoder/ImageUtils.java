package com.google.zxing.pdf417.decoder;

import com.google.zxing.ResultPoint;
import com.google.zxing.common.BitMatrix;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;

public class ImageUtils {
  private static final byte BYTE1 = 1;
  private static final byte BYTE0 = 0;

  public static BufferedImage clone(BufferedImage img) {
    ColorModel cm = img.getColorModel();
    boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
    WritableRaster raster = img.copyData(null);
    return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
  }

  public static void drawLine(Graphics2D graph, ResultPoint point1, ResultPoint point2) {
    graph.drawLine((int) point1.getX(), (int) point1.getY(), (int) point2.getX(), (int) point2.getY());
  }

  public static BufferedImage drawBound(
    BufferedImage img,
    ResultPoint topLeft,
    ResultPoint bottomLeft,
    ResultPoint topRight,
    ResultPoint bottomRight
  ) {
    return drawBound(
      img,
      topLeft,
      bottomLeft,
      topRight,
      bottomRight,
      3,
      Color.RED
    );
  }

  public static BufferedImage drawBound(
    BufferedImage img,
    ResultPoint topLeft,
    ResultPoint bottomLeft,
    ResultPoint topRight,
    ResultPoint bottomRight,
    int thickness,
    Color color
  ) {
    Graphics2D graph = img.createGraphics();
    graph.setStroke(new BasicStroke(thickness));
    graph.setColor(color);
    drawLine(graph, topLeft, topRight);
    drawLine(graph, topRight, bottomRight);
    drawLine(graph, bottomRight, bottomLeft);
    drawLine(graph, bottomLeft, topLeft);
    graph.dispose();
    return img;
  }

  public static void showImg(BufferedImage img) {
    ImageIcon icon = new ImageIcon(img);
    JFrame frame = new JFrame();
    frame.setLayout(new FlowLayout());
    frame.setSize(img.getWidth() + 30, img.getHeight() + 30);
    JLabel lbl = new JLabel();
    lbl.setIcon(icon);
    frame.add(lbl);
    frame.setVisible(true);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

  public static void showImg(BitMatrix bm) {
    BufferedImage img = new BufferedImage(bm.getWidth(), bm.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
    byte[] newData = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
    int counter = 0;
    for (int x = 0; x < bm.getWidth(); x++) {
      for (int y = 0; y < bm.getHeight(); y++) {
        newData[counter] = bm.get(x, y) ? BYTE1 : BYTE0;
        counter++;
      }
    }
    showImg(img);
  }
}
