package com.csv.demo.model;


import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Employee {

    private final String name;
    private double salary;

    public String getName() {
        return name;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public LocalDate getJoinedDate() {
        return joinedDate;
    }

    public Role getRole() {
        return role;
    }

    public double getProjectCompletionPercent() {
        return projectCompletionPercent;
    }

    public Lock getSalaryLock() {
        return salaryLock;
    }

    private final LocalDate joinedDate;
    private final Role role;
    private final double projectCompletionPercent;


    private final Lock salaryLock = new ReentrantLock();

    public Employee(String name,
                    double salary,
                    LocalDate joinedDate,
                    Role role,
                    double projectCompletionPercent) {
        this.name = name;
        this.salary = salary;
        this.joinedDate = joinedDate;
        this.role = role;
        this.projectCompletionPercent = projectCompletionPercent;
    }
    public boolean isEligible(){
        return this.projectCompletionPercent>=60&& getYearsWorked()>=1;
    }
    public void bonus(){

        salaryLock.lock();
        try{
            if(this.isEligible()){
                double yearsBonus =Math.floor( getYearsWorked())*0.02;

                double roleBonus = switch (role){
                    case DIRECTOR -> 0.5;
                    case MANAGER -> 0.2;
                    case EMPLOYEE -> 0.1;
                };

                double totalBonus = yearsBonus+roleBonus;
                salary+=salary*totalBonus;

            }
        }
        finally {
            salaryLock.unlock();
        }

    }
    public double getYearsWorked (){
        double years = ChronoUnit.YEARS.between(joinedDate,LocalDate.now());
        return years;
    }

}
