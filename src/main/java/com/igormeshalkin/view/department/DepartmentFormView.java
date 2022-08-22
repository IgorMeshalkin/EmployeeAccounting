package com.igormeshalkin.view.department;

import com.igormeshalkin.dao.DepartmentDao;
import com.igormeshalkin.model.Department;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DepartmentFormView extends JFrame {
    private static DepartmentDao departmentDao = new DepartmentDao();

    private JButton okButton = new JButton("Ок");
    private JButton cancelButton = new JButton("Отмена");

    private JTextField idField = new JTextField("", 0);
    private JTextField nameField = new JTextField("", 32);
    private JTextField phoneNumberField = new JTextField("", 32);
    private JTextField emailField = new JTextField("", 32);

    private JLabel nameLabel = new JLabel("Название:");
    private JLabel phoneNumberLabel = new JLabel("Телефон:");
    private JLabel emailLabel = new JLabel("E-mail:");

    public DepartmentFormView() {
        super("Добавить отдел");
        this.setBounds(500, 200, 400, 220);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container container = this.getContentPane();
        container.setLayout(new FlowLayout());

        container.add(nameLabel);
        container.add(nameField);

        container.add(phoneNumberLabel);
        container.add(phoneNumberField);

        container.add(emailLabel);
        container.add(emailField);

        okButton.addActionListener(new OkButtonListener());
        cancelButton.addActionListener(new CanselButtonListener());
        container.add(okButton);
        container.add(cancelButton);
    }

    public DepartmentFormView(String id, String name, String phoneNumber, String email) {
        super("Редактировать отдел");
        this.setBounds(500, 200, 400, 220);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container container = this.getContentPane();
        container.setLayout(new FlowLayout());

        idField.setText(id);
        nameField.setText(name);
        phoneNumberField.setText(phoneNumber);
        emailField.setText(email);

        container.add(nameLabel);
        container.add(nameField);

        container.add(phoneNumberLabel);
        container.add(phoneNumberField);

        container.add(emailLabel);
        container.add(emailField);

        okButton.addActionListener(new OkButtonListener());
        cancelButton.addActionListener(new CanselButtonListener());
        container.add(okButton);
        container.add(cancelButton);
    }

    class OkButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Department department = new Department();

            Long id;
            try {
                id = Long.parseLong(String.valueOf(idField.getText()));
            } catch (NumberFormatException exp) {
                id = null;
            }

            department.setId(id);
            department.setName(nameField.getText());
            department.setPhoneNumber(phoneNumberField.getText());
            department.setEmail(emailField.getText());

            if (department.isNew()) {
                departmentDao.save(department);
            } else {
                departmentDao.update(department);
            }

            setVisible(false);
            DepartmentView departmentView = new DepartmentView();
            departmentView.setVisible(true);
        }
    }

    class CanselButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            setVisible(false);
            DepartmentView departmentView = new DepartmentView();
            departmentView.setVisible(true);
        }
    }
}
