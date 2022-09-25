package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** AI OCR Contoroller. */
@RestController
@RequestMapping("api")
public class AiocrController {
  /**
   * ocr from imagePath.
   *
   * @param imagePath jpeg or png
   * @return ArrayNode
   * @throws IOException when ProcessBuilder.start()
   * @throws InterruptedException when ProcessBuilder.waitFor()
   */
  public static ArrayNode ocrFrom(String imagePath) throws IOException, InterruptedException {
    ObjectMapper mapper = new ObjectMapper();
    ArrayNode node = mapper.createArrayNode();
    String cmd = "easyocr -l ja en --output_format json --verbose '' -f ";
    ProcessBuilder pb = new ProcessBuilder((cmd + imagePath).split(" "));
    Process ps = pb.start();
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(ps.getInputStream()));
        BufferedReader error = new BufferedReader(new InputStreamReader(ps.getErrorStream())); ) {
      String inputLine;
      while ((inputLine = reader.readLine()) != null) {
        node.add(mapper.readTree(inputLine));
      }
    }
    ps.waitFor();
    ps.destroy();
    return node;
  }

  /**
   * ocr text from imagePath.
   *
   * @param imagePath jpeg or png
   * @return ArrayNode
   * @throws IOException when ProcessBuilder.start()
   * @throws InterruptedException when ProcessBuilder.waitFor()
   */
  public static String ocrTextFrom(String imagePath) throws IOException, InterruptedException {
    return String.join("\n", ocrFrom(imagePath).findValuesAsText("text"));
  }

  /**
   * GET /api/aiocr.
   *
   * @return ocr string
   * @throws IOException when ProcessBuilder.start()
   * @throws InterruptedException when ProcessBuilder.waitFor()
   */
  @GetMapping("aiocr")
  public String aiocr() throws IOException, InterruptedException {
    String imagePath = "/EasyOCR/examples/japanese.jpg";
    // return ocrTextFrom(imagePath);
    ArrayNode node = ocrFrom(imagePath);
    ObjectMapper mapper = new ObjectMapper();
    return mapper.writeValueAsString(node);
  }
}
