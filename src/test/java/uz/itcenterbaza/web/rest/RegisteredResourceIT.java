package uz.itcenterbaza.web.rest;

import uz.itcenterbaza.ItcenterbazaApp;
import uz.itcenterbaza.domain.Registered;
import uz.itcenterbaza.domain.User;
import uz.itcenterbaza.domain.Course;
import uz.itcenterbaza.repository.RegisteredRepository;
import uz.itcenterbaza.service.RegisteredService;
import uz.itcenterbaza.service.dto.RegisteredDTO;
import uz.itcenterbaza.service.mapper.RegisteredMapper;
import uz.itcenterbaza.service.dto.RegisteredCriteria;
import uz.itcenterbaza.service.RegisteredQueryService;

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

import uz.itcenterbaza.domain.enumeration.Gender;
/**
 * Integration tests for the {@link RegisteredResource} REST controller.
 */
@SpringBootTest(classes = ItcenterbazaApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class RegisteredResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MIDDLE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MIDDLE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_OF_BIRTH = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OF_BIRTH = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_OF_BIRTH = LocalDate.ofEpochDay(-1L);

    private static final Gender DEFAULT_GENDER = Gender.MALE;
    private static final Gender UPDATED_GENDER = Gender.FEMALE;

    private static final LocalDate DEFAULT_REGISTERATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REGISTERATION_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_REGISTERATION_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_TELEPHONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEPHONE = "BBBBBBBBBB";

    private static final String DEFAULT_MOBILE = "AAAAAAAAAA";
    private static final String UPDATED_MOBILE = "BBBBBBBBBB";

    @Autowired
    private RegisteredRepository registeredRepository;

    @Autowired
    private RegisteredMapper registeredMapper;

    @Autowired
    private RegisteredService registeredService;

    @Autowired
    private RegisteredQueryService registeredQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRegisteredMockMvc;

    private Registered registered;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Registered createEntity(EntityManager em) {
        Registered registered = new Registered()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .middleName(DEFAULT_MIDDLE_NAME)
            .email(DEFAULT_EMAIL)
            .dateOfBirth(DEFAULT_DATE_OF_BIRTH)
            .gender(DEFAULT_GENDER)
            .registerationDate(DEFAULT_REGISTERATION_DATE)
            .telephone(DEFAULT_TELEPHONE)
            .mobile(DEFAULT_MOBILE);
        return registered;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Registered createUpdatedEntity(EntityManager em) {
        Registered registered = new Registered()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .middleName(UPDATED_MIDDLE_NAME)
            .email(UPDATED_EMAIL)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .gender(UPDATED_GENDER)
            .registerationDate(UPDATED_REGISTERATION_DATE)
            .telephone(UPDATED_TELEPHONE)
            .mobile(UPDATED_MOBILE);
        return registered;
    }

    @BeforeEach
    public void initTest() {
        registered = createEntity(em);
    }

    @Test
    @Transactional
    public void createRegistered() throws Exception {
        int databaseSizeBeforeCreate = registeredRepository.findAll().size();
        // Create the Registered
        RegisteredDTO registeredDTO = registeredMapper.toDto(registered);
        restRegisteredMockMvc.perform(post("/api/registereds")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(registeredDTO)))
            .andExpect(status().isCreated());

        // Validate the Registered in the database
        List<Registered> registeredList = registeredRepository.findAll();
        assertThat(registeredList).hasSize(databaseSizeBeforeCreate + 1);
        Registered testRegistered = registeredList.get(registeredList.size() - 1);
        assertThat(testRegistered.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testRegistered.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testRegistered.getMiddleName()).isEqualTo(DEFAULT_MIDDLE_NAME);
        assertThat(testRegistered.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testRegistered.getDateOfBirth()).isEqualTo(DEFAULT_DATE_OF_BIRTH);
        assertThat(testRegistered.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testRegistered.getRegisterationDate()).isEqualTo(DEFAULT_REGISTERATION_DATE);
        assertThat(testRegistered.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
        assertThat(testRegistered.getMobile()).isEqualTo(DEFAULT_MOBILE);
    }

    @Test
    @Transactional
    public void createRegisteredWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = registeredRepository.findAll().size();

        // Create the Registered with an existing ID
        registered.setId(1L);
        RegisteredDTO registeredDTO = registeredMapper.toDto(registered);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRegisteredMockMvc.perform(post("/api/registereds")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(registeredDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Registered in the database
        List<Registered> registeredList = registeredRepository.findAll();
        assertThat(registeredList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = registeredRepository.findAll().size();
        // set the field null
        registered.setFirstName(null);

        // Create the Registered, which fails.
        RegisteredDTO registeredDTO = registeredMapper.toDto(registered);


        restRegisteredMockMvc.perform(post("/api/registereds")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(registeredDTO)))
            .andExpect(status().isBadRequest());

        List<Registered> registeredList = registeredRepository.findAll();
        assertThat(registeredList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = registeredRepository.findAll().size();
        // set the field null
        registered.setLastName(null);

        // Create the Registered, which fails.
        RegisteredDTO registeredDTO = registeredMapper.toDto(registered);


        restRegisteredMockMvc.perform(post("/api/registereds")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(registeredDTO)))
            .andExpect(status().isBadRequest());

        List<Registered> registeredList = registeredRepository.findAll();
        assertThat(registeredList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRegistereds() throws Exception {
        // Initialize the database
        registeredRepository.saveAndFlush(registered);

        // Get all the registeredList
        restRegisteredMockMvc.perform(get("/api/registereds?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(registered.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].middleName").value(hasItem(DEFAULT_MIDDLE_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].dateOfBirth").value(hasItem(DEFAULT_DATE_OF_BIRTH.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].registerationDate").value(hasItem(DEFAULT_REGISTERATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)))
            .andExpect(jsonPath("$.[*].mobile").value(hasItem(DEFAULT_MOBILE)));
    }
    
    @Test
    @Transactional
    public void getRegistered() throws Exception {
        // Initialize the database
        registeredRepository.saveAndFlush(registered);

        // Get the registered
        restRegisteredMockMvc.perform(get("/api/registereds/{id}", registered.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(registered.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.middleName").value(DEFAULT_MIDDLE_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.dateOfBirth").value(DEFAULT_DATE_OF_BIRTH.toString()))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.registerationDate").value(DEFAULT_REGISTERATION_DATE.toString()))
            .andExpect(jsonPath("$.telephone").value(DEFAULT_TELEPHONE))
            .andExpect(jsonPath("$.mobile").value(DEFAULT_MOBILE));
    }


    @Test
    @Transactional
    public void getRegisteredsByIdFiltering() throws Exception {
        // Initialize the database
        registeredRepository.saveAndFlush(registered);

        Long id = registered.getId();

        defaultRegisteredShouldBeFound("id.equals=" + id);
        defaultRegisteredShouldNotBeFound("id.notEquals=" + id);

        defaultRegisteredShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRegisteredShouldNotBeFound("id.greaterThan=" + id);

        defaultRegisteredShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRegisteredShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllRegisteredsByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        registeredRepository.saveAndFlush(registered);

        // Get all the registeredList where firstName equals to DEFAULT_FIRST_NAME
        defaultRegisteredShouldBeFound("firstName.equals=" + DEFAULT_FIRST_NAME);

        // Get all the registeredList where firstName equals to UPDATED_FIRST_NAME
        defaultRegisteredShouldNotBeFound("firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllRegisteredsByFirstNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        registeredRepository.saveAndFlush(registered);

        // Get all the registeredList where firstName not equals to DEFAULT_FIRST_NAME
        defaultRegisteredShouldNotBeFound("firstName.notEquals=" + DEFAULT_FIRST_NAME);

        // Get all the registeredList where firstName not equals to UPDATED_FIRST_NAME
        defaultRegisteredShouldBeFound("firstName.notEquals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllRegisteredsByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        registeredRepository.saveAndFlush(registered);

        // Get all the registeredList where firstName in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultRegisteredShouldBeFound("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME);

        // Get all the registeredList where firstName equals to UPDATED_FIRST_NAME
        defaultRegisteredShouldNotBeFound("firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllRegisteredsByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        registeredRepository.saveAndFlush(registered);

        // Get all the registeredList where firstName is not null
        defaultRegisteredShouldBeFound("firstName.specified=true");

        // Get all the registeredList where firstName is null
        defaultRegisteredShouldNotBeFound("firstName.specified=false");
    }
                @Test
    @Transactional
    public void getAllRegisteredsByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        registeredRepository.saveAndFlush(registered);

        // Get all the registeredList where firstName contains DEFAULT_FIRST_NAME
        defaultRegisteredShouldBeFound("firstName.contains=" + DEFAULT_FIRST_NAME);

        // Get all the registeredList where firstName contains UPDATED_FIRST_NAME
        defaultRegisteredShouldNotBeFound("firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllRegisteredsByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        registeredRepository.saveAndFlush(registered);

        // Get all the registeredList where firstName does not contain DEFAULT_FIRST_NAME
        defaultRegisteredShouldNotBeFound("firstName.doesNotContain=" + DEFAULT_FIRST_NAME);

        // Get all the registeredList where firstName does not contain UPDATED_FIRST_NAME
        defaultRegisteredShouldBeFound("firstName.doesNotContain=" + UPDATED_FIRST_NAME);
    }


    @Test
    @Transactional
    public void getAllRegisteredsByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        registeredRepository.saveAndFlush(registered);

        // Get all the registeredList where lastName equals to DEFAULT_LAST_NAME
        defaultRegisteredShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the registeredList where lastName equals to UPDATED_LAST_NAME
        defaultRegisteredShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllRegisteredsByLastNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        registeredRepository.saveAndFlush(registered);

        // Get all the registeredList where lastName not equals to DEFAULT_LAST_NAME
        defaultRegisteredShouldNotBeFound("lastName.notEquals=" + DEFAULT_LAST_NAME);

        // Get all the registeredList where lastName not equals to UPDATED_LAST_NAME
        defaultRegisteredShouldBeFound("lastName.notEquals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllRegisteredsByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        registeredRepository.saveAndFlush(registered);

        // Get all the registeredList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultRegisteredShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the registeredList where lastName equals to UPDATED_LAST_NAME
        defaultRegisteredShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllRegisteredsByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        registeredRepository.saveAndFlush(registered);

        // Get all the registeredList where lastName is not null
        defaultRegisteredShouldBeFound("lastName.specified=true");

        // Get all the registeredList where lastName is null
        defaultRegisteredShouldNotBeFound("lastName.specified=false");
    }
                @Test
    @Transactional
    public void getAllRegisteredsByLastNameContainsSomething() throws Exception {
        // Initialize the database
        registeredRepository.saveAndFlush(registered);

        // Get all the registeredList where lastName contains DEFAULT_LAST_NAME
        defaultRegisteredShouldBeFound("lastName.contains=" + DEFAULT_LAST_NAME);

        // Get all the registeredList where lastName contains UPDATED_LAST_NAME
        defaultRegisteredShouldNotBeFound("lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllRegisteredsByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        registeredRepository.saveAndFlush(registered);

        // Get all the registeredList where lastName does not contain DEFAULT_LAST_NAME
        defaultRegisteredShouldNotBeFound("lastName.doesNotContain=" + DEFAULT_LAST_NAME);

        // Get all the registeredList where lastName does not contain UPDATED_LAST_NAME
        defaultRegisteredShouldBeFound("lastName.doesNotContain=" + UPDATED_LAST_NAME);
    }


    @Test
    @Transactional
    public void getAllRegisteredsByMiddleNameIsEqualToSomething() throws Exception {
        // Initialize the database
        registeredRepository.saveAndFlush(registered);

        // Get all the registeredList where middleName equals to DEFAULT_MIDDLE_NAME
        defaultRegisteredShouldBeFound("middleName.equals=" + DEFAULT_MIDDLE_NAME);

        // Get all the registeredList where middleName equals to UPDATED_MIDDLE_NAME
        defaultRegisteredShouldNotBeFound("middleName.equals=" + UPDATED_MIDDLE_NAME);
    }

    @Test
    @Transactional
    public void getAllRegisteredsByMiddleNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        registeredRepository.saveAndFlush(registered);

        // Get all the registeredList where middleName not equals to DEFAULT_MIDDLE_NAME
        defaultRegisteredShouldNotBeFound("middleName.notEquals=" + DEFAULT_MIDDLE_NAME);

        // Get all the registeredList where middleName not equals to UPDATED_MIDDLE_NAME
        defaultRegisteredShouldBeFound("middleName.notEquals=" + UPDATED_MIDDLE_NAME);
    }

    @Test
    @Transactional
    public void getAllRegisteredsByMiddleNameIsInShouldWork() throws Exception {
        // Initialize the database
        registeredRepository.saveAndFlush(registered);

        // Get all the registeredList where middleName in DEFAULT_MIDDLE_NAME or UPDATED_MIDDLE_NAME
        defaultRegisteredShouldBeFound("middleName.in=" + DEFAULT_MIDDLE_NAME + "," + UPDATED_MIDDLE_NAME);

        // Get all the registeredList where middleName equals to UPDATED_MIDDLE_NAME
        defaultRegisteredShouldNotBeFound("middleName.in=" + UPDATED_MIDDLE_NAME);
    }

    @Test
    @Transactional
    public void getAllRegisteredsByMiddleNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        registeredRepository.saveAndFlush(registered);

        // Get all the registeredList where middleName is not null
        defaultRegisteredShouldBeFound("middleName.specified=true");

        // Get all the registeredList where middleName is null
        defaultRegisteredShouldNotBeFound("middleName.specified=false");
    }
                @Test
    @Transactional
    public void getAllRegisteredsByMiddleNameContainsSomething() throws Exception {
        // Initialize the database
        registeredRepository.saveAndFlush(registered);

        // Get all the registeredList where middleName contains DEFAULT_MIDDLE_NAME
        defaultRegisteredShouldBeFound("middleName.contains=" + DEFAULT_MIDDLE_NAME);

        // Get all the registeredList where middleName contains UPDATED_MIDDLE_NAME
        defaultRegisteredShouldNotBeFound("middleName.contains=" + UPDATED_MIDDLE_NAME);
    }

    @Test
    @Transactional
    public void getAllRegisteredsByMiddleNameNotContainsSomething() throws Exception {
        // Initialize the database
        registeredRepository.saveAndFlush(registered);

        // Get all the registeredList where middleName does not contain DEFAULT_MIDDLE_NAME
        defaultRegisteredShouldNotBeFound("middleName.doesNotContain=" + DEFAULT_MIDDLE_NAME);

        // Get all the registeredList where middleName does not contain UPDATED_MIDDLE_NAME
        defaultRegisteredShouldBeFound("middleName.doesNotContain=" + UPDATED_MIDDLE_NAME);
    }


    @Test
    @Transactional
    public void getAllRegisteredsByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        registeredRepository.saveAndFlush(registered);

        // Get all the registeredList where email equals to DEFAULT_EMAIL
        defaultRegisteredShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the registeredList where email equals to UPDATED_EMAIL
        defaultRegisteredShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllRegisteredsByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        registeredRepository.saveAndFlush(registered);

        // Get all the registeredList where email not equals to DEFAULT_EMAIL
        defaultRegisteredShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the registeredList where email not equals to UPDATED_EMAIL
        defaultRegisteredShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllRegisteredsByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        registeredRepository.saveAndFlush(registered);

        // Get all the registeredList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultRegisteredShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the registeredList where email equals to UPDATED_EMAIL
        defaultRegisteredShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllRegisteredsByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        registeredRepository.saveAndFlush(registered);

        // Get all the registeredList where email is not null
        defaultRegisteredShouldBeFound("email.specified=true");

        // Get all the registeredList where email is null
        defaultRegisteredShouldNotBeFound("email.specified=false");
    }
                @Test
    @Transactional
    public void getAllRegisteredsByEmailContainsSomething() throws Exception {
        // Initialize the database
        registeredRepository.saveAndFlush(registered);

        // Get all the registeredList where email contains DEFAULT_EMAIL
        defaultRegisteredShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the registeredList where email contains UPDATED_EMAIL
        defaultRegisteredShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllRegisteredsByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        registeredRepository.saveAndFlush(registered);

        // Get all the registeredList where email does not contain DEFAULT_EMAIL
        defaultRegisteredShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the registeredList where email does not contain UPDATED_EMAIL
        defaultRegisteredShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }


    @Test
    @Transactional
    public void getAllRegisteredsByDateOfBirthIsEqualToSomething() throws Exception {
        // Initialize the database
        registeredRepository.saveAndFlush(registered);

        // Get all the registeredList where dateOfBirth equals to DEFAULT_DATE_OF_BIRTH
        defaultRegisteredShouldBeFound("dateOfBirth.equals=" + DEFAULT_DATE_OF_BIRTH);

        // Get all the registeredList where dateOfBirth equals to UPDATED_DATE_OF_BIRTH
        defaultRegisteredShouldNotBeFound("dateOfBirth.equals=" + UPDATED_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    public void getAllRegisteredsByDateOfBirthIsNotEqualToSomething() throws Exception {
        // Initialize the database
        registeredRepository.saveAndFlush(registered);

        // Get all the registeredList where dateOfBirth not equals to DEFAULT_DATE_OF_BIRTH
        defaultRegisteredShouldNotBeFound("dateOfBirth.notEquals=" + DEFAULT_DATE_OF_BIRTH);

        // Get all the registeredList where dateOfBirth not equals to UPDATED_DATE_OF_BIRTH
        defaultRegisteredShouldBeFound("dateOfBirth.notEquals=" + UPDATED_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    public void getAllRegisteredsByDateOfBirthIsInShouldWork() throws Exception {
        // Initialize the database
        registeredRepository.saveAndFlush(registered);

        // Get all the registeredList where dateOfBirth in DEFAULT_DATE_OF_BIRTH or UPDATED_DATE_OF_BIRTH
        defaultRegisteredShouldBeFound("dateOfBirth.in=" + DEFAULT_DATE_OF_BIRTH + "," + UPDATED_DATE_OF_BIRTH);

        // Get all the registeredList where dateOfBirth equals to UPDATED_DATE_OF_BIRTH
        defaultRegisteredShouldNotBeFound("dateOfBirth.in=" + UPDATED_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    public void getAllRegisteredsByDateOfBirthIsNullOrNotNull() throws Exception {
        // Initialize the database
        registeredRepository.saveAndFlush(registered);

        // Get all the registeredList where dateOfBirth is not null
        defaultRegisteredShouldBeFound("dateOfBirth.specified=true");

        // Get all the registeredList where dateOfBirth is null
        defaultRegisteredShouldNotBeFound("dateOfBirth.specified=false");
    }

    @Test
    @Transactional
    public void getAllRegisteredsByDateOfBirthIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        registeredRepository.saveAndFlush(registered);

        // Get all the registeredList where dateOfBirth is greater than or equal to DEFAULT_DATE_OF_BIRTH
        defaultRegisteredShouldBeFound("dateOfBirth.greaterThanOrEqual=" + DEFAULT_DATE_OF_BIRTH);

        // Get all the registeredList where dateOfBirth is greater than or equal to UPDATED_DATE_OF_BIRTH
        defaultRegisteredShouldNotBeFound("dateOfBirth.greaterThanOrEqual=" + UPDATED_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    public void getAllRegisteredsByDateOfBirthIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        registeredRepository.saveAndFlush(registered);

        // Get all the registeredList where dateOfBirth is less than or equal to DEFAULT_DATE_OF_BIRTH
        defaultRegisteredShouldBeFound("dateOfBirth.lessThanOrEqual=" + DEFAULT_DATE_OF_BIRTH);

        // Get all the registeredList where dateOfBirth is less than or equal to SMALLER_DATE_OF_BIRTH
        defaultRegisteredShouldNotBeFound("dateOfBirth.lessThanOrEqual=" + SMALLER_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    public void getAllRegisteredsByDateOfBirthIsLessThanSomething() throws Exception {
        // Initialize the database
        registeredRepository.saveAndFlush(registered);

        // Get all the registeredList where dateOfBirth is less than DEFAULT_DATE_OF_BIRTH
        defaultRegisteredShouldNotBeFound("dateOfBirth.lessThan=" + DEFAULT_DATE_OF_BIRTH);

        // Get all the registeredList where dateOfBirth is less than UPDATED_DATE_OF_BIRTH
        defaultRegisteredShouldBeFound("dateOfBirth.lessThan=" + UPDATED_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    public void getAllRegisteredsByDateOfBirthIsGreaterThanSomething() throws Exception {
        // Initialize the database
        registeredRepository.saveAndFlush(registered);

        // Get all the registeredList where dateOfBirth is greater than DEFAULT_DATE_OF_BIRTH
        defaultRegisteredShouldNotBeFound("dateOfBirth.greaterThan=" + DEFAULT_DATE_OF_BIRTH);

        // Get all the registeredList where dateOfBirth is greater than SMALLER_DATE_OF_BIRTH
        defaultRegisteredShouldBeFound("dateOfBirth.greaterThan=" + SMALLER_DATE_OF_BIRTH);
    }


    @Test
    @Transactional
    public void getAllRegisteredsByGenderIsEqualToSomething() throws Exception {
        // Initialize the database
        registeredRepository.saveAndFlush(registered);

        // Get all the registeredList where gender equals to DEFAULT_GENDER
        defaultRegisteredShouldBeFound("gender.equals=" + DEFAULT_GENDER);

        // Get all the registeredList where gender equals to UPDATED_GENDER
        defaultRegisteredShouldNotBeFound("gender.equals=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    public void getAllRegisteredsByGenderIsNotEqualToSomething() throws Exception {
        // Initialize the database
        registeredRepository.saveAndFlush(registered);

        // Get all the registeredList where gender not equals to DEFAULT_GENDER
        defaultRegisteredShouldNotBeFound("gender.notEquals=" + DEFAULT_GENDER);

        // Get all the registeredList where gender not equals to UPDATED_GENDER
        defaultRegisteredShouldBeFound("gender.notEquals=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    public void getAllRegisteredsByGenderIsInShouldWork() throws Exception {
        // Initialize the database
        registeredRepository.saveAndFlush(registered);

        // Get all the registeredList where gender in DEFAULT_GENDER or UPDATED_GENDER
        defaultRegisteredShouldBeFound("gender.in=" + DEFAULT_GENDER + "," + UPDATED_GENDER);

        // Get all the registeredList where gender equals to UPDATED_GENDER
        defaultRegisteredShouldNotBeFound("gender.in=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    public void getAllRegisteredsByGenderIsNullOrNotNull() throws Exception {
        // Initialize the database
        registeredRepository.saveAndFlush(registered);

        // Get all the registeredList where gender is not null
        defaultRegisteredShouldBeFound("gender.specified=true");

        // Get all the registeredList where gender is null
        defaultRegisteredShouldNotBeFound("gender.specified=false");
    }

    @Test
    @Transactional
    public void getAllRegisteredsByRegisterationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        registeredRepository.saveAndFlush(registered);

        // Get all the registeredList where registerationDate equals to DEFAULT_REGISTERATION_DATE
        defaultRegisteredShouldBeFound("registerationDate.equals=" + DEFAULT_REGISTERATION_DATE);

        // Get all the registeredList where registerationDate equals to UPDATED_REGISTERATION_DATE
        defaultRegisteredShouldNotBeFound("registerationDate.equals=" + UPDATED_REGISTERATION_DATE);
    }

    @Test
    @Transactional
    public void getAllRegisteredsByRegisterationDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        registeredRepository.saveAndFlush(registered);

        // Get all the registeredList where registerationDate not equals to DEFAULT_REGISTERATION_DATE
        defaultRegisteredShouldNotBeFound("registerationDate.notEquals=" + DEFAULT_REGISTERATION_DATE);

        // Get all the registeredList where registerationDate not equals to UPDATED_REGISTERATION_DATE
        defaultRegisteredShouldBeFound("registerationDate.notEquals=" + UPDATED_REGISTERATION_DATE);
    }

    @Test
    @Transactional
    public void getAllRegisteredsByRegisterationDateIsInShouldWork() throws Exception {
        // Initialize the database
        registeredRepository.saveAndFlush(registered);

        // Get all the registeredList where registerationDate in DEFAULT_REGISTERATION_DATE or UPDATED_REGISTERATION_DATE
        defaultRegisteredShouldBeFound("registerationDate.in=" + DEFAULT_REGISTERATION_DATE + "," + UPDATED_REGISTERATION_DATE);

        // Get all the registeredList where registerationDate equals to UPDATED_REGISTERATION_DATE
        defaultRegisteredShouldNotBeFound("registerationDate.in=" + UPDATED_REGISTERATION_DATE);
    }

    @Test
    @Transactional
    public void getAllRegisteredsByRegisterationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        registeredRepository.saveAndFlush(registered);

        // Get all the registeredList where registerationDate is not null
        defaultRegisteredShouldBeFound("registerationDate.specified=true");

        // Get all the registeredList where registerationDate is null
        defaultRegisteredShouldNotBeFound("registerationDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllRegisteredsByRegisterationDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        registeredRepository.saveAndFlush(registered);

        // Get all the registeredList where registerationDate is greater than or equal to DEFAULT_REGISTERATION_DATE
        defaultRegisteredShouldBeFound("registerationDate.greaterThanOrEqual=" + DEFAULT_REGISTERATION_DATE);

        // Get all the registeredList where registerationDate is greater than or equal to UPDATED_REGISTERATION_DATE
        defaultRegisteredShouldNotBeFound("registerationDate.greaterThanOrEqual=" + UPDATED_REGISTERATION_DATE);
    }

    @Test
    @Transactional
    public void getAllRegisteredsByRegisterationDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        registeredRepository.saveAndFlush(registered);

        // Get all the registeredList where registerationDate is less than or equal to DEFAULT_REGISTERATION_DATE
        defaultRegisteredShouldBeFound("registerationDate.lessThanOrEqual=" + DEFAULT_REGISTERATION_DATE);

        // Get all the registeredList where registerationDate is less than or equal to SMALLER_REGISTERATION_DATE
        defaultRegisteredShouldNotBeFound("registerationDate.lessThanOrEqual=" + SMALLER_REGISTERATION_DATE);
    }

    @Test
    @Transactional
    public void getAllRegisteredsByRegisterationDateIsLessThanSomething() throws Exception {
        // Initialize the database
        registeredRepository.saveAndFlush(registered);

        // Get all the registeredList where registerationDate is less than DEFAULT_REGISTERATION_DATE
        defaultRegisteredShouldNotBeFound("registerationDate.lessThan=" + DEFAULT_REGISTERATION_DATE);

        // Get all the registeredList where registerationDate is less than UPDATED_REGISTERATION_DATE
        defaultRegisteredShouldBeFound("registerationDate.lessThan=" + UPDATED_REGISTERATION_DATE);
    }

    @Test
    @Transactional
    public void getAllRegisteredsByRegisterationDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        registeredRepository.saveAndFlush(registered);

        // Get all the registeredList where registerationDate is greater than DEFAULT_REGISTERATION_DATE
        defaultRegisteredShouldNotBeFound("registerationDate.greaterThan=" + DEFAULT_REGISTERATION_DATE);

        // Get all the registeredList where registerationDate is greater than SMALLER_REGISTERATION_DATE
        defaultRegisteredShouldBeFound("registerationDate.greaterThan=" + SMALLER_REGISTERATION_DATE);
    }


    @Test
    @Transactional
    public void getAllRegisteredsByTelephoneIsEqualToSomething() throws Exception {
        // Initialize the database
        registeredRepository.saveAndFlush(registered);

        // Get all the registeredList where telephone equals to DEFAULT_TELEPHONE
        defaultRegisteredShouldBeFound("telephone.equals=" + DEFAULT_TELEPHONE);

        // Get all the registeredList where telephone equals to UPDATED_TELEPHONE
        defaultRegisteredShouldNotBeFound("telephone.equals=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    public void getAllRegisteredsByTelephoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        registeredRepository.saveAndFlush(registered);

        // Get all the registeredList where telephone not equals to DEFAULT_TELEPHONE
        defaultRegisteredShouldNotBeFound("telephone.notEquals=" + DEFAULT_TELEPHONE);

        // Get all the registeredList where telephone not equals to UPDATED_TELEPHONE
        defaultRegisteredShouldBeFound("telephone.notEquals=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    public void getAllRegisteredsByTelephoneIsInShouldWork() throws Exception {
        // Initialize the database
        registeredRepository.saveAndFlush(registered);

        // Get all the registeredList where telephone in DEFAULT_TELEPHONE or UPDATED_TELEPHONE
        defaultRegisteredShouldBeFound("telephone.in=" + DEFAULT_TELEPHONE + "," + UPDATED_TELEPHONE);

        // Get all the registeredList where telephone equals to UPDATED_TELEPHONE
        defaultRegisteredShouldNotBeFound("telephone.in=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    public void getAllRegisteredsByTelephoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        registeredRepository.saveAndFlush(registered);

        // Get all the registeredList where telephone is not null
        defaultRegisteredShouldBeFound("telephone.specified=true");

        // Get all the registeredList where telephone is null
        defaultRegisteredShouldNotBeFound("telephone.specified=false");
    }
                @Test
    @Transactional
    public void getAllRegisteredsByTelephoneContainsSomething() throws Exception {
        // Initialize the database
        registeredRepository.saveAndFlush(registered);

        // Get all the registeredList where telephone contains DEFAULT_TELEPHONE
        defaultRegisteredShouldBeFound("telephone.contains=" + DEFAULT_TELEPHONE);

        // Get all the registeredList where telephone contains UPDATED_TELEPHONE
        defaultRegisteredShouldNotBeFound("telephone.contains=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    public void getAllRegisteredsByTelephoneNotContainsSomething() throws Exception {
        // Initialize the database
        registeredRepository.saveAndFlush(registered);

        // Get all the registeredList where telephone does not contain DEFAULT_TELEPHONE
        defaultRegisteredShouldNotBeFound("telephone.doesNotContain=" + DEFAULT_TELEPHONE);

        // Get all the registeredList where telephone does not contain UPDATED_TELEPHONE
        defaultRegisteredShouldBeFound("telephone.doesNotContain=" + UPDATED_TELEPHONE);
    }


    @Test
    @Transactional
    public void getAllRegisteredsByMobileIsEqualToSomething() throws Exception {
        // Initialize the database
        registeredRepository.saveAndFlush(registered);

        // Get all the registeredList where mobile equals to DEFAULT_MOBILE
        defaultRegisteredShouldBeFound("mobile.equals=" + DEFAULT_MOBILE);

        // Get all the registeredList where mobile equals to UPDATED_MOBILE
        defaultRegisteredShouldNotBeFound("mobile.equals=" + UPDATED_MOBILE);
    }

    @Test
    @Transactional
    public void getAllRegisteredsByMobileIsNotEqualToSomething() throws Exception {
        // Initialize the database
        registeredRepository.saveAndFlush(registered);

        // Get all the registeredList where mobile not equals to DEFAULT_MOBILE
        defaultRegisteredShouldNotBeFound("mobile.notEquals=" + DEFAULT_MOBILE);

        // Get all the registeredList where mobile not equals to UPDATED_MOBILE
        defaultRegisteredShouldBeFound("mobile.notEquals=" + UPDATED_MOBILE);
    }

    @Test
    @Transactional
    public void getAllRegisteredsByMobileIsInShouldWork() throws Exception {
        // Initialize the database
        registeredRepository.saveAndFlush(registered);

        // Get all the registeredList where mobile in DEFAULT_MOBILE or UPDATED_MOBILE
        defaultRegisteredShouldBeFound("mobile.in=" + DEFAULT_MOBILE + "," + UPDATED_MOBILE);

        // Get all the registeredList where mobile equals to UPDATED_MOBILE
        defaultRegisteredShouldNotBeFound("mobile.in=" + UPDATED_MOBILE);
    }

    @Test
    @Transactional
    public void getAllRegisteredsByMobileIsNullOrNotNull() throws Exception {
        // Initialize the database
        registeredRepository.saveAndFlush(registered);

        // Get all the registeredList where mobile is not null
        defaultRegisteredShouldBeFound("mobile.specified=true");

        // Get all the registeredList where mobile is null
        defaultRegisteredShouldNotBeFound("mobile.specified=false");
    }
                @Test
    @Transactional
    public void getAllRegisteredsByMobileContainsSomething() throws Exception {
        // Initialize the database
        registeredRepository.saveAndFlush(registered);

        // Get all the registeredList where mobile contains DEFAULT_MOBILE
        defaultRegisteredShouldBeFound("mobile.contains=" + DEFAULT_MOBILE);

        // Get all the registeredList where mobile contains UPDATED_MOBILE
        defaultRegisteredShouldNotBeFound("mobile.contains=" + UPDATED_MOBILE);
    }

    @Test
    @Transactional
    public void getAllRegisteredsByMobileNotContainsSomething() throws Exception {
        // Initialize the database
        registeredRepository.saveAndFlush(registered);

        // Get all the registeredList where mobile does not contain DEFAULT_MOBILE
        defaultRegisteredShouldNotBeFound("mobile.doesNotContain=" + DEFAULT_MOBILE);

        // Get all the registeredList where mobile does not contain UPDATED_MOBILE
        defaultRegisteredShouldBeFound("mobile.doesNotContain=" + UPDATED_MOBILE);
    }


    @Test
    @Transactional
    public void getAllRegisteredsByModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        registeredRepository.saveAndFlush(registered);
        User modifiedBy = UserResourceIT.createEntity(em);
        em.persist(modifiedBy);
        em.flush();
        registered.setModifiedBy(modifiedBy);
        registeredRepository.saveAndFlush(registered);
        Long modifiedById = modifiedBy.getId();

        // Get all the registeredList where modifiedBy equals to modifiedById
        defaultRegisteredShouldBeFound("modifiedById.equals=" + modifiedById);

        // Get all the registeredList where modifiedBy equals to modifiedById + 1
        defaultRegisteredShouldNotBeFound("modifiedById.equals=" + (modifiedById + 1));
    }


    @Test
    @Transactional
    public void getAllRegisteredsByCourseIsEqualToSomething() throws Exception {
        // Initialize the database
        registeredRepository.saveAndFlush(registered);
        Course course = CourseResourceIT.createEntity(em);
        em.persist(course);
        em.flush();
        registered.setCourse(course);
        registeredRepository.saveAndFlush(registered);
        Long courseId = course.getId();

        // Get all the registeredList where course equals to courseId
        defaultRegisteredShouldBeFound("courseId.equals=" + courseId);

        // Get all the registeredList where course equals to courseId + 1
        defaultRegisteredShouldNotBeFound("courseId.equals=" + (courseId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRegisteredShouldBeFound(String filter) throws Exception {
        restRegisteredMockMvc.perform(get("/api/registereds?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(registered.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].middleName").value(hasItem(DEFAULT_MIDDLE_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].dateOfBirth").value(hasItem(DEFAULT_DATE_OF_BIRTH.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].registerationDate").value(hasItem(DEFAULT_REGISTERATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)))
            .andExpect(jsonPath("$.[*].mobile").value(hasItem(DEFAULT_MOBILE)));

        // Check, that the count call also returns 1
        restRegisteredMockMvc.perform(get("/api/registereds/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRegisteredShouldNotBeFound(String filter) throws Exception {
        restRegisteredMockMvc.perform(get("/api/registereds?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRegisteredMockMvc.perform(get("/api/registereds/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingRegistered() throws Exception {
        // Get the registered
        restRegisteredMockMvc.perform(get("/api/registereds/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRegistered() throws Exception {
        // Initialize the database
        registeredRepository.saveAndFlush(registered);

        int databaseSizeBeforeUpdate = registeredRepository.findAll().size();

        // Update the registered
        Registered updatedRegistered = registeredRepository.findById(registered.getId()).get();
        // Disconnect from session so that the updates on updatedRegistered are not directly saved in db
        em.detach(updatedRegistered);
        updatedRegistered
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .middleName(UPDATED_MIDDLE_NAME)
            .email(UPDATED_EMAIL)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .gender(UPDATED_GENDER)
            .registerationDate(UPDATED_REGISTERATION_DATE)
            .telephone(UPDATED_TELEPHONE)
            .mobile(UPDATED_MOBILE);
        RegisteredDTO registeredDTO = registeredMapper.toDto(updatedRegistered);

        restRegisteredMockMvc.perform(put("/api/registereds")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(registeredDTO)))
            .andExpect(status().isOk());

        // Validate the Registered in the database
        List<Registered> registeredList = registeredRepository.findAll();
        assertThat(registeredList).hasSize(databaseSizeBeforeUpdate);
        Registered testRegistered = registeredList.get(registeredList.size() - 1);
        assertThat(testRegistered.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testRegistered.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testRegistered.getMiddleName()).isEqualTo(UPDATED_MIDDLE_NAME);
        assertThat(testRegistered.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testRegistered.getDateOfBirth()).isEqualTo(UPDATED_DATE_OF_BIRTH);
        assertThat(testRegistered.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testRegistered.getRegisterationDate()).isEqualTo(UPDATED_REGISTERATION_DATE);
        assertThat(testRegistered.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testRegistered.getMobile()).isEqualTo(UPDATED_MOBILE);
    }

    @Test
    @Transactional
    public void updateNonExistingRegistered() throws Exception {
        int databaseSizeBeforeUpdate = registeredRepository.findAll().size();

        // Create the Registered
        RegisteredDTO registeredDTO = registeredMapper.toDto(registered);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRegisteredMockMvc.perform(put("/api/registereds")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(registeredDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Registered in the database
        List<Registered> registeredList = registeredRepository.findAll();
        assertThat(registeredList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRegistered() throws Exception {
        // Initialize the database
        registeredRepository.saveAndFlush(registered);

        int databaseSizeBeforeDelete = registeredRepository.findAll().size();

        // Delete the registered
        restRegisteredMockMvc.perform(delete("/api/registereds/{id}", registered.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Registered> registeredList = registeredRepository.findAll();
        assertThat(registeredList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
