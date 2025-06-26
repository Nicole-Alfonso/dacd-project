
import org.eventstore.listener.GenericEventListener;
import org.eventstore.writer.EventWriter;
import org.junit.jupiter.api.Test;

import javax.jms.Message;
import javax.jms.TextMessage;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

class GenericEventListenerTest {

    @Test
    void testOnMessage_ValidTextMessage_CallsWriter() throws Exception {
        // Arrange
        String topic = "HotelPrice";
        String json = "{\"ss\":\"Test\",\"ts\":\"2023-06-20T00:00:00Z\"}";
        EventWriter writer = mock(EventWriter.class);
        GenericEventListener listener = new GenericEventListener(topic, writer);

        TextMessage message = mock(TextMessage.class);
        when(message.getText()).thenReturn(json);

        // Act
        listener.onMessage(message);

        // Assert
        verify(writer).write(topic, json);
    }

    @Test
    void testOnMessage_InvalidMessage_DoesNotThrow() {
        EventWriter writer = mock(EventWriter.class);
        GenericEventListener listener = new GenericEventListener("TestTopic", writer);

        Message dummy = mock(javax.jms.BytesMessage.class);

        // No debe lanzar excepción
        assertDoesNotThrow(() -> listener.onMessage(dummy));
        verifyNoInteractions(writer);
    }
}
