package com.igormeshalkin.dao;

import com.igormeshalkin.model.Post;
import com.igormeshalkin.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostDao implements EmployeeAccountingDao<Post> {
    private static Connection connection = ConnectionManager.open();

    @Override
    public List<Post> findAll() {
        List<Post> result = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM POSTS ORDER BY POST_ID");

            while (resultSet.next()) {
                Post post = getPostFromResultSet(resultSet);
                result.add(post);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Post findById(Long id) {
        Post post = null;
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("SELECT * FROM POSTS WHERE POST_ID=?");

            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            post = getPostFromResultSet(resultSet);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return post;
    }

    public Post findByName(String name) {
        Post post = null;
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("SELECT * FROM POSTS WHERE POST_NAME=?");

            preparedStatement.setString(1, name);

            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            post = getPostFromResultSet(resultSet);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return post;
    }

    @Override
    public void save(Post post) {
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("INSERT INTO POSTS (POST_NAME, SALARY) VALUES(?, ?)");

            preparedStatement.setString(1, post.getName());
            preparedStatement.setDouble(2, post.getSalary());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Post post) {
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("UPDATE POSTS SET POST_NAME=?, SALARY=? WHERE POST_ID=?");

            preparedStatement.setString(1, post.getName());
            preparedStatement.setDouble(2, post.getSalary());
            preparedStatement.setLong(3, post.getId());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Long id) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM POSTS WHERE POST_ID=?");

            preparedStatement.setLong(1, id);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Post getPostFromResultSet(ResultSet resultSet) throws SQLException {
        Post post = new Post();

        post.setId(resultSet.getLong("post_id"));
        post.setName(resultSet.getString("post_name"));
        post.setSalary(resultSet.getDouble("salary"));

        return post;
    }
}
