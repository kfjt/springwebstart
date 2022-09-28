package com.example.demo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** DemoApplication. */
@SpringBootApplication
public class DemoApplication {
  public static final String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/uploads";
  public static final String CONVERT_DIRECTORY = System.getProperty("user.dir") + "/converts";
  public static final String SUBIMAGE_DIRECTORY = System.getProperty("user.dir") + "/subimage";

  private static void mkdir(String dir) throws IOException {
    Path path = Paths.get(dir);
    if (!Files.exists(path)) {
      Files.createDirectory(path);
    }
  }

  public static void main(String[] args) throws IOException {
    for (String dir : Arrays.asList(UPLOAD_DIRECTORY, CONVERT_DIRECTORY, SUBIMAGE_DIRECTORY)) {
      mkdir(dir);
    }
    SpringApplication.run(DemoApplication.class, args);
  }
}
