package com.example.demo.controller;



import com.example.demo.dto.FileInfo;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.HandshakeResponse;
import java.io.*;
import java.util.Date;

@RestController
@RequestMapping("/file")
public class FileController {

    String folder="C:\\Users\\lyang40\\IdeaProjects\\springsecurity\\security-demo\\src\\main\\java\\com\\example\\demo\\controller";

    /**
     * 文件上传
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping
    public FileInfo upload(MultipartFile file) throws IOException {

        System.out.println(file.getName());
        System.out.println(file.getOriginalFilename());
        System.out.println(file.getSize());


        File localFile = new File(folder, new Date().getTime() + ".txt");
        file.transferTo(localFile);
        return new FileInfo(localFile.getAbsolutePath());

    }

    /**
     * 文件下载
     */

    @GetMapping("/{id}")
    public void download(HttpServletRequest request, HttpServletResponse response, @PathVariable String id) throws IOException {

        //jdk会自动关流
          try (InputStream inputStream=new FileInputStream(new File(folder,id+".txt"));
                OutputStream outputStream=response.getOutputStream();){
              response.setContentType("application/x-download");
              response.addHeader("Content-Disposition","attachment;filename=test.txt");

              IOUtils.copy(inputStream, outputStream);
              outputStream.flush();
          }
    }
}
