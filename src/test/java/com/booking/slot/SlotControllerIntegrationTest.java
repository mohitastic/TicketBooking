package com.booking.slot;

import com.booking.App;
import com.booking.slots.repository.Slot;
import com.booking.slots.repository.SlotRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Time;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = App.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@WithMockUser
public class SlotControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SlotRepository slotRepository;

    @BeforeEach
    public void before() {
        slotRepository.deleteAll();
    }

    @AfterEach
    public void after() {
        slotRepository.deleteAll();
    }

    @Test
    void shouldReturnAllAvailableSlots() throws Exception {
        final Slot slotOne = slotRepository.save(new Slot("slot1", Time.valueOf("09:00:00"), Time.valueOf("12:30:00")));
        final Slot slotTwo = slotRepository.save(new Slot("slot2", Time.valueOf("13:30:00"), Time.valueOf("17:00:00")));

        mockMvc.perform(get("/slots?date=2022-08-01"))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        "{\"slots\":[{\"id\":" + slotOne.getId() + ",\"name\":\"slot1\",\"startTime\":\"9:00 AM\",\"endTime\":\"12:30 PM\"}," +
                                "{\"id\":" + slotTwo.getId() + ",\"name\":\"slot2\",\"startTime\":\"1:30 PM\",\"endTime\":\"5:00 PM\"}]}"));
    }
}
