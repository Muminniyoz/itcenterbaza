package uz.itcenterbaza.web.rest;

import uz.itcenterbaza.ItcenterbazaApp;
import uz.itcenterbaza.domain.Student;
import uz.itcenterbaza.domain.User;
import uz.itcenterbaza.repository.StudentRepository;
import uz.itcenterbaza.service.StudentService;
import uz.itcenterbaza.service.dto.StudentDTO;
import uz.itcenterbaza.service.mapper.StudentMapper;
import uz.itcenterbaza.service.dto.StudentCriteria;
import uz.itcenterbaza.service.StudentQueryService;

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
 * Integration tests for the {@link StudentResource} REST controller.
 */
@SpringBootTest(classes = ItcenterbazaApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class StudentResourceIT {

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

    private static final String DEFAULT_THUMBNAIL_PHOTO_URL = "AAAAAAAAAA";
    private static final String UPDATED_THUMBNAIL_PHOTO_URL = "BBBBBBBBBB";

    private static final String DEFAULT_FULL_PHOTO_URL = "AAAAAAAAAA";
    private static final String UPDATED_FULL_PHOTO_URL = "BBBBBBBBBB";

    private static final Integer DEFAULT_ID_NUMBER = 1;
    private static final Integer UPDATED_ID_NUMBER = 2;
    private static final Integer SMALLER_ID_NUMBER = 1 - 1;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentQueryService studentQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStudentMockMvc;

    private Student student;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Student createEntity(EntityManager em) {
        Student student = new Student()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .middleName(DEFAULT_MIDDLE_NAME)
            .email(DEFAULT_EMAIL)
            .dateOfBirth(DEFAULT_DATE_OF_BIRTH)
            .gender(DEFAULT_GENDER)
            .registerationDate(DEFAULT_REGISTERATION_DATE)
            .telephone(DEFAULT_TELEPHONE)
            .mobile(DEFAULT_MOBILE)
            .thumbnailPhotoUrl(DEFAULT_THUMBNAIL_PHOTO_URL)
            .fullPhotoUrl(DEFAULT_FULL_PHOTO_URL)
            .idNumber(DEFAULT_ID_NUMBER);
        return student;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Student createUpdatedEntity(EntityManager em) {
        Student student = new Student()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .middleName(UPDATED_MIDDLE_NAME)
            .email(UPDATED_EMAIL)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .gender(UPDATED_GENDER)
            .registerationDate(UPDATED_REGISTERATION_DATE)
            .telephone(UPDATED_TELEPHONE)
            .mobile(UPDATED_MOBILE)
            .thumbnailPhotoUrl(UPDATED_THUMBNAIL_PHOTO_URL)
            .fullPhotoUrl(UPDATED_FULL_PHOTO_URL)
            .idNumber(UPDATED_ID_NUMBER);
        return student;
    }

    @BeforeEach
    public void initTest() {
        student = createEntity(em);
    }

    @Test
    @Transactional
    public void createStudent() throws Exception {
        int databaseSizeBeforeCreate = studentRepository.findAll().size();
        // Create the Student
        StudentDTO studentDTO = studentMapper.toDto(student);
        restStudentMockMvc.perform(post("/api/students")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(studentDTO)))
            .andExpect(status().isCreated());

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeCreate + 1);
        Student testStudent = studentList.get(studentList.size() - 1);
        assertThat(testStudent.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testStudent.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testStudent.getMiddleName()).isEqualTo(DEFAULT_MIDDLE_NAME);
        assertThat(testStudent.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testStudent.getDateOfBirth()).isEqualTo(DEFAULT_DATE_OF_BIRTH);
        assertThat(testStudent.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testStudent.getRegisterationDate()).isEqualTo(DEFAULT_REGISTERATION_DATE);
        assertThat(testStudent.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
        assertThat(testStudent.getMobile()).isEqualTo(DEFAULT_MOBILE);
        assertThat(testStudent.getThumbnailPhotoUrl()).isEqualTo(DEFAULT_THUMBNAIL_PHOTO_URL);
        assertThat(testStudent.getFullPhotoUrl()).isEqualTo(DEFAULT_FULL_PHOTO_URL);
        assertThat(testStudent.getIdNumber()).isEqualTo(DEFAULT_ID_NUMBER);
    }

    @Test
    @Transactional
    public void createStudentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = studentRepository.findAll().size();

        // Create the Student with an existing ID
        student.setId(1L);
        StudentDTO studentDTO = studentMapper.toDto(student);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStudentMockMvc.perform(post("/api/students")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(studentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = studentRepository.findAll().size();
        // set the field null
        student.setFirstName(null);

        // Create the Student, which fails.
        StudentDTO studentDTO = studentMapper.toDto(student);


        restStudentMockMvc.perform(post("/api/students")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(studentDTO)))
            .andExpect(status().isBadRequest());

        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = studentRepository.findAll().size();
        // set the field null
        student.setLastName(null);

        // Create the Student, which fails.
        StudentDTO studentDTO = studentMapper.toDto(student);


        restStudentMockMvc.perform(post("/api/students")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(studentDTO)))
            .andExpect(status().isBadRequest());

        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIdNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = studentRepository.findAll().size();
        // set the field null
        student.setIdNumber(null);

        // Create the Student, which fails.
        StudentDTO studentDTO = studentMapper.toDto(student);


        restStudentMockMvc.perform(post("/api/students")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(studentDTO)))
            .andExpect(status().isBadRequest());

        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStudents() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList
        restStudentMockMvc.perform(get("/api/students?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(student.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].middleName").value(hasItem(DEFAULT_MIDDLE_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].dateOfBirth").value(hasItem(DEFAULT_DATE_OF_BIRTH.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].registerationDate").value(hasItem(DEFAULT_REGISTERATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)))
            .andExpect(jsonPath("$.[*].mobile").value(hasItem(DEFAULT_MOBILE)))
            .andExpect(jsonPath("$.[*].thumbnailPhotoUrl").value(hasItem(DEFAULT_THUMBNAIL_PHOTO_URL)))
            .andExpect(jsonPath("$.[*].fullPhotoUrl").value(hasItem(DEFAULT_FULL_PHOTO_URL)))
            .andExpect(jsonPath("$.[*].idNumber").value(hasItem(DEFAULT_ID_NUMBER)));
    }
    
    @Test
    @Transactional
    public void getStudent() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get the student
        restStudentMockMvc.perform(get("/api/students/{id}", student.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(student.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.middleName").value(DEFAULT_MIDDLE_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.dateOfBirth").value(DEFAULT_DATE_OF_BIRTH.toString()))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.registerationDate").value(DEFAULT_REGISTERATION_DATE.toString()))
            .andExpect(jsonPath("$.telephone").value(DEFAULT_TELEPHONE))
            .andExpect(jsonPath("$.mobile").value(DEFAULT_MOBILE))
            .andExpect(jsonPath("$.thumbnailPhotoUrl").value(DEFAULT_THUMBNAIL_PHOTO_URL))
            .andExpect(jsonPath("$.fullPhotoUrl").value(DEFAULT_FULL_PHOTO_URL))
            .andExpect(jsonPath("$.idNumber").value(DEFAULT_ID_NUMBER));
    }


    @Test
    @Transactional
    public void getStudentsByIdFiltering() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        Long id = student.getId();

        defaultStudentShouldBeFound("id.equals=" + id);
        defaultStudentShouldNotBeFound("id.notEquals=" + id);

        defaultStudentShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultStudentShouldNotBeFound("id.greaterThan=" + id);

        defaultStudentShouldBeFound("id.lessThanOrEqual=" + id);
        defaultStudentShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllStudentsByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where firstName equals to DEFAULT_FIRST_NAME
        defaultStudentShouldBeFound("firstName.equals=" + DEFAULT_FIRST_NAME);

        // Get all the studentList where firstName equals to UPDATED_FIRST_NAME
        defaultStudentShouldNotBeFound("firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllStudentsByFirstNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where firstName not equals to DEFAULT_FIRST_NAME
        defaultStudentShouldNotBeFound("firstName.notEquals=" + DEFAULT_FIRST_NAME);

        // Get all the studentList where firstName not equals to UPDATED_FIRST_NAME
        defaultStudentShouldBeFound("firstName.notEquals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllStudentsByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where firstName in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultStudentShouldBeFound("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME);

        // Get all the studentList where firstName equals to UPDATED_FIRST_NAME
        defaultStudentShouldNotBeFound("firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllStudentsByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where firstName is not null
        defaultStudentShouldBeFound("firstName.specified=true");

        // Get all the studentList where firstName is null
        defaultStudentShouldNotBeFound("firstName.specified=false");
    }
                @Test
    @Transactional
    public void getAllStudentsByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where firstName contains DEFAULT_FIRST_NAME
        defaultStudentShouldBeFound("firstName.contains=" + DEFAULT_FIRST_NAME);

        // Get all the studentList where firstName contains UPDATED_FIRST_NAME
        defaultStudentShouldNotBeFound("firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllStudentsByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where firstName does not contain DEFAULT_FIRST_NAME
        defaultStudentShouldNotBeFound("firstName.doesNotContain=" + DEFAULT_FIRST_NAME);

        // Get all the studentList where firstName does not contain UPDATED_FIRST_NAME
        defaultStudentShouldBeFound("firstName.doesNotContain=" + UPDATED_FIRST_NAME);
    }


    @Test
    @Transactional
    public void getAllStudentsByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where lastName equals to DEFAULT_LAST_NAME
        defaultStudentShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the studentList where lastName equals to UPDATED_LAST_NAME
        defaultStudentShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllStudentsByLastNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where lastName not equals to DEFAULT_LAST_NAME
        defaultStudentShouldNotBeFound("lastName.notEquals=" + DEFAULT_LAST_NAME);

        // Get all the studentList where lastName not equals to UPDATED_LAST_NAME
        defaultStudentShouldBeFound("lastName.notEquals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllStudentsByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultStudentShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the studentList where lastName equals to UPDATED_LAST_NAME
        defaultStudentShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllStudentsByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where lastName is not null
        defaultStudentShouldBeFound("lastName.specified=true");

        // Get all the studentList where lastName is null
        defaultStudentShouldNotBeFound("lastName.specified=false");
    }
                @Test
    @Transactional
    public void getAllStudentsByLastNameContainsSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where lastName contains DEFAULT_LAST_NAME
        defaultStudentShouldBeFound("lastName.contains=" + DEFAULT_LAST_NAME);

        // Get all the studentList where lastName contains UPDATED_LAST_NAME
        defaultStudentShouldNotBeFound("lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllStudentsByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where lastName does not contain DEFAULT_LAST_NAME
        defaultStudentShouldNotBeFound("lastName.doesNotContain=" + DEFAULT_LAST_NAME);

        // Get all the studentList where lastName does not contain UPDATED_LAST_NAME
        defaultStudentShouldBeFound("lastName.doesNotContain=" + UPDATED_LAST_NAME);
    }


    @Test
    @Transactional
    public void getAllStudentsByMiddleNameIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where middleName equals to DEFAULT_MIDDLE_NAME
        defaultStudentShouldBeFound("middleName.equals=" + DEFAULT_MIDDLE_NAME);

        // Get all the studentList where middleName equals to UPDATED_MIDDLE_NAME
        defaultStudentShouldNotBeFound("middleName.equals=" + UPDATED_MIDDLE_NAME);
    }

    @Test
    @Transactional
    public void getAllStudentsByMiddleNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where middleName not equals to DEFAULT_MIDDLE_NAME
        defaultStudentShouldNotBeFound("middleName.notEquals=" + DEFAULT_MIDDLE_NAME);

        // Get all the studentList where middleName not equals to UPDATED_MIDDLE_NAME
        defaultStudentShouldBeFound("middleName.notEquals=" + UPDATED_MIDDLE_NAME);
    }

    @Test
    @Transactional
    public void getAllStudentsByMiddleNameIsInShouldWork() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where middleName in DEFAULT_MIDDLE_NAME or UPDATED_MIDDLE_NAME
        defaultStudentShouldBeFound("middleName.in=" + DEFAULT_MIDDLE_NAME + "," + UPDATED_MIDDLE_NAME);

        // Get all the studentList where middleName equals to UPDATED_MIDDLE_NAME
        defaultStudentShouldNotBeFound("middleName.in=" + UPDATED_MIDDLE_NAME);
    }

    @Test
    @Transactional
    public void getAllStudentsByMiddleNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where middleName is not null
        defaultStudentShouldBeFound("middleName.specified=true");

        // Get all the studentList where middleName is null
        defaultStudentShouldNotBeFound("middleName.specified=false");
    }
                @Test
    @Transactional
    public void getAllStudentsByMiddleNameContainsSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where middleName contains DEFAULT_MIDDLE_NAME
        defaultStudentShouldBeFound("middleName.contains=" + DEFAULT_MIDDLE_NAME);

        // Get all the studentList where middleName contains UPDATED_MIDDLE_NAME
        defaultStudentShouldNotBeFound("middleName.contains=" + UPDATED_MIDDLE_NAME);
    }

    @Test
    @Transactional
    public void getAllStudentsByMiddleNameNotContainsSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where middleName does not contain DEFAULT_MIDDLE_NAME
        defaultStudentShouldNotBeFound("middleName.doesNotContain=" + DEFAULT_MIDDLE_NAME);

        // Get all the studentList where middleName does not contain UPDATED_MIDDLE_NAME
        defaultStudentShouldBeFound("middleName.doesNotContain=" + UPDATED_MIDDLE_NAME);
    }


    @Test
    @Transactional
    public void getAllStudentsByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where email equals to DEFAULT_EMAIL
        defaultStudentShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the studentList where email equals to UPDATED_EMAIL
        defaultStudentShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllStudentsByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where email not equals to DEFAULT_EMAIL
        defaultStudentShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the studentList where email not equals to UPDATED_EMAIL
        defaultStudentShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllStudentsByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultStudentShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the studentList where email equals to UPDATED_EMAIL
        defaultStudentShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllStudentsByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where email is not null
        defaultStudentShouldBeFound("email.specified=true");

        // Get all the studentList where email is null
        defaultStudentShouldNotBeFound("email.specified=false");
    }
                @Test
    @Transactional
    public void getAllStudentsByEmailContainsSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where email contains DEFAULT_EMAIL
        defaultStudentShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the studentList where email contains UPDATED_EMAIL
        defaultStudentShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllStudentsByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where email does not contain DEFAULT_EMAIL
        defaultStudentShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the studentList where email does not contain UPDATED_EMAIL
        defaultStudentShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }


    @Test
    @Transactional
    public void getAllStudentsByDateOfBirthIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where dateOfBirth equals to DEFAULT_DATE_OF_BIRTH
        defaultStudentShouldBeFound("dateOfBirth.equals=" + DEFAULT_DATE_OF_BIRTH);

        // Get all the studentList where dateOfBirth equals to UPDATED_DATE_OF_BIRTH
        defaultStudentShouldNotBeFound("dateOfBirth.equals=" + UPDATED_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    public void getAllStudentsByDateOfBirthIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where dateOfBirth not equals to DEFAULT_DATE_OF_BIRTH
        defaultStudentShouldNotBeFound("dateOfBirth.notEquals=" + DEFAULT_DATE_OF_BIRTH);

        // Get all the studentList where dateOfBirth not equals to UPDATED_DATE_OF_BIRTH
        defaultStudentShouldBeFound("dateOfBirth.notEquals=" + UPDATED_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    public void getAllStudentsByDateOfBirthIsInShouldWork() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where dateOfBirth in DEFAULT_DATE_OF_BIRTH or UPDATED_DATE_OF_BIRTH
        defaultStudentShouldBeFound("dateOfBirth.in=" + DEFAULT_DATE_OF_BIRTH + "," + UPDATED_DATE_OF_BIRTH);

        // Get all the studentList where dateOfBirth equals to UPDATED_DATE_OF_BIRTH
        defaultStudentShouldNotBeFound("dateOfBirth.in=" + UPDATED_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    public void getAllStudentsByDateOfBirthIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where dateOfBirth is not null
        defaultStudentShouldBeFound("dateOfBirth.specified=true");

        // Get all the studentList where dateOfBirth is null
        defaultStudentShouldNotBeFound("dateOfBirth.specified=false");
    }

    @Test
    @Transactional
    public void getAllStudentsByDateOfBirthIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where dateOfBirth is greater than or equal to DEFAULT_DATE_OF_BIRTH
        defaultStudentShouldBeFound("dateOfBirth.greaterThanOrEqual=" + DEFAULT_DATE_OF_BIRTH);

        // Get all the studentList where dateOfBirth is greater than or equal to UPDATED_DATE_OF_BIRTH
        defaultStudentShouldNotBeFound("dateOfBirth.greaterThanOrEqual=" + UPDATED_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    public void getAllStudentsByDateOfBirthIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where dateOfBirth is less than or equal to DEFAULT_DATE_OF_BIRTH
        defaultStudentShouldBeFound("dateOfBirth.lessThanOrEqual=" + DEFAULT_DATE_OF_BIRTH);

        // Get all the studentList where dateOfBirth is less than or equal to SMALLER_DATE_OF_BIRTH
        defaultStudentShouldNotBeFound("dateOfBirth.lessThanOrEqual=" + SMALLER_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    public void getAllStudentsByDateOfBirthIsLessThanSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where dateOfBirth is less than DEFAULT_DATE_OF_BIRTH
        defaultStudentShouldNotBeFound("dateOfBirth.lessThan=" + DEFAULT_DATE_OF_BIRTH);

        // Get all the studentList where dateOfBirth is less than UPDATED_DATE_OF_BIRTH
        defaultStudentShouldBeFound("dateOfBirth.lessThan=" + UPDATED_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    public void getAllStudentsByDateOfBirthIsGreaterThanSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where dateOfBirth is greater than DEFAULT_DATE_OF_BIRTH
        defaultStudentShouldNotBeFound("dateOfBirth.greaterThan=" + DEFAULT_DATE_OF_BIRTH);

        // Get all the studentList where dateOfBirth is greater than SMALLER_DATE_OF_BIRTH
        defaultStudentShouldBeFound("dateOfBirth.greaterThan=" + SMALLER_DATE_OF_BIRTH);
    }


    @Test
    @Transactional
    public void getAllStudentsByGenderIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where gender equals to DEFAULT_GENDER
        defaultStudentShouldBeFound("gender.equals=" + DEFAULT_GENDER);

        // Get all the studentList where gender equals to UPDATED_GENDER
        defaultStudentShouldNotBeFound("gender.equals=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    public void getAllStudentsByGenderIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where gender not equals to DEFAULT_GENDER
        defaultStudentShouldNotBeFound("gender.notEquals=" + DEFAULT_GENDER);

        // Get all the studentList where gender not equals to UPDATED_GENDER
        defaultStudentShouldBeFound("gender.notEquals=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    public void getAllStudentsByGenderIsInShouldWork() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where gender in DEFAULT_GENDER or UPDATED_GENDER
        defaultStudentShouldBeFound("gender.in=" + DEFAULT_GENDER + "," + UPDATED_GENDER);

        // Get all the studentList where gender equals to UPDATED_GENDER
        defaultStudentShouldNotBeFound("gender.in=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    public void getAllStudentsByGenderIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where gender is not null
        defaultStudentShouldBeFound("gender.specified=true");

        // Get all the studentList where gender is null
        defaultStudentShouldNotBeFound("gender.specified=false");
    }

    @Test
    @Transactional
    public void getAllStudentsByRegisterationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where registerationDate equals to DEFAULT_REGISTERATION_DATE
        defaultStudentShouldBeFound("registerationDate.equals=" + DEFAULT_REGISTERATION_DATE);

        // Get all the studentList where registerationDate equals to UPDATED_REGISTERATION_DATE
        defaultStudentShouldNotBeFound("registerationDate.equals=" + UPDATED_REGISTERATION_DATE);
    }

    @Test
    @Transactional
    public void getAllStudentsByRegisterationDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where registerationDate not equals to DEFAULT_REGISTERATION_DATE
        defaultStudentShouldNotBeFound("registerationDate.notEquals=" + DEFAULT_REGISTERATION_DATE);

        // Get all the studentList where registerationDate not equals to UPDATED_REGISTERATION_DATE
        defaultStudentShouldBeFound("registerationDate.notEquals=" + UPDATED_REGISTERATION_DATE);
    }

    @Test
    @Transactional
    public void getAllStudentsByRegisterationDateIsInShouldWork() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where registerationDate in DEFAULT_REGISTERATION_DATE or UPDATED_REGISTERATION_DATE
        defaultStudentShouldBeFound("registerationDate.in=" + DEFAULT_REGISTERATION_DATE + "," + UPDATED_REGISTERATION_DATE);

        // Get all the studentList where registerationDate equals to UPDATED_REGISTERATION_DATE
        defaultStudentShouldNotBeFound("registerationDate.in=" + UPDATED_REGISTERATION_DATE);
    }

    @Test
    @Transactional
    public void getAllStudentsByRegisterationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where registerationDate is not null
        defaultStudentShouldBeFound("registerationDate.specified=true");

        // Get all the studentList where registerationDate is null
        defaultStudentShouldNotBeFound("registerationDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllStudentsByRegisterationDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where registerationDate is greater than or equal to DEFAULT_REGISTERATION_DATE
        defaultStudentShouldBeFound("registerationDate.greaterThanOrEqual=" + DEFAULT_REGISTERATION_DATE);

        // Get all the studentList where registerationDate is greater than or equal to UPDATED_REGISTERATION_DATE
        defaultStudentShouldNotBeFound("registerationDate.greaterThanOrEqual=" + UPDATED_REGISTERATION_DATE);
    }

    @Test
    @Transactional
    public void getAllStudentsByRegisterationDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where registerationDate is less than or equal to DEFAULT_REGISTERATION_DATE
        defaultStudentShouldBeFound("registerationDate.lessThanOrEqual=" + DEFAULT_REGISTERATION_DATE);

        // Get all the studentList where registerationDate is less than or equal to SMALLER_REGISTERATION_DATE
        defaultStudentShouldNotBeFound("registerationDate.lessThanOrEqual=" + SMALLER_REGISTERATION_DATE);
    }

    @Test
    @Transactional
    public void getAllStudentsByRegisterationDateIsLessThanSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where registerationDate is less than DEFAULT_REGISTERATION_DATE
        defaultStudentShouldNotBeFound("registerationDate.lessThan=" + DEFAULT_REGISTERATION_DATE);

        // Get all the studentList where registerationDate is less than UPDATED_REGISTERATION_DATE
        defaultStudentShouldBeFound("registerationDate.lessThan=" + UPDATED_REGISTERATION_DATE);
    }

    @Test
    @Transactional
    public void getAllStudentsByRegisterationDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where registerationDate is greater than DEFAULT_REGISTERATION_DATE
        defaultStudentShouldNotBeFound("registerationDate.greaterThan=" + DEFAULT_REGISTERATION_DATE);

        // Get all the studentList where registerationDate is greater than SMALLER_REGISTERATION_DATE
        defaultStudentShouldBeFound("registerationDate.greaterThan=" + SMALLER_REGISTERATION_DATE);
    }


    @Test
    @Transactional
    public void getAllStudentsByTelephoneIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where telephone equals to DEFAULT_TELEPHONE
        defaultStudentShouldBeFound("telephone.equals=" + DEFAULT_TELEPHONE);

        // Get all the studentList where telephone equals to UPDATED_TELEPHONE
        defaultStudentShouldNotBeFound("telephone.equals=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    public void getAllStudentsByTelephoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where telephone not equals to DEFAULT_TELEPHONE
        defaultStudentShouldNotBeFound("telephone.notEquals=" + DEFAULT_TELEPHONE);

        // Get all the studentList where telephone not equals to UPDATED_TELEPHONE
        defaultStudentShouldBeFound("telephone.notEquals=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    public void getAllStudentsByTelephoneIsInShouldWork() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where telephone in DEFAULT_TELEPHONE or UPDATED_TELEPHONE
        defaultStudentShouldBeFound("telephone.in=" + DEFAULT_TELEPHONE + "," + UPDATED_TELEPHONE);

        // Get all the studentList where telephone equals to UPDATED_TELEPHONE
        defaultStudentShouldNotBeFound("telephone.in=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    public void getAllStudentsByTelephoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where telephone is not null
        defaultStudentShouldBeFound("telephone.specified=true");

        // Get all the studentList where telephone is null
        defaultStudentShouldNotBeFound("telephone.specified=false");
    }
                @Test
    @Transactional
    public void getAllStudentsByTelephoneContainsSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where telephone contains DEFAULT_TELEPHONE
        defaultStudentShouldBeFound("telephone.contains=" + DEFAULT_TELEPHONE);

        // Get all the studentList where telephone contains UPDATED_TELEPHONE
        defaultStudentShouldNotBeFound("telephone.contains=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    public void getAllStudentsByTelephoneNotContainsSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where telephone does not contain DEFAULT_TELEPHONE
        defaultStudentShouldNotBeFound("telephone.doesNotContain=" + DEFAULT_TELEPHONE);

        // Get all the studentList where telephone does not contain UPDATED_TELEPHONE
        defaultStudentShouldBeFound("telephone.doesNotContain=" + UPDATED_TELEPHONE);
    }


    @Test
    @Transactional
    public void getAllStudentsByMobileIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where mobile equals to DEFAULT_MOBILE
        defaultStudentShouldBeFound("mobile.equals=" + DEFAULT_MOBILE);

        // Get all the studentList where mobile equals to UPDATED_MOBILE
        defaultStudentShouldNotBeFound("mobile.equals=" + UPDATED_MOBILE);
    }

    @Test
    @Transactional
    public void getAllStudentsByMobileIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where mobile not equals to DEFAULT_MOBILE
        defaultStudentShouldNotBeFound("mobile.notEquals=" + DEFAULT_MOBILE);

        // Get all the studentList where mobile not equals to UPDATED_MOBILE
        defaultStudentShouldBeFound("mobile.notEquals=" + UPDATED_MOBILE);
    }

    @Test
    @Transactional
    public void getAllStudentsByMobileIsInShouldWork() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where mobile in DEFAULT_MOBILE or UPDATED_MOBILE
        defaultStudentShouldBeFound("mobile.in=" + DEFAULT_MOBILE + "," + UPDATED_MOBILE);

        // Get all the studentList where mobile equals to UPDATED_MOBILE
        defaultStudentShouldNotBeFound("mobile.in=" + UPDATED_MOBILE);
    }

    @Test
    @Transactional
    public void getAllStudentsByMobileIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where mobile is not null
        defaultStudentShouldBeFound("mobile.specified=true");

        // Get all the studentList where mobile is null
        defaultStudentShouldNotBeFound("mobile.specified=false");
    }
                @Test
    @Transactional
    public void getAllStudentsByMobileContainsSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where mobile contains DEFAULT_MOBILE
        defaultStudentShouldBeFound("mobile.contains=" + DEFAULT_MOBILE);

        // Get all the studentList where mobile contains UPDATED_MOBILE
        defaultStudentShouldNotBeFound("mobile.contains=" + UPDATED_MOBILE);
    }

    @Test
    @Transactional
    public void getAllStudentsByMobileNotContainsSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where mobile does not contain DEFAULT_MOBILE
        defaultStudentShouldNotBeFound("mobile.doesNotContain=" + DEFAULT_MOBILE);

        // Get all the studentList where mobile does not contain UPDATED_MOBILE
        defaultStudentShouldBeFound("mobile.doesNotContain=" + UPDATED_MOBILE);
    }


    @Test
    @Transactional
    public void getAllStudentsByThumbnailPhotoUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where thumbnailPhotoUrl equals to DEFAULT_THUMBNAIL_PHOTO_URL
        defaultStudentShouldBeFound("thumbnailPhotoUrl.equals=" + DEFAULT_THUMBNAIL_PHOTO_URL);

        // Get all the studentList where thumbnailPhotoUrl equals to UPDATED_THUMBNAIL_PHOTO_URL
        defaultStudentShouldNotBeFound("thumbnailPhotoUrl.equals=" + UPDATED_THUMBNAIL_PHOTO_URL);
    }

    @Test
    @Transactional
    public void getAllStudentsByThumbnailPhotoUrlIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where thumbnailPhotoUrl not equals to DEFAULT_THUMBNAIL_PHOTO_URL
        defaultStudentShouldNotBeFound("thumbnailPhotoUrl.notEquals=" + DEFAULT_THUMBNAIL_PHOTO_URL);

        // Get all the studentList where thumbnailPhotoUrl not equals to UPDATED_THUMBNAIL_PHOTO_URL
        defaultStudentShouldBeFound("thumbnailPhotoUrl.notEquals=" + UPDATED_THUMBNAIL_PHOTO_URL);
    }

    @Test
    @Transactional
    public void getAllStudentsByThumbnailPhotoUrlIsInShouldWork() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where thumbnailPhotoUrl in DEFAULT_THUMBNAIL_PHOTO_URL or UPDATED_THUMBNAIL_PHOTO_URL
        defaultStudentShouldBeFound("thumbnailPhotoUrl.in=" + DEFAULT_THUMBNAIL_PHOTO_URL + "," + UPDATED_THUMBNAIL_PHOTO_URL);

        // Get all the studentList where thumbnailPhotoUrl equals to UPDATED_THUMBNAIL_PHOTO_URL
        defaultStudentShouldNotBeFound("thumbnailPhotoUrl.in=" + UPDATED_THUMBNAIL_PHOTO_URL);
    }

    @Test
    @Transactional
    public void getAllStudentsByThumbnailPhotoUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where thumbnailPhotoUrl is not null
        defaultStudentShouldBeFound("thumbnailPhotoUrl.specified=true");

        // Get all the studentList where thumbnailPhotoUrl is null
        defaultStudentShouldNotBeFound("thumbnailPhotoUrl.specified=false");
    }
                @Test
    @Transactional
    public void getAllStudentsByThumbnailPhotoUrlContainsSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where thumbnailPhotoUrl contains DEFAULT_THUMBNAIL_PHOTO_URL
        defaultStudentShouldBeFound("thumbnailPhotoUrl.contains=" + DEFAULT_THUMBNAIL_PHOTO_URL);

        // Get all the studentList where thumbnailPhotoUrl contains UPDATED_THUMBNAIL_PHOTO_URL
        defaultStudentShouldNotBeFound("thumbnailPhotoUrl.contains=" + UPDATED_THUMBNAIL_PHOTO_URL);
    }

    @Test
    @Transactional
    public void getAllStudentsByThumbnailPhotoUrlNotContainsSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where thumbnailPhotoUrl does not contain DEFAULT_THUMBNAIL_PHOTO_URL
        defaultStudentShouldNotBeFound("thumbnailPhotoUrl.doesNotContain=" + DEFAULT_THUMBNAIL_PHOTO_URL);

        // Get all the studentList where thumbnailPhotoUrl does not contain UPDATED_THUMBNAIL_PHOTO_URL
        defaultStudentShouldBeFound("thumbnailPhotoUrl.doesNotContain=" + UPDATED_THUMBNAIL_PHOTO_URL);
    }


    @Test
    @Transactional
    public void getAllStudentsByFullPhotoUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where fullPhotoUrl equals to DEFAULT_FULL_PHOTO_URL
        defaultStudentShouldBeFound("fullPhotoUrl.equals=" + DEFAULT_FULL_PHOTO_URL);

        // Get all the studentList where fullPhotoUrl equals to UPDATED_FULL_PHOTO_URL
        defaultStudentShouldNotBeFound("fullPhotoUrl.equals=" + UPDATED_FULL_PHOTO_URL);
    }

    @Test
    @Transactional
    public void getAllStudentsByFullPhotoUrlIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where fullPhotoUrl not equals to DEFAULT_FULL_PHOTO_URL
        defaultStudentShouldNotBeFound("fullPhotoUrl.notEquals=" + DEFAULT_FULL_PHOTO_URL);

        // Get all the studentList where fullPhotoUrl not equals to UPDATED_FULL_PHOTO_URL
        defaultStudentShouldBeFound("fullPhotoUrl.notEquals=" + UPDATED_FULL_PHOTO_URL);
    }

    @Test
    @Transactional
    public void getAllStudentsByFullPhotoUrlIsInShouldWork() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where fullPhotoUrl in DEFAULT_FULL_PHOTO_URL or UPDATED_FULL_PHOTO_URL
        defaultStudentShouldBeFound("fullPhotoUrl.in=" + DEFAULT_FULL_PHOTO_URL + "," + UPDATED_FULL_PHOTO_URL);

        // Get all the studentList where fullPhotoUrl equals to UPDATED_FULL_PHOTO_URL
        defaultStudentShouldNotBeFound("fullPhotoUrl.in=" + UPDATED_FULL_PHOTO_URL);
    }

    @Test
    @Transactional
    public void getAllStudentsByFullPhotoUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where fullPhotoUrl is not null
        defaultStudentShouldBeFound("fullPhotoUrl.specified=true");

        // Get all the studentList where fullPhotoUrl is null
        defaultStudentShouldNotBeFound("fullPhotoUrl.specified=false");
    }
                @Test
    @Transactional
    public void getAllStudentsByFullPhotoUrlContainsSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where fullPhotoUrl contains DEFAULT_FULL_PHOTO_URL
        defaultStudentShouldBeFound("fullPhotoUrl.contains=" + DEFAULT_FULL_PHOTO_URL);

        // Get all the studentList where fullPhotoUrl contains UPDATED_FULL_PHOTO_URL
        defaultStudentShouldNotBeFound("fullPhotoUrl.contains=" + UPDATED_FULL_PHOTO_URL);
    }

    @Test
    @Transactional
    public void getAllStudentsByFullPhotoUrlNotContainsSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where fullPhotoUrl does not contain DEFAULT_FULL_PHOTO_URL
        defaultStudentShouldNotBeFound("fullPhotoUrl.doesNotContain=" + DEFAULT_FULL_PHOTO_URL);

        // Get all the studentList where fullPhotoUrl does not contain UPDATED_FULL_PHOTO_URL
        defaultStudentShouldBeFound("fullPhotoUrl.doesNotContain=" + UPDATED_FULL_PHOTO_URL);
    }


    @Test
    @Transactional
    public void getAllStudentsByIdNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where idNumber equals to DEFAULT_ID_NUMBER
        defaultStudentShouldBeFound("idNumber.equals=" + DEFAULT_ID_NUMBER);

        // Get all the studentList where idNumber equals to UPDATED_ID_NUMBER
        defaultStudentShouldNotBeFound("idNumber.equals=" + UPDATED_ID_NUMBER);
    }

    @Test
    @Transactional
    public void getAllStudentsByIdNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where idNumber not equals to DEFAULT_ID_NUMBER
        defaultStudentShouldNotBeFound("idNumber.notEquals=" + DEFAULT_ID_NUMBER);

        // Get all the studentList where idNumber not equals to UPDATED_ID_NUMBER
        defaultStudentShouldBeFound("idNumber.notEquals=" + UPDATED_ID_NUMBER);
    }

    @Test
    @Transactional
    public void getAllStudentsByIdNumberIsInShouldWork() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where idNumber in DEFAULT_ID_NUMBER or UPDATED_ID_NUMBER
        defaultStudentShouldBeFound("idNumber.in=" + DEFAULT_ID_NUMBER + "," + UPDATED_ID_NUMBER);

        // Get all the studentList where idNumber equals to UPDATED_ID_NUMBER
        defaultStudentShouldNotBeFound("idNumber.in=" + UPDATED_ID_NUMBER);
    }

    @Test
    @Transactional
    public void getAllStudentsByIdNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where idNumber is not null
        defaultStudentShouldBeFound("idNumber.specified=true");

        // Get all the studentList where idNumber is null
        defaultStudentShouldNotBeFound("idNumber.specified=false");
    }

    @Test
    @Transactional
    public void getAllStudentsByIdNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where idNumber is greater than or equal to DEFAULT_ID_NUMBER
        defaultStudentShouldBeFound("idNumber.greaterThanOrEqual=" + DEFAULT_ID_NUMBER);

        // Get all the studentList where idNumber is greater than or equal to UPDATED_ID_NUMBER
        defaultStudentShouldNotBeFound("idNumber.greaterThanOrEqual=" + UPDATED_ID_NUMBER);
    }

    @Test
    @Transactional
    public void getAllStudentsByIdNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where idNumber is less than or equal to DEFAULT_ID_NUMBER
        defaultStudentShouldBeFound("idNumber.lessThanOrEqual=" + DEFAULT_ID_NUMBER);

        // Get all the studentList where idNumber is less than or equal to SMALLER_ID_NUMBER
        defaultStudentShouldNotBeFound("idNumber.lessThanOrEqual=" + SMALLER_ID_NUMBER);
    }

    @Test
    @Transactional
    public void getAllStudentsByIdNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where idNumber is less than DEFAULT_ID_NUMBER
        defaultStudentShouldNotBeFound("idNumber.lessThan=" + DEFAULT_ID_NUMBER);

        // Get all the studentList where idNumber is less than UPDATED_ID_NUMBER
        defaultStudentShouldBeFound("idNumber.lessThan=" + UPDATED_ID_NUMBER);
    }

    @Test
    @Transactional
    public void getAllStudentsByIdNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where idNumber is greater than DEFAULT_ID_NUMBER
        defaultStudentShouldNotBeFound("idNumber.greaterThan=" + DEFAULT_ID_NUMBER);

        // Get all the studentList where idNumber is greater than SMALLER_ID_NUMBER
        defaultStudentShouldBeFound("idNumber.greaterThan=" + SMALLER_ID_NUMBER);
    }


    @Test
    @Transactional
    public void getAllStudentsByModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);
        User modifiedBy = UserResourceIT.createEntity(em);
        em.persist(modifiedBy);
        em.flush();
        student.setModifiedBy(modifiedBy);
        studentRepository.saveAndFlush(student);
        Long modifiedById = modifiedBy.getId();

        // Get all the studentList where modifiedBy equals to modifiedById
        defaultStudentShouldBeFound("modifiedById.equals=" + modifiedById);

        // Get all the studentList where modifiedBy equals to modifiedById + 1
        defaultStudentShouldNotBeFound("modifiedById.equals=" + (modifiedById + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultStudentShouldBeFound(String filter) throws Exception {
        restStudentMockMvc.perform(get("/api/students?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(student.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].middleName").value(hasItem(DEFAULT_MIDDLE_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].dateOfBirth").value(hasItem(DEFAULT_DATE_OF_BIRTH.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].registerationDate").value(hasItem(DEFAULT_REGISTERATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)))
            .andExpect(jsonPath("$.[*].mobile").value(hasItem(DEFAULT_MOBILE)))
            .andExpect(jsonPath("$.[*].thumbnailPhotoUrl").value(hasItem(DEFAULT_THUMBNAIL_PHOTO_URL)))
            .andExpect(jsonPath("$.[*].fullPhotoUrl").value(hasItem(DEFAULT_FULL_PHOTO_URL)))
            .andExpect(jsonPath("$.[*].idNumber").value(hasItem(DEFAULT_ID_NUMBER)));

        // Check, that the count call also returns 1
        restStudentMockMvc.perform(get("/api/students/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultStudentShouldNotBeFound(String filter) throws Exception {
        restStudentMockMvc.perform(get("/api/students?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restStudentMockMvc.perform(get("/api/students/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingStudent() throws Exception {
        // Get the student
        restStudentMockMvc.perform(get("/api/students/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStudent() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        int databaseSizeBeforeUpdate = studentRepository.findAll().size();

        // Update the student
        Student updatedStudent = studentRepository.findById(student.getId()).get();
        // Disconnect from session so that the updates on updatedStudent are not directly saved in db
        em.detach(updatedStudent);
        updatedStudent
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .middleName(UPDATED_MIDDLE_NAME)
            .email(UPDATED_EMAIL)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .gender(UPDATED_GENDER)
            .registerationDate(UPDATED_REGISTERATION_DATE)
            .telephone(UPDATED_TELEPHONE)
            .mobile(UPDATED_MOBILE)
            .thumbnailPhotoUrl(UPDATED_THUMBNAIL_PHOTO_URL)
            .fullPhotoUrl(UPDATED_FULL_PHOTO_URL)
            .idNumber(UPDATED_ID_NUMBER);
        StudentDTO studentDTO = studentMapper.toDto(updatedStudent);

        restStudentMockMvc.perform(put("/api/students")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(studentDTO)))
            .andExpect(status().isOk());

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeUpdate);
        Student testStudent = studentList.get(studentList.size() - 1);
        assertThat(testStudent.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testStudent.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testStudent.getMiddleName()).isEqualTo(UPDATED_MIDDLE_NAME);
        assertThat(testStudent.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testStudent.getDateOfBirth()).isEqualTo(UPDATED_DATE_OF_BIRTH);
        assertThat(testStudent.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testStudent.getRegisterationDate()).isEqualTo(UPDATED_REGISTERATION_DATE);
        assertThat(testStudent.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testStudent.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testStudent.getThumbnailPhotoUrl()).isEqualTo(UPDATED_THUMBNAIL_PHOTO_URL);
        assertThat(testStudent.getFullPhotoUrl()).isEqualTo(UPDATED_FULL_PHOTO_URL);
        assertThat(testStudent.getIdNumber()).isEqualTo(UPDATED_ID_NUMBER);
    }

    @Test
    @Transactional
    public void updateNonExistingStudent() throws Exception {
        int databaseSizeBeforeUpdate = studentRepository.findAll().size();

        // Create the Student
        StudentDTO studentDTO = studentMapper.toDto(student);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudentMockMvc.perform(put("/api/students")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(studentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteStudent() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        int databaseSizeBeforeDelete = studentRepository.findAll().size();

        // Delete the student
        restStudentMockMvc.perform(delete("/api/students/{id}", student.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
