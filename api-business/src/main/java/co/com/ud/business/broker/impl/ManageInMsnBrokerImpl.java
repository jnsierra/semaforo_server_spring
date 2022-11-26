/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.com.ud.business.broker.impl;

import co.com.ud.business.broker.ManageInMsnBroker;
import co.com.ud.utiles.dto.MensajeBrokerDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Jesus.Sierra
 */
@Slf4j
public class ManageInMsnBrokerImpl implements ManageInMsnBroker {
    
    private ObjectMapper objectMapper;

    public ManageInMsnBrokerImpl() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public Optional<String> tipoMsn(String msn) {
        if (Objects.nonNull(msn) && !"".equalsIgnoreCase(msn)) {
            if (msn.contains("MSNSISTEMA")) {
                return Optional.of("SISTEMA");
            }
            return Optional.of("CONSULTA");
        }

        return Optional.empty();
    }
    @Override
    public String extracItemString(Integer item, String msn){
        String[] parts = msn.split("\\|");
        return parts[item];
    }

    @Override
    public Optional<MensajeBrokerDto> generateMsn(String msn) {
        try {
            MensajeBrokerDto mensaje = this.objectMapper.readValue(msn, MensajeBrokerDto.class);
            return Optional.of(mensaje);
        } catch (JsonProcessingException ex) {
            log.error("Fallo al convertir objeto {} ", msn, ex);
        }
        return Optional.empty();
    }

}
