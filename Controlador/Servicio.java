/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author PROGRAMADOR
 */
public class Servicio{
    private volatile Runnable delegate;
//    private volatile ScheduledFuture<?> self;
    
    public void agregarRunnable(Runnable delegate) {
        this.delegate = delegate;
    }
    public void correr(ScheduledExecutorService executor,int timeInitial, long period, TimeUnit unit) {
        executor.scheduleWithFixedDelay(delegate, timeInitial, period, unit);
    }
}

