package com.igormeshalkin.view;

import com.igormeshalkin.view.department.DepartmentView;
import com.igormeshalkin.view.employee.EmployeeView;
import com.igormeshalkin.view.post.PostView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainView extends JFrame {
    private JButton employeesButton = new JButton("Сотрудники");
    private JButton departmentsButton = new JButton("Отделы");
    private JButton postsButton = new JButton("Должности");

    public MainView() {
        super("Учёт сотрудников");
        this.setBounds(400, 250, 610, 200);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container container = this.getContentPane();
        container.setLayout(new GridLayout());

        employeesButton.addActionListener(new EmployeesButtonListener());
        departmentsButton.addActionListener(new DepartmentsButtonListener());
        postsButton.addActionListener(new PostsButtonListener());

        container.add(employeesButton);
        container.add(departmentsButton);
        container.add(postsButton);
    }

    class EmployeesButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            setVisible(false);
            EmployeeView employeeView = new EmployeeView();
            employeeView.setVisible(true);
        }
    }

    class DepartmentsButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            setVisible(false);
            DepartmentView departmentsView = new DepartmentView();
            departmentsView.setVisible(true);
        }
    }

    class PostsButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            setVisible(false);
            PostView postView = new PostView();
            postView.setVisible(true);
        }
    }

}
