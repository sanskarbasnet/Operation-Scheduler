package com.example.operationschedularproject;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert;
import java.io.IOException;
import java.util.*;

public class MainScreenController {
    @FXML
    Label name;
    HealthProfessional currentUser = LogInController.user;
    @FXML
    TableView<Appointment> table;
    @FXML
    private TableColumn<Appointment, String> patientNameColumn;
    @FXML
    private TableColumn<Appointment, String> dateColumn;
    @FXML
    private TableColumn<Appointment, String> typeColumn;
    @FXML
    private TableColumn<Appointment, String> startTimeColumn;
    @FXML
    private TableColumn<Appointment, String> endTimeColumn;
    public static ObservableList<Appointment> appointmentObservableList;
    public static Appointment selectedAppointment;
    public static Stack<UndoAction> undoStack = new Stack<>();
    public static Stack<Appointment> editStack = new Stack<>();

   public void onAdd(ActionEvent e) throws IOException {
       SceneLoader.loadScene("AddAppointment.fxml", e);
   }

    public void onDelete(ActionEvent e){
        selectedAppointment = table.getSelectionModel().getSelectedItem();
        if (selectedAppointment != null) {
            currentUser.diary.delete(selectedAppointment);
            appointmentObservableList.remove(selectedAppointment);
            table.getItems().remove(selectedAppointment);
            undoStack.push(new UndoAction(ActionType.DELETE,selectedAppointment));
        } else {
            AlertDisplay.showAlert(Alert.AlertType.ERROR, "Error", "No appointment selected.");
        }
    }
    public void onEdit(ActionEvent e) throws IOException {
       selectedAppointment = table.getSelectionModel().getSelectedItem();
       editStack.push(selectedAppointment);
        if (selectedAppointment != null){
           SceneLoader.loadScene("EditAppointment.fxml", e);
        } else {
            AlertDisplay.showAlert(Alert.AlertType.ERROR, "Error", "No appointment selected.");
        }
    }
    public void onUndo(ActionEvent e){
      if(undoStack.isEmpty()){
          AlertDisplay.showAlert(Alert.AlertType.ERROR, "Error", "Nothing to undo");
          return;
      }
        UndoAction undoAction = undoStack.pop();
        ActionType actionType = undoAction.getActionType();
        Appointment appointment = (Appointment) undoAction.getObject();

        switch (actionType) {
            case ADD:
                // Undo the add operation
                if (appointment != null) {
                    currentUser.diary.delete(appointment);
                    appointmentObservableList.remove(appointment);
                }
                break;
            case DELETE:
                // Undo the delete operation
                if (appointment != null) {
                    currentUser.diary.add(appointment);
                    appointmentObservableList.add(appointment);
                }
                break;
            case EDIT:
                // Undo the edit operation
                if (appointment != null) {
                    Appointment prevAppointment = editStack.pop();
                    currentUser.diary.edit(prevAppointment, appointment);
                    appointmentObservableList.remove(appointment);
                    appointmentObservableList.add(prevAppointment);
                }
                break;
        }
        table.setItems(appointmentObservableList);
    }
    public void onTaskList(ActionEvent e) throws IOException {
       SceneLoader.loadScene("TaskList.fxml",e);
    }
    @FXML
    public void initialize() {

       try {
           patientNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().patient));
           typeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTreatmentType()));
           dateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDate()));
           startTimeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStartTime()));
           endTimeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEndTime()));
           if(appointmentObservableList != null) {
               table.setItems(appointmentObservableList);
           }
           name.setText("Hi!, " + currentUser.getName());
           table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
       } catch (Exception e){
           System.out.println("error");
       }
    }
}


