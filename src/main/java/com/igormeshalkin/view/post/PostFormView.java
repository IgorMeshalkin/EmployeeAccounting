package com.igormeshalkin.view.post;

import com.igormeshalkin.dao.PostDao;
import com.igormeshalkin.model.Post;
import com.igormeshalkin.util.BossPostUtil;
import com.igormeshalkin.util.SalaryStringFormat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PostFormView extends JFrame {
    private static PostDao postDao = new PostDao();

    private JButton okButton = new JButton("Ок");
    private JButton cancelButton = new JButton("Отмена");

    private JTextField idField = new JTextField("", 0);
    private JTextField nameField = new JTextField("", 32);
    private JTextField salaryField = new JTextField("", 32);

    private JLabel nameLabel = new JLabel("Название:");
    private JLabel salaryLabel = new JLabel("Заработная плата:");

    public PostFormView() {
        super("Добавить должность");
        this.setBounds(500, 200, 400, 170);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container container = this.getContentPane();
        container.setLayout(new FlowLayout());

        container.add(nameLabel);
        container.add(nameField);

        container.add(salaryLabel);
        container.add(salaryField);

        okButton.addActionListener(new OkButtonListener());
        cancelButton.addActionListener(new CancelButtonListener());
        container.add(okButton);
        container.add(cancelButton);
    }

    public PostFormView(String id, String name, String salary) {
        super("Редактировать должность");
        this.setBounds(500, 200, 400, 170);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container container = this.getContentPane();
        container.setLayout(new FlowLayout());

        idField.setText(id);
        nameField.setText(name);
        salaryField.setText(SalaryStringFormat.reformatSalary(salary));

        container.add(nameLabel);
        container.add(nameField);

        container.add(salaryLabel);
        container.add(salaryField);

        okButton.addActionListener(new OkButtonListener());
        cancelButton.addActionListener(new CancelButtonListener());
        container.add(okButton);
        container.add(cancelButton);
    }

    class OkButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Post post = new Post();

            Long id;
            try {
                id = Long.parseLong(String.valueOf(idField.getText()));
            } catch (NumberFormatException exp) {
                id = null;
            }

            Double salary;
            try {
                String salaryString = String.valueOf(salaryField.getText());
                salaryString = salaryString.replaceAll(",", ".");
                salary = Double.parseDouble(salaryString);
            } catch (NumberFormatException exp) {
                salary = 0.0;
            }

            post.setId(id);
            post.setName(nameField.getText());
            post.setSalary(salary);

            if (post.isNew()) {
                postDao.save(post);
            } else {
                if (post.getId() == BossPostUtil.BOSS_POST_ID && !post.getName().equals(BossPostUtil.BOSS_POST_NAME)) {
                    post.setName(BossPostUtil.BOSS_POST_NAME);
                    JOptionPane.showMessageDialog(null, "Эту должность нельзя переименовать");
                }
                postDao.update(post);
            }

            setVisible(false);
            PostView postView = new PostView();
            postView.setVisible(true);
        }
    }

    class CancelButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            setVisible(false);
            PostView postView = new PostView();
            postView.setVisible(true);
        }
    }
}
