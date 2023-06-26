package com.example.rejavaexam.controller;

import com.example.rejavaexam.dao.EmployeeDao;
import com.example.rejavaexam.model.Employee;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "main" , value = "/")
public class EmployeeServlet extends HttpServlet {
    private EmployeeDao employeeDao;

    public void init() {
        this.employeeDao = new EmployeeDao();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request,response);
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getServletPath();

        action = action == null ? "/" : action;

        try {
            switch (action) {
                case "/create" :
                    showCreatePage(request, response);
                    break;
                case "/insert":
                    insert(request,response);
                    break;
                case "/delete":
                    delete(request,response);
                    break;
                case "/edit":
                    isEdit(request,response);
                    break;
                case "/update":
                    updateEmployee(request,response);
                    break;
                case "/search":
                    System.out.println("search");
                    search(request,response);
                    break;
                default:
                    listAll(request,response);
                    break;
            }
        }catch(SQLException e) {
            e.printStackTrace();
        }
    }

    private void listAll(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        List<Employee> employees = employeeDao.selectAll();
        request.setAttribute("employees", employees);
        RequestDispatcher dispatcher = request.getRequestDispatcher("home.jsp");
        dispatcher.forward(request, response);
    }

    private void search(HttpServletRequest request, HttpServletResponse response) throws SQLException,IOException,ServletException{
        String search = request.getParameter("search");
        List<Employee> employees = employeeDao.search(search);
        request.setAttribute("employees", employees);
        RequestDispatcher dispatcher = request.getRequestDispatcher("home.jsp");
        dispatcher.forward(request, response);
    }
    private void showCreatePage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("create.jsp");
        dispatcher.forward(request, response);
    }

    private void isEdit(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        Employee employee = employeeDao.selectById(Integer.parseInt(request.getParameter("id")));
        RequestDispatcher dispatcher = request.getRequestDispatcher("create.jsp");
        request.setAttribute("employee", employee);
        dispatcher.forward(request,response);
    }

    private void insert(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        String fullName = request.getParameter("fullName");
        String birthDay = request.getParameter("birthDay");
        String address = request.getParameter("address");
        String position = request.getParameter("position");
        String department = request.getParameter("department");
        Employee newEmployee = new Employee(fullName,birthDay,address,position,department);
        employeeDao.insert(newEmployee);
        response.sendRedirect("/");
    }

    private void delete(HttpServletRequest request, HttpServletResponse response) throws SQLException,ServletException, IOException {
        employeeDao.delete(Integer.parseInt(request.getParameter("id")));
        response.sendRedirect("/");
    }

    private void updateEmployee(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException{
        int id = Integer.parseInt(request.getParameter("id"));
        String fullName = request.getParameter("fullName");
        String birthDay = request.getParameter("birthDay");
        String address = request.getParameter("address");
        String position = request.getParameter("position");
        String department = request.getParameter("department");

        Employee employee = new Employee(id, fullName, birthDay,address,position,department);
        employeeDao.update(employee);
        response.sendRedirect("/");
    }
}
