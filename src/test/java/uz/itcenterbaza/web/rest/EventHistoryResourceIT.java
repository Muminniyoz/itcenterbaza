package uz.itcenterbaza.web.rest;

import uz.itcenterbaza.ItcenterbazaApp;
import uz.itcenterbaza.domain.EventHistory;
import uz.itcenterbaza.domain.Center;
import uz.itcenterbaza.domain.User;
import uz.itcenterbaza.repository.EventHistoryRepository;
import uz.itcenterbaza.service.EventHistoryService;
import uz.itcenterbaza.service.dto.EventHistoryDTO;
import uz.itcenterbaza.service.mapper.EventHistoryMapper;
import uz.itcenterbaza.service.dto.EventHistoryCriteria;
import uz.itcenterbaza.service.EventHistoryQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static uz.itcenterbaza.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import uz.itcenterbaza.domain.enumeration.EventType;
/**
 * Integration tests for the {@link EventHistoryResource} REST controller.
 */
@SpringBootTest(classes = ItcenterbazaApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class EventHistoryResourceIT {

    private static final EventType DEFAULT_TYPE = EventType.MESSAGE;
    private static final EventType UPDATED_TYPE = EventType.ERROR;

    private static final String DEFAULT_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_TEXT = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    @Autowired
    private EventHistoryRepository eventHistoryRepository;

    @Mock
    private EventHistoryRepository eventHistoryRepositoryMock;

    @Autowired
    private EventHistoryMapper eventHistoryMapper;

    @Mock
    private EventHistoryService eventHistoryServiceMock;

    @Autowired
    private EventHistoryService eventHistoryService;

    @Autowired
    private EventHistoryQueryService eventHistoryQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEventHistoryMockMvc;

    private EventHistory eventHistory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventHistory createEntity(EntityManager em) {
        EventHistory eventHistory = new EventHistory()
            .type(DEFAULT_TYPE)
            .text(DEFAULT_TEXT)
            .time(DEFAULT_TIME);
        return eventHistory;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventHistory createUpdatedEntity(EntityManager em) {
        EventHistory eventHistory = new EventHistory()
            .type(UPDATED_TYPE)
            .text(UPDATED_TEXT)
            .time(UPDATED_TIME);
        return eventHistory;
    }

    @BeforeEach
    public void initTest() {
        eventHistory = createEntity(em);
    }

    @Test
    @Transactional
    public void createEventHistory() throws Exception {
        int databaseSizeBeforeCreate = eventHistoryRepository.findAll().size();
        // Create the EventHistory
        EventHistoryDTO eventHistoryDTO = eventHistoryMapper.toDto(eventHistory);
        restEventHistoryMockMvc.perform(post("/api/event-histories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(eventHistoryDTO)))
            .andExpect(status().isCreated());

        // Validate the EventHistory in the database
        List<EventHistory> eventHistoryList = eventHistoryRepository.findAll();
        assertThat(eventHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        EventHistory testEventHistory = eventHistoryList.get(eventHistoryList.size() - 1);
        assertThat(testEventHistory.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testEventHistory.getText()).isEqualTo(DEFAULT_TEXT);
        assertThat(testEventHistory.getTime()).isEqualTo(DEFAULT_TIME);
    }

    @Test
    @Transactional
    public void createEventHistoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = eventHistoryRepository.findAll().size();

        // Create the EventHistory with an existing ID
        eventHistory.setId(1L);
        EventHistoryDTO eventHistoryDTO = eventHistoryMapper.toDto(eventHistory);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEventHistoryMockMvc.perform(post("/api/event-histories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(eventHistoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the EventHistory in the database
        List<EventHistory> eventHistoryList = eventHistoryRepository.findAll();
        assertThat(eventHistoryList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllEventHistories() throws Exception {
        // Initialize the database
        eventHistoryRepository.saveAndFlush(eventHistory);

        // Get all the eventHistoryList
        restEventHistoryMockMvc.perform(get("/api/event-histories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT)))
            .andExpect(jsonPath("$.[*].time").value(hasItem(sameInstant(DEFAULT_TIME))));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllEventHistoriesWithEagerRelationshipsIsEnabled() throws Exception {
        when(eventHistoryServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEventHistoryMockMvc.perform(get("/api/event-histories?eagerload=true"))
            .andExpect(status().isOk());

        verify(eventHistoryServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllEventHistoriesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(eventHistoryServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEventHistoryMockMvc.perform(get("/api/event-histories?eagerload=true"))
            .andExpect(status().isOk());

        verify(eventHistoryServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getEventHistory() throws Exception {
        // Initialize the database
        eventHistoryRepository.saveAndFlush(eventHistory);

        // Get the eventHistory
        restEventHistoryMockMvc.perform(get("/api/event-histories/{id}", eventHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(eventHistory.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.text").value(DEFAULT_TEXT))
            .andExpect(jsonPath("$.time").value(sameInstant(DEFAULT_TIME)));
    }


    @Test
    @Transactional
    public void getEventHistoriesByIdFiltering() throws Exception {
        // Initialize the database
        eventHistoryRepository.saveAndFlush(eventHistory);

        Long id = eventHistory.getId();

        defaultEventHistoryShouldBeFound("id.equals=" + id);
        defaultEventHistoryShouldNotBeFound("id.notEquals=" + id);

        defaultEventHistoryShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEventHistoryShouldNotBeFound("id.greaterThan=" + id);

        defaultEventHistoryShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEventHistoryShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllEventHistoriesByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        eventHistoryRepository.saveAndFlush(eventHistory);

        // Get all the eventHistoryList where type equals to DEFAULT_TYPE
        defaultEventHistoryShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the eventHistoryList where type equals to UPDATED_TYPE
        defaultEventHistoryShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllEventHistoriesByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eventHistoryRepository.saveAndFlush(eventHistory);

        // Get all the eventHistoryList where type not equals to DEFAULT_TYPE
        defaultEventHistoryShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the eventHistoryList where type not equals to UPDATED_TYPE
        defaultEventHistoryShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllEventHistoriesByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        eventHistoryRepository.saveAndFlush(eventHistory);

        // Get all the eventHistoryList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultEventHistoryShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the eventHistoryList where type equals to UPDATED_TYPE
        defaultEventHistoryShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllEventHistoriesByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventHistoryRepository.saveAndFlush(eventHistory);

        // Get all the eventHistoryList where type is not null
        defaultEventHistoryShouldBeFound("type.specified=true");

        // Get all the eventHistoryList where type is null
        defaultEventHistoryShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    public void getAllEventHistoriesByTextIsEqualToSomething() throws Exception {
        // Initialize the database
        eventHistoryRepository.saveAndFlush(eventHistory);

        // Get all the eventHistoryList where text equals to DEFAULT_TEXT
        defaultEventHistoryShouldBeFound("text.equals=" + DEFAULT_TEXT);

        // Get all the eventHistoryList where text equals to UPDATED_TEXT
        defaultEventHistoryShouldNotBeFound("text.equals=" + UPDATED_TEXT);
    }

    @Test
    @Transactional
    public void getAllEventHistoriesByTextIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eventHistoryRepository.saveAndFlush(eventHistory);

        // Get all the eventHistoryList where text not equals to DEFAULT_TEXT
        defaultEventHistoryShouldNotBeFound("text.notEquals=" + DEFAULT_TEXT);

        // Get all the eventHistoryList where text not equals to UPDATED_TEXT
        defaultEventHistoryShouldBeFound("text.notEquals=" + UPDATED_TEXT);
    }

    @Test
    @Transactional
    public void getAllEventHistoriesByTextIsInShouldWork() throws Exception {
        // Initialize the database
        eventHistoryRepository.saveAndFlush(eventHistory);

        // Get all the eventHistoryList where text in DEFAULT_TEXT or UPDATED_TEXT
        defaultEventHistoryShouldBeFound("text.in=" + DEFAULT_TEXT + "," + UPDATED_TEXT);

        // Get all the eventHistoryList where text equals to UPDATED_TEXT
        defaultEventHistoryShouldNotBeFound("text.in=" + UPDATED_TEXT);
    }

    @Test
    @Transactional
    public void getAllEventHistoriesByTextIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventHistoryRepository.saveAndFlush(eventHistory);

        // Get all the eventHistoryList where text is not null
        defaultEventHistoryShouldBeFound("text.specified=true");

        // Get all the eventHistoryList where text is null
        defaultEventHistoryShouldNotBeFound("text.specified=false");
    }
                @Test
    @Transactional
    public void getAllEventHistoriesByTextContainsSomething() throws Exception {
        // Initialize the database
        eventHistoryRepository.saveAndFlush(eventHistory);

        // Get all the eventHistoryList where text contains DEFAULT_TEXT
        defaultEventHistoryShouldBeFound("text.contains=" + DEFAULT_TEXT);

        // Get all the eventHistoryList where text contains UPDATED_TEXT
        defaultEventHistoryShouldNotBeFound("text.contains=" + UPDATED_TEXT);
    }

    @Test
    @Transactional
    public void getAllEventHistoriesByTextNotContainsSomething() throws Exception {
        // Initialize the database
        eventHistoryRepository.saveAndFlush(eventHistory);

        // Get all the eventHistoryList where text does not contain DEFAULT_TEXT
        defaultEventHistoryShouldNotBeFound("text.doesNotContain=" + DEFAULT_TEXT);

        // Get all the eventHistoryList where text does not contain UPDATED_TEXT
        defaultEventHistoryShouldBeFound("text.doesNotContain=" + UPDATED_TEXT);
    }


    @Test
    @Transactional
    public void getAllEventHistoriesByTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        eventHistoryRepository.saveAndFlush(eventHistory);

        // Get all the eventHistoryList where time equals to DEFAULT_TIME
        defaultEventHistoryShouldBeFound("time.equals=" + DEFAULT_TIME);

        // Get all the eventHistoryList where time equals to UPDATED_TIME
        defaultEventHistoryShouldNotBeFound("time.equals=" + UPDATED_TIME);
    }

    @Test
    @Transactional
    public void getAllEventHistoriesByTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eventHistoryRepository.saveAndFlush(eventHistory);

        // Get all the eventHistoryList where time not equals to DEFAULT_TIME
        defaultEventHistoryShouldNotBeFound("time.notEquals=" + DEFAULT_TIME);

        // Get all the eventHistoryList where time not equals to UPDATED_TIME
        defaultEventHistoryShouldBeFound("time.notEquals=" + UPDATED_TIME);
    }

    @Test
    @Transactional
    public void getAllEventHistoriesByTimeIsInShouldWork() throws Exception {
        // Initialize the database
        eventHistoryRepository.saveAndFlush(eventHistory);

        // Get all the eventHistoryList where time in DEFAULT_TIME or UPDATED_TIME
        defaultEventHistoryShouldBeFound("time.in=" + DEFAULT_TIME + "," + UPDATED_TIME);

        // Get all the eventHistoryList where time equals to UPDATED_TIME
        defaultEventHistoryShouldNotBeFound("time.in=" + UPDATED_TIME);
    }

    @Test
    @Transactional
    public void getAllEventHistoriesByTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventHistoryRepository.saveAndFlush(eventHistory);

        // Get all the eventHistoryList where time is not null
        defaultEventHistoryShouldBeFound("time.specified=true");

        // Get all the eventHistoryList where time is null
        defaultEventHistoryShouldNotBeFound("time.specified=false");
    }

    @Test
    @Transactional
    public void getAllEventHistoriesByTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventHistoryRepository.saveAndFlush(eventHistory);

        // Get all the eventHistoryList where time is greater than or equal to DEFAULT_TIME
        defaultEventHistoryShouldBeFound("time.greaterThanOrEqual=" + DEFAULT_TIME);

        // Get all the eventHistoryList where time is greater than or equal to UPDATED_TIME
        defaultEventHistoryShouldNotBeFound("time.greaterThanOrEqual=" + UPDATED_TIME);
    }

    @Test
    @Transactional
    public void getAllEventHistoriesByTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventHistoryRepository.saveAndFlush(eventHistory);

        // Get all the eventHistoryList where time is less than or equal to DEFAULT_TIME
        defaultEventHistoryShouldBeFound("time.lessThanOrEqual=" + DEFAULT_TIME);

        // Get all the eventHistoryList where time is less than or equal to SMALLER_TIME
        defaultEventHistoryShouldNotBeFound("time.lessThanOrEqual=" + SMALLER_TIME);
    }

    @Test
    @Transactional
    public void getAllEventHistoriesByTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        eventHistoryRepository.saveAndFlush(eventHistory);

        // Get all the eventHistoryList where time is less than DEFAULT_TIME
        defaultEventHistoryShouldNotBeFound("time.lessThan=" + DEFAULT_TIME);

        // Get all the eventHistoryList where time is less than UPDATED_TIME
        defaultEventHistoryShouldBeFound("time.lessThan=" + UPDATED_TIME);
    }

    @Test
    @Transactional
    public void getAllEventHistoriesByTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventHistoryRepository.saveAndFlush(eventHistory);

        // Get all the eventHistoryList where time is greater than DEFAULT_TIME
        defaultEventHistoryShouldNotBeFound("time.greaterThan=" + DEFAULT_TIME);

        // Get all the eventHistoryList where time is greater than SMALLER_TIME
        defaultEventHistoryShouldBeFound("time.greaterThan=" + SMALLER_TIME);
    }


    @Test
    @Transactional
    public void getAllEventHistoriesByCenterIsEqualToSomething() throws Exception {
        // Initialize the database
        eventHistoryRepository.saveAndFlush(eventHistory);
        Center center = CenterResourceIT.createEntity(em);
        em.persist(center);
        em.flush();
        eventHistory.setCenter(center);
        eventHistoryRepository.saveAndFlush(eventHistory);
        Long centerId = center.getId();

        // Get all the eventHistoryList where center equals to centerId
        defaultEventHistoryShouldBeFound("centerId.equals=" + centerId);

        // Get all the eventHistoryList where center equals to centerId + 1
        defaultEventHistoryShouldNotBeFound("centerId.equals=" + (centerId + 1));
    }


    @Test
    @Transactional
    public void getAllEventHistoriesByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        eventHistoryRepository.saveAndFlush(eventHistory);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        eventHistory.setUser(user);
        eventHistoryRepository.saveAndFlush(eventHistory);
        Long userId = user.getId();

        // Get all the eventHistoryList where user equals to userId
        defaultEventHistoryShouldBeFound("userId.equals=" + userId);

        // Get all the eventHistoryList where user equals to userId + 1
        defaultEventHistoryShouldNotBeFound("userId.equals=" + (userId + 1));
    }


    @Test
    @Transactional
    public void getAllEventHistoriesByOpenedUserIsEqualToSomething() throws Exception {
        // Initialize the database
        eventHistoryRepository.saveAndFlush(eventHistory);
        User openedUser = UserResourceIT.createEntity(em);
        em.persist(openedUser);
        em.flush();
        eventHistory.addOpenedUser(openedUser);
        eventHistoryRepository.saveAndFlush(eventHistory);
        Long openedUserId = openedUser.getId();

        // Get all the eventHistoryList where openedUser equals to openedUserId
        defaultEventHistoryShouldBeFound("openedUserId.equals=" + openedUserId);

        // Get all the eventHistoryList where openedUser equals to openedUserId + 1
        defaultEventHistoryShouldNotBeFound("openedUserId.equals=" + (openedUserId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEventHistoryShouldBeFound(String filter) throws Exception {
        restEventHistoryMockMvc.perform(get("/api/event-histories?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT)))
            .andExpect(jsonPath("$.[*].time").value(hasItem(sameInstant(DEFAULT_TIME))));

        // Check, that the count call also returns 1
        restEventHistoryMockMvc.perform(get("/api/event-histories/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEventHistoryShouldNotBeFound(String filter) throws Exception {
        restEventHistoryMockMvc.perform(get("/api/event-histories?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEventHistoryMockMvc.perform(get("/api/event-histories/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingEventHistory() throws Exception {
        // Get the eventHistory
        restEventHistoryMockMvc.perform(get("/api/event-histories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEventHistory() throws Exception {
        // Initialize the database
        eventHistoryRepository.saveAndFlush(eventHistory);

        int databaseSizeBeforeUpdate = eventHistoryRepository.findAll().size();

        // Update the eventHistory
        EventHistory updatedEventHistory = eventHistoryRepository.findById(eventHistory.getId()).get();
        // Disconnect from session so that the updates on updatedEventHistory are not directly saved in db
        em.detach(updatedEventHistory);
        updatedEventHistory
            .type(UPDATED_TYPE)
            .text(UPDATED_TEXT)
            .time(UPDATED_TIME);
        EventHistoryDTO eventHistoryDTO = eventHistoryMapper.toDto(updatedEventHistory);

        restEventHistoryMockMvc.perform(put("/api/event-histories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(eventHistoryDTO)))
            .andExpect(status().isOk());

        // Validate the EventHistory in the database
        List<EventHistory> eventHistoryList = eventHistoryRepository.findAll();
        assertThat(eventHistoryList).hasSize(databaseSizeBeforeUpdate);
        EventHistory testEventHistory = eventHistoryList.get(eventHistoryList.size() - 1);
        assertThat(testEventHistory.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testEventHistory.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testEventHistory.getTime()).isEqualTo(UPDATED_TIME);
    }

    @Test
    @Transactional
    public void updateNonExistingEventHistory() throws Exception {
        int databaseSizeBeforeUpdate = eventHistoryRepository.findAll().size();

        // Create the EventHistory
        EventHistoryDTO eventHistoryDTO = eventHistoryMapper.toDto(eventHistory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventHistoryMockMvc.perform(put("/api/event-histories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(eventHistoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the EventHistory in the database
        List<EventHistory> eventHistoryList = eventHistoryRepository.findAll();
        assertThat(eventHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteEventHistory() throws Exception {
        // Initialize the database
        eventHistoryRepository.saveAndFlush(eventHistory);

        int databaseSizeBeforeDelete = eventHistoryRepository.findAll().size();

        // Delete the eventHistory
        restEventHistoryMockMvc.perform(delete("/api/event-histories/{id}", eventHistory.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EventHistory> eventHistoryList = eventHistoryRepository.findAll();
        assertThat(eventHistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
