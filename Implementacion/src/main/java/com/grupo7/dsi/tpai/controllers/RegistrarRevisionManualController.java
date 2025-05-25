package com.grupo7.dsi.tpai.controllers;

import com.grupo7.dsi.tpai.boundaries.PantallaRegistrarRevisionManual;
import com.grupo7.dsi.tpai.dto.DetalleEventoSismicoDTO;
import com.grupo7.dsi.tpai.models.*;
import com.grupo7.dsi.tpai.service.EstadoService;
import com.grupo7.dsi.tpai.service.EventoSismicoService;
import com.grupo7.dsi.tpai.service.SismografoService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class RegistrarRevisionManualController {


    @Autowired
    private EventoSismicoService eventoSismicoService;
    private List<EventoSismico> eventosBDD;
    @Autowired
    private EstadoService estadoService;
    private List<Estado> estadosBDD;
    @Autowired
    private SismografoService sismografoService;
    private List<Sismografo> sismografosBDD;

    @Autowired @Lazy
    private PantallaRegistrarRevisionManual pantalla;
    private GenerarSismogramaController generarSismogramaController;

    private EventoSismico eventoSismicoSeleccionado;

    @PostConstruct
    private void init() {
        this.eventosBDD = eventoSismicoService.findAll();
        this.estadosBDD = estadoService.findAll();
        this.sismografosBDD = sismografoService.findAll();
        this.generarSismogramaController = new GenerarSismogramaController();
    }

    public void registrarResultadoDeRevisionManual() {
        List<String> raws = buscarEventosSismicosPendientesDeRevision();
        pantalla.mostrarEventosSismicosPendientesDeRevision(raws);
    }

    public List<String> buscarEventosSismicosPendientesDeRevision() {
        List<EventoSismico> eventoSismicosPendientes = new ArrayList<>();
        for (EventoSismico evento : eventosBDD) {
            if (evento.estaPendienteRevision()) {
                eventoSismicosPendientes.add(evento);
            }
        }
        // ordenar por fecha y hora de ocurrencia
        eventoSismicosPendientes = filtrarPorFechayHoraDeOcurrencia(eventoSismicosPendientes);

        // convertir a String con 4 campos separados por coma
        List<String> raws = new ArrayList<>();
        for (EventoSismico evento : eventoSismicosPendientes) {
            // antes ponías clasificación en p[3], ahora repetimos origen:
            String raw = String.join(",",
                    evento.getFechaHoraOcurrencia().toString(),
                    evento.getOrigenGeneracion().getNombre(),
                    String.valueOf(evento.getMagnitud().getNumero()),
                    evento.getOrigenGeneracion().getNombre()    // <-- aquí
            );
            raws.add(raw);
        }
        return raws;
    }

    private List<EventoSismico> filtrarPorFechayHoraDeOcurrencia(List<EventoSismico> eventos) {
        return eventos.stream()
                .sorted(Comparator.comparing(EventoSismico::getFechaHoraOcurrencia))
                .collect(Collectors.toList());
    }

    public void tomarSeleccionEventoSismico(String fechaHoraStr, String origen) {
        LocalDateTime fecha = LocalDateTime.parse(fechaHoraStr);
        // busco el evento sísmico seleccionado
        for (EventoSismico evento : eventosBDD) {
            if (evento.getFechaHoraOcurrencia().equals(fecha)
                    && evento.getOrigenGeneracion().getNombre().equals(origen)) {
                this.eventoSismicoSeleccionado = evento;
            }
        }

        // Cambio de estado del evento sísmico
        cambioEstadoEventoSismico();

        // Buscar los datos del evento sismico
        buscarDatosEventoSismicoSeleccionado();
    }

    private void cambioEstadoEventoSismico() {
        Estado estadoBloqueado = estadosBDD.stream()
                .filter(estado -> estado.sosBloqueadoEnRevision())
                .findFirst()
                .orElse(null);

        // Cambiar el estado del evento sísmico a "Bloqueado"
        this.eventoSismicoSeleccionado.cambioEstadoBloqueadoEnRevision(estadoBloqueado);

    }

    private void buscarDatosEventoSismicoSeleccionado() {
        if (eventoSismicoSeleccionado == null) {
            pantalla.mostrarDatosEventoSismicoSeleccionado(null);
            return;
        }

        DetalleEventoSismicoDTO dto = new DetalleEventoSismicoDTO();

        // Datos principales
        List<String> alcanceClasificacionOrigen = eventoSismicoSeleccionado.buscarDatosEventoSismico();
        dto.setDatosPrincipales(alcanceClasificacionOrigen);

        // Clasificar series
        Map<EstacionSismologica, List<SerieTemporal>> seriesClasificadas = clasificarSeriesTemporales();
        Map<String, List<String>> seriesPorEstacion = new HashMap<>();

        for (Map.Entry<EstacionSismologica, List<SerieTemporal>> entry : seriesClasificadas.entrySet()) {
            String nombreEstacion = entry.getKey().getNombre();
            List<SerieTemporal> series = entry.getValue();
            List<String> seriesStr = new ArrayList<>();

            if (series == null || series.isEmpty()) {
                seriesStr.add("⚠️ No hay series temporales registradas para esta estación.");
            } else {
                for (SerieTemporal serie : series) {
                    String estadoAlarma = (serie.getCondicionAlarma() != null && serie.getCondicionAlarma()) ? "Activada" : "Desactivada";

                    String encabezado = "⏱ Tiempo: " + serie.getFechaHoraRegistro() +
                            " | 🔔 Alarma: " + estadoAlarma +
                            " | 📶 Frecuencia: " + serie.getFrecuenciaMuestreo();
                    seriesStr.add(encabezado);

                    if (serie.getMuestrasSismicas() != null && !serie.getMuestrasSismicas().isEmpty()) {
                        for (MuestraSismica muestra : serie.getMuestrasSismicas()) {
                            String lineaMuestra = "    ↳ Muestra @ " + muestra.getFechaHoraMuestra();
                            seriesStr.add(lineaMuestra);

                            if (muestra.getDetallesMuestraSismica() != null && !muestra.getDetallesMuestraSismica().isEmpty()) {
                                for (DetalleMuestraSismica detalle : muestra.getDetallesMuestraSismica()) {
                                    String lineaDetalle = "        • Valor: " + detalle.getValor();
                                    seriesStr.add(lineaDetalle);
                                }
                            } else {
                                seriesStr.add("        • Sin detalles de muestra");
                            }
                        }
                    } else {
                        seriesStr.add("    • No hay muestras asociadas");
                    }

                    seriesStr.add(""); // Separador visual entre series
                }
            }

            seriesPorEstacion.put(nombreEstacion, seriesStr);
        }
        dto.setSeriesTemporalesPorEstacion(seriesPorEstacion);

        // Códigos de sismogramas
        List<String> codigos = generarSismogramaController
                .generarSismograma(new ArrayList<>(seriesClasificadas.keySet()));
        dto.setCodigosSismograma(codigos);

        // Mostrar datos en la pantalla
        pantalla.mostrarDatosEventoSismicoSeleccionado(dto);
    }

    public Map<EstacionSismologica, List<SerieTemporal>> clasificarSeriesTemporales() {
        Map<EstacionSismologica, List<SerieTemporal>> seriesTemporalesClasificadas = new HashMap<>();

        List<SerieTemporal> seriesEvento = eventoSismicoSeleccionado.getSerieTemporal();

        for (Sismografo sismografo : sismografosBDD) {
            EstacionSismologica estacionSismologica = sismografo.getEstacionSismologica();
            List<SerieTemporal> coincidentes = new ArrayList<>();

            for (SerieTemporal serieEvento : seriesEvento) {
                if (sismografo.getSeriesTemporales().contains(serieEvento)) {
                    coincidentes.add(serieEvento);
                }
            }

            if (!coincidentes.isEmpty()) {
                seriesTemporalesClasificadas.put(estacionSismologica, coincidentes);
            }
        }

        return seriesTemporalesClasificadas;
    }


}