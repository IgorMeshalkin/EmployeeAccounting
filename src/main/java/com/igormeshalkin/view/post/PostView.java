package com.igormeshalkin.view.post;

import com.igormeshalkin.dao.PostDao;
import com.igormeshalkin.model.Post;
import com.igormeshalkin.util.BossPostUtil;
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

public class PostView extends JFrame {
    private static PostDao postDao = new PostDao();
    private List<Post> postList = postDao.findAll();

    private DefaultTableModel dm = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int i, int i1) {
            return false;
        }
    };

    {
        Object[] columnHeaders = {"№", "Название", "Заработная плата", "Id"};
        Object[][] tableData = new String[postList.size()][4];

        postList.forEach(post -> {
            tableData[postList.indexOf(post)][0] = String.valueOf(postList.indexOf(post) + 1);
            tableData[postList.indexOf(post)][1] = post.getName();
            tableData[postList.indexOf(post)][2] = SalaryStringFormat.formatSalary(post.getSalary());
            tableData[postList.indexOf(post)][3] = String.valueOf(post.getId());
        });

        dm.setDataVector(tableData, columnHeaders);
    }

    private JTable table = new JTable(dm);

    private JButton addButton = new JButton("Добавить");
    private JButton updateButton = new JButton("Редактировать");
    private JButton removeButton = new JButton("Удалить");
    private JButton backButton = new JButton("Назад");

    public PostView() {
        super("Должности");
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
        table.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);

        table.getColumnModel().getColumn(0).setMinWidth(35);
        table.getColumnModel().getColumn(0).setMaxWidth(35);
        table.getColumnModel().getColumn(1).setMinWidth(500);
        table.getColumnModel().getColumn(1).setMaxWidth(500);
        table.removeColumn(table.getColumnModel().getColumn(3));

        addButton.addActionListener(new PostView.AddButtonListener());
        updateButton.addActionListener(new PostView.UpdateButtonListener());
        removeButton.addActionListener(new PostView.RemoveButtonListener());
        backButton.addActionListener(new PostView.BackButtonListener());

        container.add(addButton);
        container.add(updateButton);
        container.add(removeButton);
        container.add(backButton);
    }

    class AddButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            setVisible(false);
            PostFormView postFormView = new PostFormView();
            postFormView.setVisible(true);
        }
    }

    class UpdateButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (table.getSelectedRow() != -1) {
                setVisible(false);

                String id = String.valueOf(table.getModel().getValueAt(table.getSelectedRow(), 3));
                String currentName = String.valueOf(table.getModel().getValueAt(table.getSelectedRow(), 1));
                String currentSalary = String.valueOf(table.getModel().getValueAt(table.getSelectedRow(), 2));

                PostFormView postFormView = new PostFormView(id, currentName, currentSalary);
                postFormView.setVisible(true);
            }
        }
    }

    class RemoveButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (table.getSelectedRow() != -1) {
                Long removablePostId = Long.parseLong(String.valueOf(table.getModel().getValueAt(table.getSelectedRow(), 3)));

                if (removablePostId.equals(BossPostUtil.BOSS_POST_ID)) {
                    JOptionPane.showMessageDialog(null, "Эту должность нельзя удалить");
                    return;
                }

                int option = JOptionPane.showConfirmDialog(null, "Вы действительно хотите удалить должность \"" + String.valueOf(table.getModel().getValueAt(table.getSelectedRow(), 1)) + "\"?", "Удаление", YES_NO_OPTION);

                if (option == 0) {
                    setVisible(false);

                    postDao.delete(removablePostId);

                    PostView postView = new PostView();
                    postView.setVisible(true);
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
