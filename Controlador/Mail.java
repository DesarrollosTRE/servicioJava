package Controlador;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author PROGRAMADOR
 */
public class Mail {
    
    private String username;
    private String password;
    private String mensaje;
    private String asunto;
    private String para;

    public Mail(String username, String password, String para) {
        this.username = username;
        this.password = password;
        this.para = para;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje=mensaje;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getPara() {
        return para;
    }

    public void setPara(String para) {
        this.para = para;
    }

    //metodos
    public void enviarEmail() throws Exception{  
    Properties props = new Properties();
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.host", "smtp.gmail.com");
    props.put("mail.smtp.port", "587");
 
    Session session = Session.getInstance(props,
    new javax.mail.Authenticator() {
    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(getUsername(), getPassword());
                    }
                });
 
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(getUsername()));
            message.setRecipients(Message.RecipientType.TO,
            InternetAddress.parse(getPara()));
            message.setSubject(getAsunto());
            message.setContent(getMensaje(), "text/html");
            Transport.send(message);
        } catch (MessagingException e) {
            throw new Exception("Se ha producido un error al enviar email "+e.getMessage());
        }
    }
}
