package com.grupo7.dsi.tpai.models.interfaces.state.eventoSismicoStates;

import com.grupo7.dsi.tpai.models.interfaces.state.IStateEventoSismico;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class EstadoEventoConverter implements AttributeConverter<IStateEventoSismico, String> {
    @Override
    public String convertToDatabaseColumn(IStateEventoSismico state) {
        return (state == null) ? null : EstadoEventoRegistry.codeOf(state);
    }
    @Override
    public IStateEventoSismico convertToEntityAttribute(String code) {
        return EstadoEventoRegistry.fromCode(code);
    }
}
