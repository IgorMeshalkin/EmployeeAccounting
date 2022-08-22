package com.igormeshalkin.dao;

import com.igormeshalkin.model.Department;
import com.igormeshalkin.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDao implements EmployeeAccountingDao<Department> {
    private static Connection connection = ConnectionManager.open();

    @Override
    public List<Department> findAll() {
        List<Department> result = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM DEPARTMENTS ORDER BY DEP_ID");

            while (resultSet.next()) {
                Department department = getDepartmentFromResultSet(resultSet);
                result.add(department);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Department findById(Long id) {
        Department department = null;
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("SELECT * FROM DEPARTMENTS WHERE DEP_ID=?");

            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();

            department = getDepartmentFromResultSet(resultSet);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return department;
    }

    public Department findByName(String name) {
        Department department = null;
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("SELECT * FROM DEPARTMENTS WHERE DEPARTMENT_NAME=?");

            preparedStatement.setString(1, name);

            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();

            department = getDepartmentFromResultSet(resultSet);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return department;
    }

    @Override
    public void save(Department department) {
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("INSERT INTO DEPARTMENTS (DEPARTMENT_NAME, PHONE_NUMBER, EMAIL) VALUES(?, ?, ?)");

            preparedStatement.setString(1, department.getName());
            preparedStatement.setString(2, department.getPhoneNumber());
            preparedStatement.setString(3, department.getEmail());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Department department) {
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("UPDATE DEPARTMENTS SET DEPARTMENT_NAME =?, PHONE_NUMBER=?, EMAIL=? WHERE DEP_ID=?");

            preparedStatement.setString(1, department.getName());
            preparedStatement.setString(2, department.getPhoneNumber());
            preparedStatement.setString(3, department.getEmail());
            preparedStatement.setLong(4, department.getId());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Long id) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM DEPARTMENTS WHERE DEP_ID=?");

            preparedStatement.setLong(1, id);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Department getDepartmentFromResultSet(ResultSet resultSet) throws SQLException {
        Department department = new Department();

        department.setId(resultSet.getLong("dep_id"));
        department.setName(resultSet.getString("department_name"));
        department.setPhoneNumber(resultSet.getString("phone_number"));
        department.setEmail(resultSet.getString("email"));

        return department;
    }
}
