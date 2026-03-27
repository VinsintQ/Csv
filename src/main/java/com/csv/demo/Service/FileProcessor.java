package com.csv.demo.Service;


import com.csv.demo.model.Employee;
import com.csv.demo.model.Role;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class FileProcessor {
    List<Employee> employees = new ArrayList<>();
    public byte[] processCSV(MultipartFile file)throws Exception{

        try(
                BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))

        ){
            reader.lines().skip(1).forEach(line->{
                String[] parts = line.split(",");


                int offset = parts.length == 6 ? 1 : 0;

                Role role = switch (parts[offset + 3].trim().toLowerCase()){
                    case "director" -> Role.DIRECTOR;
                    case "manager"  -> Role.MANAGER;
                    case "employee" -> Role.EMPLOYEE;
                    default -> throw new IllegalArgumentException("Unknown role: " + parts[offset + 3]);
                };

                double completion = Double.parseDouble(parts[offset + 4].trim());

                if (completion <= 1.0) completion *= 100;

                employees.add(new Employee(
                        parts[offset].trim(),
                        Double.parseDouble(parts[offset + 1].trim()),
                        LocalDate.parse(parts[offset + 2].trim()),
                        role,
                        completion));
            });

        }
        List<Employee>updated = new ArrayList<>();
        ExecutorService pool = Executors.newFixedThreadPool(4);
        for (Employee employee : employees) {
            pool.submit(() -> employee.bonus());
        }

        pool.shutdown();
        pool.awaitTermination(1, TimeUnit.MINUTES);

        StringBuilder csv = new StringBuilder();
        csv.append("name,salary,joinedDate,role,projectCompletionPercent\n");

        for(Employee employee:employees){
            csv.append(employee.getName()).append(",").
                    append(employee.getSalary()).append(",").
                    append(employee.getJoinedDate()).append(",").
                    append(employee.getRole()).append(",").
                    append(employee.getProjectCompletionPercent()).
                    append("\n");
            ;
        }
        employees.clear();
        return csv.toString().getBytes(StandardCharsets.UTF_8);
    }
}
