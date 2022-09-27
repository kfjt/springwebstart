package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** AI OCR Contoroller. */
@RestController
@RequestMapping("api")
public class AiocrController {
  public static final String CONVERT_DIRECTORY = System.getProperty("user.dir") + "/converts";

  private Set<String> ls(String dir, String id) throws IOException {
    try (Stream<Path> stream = Files.list(Paths.get(dir))) {
      return stream
          .filter(file -> !Files.isDirectory(file))
          .map(Path::getFileName)
          .map(Path::toString)
          .filter(filename -> filename.startsWith(id))
          .collect(Collectors.toSet());
    }
  }

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
    String cmd = "easyocr -l ja en --canvas_size 2560 --output_format json --verbose '' -f ";
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
   * GET /api/aiocr/{id}.
   *
   * @return ocr string
   * @throws IOException when ProcessBuilder.start()
   * @throws InterruptedException when ProcessBuilder.waitFor()
   */
  @GetMapping("aiocr/{id}")
  public String aiocr(@PathVariable("id") String id) throws IOException, InterruptedException {
    String imagePath = Paths.get(CONVERT_DIRECTORY, id + "-1.png").toString();
    // return ocrTextFrom(imagePath);
    ArrayNode node = ocrFrom(imagePath);
    ObjectMapper mapper = new ObjectMapper();
    return mapper.writeValueAsString(node);
  }

  /**
   * GET /api/pdf2img/{id}.
   *
   * @return path/to/img
   * @throws IOException ProcessBuilder.start
   * @throws InterruptedException ProcessBuilder.waitFor
   */
  @GetMapping("pdf2img/{id}")
  public String pdf2img(@PathVariable("id") String id) throws IOException, InterruptedException {
    String pdffile = Paths.get(PdfController.UPLOAD_DIRECTORY, id + ".pdf").toString();

    ProcessBuilder pb =
        new ProcessBuilder("pdftoppm", "-png", pdffile, CONVERT_DIRECTORY + "/" + id);
    Process ps = pb.start();
    ps.waitFor();
    ps.destroy();

    return ls(CONVERT_DIRECTORY, id).toString();
  }
}
