package com.csv.demo.Controller;



import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class EmployeeController {

    private Processor Processor;
    public EmployeeController(Processor Processor){
        this.Processor=Processor;
    }
    @PostMapping("/")
    public ResponseEntity<byte[]> getUpdatedList(MultipartFile file) throws Exception{

        byte[] updatedFile = Processor.process(file);


        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"updated.csv\"")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(updatedFile);
    }
}
