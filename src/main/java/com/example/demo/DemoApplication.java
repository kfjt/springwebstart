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
  private static final Path WORK_DIR = Paths.get(System.getProperty("user.dir"), "workdir");
  public static final Path UPLOAD_DIRECTORY = WORK_DIR.resolve(Paths.get("uploads"));
  public static final Path CONVERT_DIRECTORY = WORK_DIR.resolve(Paths.get("converts"));
  public static final Path SUBIMAGE_DIRECTORY = WORK_DIR.resolve(Paths.get("subimage"));

  /**
   * mkdir.
   *
   * @param path path
   * @throws IOException Files.createDirectory
   */
  private static void mkdir(Path path) throws IOException {
    if (!Files.exists(path)) {
      Files.createDirectory(path);
    }
  }

  /**
   * main.
   *
   * @param args args
   * @throws IOException mkdir
   */
  public static void main(String[] args) throws IOException {
    for (Path dir :
        Arrays.asList(WORK_DIR, UPLOAD_DIRECTORY, CONVERT_DIRECTORY, SUBIMAGE_DIRECTORY)) {
      mkdir(dir);
    }
    SpringApplication.run(DemoApplication.class, args);
  }
}
