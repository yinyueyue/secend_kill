package com.yue.dmall;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author: Yueyue.Yin
 * @description:
 * @date:created in 2022/9/2 17:11
 * @modified by:
 */
@Slf4j
@RestController
public class HealthCheck {

  @GetMapping("/health")
  public String health() {
    log.info("I am healthy");
    return "ok";
  }

  String fileDirectory = "D://files";

  @PostMapping("/upload")
  public String uploadFile(MultipartFile file) throws IOException {
    String fileName = UUID.randomUUID().toString();
    String originalFilename = file.getOriginalFilename();
    int index = originalFilename.lastIndexOf(".");
    if (index != -1) {
      fileName = fileName + originalFilename.substring(index);
    }
    File folder = new File(
        fileDirectory + "/" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
    if (!folder.isDirectory()) {
      folder.mkdirs();
    }

    Files.copy(file.getInputStream(), new File(folder, fileName).toPath(),
        StandardCopyOption.REPLACE_EXISTING);

    return fileName;
  }

  @GetMapping("/review/{fileName}")
  public void review(@PathVariable("fileName") String fileName, HttpServletResponse response)
      throws IOException {
    File file = new File(
        fileDirectory + "/" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "/"
            + fileName);

    response.setHeader("Content-Disposition", "inline;fileName=" + URLEncoder.encode(fileName,
        StandardCharsets.UTF_8));
    response.addHeader("Content-length", file.length() + "");
    OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
    FileUtil.writeToStream(file, outputStream);
  }

  @GetMapping("/download/{fileName}")
  public void download(@PathVariable("fileName") String fileName, HttpServletResponse response)
      throws IOException {
    File file = new File(
        fileDirectory + "/" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "/"
            + fileName);
    response.setContentType("application/octet-stream");
    response.setHeader("content-type", "application/octet-stream");
    response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(fileName,
        StandardCharsets.UTF_8));
    response.addHeader("Content-length", file.length() + "");
    OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
    FileUtil.writeToStream(file, outputStream);
  }

}
