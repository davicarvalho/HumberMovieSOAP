/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package handlers;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.soap.Node;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import org.w3c.dom.NodeList;

/**
 *
 * @author davicarvalho
 */
public class DisneyHandler  implements SOAPHandler<SOAPMessageContext>{

     @Override
    public Set<QName> getHeaders() {
        return new HashSet<QName>();
    }

    @Override
    public boolean handleMessage(SOAPMessageContext context) {
        try {
            SOAPMessage message = context.getMessage();
            QName op = (QName) context.get(MessageContext.WSDL_OPERATION);
            String operationName = op.getLocalPart();
            Node node = (Node) message.getSOAPBody().getFirstChild();
            
            boolean outbound = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
            if(!outbound && 
                    (operationName.contains("add") || operationName.contains("update") )){
                NodeList nodeList = node.getFirstChild().getChildNodes();
                int length = nodeList.getLength();
                for(int i = 0; i< length; i++){
                    org.w3c.dom.Node n = nodeList.item(i);
                    String content = n.getTextContent();
                    if(content!= null && content.toLowerCase().contains("disney")){
                       return false; 
                    }
                }
            }
            
        } catch (Exception ex) {
            Logger.getLogger(DisneyHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
       return true;
    }

    @Override
    public boolean handleFault(SOAPMessageContext context) {
        return true;
    }

    @Override
    public void close(MessageContext context) {
        
    }
    
}
