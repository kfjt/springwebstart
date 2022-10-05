package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.imageio.ImageIO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** AI OCR Contoroller. */
@RestController
@RequestMapping("api")
public class ApiController {
  private Set<String> ls(Path convertDirectory, String id) throws IOException {
    try (Stream<Path> stream = Files.list(convertDirectory)) {
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
    String imagePath =
        DemoApplication.CONVERT_DIRECTORY.resolve(Paths.get(id + "-1.png")).toString();
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
    String pdffile = DemoApplication.UPLOAD_DIRECTORY.resolve(Paths.get(id + ".pdf")).toString();

    ProcessBuilder pb =
        new ProcessBuilder(
            "pdftoppm", "-png", pdffile, DemoApplication.CONVERT_DIRECTORY + "/" + id);
    Process ps = pb.start();
    ps.waitFor();
    ps.destroy();

    return ls(DemoApplication.CONVERT_DIRECTORY, id).toString();
  }

  /**
   * GET /api/subimage/{id}.
   *
   * @return dummy
   * @throws IOException ImageIO.read | ImageIO.write
   */
  @GetMapping("subimage/{id}")
  public String subimage(@PathVariable("id") String id) throws IOException {
    File pngfile = DemoApplication.CONVERT_DIRECTORY.resolve(Paths.get(id + "-1.png")).toFile();
    int x = 10;
    int y = 20;
    int w = 130;
    int h = 140;
    BufferedImage subimage = ImageIO.read(pngfile).getSubimage(x, y, w, h);
    String subfilename = id + "." + x + "." + y + "." + w + "." + h + ".png";
    File subfile = DemoApplication.SUBIMAGE_DIRECTORY.resolve(Paths.get(subfilename)).toFile();
    ImageIO.write(subimage, "png", subfile);
    return subfilename;
  }
}
