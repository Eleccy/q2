package com.baesystems.scmWebForms;

import jakarta.inject.Inject;
//import jakarta.ws.rs.BeanParam;
//import jakarta.ws.rs.Consumes;
//import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
//import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
//import static jakarta.ws.rs.core.MediaType.MULTIPART_FORM_DATA;
//import org.jboss.resteasy.reactive.MultipartForm;
//import org.jboss.resteasy.reactive.PartType;
//import org.jboss.resteasy.reactive.RestForm;
//import org.jboss.resteasy.reactive.RestMatrix;
//import org.jboss.resteasy.reactive.RestPath;
//import org.jboss.resteasy.reactive.RestQuery;

import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration.Configuration;

import com.nanotec.jhhjarpackage.jhhjarclass;

// Keycloak
// THIS DOESN'T WORK:
// ?access_token=<access token from user login in Keycloak>
// THIS WORKS:
// using Postman to put the same Access Code (copy red/green/blue text in Keycloak) into Authorisation and Bearer Token selected
// NOTE: tokens are valid for 10 minutes and have to login again to get another
// TO ACCESS REQUEST TOKEN:
// curl --insecure -X POST -d "client_secret=secret" -d "client_id=quarkus-app" -d "username=alice" -d "password=alice" -d "grant_type=password" "http://localhost:54774/realms/quarkus/protocol/openid-connect/token"
// copy returned "access_token" part and can use in POST request in Postman
import org.jboss.resteasy.reactive.NoCache;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.annotation.security.RolesAllowed;

// Markdown
import io.quarkus.qute.TemplateInstance;
import io.quarkus.qute.Template;
import io.quarkus.vault.VaultKVSecretEngine;
import jakarta.ws.rs.PathParam;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/*
    Remember to go to C:\artemis and type "artemis run" to run server.
    https://www.w3schools.com/css/css3_buttons.asp
*/

/**
 * A simple resource showing the last price.
 */
//@Path("/prices")
@Path("/api/v1")
public class PriceResource {

    String lastMessageType = "JH";
    String lastMessageXML = "";
    String validateMessage = "False";
    
    @Inject
    PriceConsumer consumer;

    // Keycloak
    @Inject
    SecurityIdentity securityIdentity;
        
    @GET
    @Path("last")
    @Produces(MediaType.TEXT_PLAIN)
    public String last() {
        System.out.println("LP11");
        return consumer.getLastPrice();
    }
    
    @GET
    @Path("message")
    public void message(
            @QueryParam("messagename") String messageName,
            @QueryParam("validateCB") String messageValidate,
            @QueryParam("messagexml") String messageXml) {
        lastMessageType = messageName;
        validateMessage = messageValidate;
        lastMessageXML  = messageXml;
        System.out.println("Message Name      ="+messageName);
        System.out.println("Message Validate  ="+messageValidate);
        System.out.println("Message XML       ="+messageXml);
        if ("True".equals(messageValidate)) {
            System.out.println("Validate the message");
        } else {
            System.out.println("Don't validate the message");            
        }
        jhhjarclass.printHello();
    }

    @GET
    @Path("lastmessagetype")
    // Keycloak
    @RolesAllowed("user")
    @Produces(MediaType.TEXT_PLAIN)
    public String lastMessageType() {
        System.out.println("LMT="+lastMessageType);
        return lastMessageType;
    }

    @GET
    @Path("lastmessagexml")
    @Produces(MediaType.TEXT_PLAIN)
    public String lastMessageXML() {
        System.out.println("LMX="+lastMessageType);
        return lastMessageXML;
    }
    
    
    
    
    @Inject
    Template markdown;

    @GET  
    @Path("markdown")
//    @Consumes(MediaType.TEXT_HTML)                         
    @Produces(MediaType.TEXT_HTML)
//    public TemplateInstance markdown() {

    // Keycloak
    @RolesAllowed("user")
    public String markdown() {
        System.out.println("markdown called");
        return (markdown.render()); 
    }
    
    
    
    
    
    
    /*
        API for SCMWebForms SendXML used primarily for TestComplete or any other App to send an XML message
        example URL to send a message:
            localhost:8080/api/v1/SendXML?messageName="message Name"&filenameXml="Auto XML Message which could be really big string"
    */
    @GET
    @Path("SendXML")
    
    // Keycloak
    @RolesAllowed("admin")
    
    public String sendXML(
            @QueryParam("messagename") String messageName,
            @QueryParam("filenamexml") String filenameXml) {
        lastMessageType = messageName;
        lastMessageXML  = filenameXml;
        System.out.println("Message Name ="+messageName);
        System.out.println("Filename XML ="+filenameXml);
        
        return "OK";
    }

    
    // Vault - if vault not running then comment this code out and in application.properties as app not start up!
    /*
    @Inject
    VaultKVSecretEngine kvSecretEngine;
    
    @GET
    @Path("/secrets/{vault-path}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getSecrets(@PathParam("vault-path") String vaultPath) {
        return kvSecretEngine.readSecret("myapps/vault-quickstart/" + vaultPath).toString();
    }
    
    @ConfigProperty(name = "a-private-key")
    String privateKey;
    
    @GET
    @Path("/private-key")
    @Produces(MediaType.TEXT_PLAIN)
    public String privateKey() {
        return privateKey;
    }
*/
}
