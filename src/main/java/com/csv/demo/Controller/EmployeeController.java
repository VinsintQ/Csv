package com.csv.demo.Controller;



import com.csv.demo.Service.FileProcessor;
import com.csv.demo.Service.FileProcessor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class EmployeeController {

    private FileProcessor fileProcessor;
    public EmployeeController(FileProcessor fileProcessor){
        this.fileProcessor=fileProcessor;
    }
    @PostMapping("/")
    public ResponseEntity<byte[]> getUpdatedList(MultipartFile file) throws Exception{

        byte[] updatedFile = fileProcessor.processCSV(file);


        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"updated.csv\"")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(updatedFile);
    }
}
