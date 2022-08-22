package com.igormeshalkin.dao;

import com.igormeshalkin.model.Department;
import com.igormeshalkin.model.Employee;
import com.igormeshalkin.model.Post;
import com.igormeshalkin.util.BossPostUtil;
import com.igormeshalkin.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDao implements EmployeeAccountingDao<Employee> {
    private static Connection connection = ConnectionManager.open();

    @Override
    public List<Employee> findAll() {
        List<Employee> result = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM EMPLOYEES LEFT JOIN DEPARTMENTS ON EMPLOYEES.DEPARTMENT = DEPARTMENTS.DEP_ID LEFT JOIN POSTS ON EMPLOYEES.POST = POSTS.POST_ID ORDER BY EMP_ID");

            while (resultSet.next()) {
                Employee employee = getEmployeeFromResultSet(resultSet);
                result.add(employee);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Employee findById(Long id) {
        Employee employee = null;
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("SELECT * FROM EMPLOYEES LEFT JOIN DEPARTMENTS ON EMPLOYEES.DEPARTMENT = DEPARTMENTS.DEP_ID LEFT JOIN POSTS ON EMPLOYEES.POST = POSTS.POST_ID WHERE EMPLOYEES.EMP_ID=?");

            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();

            employee = getEmployeeFromResultSet(resultSet);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employee;
    }

    @Override
    public void save(Employee employee) {
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("INSERT INTO EMPLOYEES (FIRST_NAME, LAST_NAME, DEPARTMENT, POST) VALUES(?, ?, ?, ?)");

            preparedStatement.setString(1, employee.getFirstName());
            preparedStatement.setString(2, employee.getLastName());
            preparedStatement.setLong(3, employee.getDepartment().getId());
            preparedStatement.setLong(4, employee.getPost().getId());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Employee employee) {
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("UPDATE EMPLOYEES SET FIRST_NAME=?, LAST_NAME=?, DEPARTMENT=?, POST=?  WHERE EMP_ID=?");

            preparedStatement.setString(1, employee.getFirstName());
            preparedStatement.setString(2, employee.getLastName());
            preparedStatement.setLong(3, employee.getDepartment().getId());
            preparedStatement.setLong(4, employee.getPost().getId());
            preparedStatement.setLong(5, employee.getId());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Long id) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM EMPLOYEES WHERE EMP_ID=?");

            preparedStatement.setLong(1, id);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getDepartmentBossName(Long department_id) {
        String firstName = null;
        String lastName = null;
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("SELECT FIRST_NAME, LAST_NAME FROM EMPLOYEES LEFT JOIN DEPARTMENTS ON EMPLOYEES.DEPARTMENT = DEPARTMENTS.DEP_ID LEFT JOIN POSTS ON EMPLOYEES.POST = POSTS.POST_ID WHERE DEP_ID=? AND POST_ID=?");

            preparedStatement.setLong(1, department_id);
            preparedStatement.setLong(2, BossPostUtil.BOSS_POST_ID);

            ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();

            firstName = resultSet.getString("first_name");
            lastName = resultSet.getString("last_name");

        } catch (SQLException e) {
            return null;
        }
        return firstName + " " + lastName;
    }

    private Employee getEmployeeFromResultSet(ResultSet resultSet) throws SQLException {
        Employee employee = new Employee();
        Department department = new Department();
        Post post = new Post();

        department.setId(resultSet.getLong("dep_id"));
        department.setName(resultSet.getString("department_name"));
        department.setPhoneNumber(resultSet.getString("phone_number"));
        department.setEmail(resultSet.getString("email"));

        post.setId(resultSet.getLong("post_id"));
        post.setName(resultSet.getString("post_name"));
        post.setSalary(resultSet.getDouble("salary"));

        employee.setId(resultSet.getLong("emp_id"));
        employee.setFirstName(resultSet.getString("first_name"));
        employee.setLastName(resultSet.getString("last_name"));
        employee.setDepartment(department);
        employee.setPost(post);

        return employee;
    }
}
