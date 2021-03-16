/*
 * Copyright 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.zxing.pdf417;

import com.google.zxing.*;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.pdf417.decoder.ImageUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Collectors;

public final class PDF417BadImageTestCase {

  private static final PDF417Reader barcodeReader = new PDF417Reader();

  private static Result decode(BinaryBitmap source) throws FormatException, ChecksumException, NotFoundException {
    Map<DecodeHintType, Object> hints = new EnumMap<>(DecodeHintType.class);
    hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
    return barcodeReader.decode(source, hints);
  }

  private static Result process(URL imgUrl) throws FormatException, ChecksumException, NotFoundException, IOException {
    BufferedImage img = ImageIO.read(imgUrl);
    LuminanceSource source = new BufferedImageLuminanceSource(img);
    BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source), img);
    return decode(bitmap);
  }

  public static void main(String[] args) throws IOException {
    Path basePath = Paths.get("/tmp/barcodes");
    for (Path path : Files.walk(basePath).collect(Collectors.toList())) {
      String fileName = path.getFileName().toString();
      if (fileName.endsWith(".png") || fileName.endsWith(".jpg")) {
        if (fileName.equals("02_frame344.jpg")) {
          BufferedImage img = ImageIO.read(path.toFile());
          System.out.println("--------------");
          System.out.println(path);
          System.out.println("--------------");
          try {
            System.out.println(process(path.toUri().toURL()).getText());
          } catch (Throwable ex) {
            ex.printStackTrace();
          }
          System.out.println("--------------");
        }
      }
    }
  }
}
