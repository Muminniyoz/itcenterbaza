package uz.itcenterbaza.web.rest;

import uz.itcenterbaza.ItcenterbazaApp;
import uz.itcenterbaza.domain.Participant;
import uz.itcenterbaza.domain.Student;
import uz.itcenterbaza.domain.Course;
import uz.itcenterbaza.repository.ParticipantRepository;
import uz.itcenterbaza.service.ParticipantService;
import uz.itcenterbaza.service.dto.ParticipantDTO;
import uz.itcenterbaza.service.mapper.ParticipantMapper;
import uz.itcenterbaza.service.dto.ParticipantCriteria;
import uz.itcenterbaza.service.ParticipantQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import uz.itcenterbaza.domain.enumeration.ParticipantStatus;
/**
 * Integration tests for the {@link ParticipantResource} REST controller.
 */
@SpringBootTest(classes = ItcenterbazaApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ParticipantResourceIT {

    private static final LocalDate DEFAULT_STARTING_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_STARTING_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_STARTING_DATE = LocalDate.ofEpochDay(-1L);

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final ParticipantStatus DEFAULT_STATUS = ParticipantStatus.ACTIVE;
    private static final ParticipantStatus UPDATED_STATUS = ParticipantStatus.LEAVE;

    private static final String DEFAULT_CONTRACT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_CONTRACT_NUMBER = "BBBBBBBBBB";

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private ParticipantMapper participantMapper;

    @Autowired
    private ParticipantService participantService;

    @Autowired
    private ParticipantQueryService participantQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restParticipantMockMvc;

    private Participant participant;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Participant createEntity(EntityManager em) {
        Participant participant = new Participant()
            .startingDate(DEFAULT_STARTING_DATE)
            .active(DEFAULT_ACTIVE)
            .status(DEFAULT_STATUS)
            .contractNumber(DEFAULT_CONTRACT_NUMBER);
        return participant;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Participant createUpdatedEntity(EntityManager em) {
        Participant participant = new Participant()
            .startingDate(UPDATED_STARTING_DATE)
            .active(UPDATED_ACTIVE)
            .status(UPDATED_STATUS)
            .contractNumber(UPDATED_CONTRACT_NUMBER);
        return participant;
    }

    @BeforeEach
    public void initTest() {
        participant = createEntity(em);
    }

    @Test
    @Transactional
    public void createParticipant() throws Exception {
        int databaseSizeBeforeCreate = participantRepository.findAll().size();
        // Create the Participant
        ParticipantDTO participantDTO = participantMapper.toDto(participant);
        restParticipantMockMvc.perform(post("/api/participants")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(participantDTO)))
            .andExpect(status().isCreated());

        // Validate the Participant in the database
        List<Participant> participantList = participantRepository.findAll();
        assertThat(participantList).hasSize(databaseSizeBeforeCreate + 1);
        Participant testParticipant = participantList.get(participantList.size() - 1);
        assertThat(testParticipant.getStartingDate()).isEqualTo(DEFAULT_STARTING_DATE);
        assertThat(testParticipant.isActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testParticipant.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testParticipant.getContractNumber()).isEqualTo(DEFAULT_CONTRACT_NUMBER);
    }

    @Test
    @Transactional
    public void createParticipantWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = participantRepository.findAll().size();

        // Create the Participant with an existing ID
        participant.setId(1L);
        ParticipantDTO participantDTO = participantMapper.toDto(participant);

        // An entity with an existing ID cannot be created, so this API call must fail
        restParticipantMockMvc.perform(post("/api/participants")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(participantDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Participant in the database
        List<Participant> participantList = participantRepository.findAll();
        assertThat(participantList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllParticipants() throws Exception {
        // Initialize the database
        participantRepository.saveAndFlush(participant);

        // Get all the participantList
        restParticipantMockMvc.perform(get("/api/participants?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(participant.getId().intValue())))
            .andExpect(jsonPath("$.[*].startingDate").value(hasItem(DEFAULT_STARTING_DATE.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].contractNumber").value(hasItem(DEFAULT_CONTRACT_NUMBER)));
    }
    
    @Test
    @Transactional
    public void getParticipant() throws Exception {
        // Initialize the database
        participantRepository.saveAndFlush(participant);

        // Get the participant
        restParticipantMockMvc.perform(get("/api/participants/{id}", participant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(participant.getId().intValue()))
            .andExpect(jsonPath("$.startingDate").value(DEFAULT_STARTING_DATE.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.contractNumber").value(DEFAULT_CONTRACT_NUMBER));
    }


    @Test
    @Transactional
    public void getParticipantsByIdFiltering() throws Exception {
        // Initialize the database
        participantRepository.saveAndFlush(participant);

        Long id = participant.getId();

        defaultParticipantShouldBeFound("id.equals=" + id);
        defaultParticipantShouldNotBeFound("id.notEquals=" + id);

        defaultParticipantShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultParticipantShouldNotBeFound("id.greaterThan=" + id);

        defaultParticipantShouldBeFound("id.lessThanOrEqual=" + id);
        defaultParticipantShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllParticipantsByStartingDateIsEqualToSomething() throws Exception {
        // Initialize the database
        participantRepository.saveAndFlush(participant);

        // Get all the participantList where startingDate equals to DEFAULT_STARTING_DATE
        defaultParticipantShouldBeFound("startingDate.equals=" + DEFAULT_STARTING_DATE);

        // Get all the participantList where startingDate equals to UPDATED_STARTING_DATE
        defaultParticipantShouldNotBeFound("startingDate.equals=" + UPDATED_STARTING_DATE);
    }

    @Test
    @Transactional
    public void getAllParticipantsByStartingDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        participantRepository.saveAndFlush(participant);

        // Get all the participantList where startingDate not equals to DEFAULT_STARTING_DATE
        defaultParticipantShouldNotBeFound("startingDate.notEquals=" + DEFAULT_STARTING_DATE);

        // Get all the participantList where startingDate not equals to UPDATED_STARTING_DATE
        defaultParticipantShouldBeFound("startingDate.notEquals=" + UPDATED_STARTING_DATE);
    }

    @Test
    @Transactional
    public void getAllParticipantsByStartingDateIsInShouldWork() throws Exception {
        // Initialize the database
        participantRepository.saveAndFlush(participant);

        // Get all the participantList where startingDate in DEFAULT_STARTING_DATE or UPDATED_STARTING_DATE
        defaultParticipantShouldBeFound("startingDate.in=" + DEFAULT_STARTING_DATE + "," + UPDATED_STARTING_DATE);

        // Get all the participantList where startingDate equals to UPDATED_STARTING_DATE
        defaultParticipantShouldNotBeFound("startingDate.in=" + UPDATED_STARTING_DATE);
    }

    @Test
    @Transactional
    public void getAllParticipantsByStartingDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        participantRepository.saveAndFlush(participant);

        // Get all the participantList where startingDate is not null
        defaultParticipantShouldBeFound("startingDate.specified=true");

        // Get all the participantList where startingDate is null
        defaultParticipantShouldNotBeFound("startingDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllParticipantsByStartingDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        participantRepository.saveAndFlush(participant);

        // Get all the participantList where startingDate is greater than or equal to DEFAULT_STARTING_DATE
        defaultParticipantShouldBeFound("startingDate.greaterThanOrEqual=" + DEFAULT_STARTING_DATE);

        // Get all the participantList where startingDate is greater than or equal to UPDATED_STARTING_DATE
        defaultParticipantShouldNotBeFound("startingDate.greaterThanOrEqual=" + UPDATED_STARTING_DATE);
    }

    @Test
    @Transactional
    public void getAllParticipantsByStartingDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        participantRepository.saveAndFlush(participant);

        // Get all the participantList where startingDate is less than or equal to DEFAULT_STARTING_DATE
        defaultParticipantShouldBeFound("startingDate.lessThanOrEqual=" + DEFAULT_STARTING_DATE);

        // Get all the participantList where startingDate is less than or equal to SMALLER_STARTING_DATE
        defaultParticipantShouldNotBeFound("startingDate.lessThanOrEqual=" + SMALLER_STARTING_DATE);
    }

    @Test
    @Transactional
    public void getAllParticipantsByStartingDateIsLessThanSomething() throws Exception {
        // Initialize the database
        participantRepository.saveAndFlush(participant);

        // Get all the participantList where startingDate is less than DEFAULT_STARTING_DATE
        defaultParticipantShouldNotBeFound("startingDate.lessThan=" + DEFAULT_STARTING_DATE);

        // Get all the participantList where startingDate is less than UPDATED_STARTING_DATE
        defaultParticipantShouldBeFound("startingDate.lessThan=" + UPDATED_STARTING_DATE);
    }

    @Test
    @Transactional
    public void getAllParticipantsByStartingDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        participantRepository.saveAndFlush(participant);

        // Get all the participantList where startingDate is greater than DEFAULT_STARTING_DATE
        defaultParticipantShouldNotBeFound("startingDate.greaterThan=" + DEFAULT_STARTING_DATE);

        // Get all the participantList where startingDate is greater than SMALLER_STARTING_DATE
        defaultParticipantShouldBeFound("startingDate.greaterThan=" + SMALLER_STARTING_DATE);
    }


    @Test
    @Transactional
    public void getAllParticipantsByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        participantRepository.saveAndFlush(participant);

        // Get all the participantList where active equals to DEFAULT_ACTIVE
        defaultParticipantShouldBeFound("active.equals=" + DEFAULT_ACTIVE);

        // Get all the participantList where active equals to UPDATED_ACTIVE
        defaultParticipantShouldNotBeFound("active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllParticipantsByActiveIsNotEqualToSomething() throws Exception {
        // Initialize the database
        participantRepository.saveAndFlush(participant);

        // Get all the participantList where active not equals to DEFAULT_ACTIVE
        defaultParticipantShouldNotBeFound("active.notEquals=" + DEFAULT_ACTIVE);

        // Get all the participantList where active not equals to UPDATED_ACTIVE
        defaultParticipantShouldBeFound("active.notEquals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllParticipantsByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        participantRepository.saveAndFlush(participant);

        // Get all the participantList where active in DEFAULT_ACTIVE or UPDATED_ACTIVE
        defaultParticipantShouldBeFound("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE);

        // Get all the participantList where active equals to UPDATED_ACTIVE
        defaultParticipantShouldNotBeFound("active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllParticipantsByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        participantRepository.saveAndFlush(participant);

        // Get all the participantList where active is not null
        defaultParticipantShouldBeFound("active.specified=true");

        // Get all the participantList where active is null
        defaultParticipantShouldNotBeFound("active.specified=false");
    }

    @Test
    @Transactional
    public void getAllParticipantsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        participantRepository.saveAndFlush(participant);

        // Get all the participantList where status equals to DEFAULT_STATUS
        defaultParticipantShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the participantList where status equals to UPDATED_STATUS
        defaultParticipantShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllParticipantsByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        participantRepository.saveAndFlush(participant);

        // Get all the participantList where status not equals to DEFAULT_STATUS
        defaultParticipantShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the participantList where status not equals to UPDATED_STATUS
        defaultParticipantShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllParticipantsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        participantRepository.saveAndFlush(participant);

        // Get all the participantList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultParticipantShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the participantList where status equals to UPDATED_STATUS
        defaultParticipantShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllParticipantsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        participantRepository.saveAndFlush(participant);

        // Get all the participantList where status is not null
        defaultParticipantShouldBeFound("status.specified=true");

        // Get all the participantList where status is null
        defaultParticipantShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    public void getAllParticipantsByContractNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        participantRepository.saveAndFlush(participant);

        // Get all the participantList where contractNumber equals to DEFAULT_CONTRACT_NUMBER
        defaultParticipantShouldBeFound("contractNumber.equals=" + DEFAULT_CONTRACT_NUMBER);

        // Get all the participantList where contractNumber equals to UPDATED_CONTRACT_NUMBER
        defaultParticipantShouldNotBeFound("contractNumber.equals=" + UPDATED_CONTRACT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllParticipantsByContractNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        participantRepository.saveAndFlush(participant);

        // Get all the participantList where contractNumber not equals to DEFAULT_CONTRACT_NUMBER
        defaultParticipantShouldNotBeFound("contractNumber.notEquals=" + DEFAULT_CONTRACT_NUMBER);

        // Get all the participantList where contractNumber not equals to UPDATED_CONTRACT_NUMBER
        defaultParticipantShouldBeFound("contractNumber.notEquals=" + UPDATED_CONTRACT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllParticipantsByContractNumberIsInShouldWork() throws Exception {
        // Initialize the database
        participantRepository.saveAndFlush(participant);

        // Get all the participantList where contractNumber in DEFAULT_CONTRACT_NUMBER or UPDATED_CONTRACT_NUMBER
        defaultParticipantShouldBeFound("contractNumber.in=" + DEFAULT_CONTRACT_NUMBER + "," + UPDATED_CONTRACT_NUMBER);

        // Get all the participantList where contractNumber equals to UPDATED_CONTRACT_NUMBER
        defaultParticipantShouldNotBeFound("contractNumber.in=" + UPDATED_CONTRACT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllParticipantsByContractNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        participantRepository.saveAndFlush(participant);

        // Get all the participantList where contractNumber is not null
        defaultParticipantShouldBeFound("contractNumber.specified=true");

        // Get all the participantList where contractNumber is null
        defaultParticipantShouldNotBeFound("contractNumber.specified=false");
    }
                @Test
    @Transactional
    public void getAllParticipantsByContractNumberContainsSomething() throws Exception {
        // Initialize the database
        participantRepository.saveAndFlush(participant);

        // Get all the participantList where contractNumber contains DEFAULT_CONTRACT_NUMBER
        defaultParticipantShouldBeFound("contractNumber.contains=" + DEFAULT_CONTRACT_NUMBER);

        // Get all the participantList where contractNumber contains UPDATED_CONTRACT_NUMBER
        defaultParticipantShouldNotBeFound("contractNumber.contains=" + UPDATED_CONTRACT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllParticipantsByContractNumberNotContainsSomething() throws Exception {
        // Initialize the database
        participantRepository.saveAndFlush(participant);

        // Get all the participantList where contractNumber does not contain DEFAULT_CONTRACT_NUMBER
        defaultParticipantShouldNotBeFound("contractNumber.doesNotContain=" + DEFAULT_CONTRACT_NUMBER);

        // Get all the participantList where contractNumber does not contain UPDATED_CONTRACT_NUMBER
        defaultParticipantShouldBeFound("contractNumber.doesNotContain=" + UPDATED_CONTRACT_NUMBER);
    }


    @Test
    @Transactional
    public void getAllParticipantsByStudentIsEqualToSomething() throws Exception {
        // Initialize the database
        participantRepository.saveAndFlush(participant);
        Student student = StudentResourceIT.createEntity(em);
        em.persist(student);
        em.flush();
        participant.setStudent(student);
        participantRepository.saveAndFlush(participant);
        Long studentId = student.getId();

        // Get all the participantList where student equals to studentId
        defaultParticipantShouldBeFound("studentId.equals=" + studentId);

        // Get all the participantList where student equals to studentId + 1
        defaultParticipantShouldNotBeFound("studentId.equals=" + (studentId + 1));
    }


    @Test
    @Transactional
    public void getAllParticipantsByCourseIsEqualToSomething() throws Exception {
        // Initialize the database
        participantRepository.saveAndFlush(participant);
        Course course = CourseResourceIT.createEntity(em);
        em.persist(course);
        em.flush();
        participant.setCourse(course);
        participantRepository.saveAndFlush(participant);
        Long courseId = course.getId();

        // Get all the participantList where course equals to courseId
        defaultParticipantShouldBeFound("courseId.equals=" + courseId);

        // Get all the participantList where course equals to courseId + 1
        defaultParticipantShouldNotBeFound("courseId.equals=" + (courseId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultParticipantShouldBeFound(String filter) throws Exception {
        restParticipantMockMvc.perform(get("/api/participants?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(participant.getId().intValue())))
            .andExpect(jsonPath("$.[*].startingDate").value(hasItem(DEFAULT_STARTING_DATE.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].contractNumber").value(hasItem(DEFAULT_CONTRACT_NUMBER)));

        // Check, that the count call also returns 1
        restParticipantMockMvc.perform(get("/api/participants/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultParticipantShouldNotBeFound(String filter) throws Exception {
        restParticipantMockMvc.perform(get("/api/participants?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restParticipantMockMvc.perform(get("/api/participants/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingParticipant() throws Exception {
        // Get the participant
        restParticipantMockMvc.perform(get("/api/participants/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateParticipant() throws Exception {
        // Initialize the database
        participantRepository.saveAndFlush(participant);

        int databaseSizeBeforeUpdate = participantRepository.findAll().size();

        // Update the participant
        Participant updatedParticipant = participantRepository.findById(participant.getId()).get();
        // Disconnect from session so that the updates on updatedParticipant are not directly saved in db
        em.detach(updatedParticipant);
        updatedParticipant
            .startingDate(UPDATED_STARTING_DATE)
            .active(UPDATED_ACTIVE)
            .status(UPDATED_STATUS)
            .contractNumber(UPDATED_CONTRACT_NUMBER);
        ParticipantDTO participantDTO = participantMapper.toDto(updatedParticipant);

        restParticipantMockMvc.perform(put("/api/participants")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(participantDTO)))
            .andExpect(status().isOk());

        // Validate the Participant in the database
        List<Participant> participantList = participantRepository.findAll();
        assertThat(participantList).hasSize(databaseSizeBeforeUpdate);
        Participant testParticipant = participantList.get(participantList.size() - 1);
        assertThat(testParticipant.getStartingDate()).isEqualTo(UPDATED_STARTING_DATE);
        assertThat(testParticipant.isActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testParticipant.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testParticipant.getContractNumber()).isEqualTo(UPDATED_CONTRACT_NUMBER);
    }

    @Test
    @Transactional
    public void updateNonExistingParticipant() throws Exception {
        int databaseSizeBeforeUpdate = participantRepository.findAll().size();

        // Create the Participant
        ParticipantDTO participantDTO = participantMapper.toDto(participant);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParticipantMockMvc.perform(put("/api/participants")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(participantDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Participant in the database
        List<Participant> participantList = participantRepository.findAll();
        assertThat(participantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteParticipant() throws Exception {
        // Initialize the database
        participantRepository.saveAndFlush(participant);

        int databaseSizeBeforeDelete = participantRepository.findAll().size();

        // Delete the participant
        restParticipantMockMvc.perform(delete("/api/participants/{id}", participant.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Participant> participantList = participantRepository.findAll();
        assertThat(participantList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
