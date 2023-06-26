package com.example.rejavaexam.dao;

import com.example.rejavaexam.model.Employee;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDao {
    private String URL = "jdbc:mysql://localhost:3306/employeeDb?useSSL=false";
    private String USERNAME = "root";
    private String PASS = "";

    private static final String INSERT = "insert into employees (fullName, birthDay, address,position,department) values (?, ?, ?, ?, ?);";
    private static final String SELECT_BY_ID = "select * from employees where id = ? ;";
    private static final String SEARCH = "select * from employees where fullName like ? ;";
    private static final String SELECT_ALL = "select * from employees ;";
    private static final String DELETE = "delete from employees where id = ? ;";
    private static final String UPDATE = "update employees set fullName = ?, birthDay = ?, address = ?,position = ?,department = ? where id = ? ;";

    protected Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL,USERNAME,PASS);
            System.out.println("Connect success !");
        }catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return conn;
    }

    //Select all
    public List<Employee> selectAll() throws SQLException {
        List<Employee> listDatas = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(SELECT_ALL);
        ) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                int id = resultSet.getInt("id");
                String fullName = resultSet.getString("fullName");
                String birthDay = resultSet.getString("birthDay");
                String address = resultSet.getString("address");
                String position = resultSet.getString("position");
                String department = resultSet.getString("department");
                listDatas.add(new Employee(id, fullName,birthDay,address,position,department));
            }

        } catch (SQLException e){
            e.printStackTrace();
        }
        return  listDatas;
    }

    // Select by id
    public Employee selectById(int id) throws SQLException {
        Employee employee = null;
        try (Connection conn = getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(SELECT_BY_ID)
        ) {
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String fullName = resultSet.getString("fullName");
                String birthDay = resultSet.getString("birthDay");
                String address = resultSet.getString("address");
                String position = resultSet.getString("position");
                String department = resultSet.getString("department");
                employee = new Employee(id,fullName,birthDay,address,position,department);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return  employee;
    }

    //Insert
    public void insert(Employee employee) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(INSERT)
        ) {
            preparedStatement.setString(1, employee.getFullName());
            preparedStatement.setString(2, employee.getBirthDay());
            preparedStatement.setString(3, employee.getAddress());
            preparedStatement.setString(4, employee.getPosition());
            preparedStatement.setString(5, employee.getDepartment());
            preparedStatement.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    //Update
    public boolean update(Employee employee) throws SQLException {
        boolean updated;
        try (Connection conn = getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(UPDATE);
        ) {
            preparedStatement.setString(1, employee.getFullName());
            preparedStatement.setString(2, employee.getBirthDay());
            preparedStatement.setString(3, employee.getAddress());
            preparedStatement.setString(4, employee.getPosition());
            preparedStatement.setString(5, employee.getDepartment());
            preparedStatement.setInt(6, employee.getId());

            updated = preparedStatement.executeUpdate() > 0;

        }
        return  updated;
    }

    //Delete
    public boolean delete(int id) throws SQLException {
        boolean deleted;
        try(Connection conn = getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(DELETE);)
        {
            preparedStatement.setInt(1,id);
            deleted = preparedStatement.executeUpdate() > 0;
        }
        return  deleted;
    }

    // Search
    public List<Employee> search(String search) throws SQLException {
        List<Employee> listDatas = null;
        try (Connection conn = getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(SEARCH)
        ) {
            preparedStatement.setString(1, "'%" + search + "%'");

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                int id = resultSet.getInt("id");
                String fullName = resultSet.getString("fullName");
                String birthDay = resultSet.getString("birthDay");
                String address = resultSet.getString("address");
                String position = resultSet.getString("position");
                String department = resultSet.getString("department");
                listDatas.add(new Employee(id, fullName,birthDay,address,position,department));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return  listDatas;
    }
}
