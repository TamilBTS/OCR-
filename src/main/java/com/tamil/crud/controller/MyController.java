package com.tamil.crud.controller;

import java.awt.Image;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;


import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

@Controller
public class MyController {

	@RequestMapping("/")
	public String index() {
		return "index";
	}

	@PostMapping("/ocr")
	public String ocrRead(@RequestParam("img") MultipartFile file) {
		// if(!bindingResult.hasErrors()) {
		String fileName = file.getOriginalFilename();
		System.out.println(fileName);
		try {
			//storing the file in project folder
			ITesseract image = new Tesseract();
			FileOutputStream fos = new FileOutputStream("src/main/resources/static/myImages/" + fileName);
			fos.write(file.getBytes());
			fos.close();
			
			//Extracting data using Tesseract
			File myFile = new File("src/main/resources/static/myImages/" + fileName);
			image.setDatapath("D:\\tessdata-4.0.0");
			image.setLanguage("eng");
			String res = image.doOCR(myFile);
			System.out.println(res);
			
			//creating a text file in java
			BufferedWriter writer = new BufferedWriter(new FileWriter("file.txt"));
			writer.write(res);
			writer.close();

		} catch (IOException | TesseractException e) {
			e.printStackTrace();
		}
		// }
		return "redirect:/download";
	}
	@RequestMapping("/download")
	public ResponseEntity<InputStreamResource> download() {
		
		try {
			File file = new File("file.txt");
	        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

	        return ResponseEntity.ok()
	                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
	                .contentType(MediaType.APPLICATION_OCTET_STREAM)
	                .contentLength(file.length())
	                .body(resource);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
