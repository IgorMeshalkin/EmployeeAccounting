package com.igormeshalkin.model;

public class Post extends AbstractBaseModel {
    private String name;
    private Double salary;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + super.getId() +
                ", name='" + name + '\'' +
                ", salary=" + salary +
                '}';
    }
}
