package com.booking.slot;

import com.booking.App;
import com.booking.slots.repository.Slot;
import com.booking.slots.repository.SlotRepository;
import com.booking.toggles.Features;
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
import org.togglz.junit5.AllDisabled;
import org.togglz.junit5.AllEnabled;
import org.togglz.testing.TestFeatureManager;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;

import static com.booking.users.Role.Code.ADMIN;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = App.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@WithMockUser(roles = ADMIN)
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
    @AllEnabled(Features.class)
    void shouldReturnAllAvailableSlotsWhenFeatureIsEnabled(TestFeatureManager testFeatureManager) throws Exception {
        final Slot slotOne = slotRepository.save(new Slot("slot1", Time.valueOf("09:00:00"), Time.valueOf("12:30:00")));
        final Slot slotTwo = slotRepository.save(new Slot("slot2", Time.valueOf("13:30:00"), Time.valueOf("17:00:00")));
        Date date = Date.valueOf(LocalDate.now().plusDays(1));

        mockMvc.perform(get("/slots?date=" + date))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        "{\"slots\":[{\"id\":" + slotOne.getId() + ",\"name\":\"slot1\",\"startTime\":\"9:00 AM\",\"endTime\":\"12:30 PM\"}," +
                                "{\"id\":" + slotTwo.getId() + ",\"name\":\"slot2\",\"startTime\":\"1:30 PM\",\"endTime\":\"5:00 PM\"}]}"));
    }

    @WithMockUser
    @Test
    @AllEnabled(Features.class)
    void shouldNotReturnAllAvailableSlotsWhenUserIsNotAdminAndFeatureIsEnabled() throws Exception {
        Date date = Date.valueOf(LocalDate.now().plusDays(1));

        mockMvc.perform(get("/slots?date=" + date))
                .andExpect(status().isForbidden());
    }

    @Test
    @AllEnabled(Features.class)
    void shouldReturnBadRequestWhenPastDateIsGivenWhenFeatureIsEnabled() throws Exception {
        mockMvc.perform(get("/slots?date=2022-04-21"))
                .andExpect(status().isBadRequest());

    }

    @Test
    @AllDisabled(Features.class)
    void shouldNotReturnSlotsWhenFeatureIsDisabled(TestFeatureManager testFeatureManager) throws Exception {
        Date date = Date.valueOf(LocalDate.now().plusDays(1));
        mockMvc.perform(get("/slots?date=" + date))
                .andExpect(status().isBadRequest());
    }
}
