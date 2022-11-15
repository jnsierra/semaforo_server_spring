/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.com.ud.business.broker.impl;

import co.com.ud.business.broker.MensajesBroker;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author Usuario
 */
@Component
@Scope("singleton")
public class MensajeBrokerImpl implements MensajesBroker{
    
    private SocketClienteBroker socketClienteBroker;

    @Autowired
    public MensajeBrokerImpl(SocketClienteBroker socketClienteBroker) {
        this.socketClienteBroker = socketClienteBroker;
    }

    @Override
    public Optional<Boolean> mensaje(String msn) {
        return Optional.of(Boolean.TRUE);
    }

    @Override
    public void iniciarHilo() {
        socketClienteBroker.start();
    }
    
}