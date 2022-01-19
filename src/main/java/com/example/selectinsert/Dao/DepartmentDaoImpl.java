package com.example.selectinsert.Dao;

import com.example.selectinsert.Entity.Department;
import com.example.selectinsert.Entity.Faculty;
import com.example.selectinsert.Util.DaoService;
import com.example.selectinsert.Util.MySQLConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDaoImpl implements DaoService<Department> {

    @Override
    public List<Department> fecthAll() throws SQLException, ClassNotFoundException {
        List<Department> departments = new ArrayList<>();
        try (Connection connection = MySQLConnection.createConnection()) {
            String query = "SELECT d.id, d.name, d.faculty_id, f.name AS faculty_name FROM department d JOIN faculty f ON d.faculty_id = f.id";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        Faculty faculty = new Faculty();
                        faculty.setId(resultSet.getInt("faculty_id"));
                        faculty.setName(resultSet.getString("faculty_name"));

                        Department department = new Department();
                        department.setId(resultSet.getInt("id"));
                        department.setName(resultSet.getString("name"));
                        department.setFaculty(faculty);
                        departments.add(department);
                    }
                }
            }
        }
        return departments;
    }

    @Override
    public int addData(Department object) throws SQLException, ClassNotFoundException {
        int result = 0;
        Connection connection = MySQLConnection.createConnection();
        String query = " INSERT INTO department(name, faculty_id) VALUES(?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, object.getName());
        preparedStatement.setInt(2, object.getFaculty().getId());
        if (preparedStatement.executeUpdate() !=0) {
            connection.commit();
            result = 1;
        } else {
            connection.rollback();
        }
        return result;
    }

    @Override
    public int updateData(Department object) throws SQLException, ClassNotFoundException {
        int result = 0;
        Connection connection = MySQLConnection.createConnection();
        String query = "UPDATE department SET name = ?, faculty_id = ? WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, object.getName());
        preparedStatement.setInt(2, object.getFaculty().getId());
        preparedStatement.setInt(3, object.getId());
        if (preparedStatement.executeUpdate() !=0) {
            connection.commit();
            result = 1;
        } else {
            connection.rollback();
        }
        return result;
    }

    @Override
    public int deleteData(Department object) throws SQLException, ClassNotFoundException {
        int result = 0;
        Connection connection = MySQLConnection.createConnection();
        String query = " DELETE FROM department WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, object.getId());

        if (preparedStatement.executeUpdate() !=0) {
            connection.commit();
            result = 1;
        } else {
            connection.rollback();
        }
        return result;
    }
}
