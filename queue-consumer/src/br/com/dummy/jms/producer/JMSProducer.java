package br.com.dummy.jms.producer;

import br.com.dummy.jms.bean.User;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;
  
public class JMSProducer {
  
    public void queueProducer(String string) {
        QueueConnectionFactory queueConnectionFactory;
        QueueConnection queueConnection = null;
        QueueSession queueSession;
        Queue queue;
        QueueSender queueSender;
  
        // Aqui ser�o criadas as propriedades para o servi�o JNDI.
        Properties properties = new Properties();
        //propriedade que define qual classe implementa o servidor JNDI.
        properties.put(Context.INITIAL_CONTEXT_FACTORY, 
                "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        //propriedade que define a URL de conex�o com a fila no provedor
        properties.put(Context.PROVIDER_URL, "tcp://localhost:61616");
        //propriedade que define a fila
        properties.put("queue.testQueue", "testQueue");
        InitialContext iContext = null;
        try {
            //Inicia o contexto com as configura��es contidas nas propriedades.
            iContext = new InitialContext(properties);
            //Busca pelo Connection Factory apropriado para criar a f�brica de conex�o.
            queueConnectionFactory = (QueueConnectionFactory) iContext.lookup("ConnectionFactory");
            //Cria uma conex�o a partir da f�brica de conex�es.
            queueConnection = queueConnectionFactory.createQueueConnection();
            //Inicia a conex�o.
            queueConnection.start();
            //Cria uma sess�o a partir da conex�o.
            queueSession = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            //Busca pelo destino queue apropriado.
            queue = (Queue) iContext.lookup("testQueue");
            //Cria um objeto QueueSender com o destino espec�fico.
            queueSender = queueSession.createSender(queue);
            //Cria o corpo da mensagem a partir da sess�o e do m�todo createObjectMessage().
            ObjectMessage objectMessage = queueSession.createObjectMessage(string);
            //Envia a mensagem em um objetoMessage contento o objeto User.
            queueSender.send(objectMessage);
  
            System.out.println("Mensagem enviada com sucesso!");
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (JMSException e) {
            e.printStackTrace();
        } finally {
            try {
                //Fecha a conex�o
                if (queueConnection != null) {
                    queueConnection.close();
                }
                //Fecha o contexto
                if (iContext != null) {
                    iContext.close();
                }
            } catch (JMSException e) {
                e.printStackTrace();
            } catch (NamingException e) {
                e.printStackTrace();
            }
        }
    }
  
    public static void main(String[] args) {
        /*User user = new User();
        user.setId(System.currentTimeMillis());
        user.setFirstName("Marcio");
        user.setSurname("de Souza");*/
    	
    	String str = "Ericson de Barros Machado";
  
        new JMSProducer().queueProducer(str);
    }
}
