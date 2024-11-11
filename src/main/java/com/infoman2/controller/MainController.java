package com.infoman2.controller;

import com.infoman2.DatabaseConnection;
import com.infoman2.model.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MainController {
    @FXML
    private TextField firstName;
    @FXML
    private TextField middleName;
    @FXML
    private TextField lastName;
    @FXML
    private TextField phoneNumber;
    @FXML
    private TextField email;

    @FXML
    private RadioButton maleRadioButton;
    @FXML
    private RadioButton femaleRadioButton;
    @FXML
    private TableView<Student> table;

    @FXML
    private TableColumn<Student, String> colFN;
    @FXML
    private TableColumn<Student, String> colMN;
    @FXML
    private TableColumn<Student, String> colLN;
    @FXML
    private TableColumn<Student, String> colPN;
    @FXML
    private TableColumn<Student, String> colEmail;
    @FXML
    private TableColumn<Student, String> colGender;

    private boolean isEditing = false;
    private int studentId = 0;
    private DatabaseConnection db;

    private ObservableList<Student> studentList = FXCollections.observableArrayList();

    private ToggleGroup genderToggleGroup;

    public void initialize() throws SQLException {
        db = new DatabaseConnection();

        colFN.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colMN.setCellValueFactory(new PropertyValueFactory<>("middleName"));
        colLN.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colPN.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));

        // Initialize the ToggleGroup for gender selection
        genderToggleGroup = new ToggleGroup();
        maleRadioButton.setToggleGroup(genderToggleGroup);
        femaleRadioButton.setToggleGroup(genderToggleGroup);

        loadStudent();
    }

    public void loadStudent() throws SQLException {
        studentList.clear();
        String sql = "SELECT * FROM students";
        Statement stmt = db.getConnection().createStatement();
        ResultSet result = stmt.executeQuery(sql);

        while (result.next()) {
            Student student = new Student(result.getInt("id"),
                    result.getString("first_name"),
                    result.getString("middle_name"),
                    result.getString("last_name"),
                    result.getString("phone_number"),
                    result.getString("email"),
                    result.getString("gender"));
            studentList.add(student);
        }
        table.setItems(studentList);
    }

    @FXML
    private void save() throws SQLException {
        String gender = maleRadioButton.isSelected() ? "Male" : "Female"; // Set gender based on selected button
        if (!isEditing) {
            String sql = "INSERT INTO students(first_name, middle_name, last_name, email, phone_number, gender) VALUES(?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = db.getConnection().prepareStatement(sql);

            pstmt.setString(1, firstName.getText());
            pstmt.setString(2, middleName.getText());
            pstmt.setString(3, lastName.getText());
            pstmt.setString(4, email.getText());
            pstmt.setString(5, phoneNumber.getText());
            pstmt.setString(6, gender);

            if (pstmt.executeUpdate() == 1) {
                firstName.clear();
                middleName.clear();
                lastName.clear();
                email.clear();
                phoneNumber.clear();
                genderToggleGroup.selectToggle(null); // Clear gender selection
                loadStudent();
            }
        } else {
            String sql = "UPDATE students SET first_name = ?, middle_name = ?, last_name = ?, phone_number = ?, email = ?, gender = ? WHERE id = ?";
            try {
                PreparedStatement pstmt = db.getConnection().prepareStatement(sql);
                pstmt.setString(1, firstName.getText());
                pstmt.setString(2, middleName.getText());
                pstmt.setString(3, lastName.getText());
                pstmt.setString(4, phoneNumber.getText());
                pstmt.setString(5, email.getText());
                pstmt.setString(6, gender);
                pstmt.setInt(7, studentId);

                if (pstmt.executeUpdate() == 1) {
                    firstName.clear();
                    middleName.clear();
                    lastName.clear();
                    email.clear();
                    phoneNumber.clear();
                    genderToggleGroup.selectToggle(null); // Clear gender selection
                    loadStudent();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void delete() {
        Student selectedStudent = table.getSelectionModel().getSelectedItem();
        if (selectedStudent != null) {
            String sql = "DELETE FROM students WHERE id = ?";
            try {
                PreparedStatement pstmt = db.getConnection().prepareStatement(sql);
                pstmt.setInt(1, selectedStudent.getId());
                pstmt.executeUpdate();

                studentList.remove(selectedStudent);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void edit() {
        Student selectedStudent = table.getSelectionModel().getSelectedItem();
        if (selectedStudent != null) {
            firstName.setText(selectedStudent.getFirstName());
            middleName.setText(selectedStudent.getMiddleName());
            lastName.setText(selectedStudent.getLastName());
            phoneNumber.setText(selectedStudent.getPhoneNumber());
            email.setText(selectedStudent.getEmail());

            // Set the selected gender based on the student's data
            if ("Male".equals(selectedStudent.getGender())) {
                genderToggleGroup.selectToggle(maleRadioButton);
            } else {
                genderToggleGroup.selectToggle(femaleRadioButton);
            }

            isEditing = true;
            studentId = selectedStudent.getId();
        }
    }
}
