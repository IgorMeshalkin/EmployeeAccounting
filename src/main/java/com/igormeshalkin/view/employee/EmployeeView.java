package com.igormeshalkin.view.employee;

import com.igormeshalkin.dao.EmployeeDao;
import com.igormeshalkin.model.Employee;
import com.igormeshalkin.util.SalaryStringFormat;
import com.igormeshalkin.view.MainView;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import static javax.swing.JOptionPane.YES_NO_OPTION;

public class EmployeeView extends JFrame {
    private static EmployeeDao employeeDao = new EmployeeDao();
    private List<Employee> employeeList = employeeDao.findAll();

    private DefaultTableModel dm = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int i, int i1) {
            return false;
        }
    };

    {
        Object[] columnHeaders = {"№", "Имя", "Фамилия", "Отдел", "Должность", "Зарплата", "Id"};
        Object[][] tableData = new String[employeeList.size()][7];

        employeeList.forEach(emp -> {
            tableData[employeeList.indexOf(emp)][0] = String.valueOf(employeeList.indexOf(emp) + 1);
            tableData[employeeList.indexOf(emp)][1] = emp.getFirstName();
            tableData[employeeList.indexOf(emp)][2] = emp.getLastName();
            tableData[employeeList.indexOf(emp)][3] = emp.getDepartment().getName();
            tableData[employeeList.indexOf(emp)][4] = emp.getPost().getName();
            tableData[employeeList.indexOf(emp)][5] = SalaryStringFormat.formatSalary(emp.getPost().getSalary());
            tableData[employeeList.indexOf(emp)][6] = String.valueOf(emp.getId());
        });

        dm.setDataVector(tableData, columnHeaders);
    }

    private JTable table = new JTable(dm);

    private JButton addButton = new JButton("Добавить");
    private JButton updateButton = new JButton("Редактировать");
    private JButton removeButton = new JButton("Удалить");
    private JButton backButton = new JButton("Назад");

    public EmployeeView() {
        super("Сотрудники");
        this.setBounds(300, 200, 810, 340);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container container = this.getContentPane();
        container.setLayout(new FlowLayout());

        table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        table.setPreferredScrollableViewportSize(new Dimension(780, 240));
        JScrollPane scrollPane = new JScrollPane(table);
        this.getContentPane().add(scrollPane);

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        table.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);

        table.getColumnModel().getColumn(0).setMinWidth(20);
        table.getColumnModel().getColumn(0).setMaxWidth(35);
        table.getColumnModel().getColumn(1).setMinWidth(100);
        table.getColumnModel().getColumn(1).setMaxWidth(300);
        table.getColumnModel().getColumn(2).setMinWidth(100);
        table.getColumnModel().getColumn(2).setMaxWidth(300);
        table.getColumnModel().getColumn(3).setMinWidth(250);
        table.getColumnModel().getColumn(3).setMaxWidth(300);
        table.getColumnModel().getColumn(4).setMinWidth(200);
        table.getColumnModel().getColumn(4).setMaxWidth(300);
        table.removeColumn(table.getColumnModel().getColumn(6));

        addButton.addActionListener(new EmployeeView.AddButtonListener());
        updateButton.addActionListener(new EmployeeView.UpdateButtonListener());
        removeButton.addActionListener(new EmployeeView.RemoveButtonListener());
        backButton.addActionListener(new EmployeeView.BackButtonListener());

        container.add(addButton);
        container.add(updateButton);
        container.add(removeButton);
        container.add(backButton);
    }

    class AddButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            setVisible(false);
            EmployeeFormView employeeFormView = new EmployeeFormView();
            employeeFormView.setVisible(true);
        }
    }

    class UpdateButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (table.getSelectedRow() != -1) {
                setVisible(false);

                String id = String.valueOf(table.getModel().getValueAt(table.getSelectedRow(), 6));
                String currentFirstName = String.valueOf(table.getModel().getValueAt(table.getSelectedRow(), 1));
                String currentLastName = String.valueOf(table.getModel().getValueAt(table.getSelectedRow(), 2));
                String currentDepartmentName = String.valueOf(table.getModel().getValueAt(table.getSelectedRow(), 3));
                String currentPostName = String.valueOf(table.getModel().getValueAt(table.getSelectedRow(), 4));

                EmployeeFormView employeeFormView = new EmployeeFormView(id, currentFirstName, currentLastName, currentDepartmentName, currentPostName);
                employeeFormView.setVisible(true);
            }
        }
    }

    class RemoveButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (table.getSelectedRow() != -1) {
                int option = JOptionPane.showConfirmDialog(null, "Вы действительно хотите удалить сотрудника \""
                        + String.valueOf(table.getModel().getValueAt(table.getSelectedRow(), 1))
                        + " " + String.valueOf(table.getModel().getValueAt(table.getSelectedRow(), 2)) + "\"?", "Удаление", YES_NO_OPTION);

                if (option == 0) {
                    setVisible(false);

                    employeeDao.delete(
                            Long.parseLong(String.valueOf(table.getModel().getValueAt(table.getSelectedRow(), 6))));

                    EmployeeView employeeView = new EmployeeView();
                    employeeView.setVisible(true);
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

