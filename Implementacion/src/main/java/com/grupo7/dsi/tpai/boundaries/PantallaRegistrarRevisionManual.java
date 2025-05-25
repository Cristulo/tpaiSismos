package com.grupo7.dsi.tpai.boundaries;

import com.grupo7.dsi.tpai.dto.DetalleEventoSismicoDTO;
import com.grupo7.dsi.tpai.dto.EventoSismicoTablaDTO;
import com.grupo7.dsi.tpai.controllers.RegistrarRevisionManualController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@Component
public class PantallaRegistrarRevisionManual implements Initializable {

    @Autowired
    private RegistrarRevisionManualController gestor;

    @FXML private AnchorPane rootPane;  // Contenedor base definido en VistaRegistrarRevisionManual.fxml

    // ↓ Estos se inyectan al cargar la tabla ↓
    @FXML private TableView<EventoSismicoTablaDTO> tablaEventos;
    @FXML private TableColumn<EventoSismicoTablaDTO, Boolean> colSeleccionar;
    @FXML private TableColumn<EventoSismicoTablaDTO, String>  colFechaHora;
    @FXML private TableColumn<EventoSismicoTablaDTO, String>  colMagnitud;
    @FXML private TableColumn<EventoSismicoTablaDTO, String>  colLugarOrigen;

    // Estos se inyectan al cargar el detalle
    @FXML private ListView<String> listDatosPrincipales;
    @FXML private VBox boxSeriesPorEstacion;
    @FXML private ListView<String> listCodigosSismograma;

    private Stage stage;

    @Override
    public void initialize(URL loc, ResourceBundle res) {
        // no hago nada: espero al optRegistrar…
    }

    public void optRegistrarResultadoDeRevisionManual() {
        habilitarVentana();
        gestor.registrarResultadoDeRevisionManual();
    }

    private void habilitarVentana() {
        if (stage == null) {
            try {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/views/PantallaRegistrarRevisionManual.fxml")
                );
                loader.setControllerFactory(cls -> this);
                Parent root = loader.load();
                stage = new Stage();
                stage.setTitle("Registrar Revisión Manual");
                stage.setScene(new Scene(root));
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        stage.show();
    }

    public void mostrarEventosSismicosPendientesDeRevision(List<String> raws) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/views/VistaRegistrarRevisionManualTabla.fxml")
            );
            loader.setControllerFactory(cls -> this);
            AnchorPane tablaView = loader.load();

            rootPane.getChildren().setAll(tablaView);
            AnchorPane.setTopAnchor(tablaView, 0.0);
            AnchorPane.setBottomAnchor(tablaView, 0.0);
            AnchorPane.setLeftAnchor(tablaView, 0.0);
            AnchorPane.setRightAnchor(tablaView, 0.0);

            // 1) activa edición
            tablaEventos.setEditable(true);
            colSeleccionar.setEditable(true);

            // 2) configura las columnas
            colSeleccionar.setCellValueFactory(c -> c.getValue().seleccionadoProperty());
            colSeleccionar.setCellFactory(CheckBoxTableCell.forTableColumn(colSeleccionar));
            colFechaHora .setCellValueFactory(c -> c.getValue().fechaHoraProperty());
            colMagnitud .setCellValueFactory(c -> c.getValue().magnitudProperty());
            colLugarOrigen.setCellValueFactory(c -> c.getValue().lugarOrigenProperty());

            // 3) llena con datos
            ObservableList<EventoSismicoTablaDTO> data = FXCollections.observableArrayList(
                    raws.stream().map(l -> {
                        String[] p = l.split(",", -1);
                        return new EventoSismicoTablaDTO(
                                p.length>0?p[0]:"",
                                p.length>1?p[1]:"",
                                p.length>2?p[2]:"",
                                p.length>3?p[3]:""
                        );
                    }).collect(Collectors.toList())
            );
            tablaEventos.setItems(data);

            // 4) forzar sólo uno seleccionado
            data.forEach(e ->
                    e.seleccionadoProperty().addListener((o,old,n) -> {
                        if (n) data.stream()
                                .filter(x->x!=e)
                                .forEach(x->x.seleccionadoProperty().set(false));
                    })
            );

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void tomarSeleccionEventoSismico() {
        EventoSismicoTablaDTO sel = tablaEventos.getItems().stream()
                .filter(EventoSismicoTablaDTO::isSeleccionado)
                .findFirst().orElse(null);

        if (sel == null) {
            new Alert(Alert.AlertType.WARNING,
                    "Debe seleccionar un evento sísmico").showAndWait();
            return;
        }

        gestor.tomarSeleccionEventoSismico(
                sel.getFechaHora(), sel.getOrigenGeneracion()
        );
    }

    public void mostrarDatosEventoSismicoSeleccionado(DetalleEventoSismicoDTO dto) {
        if (dto == null) {
            new Alert(Alert.AlertType.WARNING, "No hay evento sísmico seleccionado.").showAndWait();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/VistaRegistrarRevisionManualDetalleES.fxml")); // ✅ acá estaba el problema
            loader.setControllerFactory(cls -> this); // mantenemos el mismo controller
            AnchorPane detalleView = loader.load();

            rootPane.getChildren().setAll(detalleView);
            AnchorPane.setTopAnchor(detalleView, 0.0);
            AnchorPane.setBottomAnchor(detalleView, 0.0);
            AnchorPane.setLeftAnchor(detalleView, 0.0);
            AnchorPane.setRightAnchor(detalleView, 0.0);

            Platform.runLater(() -> {
                listDatosPrincipales.getItems().setAll(dto.getDatosPrincipales());

                boxSeriesPorEstacion.getChildren().clear();
                dto.getSeriesTemporalesPorEstacion().forEach((estacion, series) -> {
                    Label lblEstacion = new Label("Estación: " + estacion);
                    VBox vboxSeries = new VBox(5);
                    series.forEach(s -> vboxSeries.getChildren().add(new Label(" • " + s)));

                    VBox seccion = new VBox(lblEstacion, vboxSeries);
                    seccion.setSpacing(5);
                    seccion.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 10; -fx-background-radius: 5;");
                    boxSeriesPorEstacion.getChildren().add(seccion);
                });

                listCodigosSismograma.getItems().setAll(dto.getCodigosSismograma());
            });

        } catch (IOException ex) {
            ex.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error al cargar la vista de detalle: " + ex.getMessage()).showAndWait();
        }
    }
}
