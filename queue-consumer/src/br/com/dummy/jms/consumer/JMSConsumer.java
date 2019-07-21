package br.com.dummy.jms.consumer;

import br.com.dummy.jms.bean.User;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;
  
public class JMSConsumer implements MessageListener {
  
    public void queueConsumer() {
        QueueConnectionFactory queueConnectionFactory;
        QueueConnection queueConnection = null;
        QueueSession queueSession;
        Queue queue;
        QueueReceiver queueReceiver;
  
        //Aqui ser�o criadas as propriedades para o servi�o JNDI.
        Properties properties = new Properties();
        //Propriedade que define qual classe implementa o servidor JNDI.
        properties.put(Context.INITIAL_CONTEXT_FACTORY,
                "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        //Propriedade que define a URL de conex�o com a fila no provedor.
        properties.put(Context.PROVIDER_URL, "tcp://localhost:61616");
        //Propriedade que define a fila no contexto.
        properties.put("queue.testQueue", "testQueue");
        InitialContext iContext = null;
        try {
            //Inicia o contexto com as configura��es das propriedades.
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
            //Cria um objeto QueueReceiver com o destino espec�fico.
            queueReceiver = queueSession.createReceiver(queue);
            //Inicia o ouvinte que ir� capturar as mensagem no m�todo onMessage().
            queueReceiver.setMessageListener(this);
  
            System.out.println("Ouvinte ativado!");
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (JMSException e) {
            e.printStackTrace();
        } finally {
            try {
                if (iContext != null) {
                    iContext.close();
                }
            } catch (NamingException e) {
                e.printStackTrace();
            }
        }
    }
  
    @Override
    public void onMessage(Message message) {
        try {
            //Testa se a mensagem � realmente do tipo ObjectMessage.
            if (message instanceof TextMessage) {
                //Transforma o objeto message em objectMessage.
                ObjectMessage objectMessage = (ObjectMessage) message;
                //Captura pelo objectMessage o objeto User por meio do m�todo getObject().
                String str = (String) objectMessage.getObject();
                //Imprime no console o objeto user.
                System.out.println(str.toString());
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
  
    public static void main(String[] args) {
        new JMSConsumer().queueConsumer();
    }
}
