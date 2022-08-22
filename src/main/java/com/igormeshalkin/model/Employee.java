package com.igormeshalkin.model;

public class Employee extends AbstractBaseModel {
    private String firstName;
    private String lastName;
    private Department department;
    private Post post;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "Id=" + super.getId() +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", department='" + department.getName() + '\'' +
                ", post='" + post.getName() + '\'' +
                ", salary=" + post.getSalary() +
                '}';
    }
}
