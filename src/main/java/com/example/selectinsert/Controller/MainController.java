package com.example.selectinsert.Controller;

import com.example.selectinsert.Dao.DepartmentDaoImpl;
import com.example.selectinsert.Dao.FacultyDaoImpl;
import com.example.selectinsert.Entity.Department;
import com.example.selectinsert.Entity.Faculty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;


public class MainController implements Initializable {
    @FXML
    private TextField txtFacultyName;
    @FXML
    private TextField txtDepartmentName;
    @FXML
    private ComboBox<Faculty> comboFaculty;
    @FXML
    private TableView<Faculty> tableFaculty;
    @FXML
    private TableColumn<Faculty, Integer> facultycol01;
    @FXML
    private TableColumn<Faculty, String> facultycol02;
    @FXML
    private TableView<Department> tableDepartment;
    @FXML
    private TableColumn<Department, Integer> department01;
    @FXML
    private TableColumn<Department, String> department02;
    @FXML
    private TableColumn<Department, Faculty> department03;

    private ObservableList<Faculty> faculties;
    private ObservableList<Department> departments;
    private FacultyDaoImpl facultyDao;
    private DepartmentDaoImpl departmentDao;
    @FXML
    private Button btnSaveFaculty;
    @FXML
    private Button btnUpdateFaculty;
    @FXML
    private Button btnDeleteFaculty;
    @FXML
    private Button btnSaveDepartment;
    @FXML
    private Button btnUpdateDepartment;
    @FXML
    private Button btnDeleteDepartment;
    private Faculty selectedFaculty;
    private Department selectedDepartment;


    @FXML
    private void saveFacultyAction(ActionEvent actionEvent) {
        if (txtFacultyName.getText().trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please fill faculty name");
            alert.showAndWait();
        } else {
            Faculty faculty = new Faculty();
            faculty.setName(txtFacultyName.getText().trim());

            try {
                if (facultyDao.addData(faculty) == 1) {
                    faculties.clear();
                    faculties.addAll(facultyDao.fecthAll());
                    resetFaculty();
                }
            } catch (SQLException | ClassNotFoundException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    @FXML
    private void saveDepartmentAction(ActionEvent actionEvent) {
        if (txtDepartmentName.getText().trim().isEmpty() || comboFaculty.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please fill department name & select faculty data");
            alert.showAndWait();
        } else {
            Department department = new Department();
            department.setName(txtDepartmentName.getText().trim());
            department.setFaculty(comboFaculty.getValue());

            try {
                if (departmentDao.addData(department) == 1) {
                    departments.clear();
                    departments.addAll(departmentDao.fecthAll());
                    resetDepartment();
                }
            } catch (SQLException | ClassNotFoundException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        facultyDao = new FacultyDaoImpl();
        departmentDao = new DepartmentDaoImpl();
        faculties = FXCollections.observableArrayList();
        departments = FXCollections.observableArrayList();

        try {
            faculties.addAll(facultyDao.fecthAll());
            departments.addAll(departmentDao.fecthAll());
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        comboFaculty.setItems(faculties);
        tableFaculty.setItems(faculties);
        facultycol01.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getId()).asObject());
        facultycol02.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));

        tableDepartment.setItems(departments);
        department01.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getId()).asObject());
        department02.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        department03.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getFaculty()));

    }

    @FXML
    private void updateFacultyAction(ActionEvent actionEvent) {
        if (txtFacultyName.getText().trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please fill faculty name");
            alert.showAndWait();
        } else {
            selectedFaculty.setName(txtFacultyName.getText().trim());
            try {
                if (facultyDao.updateData(selectedFaculty) == 1) {
                    faculties.clear();
                    faculties.addAll(facultyDao.fecthAll());
                    resetFaculty();
                }
            } catch (SQLException | ClassNotFoundException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    @FXML
    private void deleteFacultyAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        deleteObject(selectedFaculty);
    }

    @FXML
    private void tableFacultyCliked(MouseEvent mouseEvent) {
        selectedFaculty = tableFaculty.getSelectionModel().getSelectedItem();
        if (selectedFaculty != null) {
            txtFacultyName.setText(selectedFaculty.getName());
            btnSaveFaculty.setDisable(true);
            btnUpdateFaculty.setDisable(false);
            btnDeleteFaculty.setDisable(false);
        }
    }

    @FXML
    private void updateDepartmentAction(ActionEvent actionEvent) {
        if (txtDepartmentName.getText().trim().isEmpty() || comboFaculty == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("please fill department name and faculty");
            alert.showAndWait();
        } else {
            selectedDepartment.setName(txtDepartmentName.getText().trim());
            selectedDepartment.setFaculty(comboFaculty.getValue());
            try {
                if (departmentDao.updateData(selectedDepartment) == 1) {
                    departments.clear();
                    departments.addAll(departmentDao.fecthAll());
                    resetDepartment();
                }
            } catch (SQLException | ClassNotFoundException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    @FXML
    private void deleteDepartmentAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        deleteObject(selectedDepartment);
    }

    @FXML
    private void tableDepartmentCliked(MouseEvent mouseEvent) {
        selectedDepartment = tableDepartment.getSelectionModel().getSelectedItem();
        if (selectedDepartment != null) {
            txtDepartmentName.setText(selectedDepartment.getName());
            btnSaveDepartment.setDisable(true);
            btnUpdateDepartment.setDisable(false);
            btnDeleteDepartment.setDisable(false);
        }
    }

    private void resetFaculty() {
        txtFacultyName.clear();
        selectedFaculty = null;
        btnSaveFaculty.setDisable(false);
        btnUpdateFaculty.setDisable(true);
        btnDeleteFaculty.setDisable(true);
    }

    private void resetDepartment() {
        txtDepartmentName.clear();
        selectedDepartment = null;
        btnSaveDepartment.setDisable(false);
        btnUpdateDepartment.setDisable(true);
        btnDeleteDepartment.setDisable(true);
    }

    private void deleteObject(Object object) throws SQLException, ClassNotFoundException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Are you sure want to delete?");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            if (object instanceof Faculty) {
                try {
                    if (facultyDao.deleteData(selectedFaculty) == 1) {
                        faculties.clear();
                        faculties.addAll(facultyDao.fecthAll());
                        resetFaculty();
                    }
                } catch (SQLException | ClassNotFoundException throwables) {
                    throwables.printStackTrace();
                }
            } else if (object instanceof Department) {
                try {
                    if (departmentDao.deleteData(selectedDepartment) == 1) {
                        departments.clear();
                        departments.addAll(departmentDao.fecthAll());
                        resetDepartment();
                    }
                } catch (SQLException | ClassNotFoundException throwables) {
                    throwables.printStackTrace();
                }
            }
            }
        }
    }