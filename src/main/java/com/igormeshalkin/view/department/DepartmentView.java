package com.igormeshalkin.view.department;

import com.igormeshalkin.dao.DepartmentDao;
import com.igormeshalkin.model.Department;
import com.igormeshalkin.view.MainView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import static javax.swing.JOptionPane.YES_NO_OPTION;

public class DepartmentView extends JFrame {
    private static DepartmentDao departmentDao = new DepartmentDao();
    private List<Department> departmentList = departmentDao.findAll();

    private DefaultTableModel dm = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int i, int i1) {
            return false;
        }
    };

    {
        Object[] columnHeaders = {"№", "Название", "Телефон", "E-mail", "Id"};
        Object[][] tableData = new String[departmentList.size()][5];

        departmentList.forEach(dep -> {
            tableData[departmentList.indexOf(dep)][0] = String.valueOf(departmentList.indexOf(dep) + 1);
            tableData[departmentList.indexOf(dep)][1] = dep.getName();
            tableData[departmentList.indexOf(dep)][2] = dep.getPhoneNumber();
            tableData[departmentList.indexOf(dep)][3] = dep.getEmail();
            tableData[departmentList.indexOf(dep)][4] = String.valueOf(dep.getId());
        });

        dm.setDataVector(tableData, columnHeaders);
    }

    private JTable table = new JTable(dm);

    private JButton addButton = new JButton("Добавить");
    private JButton updateButton = new JButton("Редактировать");
    private JButton removeButton = new JButton("Удалить");
    private JButton backButton = new JButton("Назад");

    public DepartmentView() {
        super("Отделы");
        this.setBounds(300, 200, 810, 340);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container container = this.getContentPane();
        container.setLayout(new FlowLayout());

        table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        table.setPreferredScrollableViewportSize(new Dimension(780, 240));
        JScrollPane scrollPane = new JScrollPane(table);
        this.getContentPane().add(scrollPane);

        table.getColumnModel().getColumn(0).setMinWidth(35);
        table.getColumnModel().getColumn(0).setMaxWidth(35);
        table.getColumnModel().getColumn(1).setMinWidth(430);
        table.getColumnModel().getColumn(1).setMaxWidth(430);
        table.removeColumn(table.getColumnModel().getColumn(4));

        addButton.addActionListener(new AddButtonListener());
        updateButton.addActionListener(new UpdateButtonListener());
        removeButton.addActionListener(new RemoveButtonListener());
        backButton.addActionListener(new BackButtonListener());

        container.add(addButton);
        container.add(updateButton);
        container.add(removeButton);
        container.add(backButton);
    }

    class AddButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            setVisible(false);
            DepartmentFormView departmentFormView = new DepartmentFormView();
            departmentFormView.setVisible(true);
        }
    }

    class UpdateButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (table.getSelectedRow() != -1) {
                setVisible(false);

                String id = String.valueOf(table.getModel().getValueAt(table.getSelectedRow(), 4));
                String currentName = String.valueOf(table.getModel().getValueAt(table.getSelectedRow(), 1));
                String currentPhoneNumber = String.valueOf(table.getModel().getValueAt(table.getSelectedRow(), 2));
                String currentEmail = String.valueOf(table.getModel().getValueAt(table.getSelectedRow(), 3));

                DepartmentFormView departmentFormView = new DepartmentFormView(id, currentName, currentPhoneNumber, currentEmail);
                departmentFormView.setVisible(true);
            }
        }
    }

    class RemoveButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (table.getSelectedRow() != -1) {
                int option = JOptionPane.showConfirmDialog(null, "Вы действительно хотите удалить \""
                        + String.valueOf(table.getModel().getValueAt(table.getSelectedRow(), 1)) + "\"?", "Удаление", YES_NO_OPTION);

                if (option == 0) {
                    setVisible(false);

                    departmentDao.delete(
                            Long.parseLong(String.valueOf(table.getModel().getValueAt(table.getSelectedRow(), 4))));

                    DepartmentView departmentView = new DepartmentView();
                    departmentView.setVisible(true);
                }
            }
        }
    }

    class BackButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            setVisible(false);
            MainView mainView = new MainView();
            mainView.setVisible(true);
        }
    }
}

