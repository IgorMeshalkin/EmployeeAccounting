package com.igormeshalkin.view.employee;

import com.igormeshalkin.dao.DepartmentDao;
import com.igormeshalkin.dao.EmployeeDao;
import com.igormeshalkin.dao.PostDao;
import com.igormeshalkin.model.Department;
import com.igormeshalkin.model.Employee;
import com.igormeshalkin.model.Post;
import com.igormeshalkin.util.BossPostUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class EmployeeFormView extends JFrame {
    private static EmployeeDao employeeDao = new EmployeeDao();
    private static DepartmentDao departmentDao = new DepartmentDao();
    private static PostDao postDao = new PostDao();

    List<Department> departmentList = departmentDao.findAll();
    String[] departmentNamesForComboBox = new String[departmentList.size()];

    {
        departmentList.forEach(dep -> {
            departmentNamesForComboBox[departmentList.indexOf(dep)] = dep.getName();
        });
    }

    private JComboBox departmentComboBox = new JComboBox(departmentNamesForComboBox);

    List<Post> postList = postDao.findAll();
    String[] postNamesForComboBox = new String[postList.size()];

    {
        postList.forEach(post -> {
            postNamesForComboBox[postList.indexOf(post)] = post.getName();
        });
    }

    private JComboBox postComboBox = new JComboBox(postNamesForComboBox);

    private JButton okButton = new JButton("Ок");
    private JButton canselButton = new JButton("Отмена");

    private JTextField idField = new JTextField("", 0);
    private JTextField firstNameField = new JTextField("", 32);
    private JTextField lastNameField = new JTextField("", 32);

    private JLabel firstNameLabel = new JLabel("Имя:");
    private JLabel lastNameLabel = new JLabel("Фамилия:");
    private JLabel departmentLabel = new JLabel("Отдел:");
    private JLabel postLabel = new JLabel("Должность:");

    {
        firstNameLabel.setBounds(180, 5, 100, 10);
        firstNameField.setBounds(15, 20, 350, 20);

        lastNameLabel.setBounds(167, 48, 100, 10);
        lastNameField.setBounds(15, 65, 350, 20);

        departmentLabel.setBounds(175, 90, 100, 15);
        departmentComboBox.setBounds(55, 110, 280, 20);

        postLabel.setBounds(162, 138, 100, 15);
        postComboBox.setBounds(55, 158, 280, 20);

        okButton.setBounds(118, 200, 60, 25);
        canselButton.setBounds(183, 200, 90, 25);
    }


    public EmployeeFormView() {
        super("Добавить сотрудника");
        this.setBounds(500, 200, 400, 280);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container container = this.getContentPane();
        container.setLayout(null);

        container.add(firstNameLabel);
        container.add(firstNameField);

        container.add(lastNameLabel);
        container.add(lastNameField);

        container.add(departmentLabel);
        container.add(departmentComboBox);

        container.add(postLabel);
        container.add(postComboBox);

        okButton.addActionListener(new EmployeeFormView.OkButtonListener());
        canselButton.addActionListener(new CanselButtonListener());
        container.add(okButton);
        container.add(canselButton);
    }

    public EmployeeFormView(String id, String firstName, String lastName, String departmentName, String postName) {
        super("Редактировать запись о сотруднике");
        this.setBounds(500, 200, 400, 280);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container container = this.getContentPane();
        container.setLayout(null);

        idField.setText(id);

        firstNameField.setText(firstName);
        container.add(firstNameLabel);
        container.add(firstNameField);

        lastNameField.setText(lastName);
        container.add(lastNameLabel);
        container.add(lastNameField);

        departmentComboBox.setSelectedItem(departmentName);
        container.add(departmentLabel);
        container.add(departmentComboBox);

        postComboBox.setSelectedItem(postName);
        container.add(postLabel);
        container.add(postComboBox);

        okButton.addActionListener(new EmployeeFormView.OkButtonListener());
        canselButton.addActionListener(new CanselButtonListener());
        container.add(okButton);
        container.add(canselButton);
    }

    class OkButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Department selectedDepartment = departmentDao.findByName(String.valueOf(departmentComboBox.getSelectedItem()));
            Post selectedPost = postDao.findByName(String.valueOf(postComboBox.getSelectedItem()));

            if (selectedPost.getName().equals(BossPostUtil.BOSS_POST_NAME)) {
                String departmentBoss = employeeDao.getDepartmentBossName(selectedDepartment.getId());
                if (departmentBoss != null) {
                    JOptionPane.showMessageDialog(null, "Начальник этого отдела " + departmentBoss + ",\nвы не можете назначить второго начальника");
                    return;
                }
            }

            Employee employee = new Employee();

            Long id;
            try {
                id = Long.parseLong(String.valueOf(idField.getText()));
            } catch (NumberFormatException exp) {
                id = null;
            }

            employee.setId(id);
            employee.setFirstName(firstNameField.getText());
            employee.setLastName(lastNameField.getText());
            employee.setDepartment(selectedDepartment);
            employee.setPost(selectedPost);

            if (employee.isNew()) {
                employeeDao.save(employee);
            } else {
                employeeDao.update(employee);
            }

            setVisible(false);
            EmployeeView employeeView = new EmployeeView();
            employeeView.setVisible(true);
        }
    }

    class CanselButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            setVisible(false);
            EmployeeView employeeView = new EmployeeView();
            employeeView.setVisible(true);
        }
    }
}

