package uz.itcenterbaza.web.rest;

import uz.itcenterbaza.ItcenterbazaApp;
import uz.itcenterbaza.domain.Teacher;
import uz.itcenterbaza.domain.User;
import uz.itcenterbaza.domain.Skill;
import uz.itcenterbaza.repository.TeacherRepository;
import uz.itcenterbaza.service.TeacherService;
import uz.itcenterbaza.service.dto.TeacherDTO;
import uz.itcenterbaza.service.mapper.TeacherMapper;
import uz.itcenterbaza.service.dto.TeacherCriteria;
import uz.itcenterbaza.service.TeacherQueryService;

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
import org.springframework.util.Base64Utils;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import uz.itcenterbaza.domain.enumeration.Gender;
/**
 * Integration tests for the {@link TeacherResource} REST controller.
 */
@SpringBootTest(classes = ItcenterbazaApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class TeacherResourceIT {

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

    private static final String DEFAULT_FULL_PHOTO_URL = "AAAAAAAAAA";
    private static final String UPDATED_FULL_PHOTO_URL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String DEFAULT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_ABOUT = "AAAAAAAAAA";
    private static final String UPDATED_ABOUT = "BBBBBBBBBB";

    private static final String DEFAULT_PORTFOLIA = "AAAAAAAAAA";
    private static final String UPDATED_PORTFOLIA = "BBBBBBBBBB";

    private static final byte[] DEFAULT_INFO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_INFO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_INFO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_INFO_CONTENT_TYPE = "image/png";

    private static final LocalDate DEFAULT_LEAVE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LEAVE_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_LEAVE_DATE = LocalDate.ofEpochDay(-1L);

    private static final Boolean DEFAULT_IS_SHOWING_HOME = false;
    private static final Boolean UPDATED_IS_SHOWING_HOME = true;

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    @Autowired
    private TeacherRepository teacherRepository;

    @Mock
    private TeacherRepository teacherRepositoryMock;

    @Autowired
    private TeacherMapper teacherMapper;

    @Mock
    private TeacherService teacherServiceMock;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private TeacherQueryService teacherQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTeacherMockMvc;

    private Teacher teacher;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Teacher createEntity(EntityManager em) {
        Teacher teacher = new Teacher()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .middleName(DEFAULT_MIDDLE_NAME)
            .email(DEFAULT_EMAIL)
            .dateOfBirth(DEFAULT_DATE_OF_BIRTH)
            .gender(DEFAULT_GENDER)
            .registerationDate(DEFAULT_REGISTERATION_DATE)
            .telephone(DEFAULT_TELEPHONE)
            .mobile(DEFAULT_MOBILE)
            .fullPhotoUrl(DEFAULT_FULL_PHOTO_URL)
            .active(DEFAULT_ACTIVE)
            .key(DEFAULT_KEY)
            .about(DEFAULT_ABOUT)
            .portfolia(DEFAULT_PORTFOLIA)
            .info(DEFAULT_INFO)
            .infoContentType(DEFAULT_INFO_CONTENT_TYPE)
            .leaveDate(DEFAULT_LEAVE_DATE)
            .isShowingHome(DEFAULT_IS_SHOWING_HOME)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE);
        return teacher;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Teacher createUpdatedEntity(EntityManager em) {
        Teacher teacher = new Teacher()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .middleName(UPDATED_MIDDLE_NAME)
            .email(UPDATED_EMAIL)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .gender(UPDATED_GENDER)
            .registerationDate(UPDATED_REGISTERATION_DATE)
            .telephone(UPDATED_TELEPHONE)
            .mobile(UPDATED_MOBILE)
            .fullPhotoUrl(UPDATED_FULL_PHOTO_URL)
            .active(UPDATED_ACTIVE)
            .key(UPDATED_KEY)
            .about(UPDATED_ABOUT)
            .portfolia(UPDATED_PORTFOLIA)
            .info(UPDATED_INFO)
            .infoContentType(UPDATED_INFO_CONTENT_TYPE)
            .leaveDate(UPDATED_LEAVE_DATE)
            .isShowingHome(UPDATED_IS_SHOWING_HOME)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);
        return teacher;
    }

    @BeforeEach
    public void initTest() {
        teacher = createEntity(em);
    }

    @Test
    @Transactional
    public void createTeacher() throws Exception {
        int databaseSizeBeforeCreate = teacherRepository.findAll().size();
        // Create the Teacher
        TeacherDTO teacherDTO = teacherMapper.toDto(teacher);
        restTeacherMockMvc.perform(post("/api/teachers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(teacherDTO)))
            .andExpect(status().isCreated());

        // Validate the Teacher in the database
        List<Teacher> teacherList = teacherRepository.findAll();
        assertThat(teacherList).hasSize(databaseSizeBeforeCreate + 1);
        Teacher testTeacher = teacherList.get(teacherList.size() - 1);
        assertThat(testTeacher.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testTeacher.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testTeacher.getMiddleName()).isEqualTo(DEFAULT_MIDDLE_NAME);
        assertThat(testTeacher.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testTeacher.getDateOfBirth()).isEqualTo(DEFAULT_DATE_OF_BIRTH);
        assertThat(testTeacher.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testTeacher.getRegisterationDate()).isEqualTo(DEFAULT_REGISTERATION_DATE);
        assertThat(testTeacher.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
        assertThat(testTeacher.getMobile()).isEqualTo(DEFAULT_MOBILE);
        assertThat(testTeacher.getFullPhotoUrl()).isEqualTo(DEFAULT_FULL_PHOTO_URL);
        assertThat(testTeacher.isActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testTeacher.getKey()).isEqualTo(DEFAULT_KEY);
        assertThat(testTeacher.getAbout()).isEqualTo(DEFAULT_ABOUT);
        assertThat(testTeacher.getPortfolia()).isEqualTo(DEFAULT_PORTFOLIA);
        assertThat(testTeacher.getInfo()).isEqualTo(DEFAULT_INFO);
        assertThat(testTeacher.getInfoContentType()).isEqualTo(DEFAULT_INFO_CONTENT_TYPE);
        assertThat(testTeacher.getLeaveDate()).isEqualTo(DEFAULT_LEAVE_DATE);
        assertThat(testTeacher.isIsShowingHome()).isEqualTo(DEFAULT_IS_SHOWING_HOME);
        assertThat(testTeacher.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testTeacher.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createTeacherWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = teacherRepository.findAll().size();

        // Create the Teacher with an existing ID
        teacher.setId(1L);
        TeacherDTO teacherDTO = teacherMapper.toDto(teacher);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTeacherMockMvc.perform(post("/api/teachers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(teacherDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Teacher in the database
        List<Teacher> teacherList = teacherRepository.findAll();
        assertThat(teacherList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = teacherRepository.findAll().size();
        // set the field null
        teacher.setFirstName(null);

        // Create the Teacher, which fails.
        TeacherDTO teacherDTO = teacherMapper.toDto(teacher);


        restTeacherMockMvc.perform(post("/api/teachers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(teacherDTO)))
            .andExpect(status().isBadRequest());

        List<Teacher> teacherList = teacherRepository.findAll();
        assertThat(teacherList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = teacherRepository.findAll().size();
        // set the field null
        teacher.setLastName(null);

        // Create the Teacher, which fails.
        TeacherDTO teacherDTO = teacherMapper.toDto(teacher);


        restTeacherMockMvc.perform(post("/api/teachers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(teacherDTO)))
            .andExpect(status().isBadRequest());

        List<Teacher> teacherList = teacherRepository.findAll();
        assertThat(teacherList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTeachers() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList
        restTeacherMockMvc.perform(get("/api/teachers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(teacher.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].middleName").value(hasItem(DEFAULT_MIDDLE_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].dateOfBirth").value(hasItem(DEFAULT_DATE_OF_BIRTH.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].registerationDate").value(hasItem(DEFAULT_REGISTERATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)))
            .andExpect(jsonPath("$.[*].mobile").value(hasItem(DEFAULT_MOBILE)))
            .andExpect(jsonPath("$.[*].fullPhotoUrl").value(hasItem(DEFAULT_FULL_PHOTO_URL)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY)))
            .andExpect(jsonPath("$.[*].about").value(hasItem(DEFAULT_ABOUT.toString())))
            .andExpect(jsonPath("$.[*].portfolia").value(hasItem(DEFAULT_PORTFOLIA.toString())))
            .andExpect(jsonPath("$.[*].infoContentType").value(hasItem(DEFAULT_INFO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].info").value(hasItem(Base64Utils.encodeToString(DEFAULT_INFO))))
            .andExpect(jsonPath("$.[*].leaveDate").value(hasItem(DEFAULT_LEAVE_DATE.toString())))
            .andExpect(jsonPath("$.[*].isShowingHome").value(hasItem(DEFAULT_IS_SHOWING_HOME.booleanValue())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllTeachersWithEagerRelationshipsIsEnabled() throws Exception {
        when(teacherServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTeacherMockMvc.perform(get("/api/teachers?eagerload=true"))
            .andExpect(status().isOk());

        verify(teacherServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllTeachersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(teacherServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTeacherMockMvc.perform(get("/api/teachers?eagerload=true"))
            .andExpect(status().isOk());

        verify(teacherServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getTeacher() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get the teacher
        restTeacherMockMvc.perform(get("/api/teachers/{id}", teacher.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(teacher.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.middleName").value(DEFAULT_MIDDLE_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.dateOfBirth").value(DEFAULT_DATE_OF_BIRTH.toString()))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.registerationDate").value(DEFAULT_REGISTERATION_DATE.toString()))
            .andExpect(jsonPath("$.telephone").value(DEFAULT_TELEPHONE))
            .andExpect(jsonPath("$.mobile").value(DEFAULT_MOBILE))
            .andExpect(jsonPath("$.fullPhotoUrl").value(DEFAULT_FULL_PHOTO_URL))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY))
            .andExpect(jsonPath("$.about").value(DEFAULT_ABOUT.toString()))
            .andExpect(jsonPath("$.portfolia").value(DEFAULT_PORTFOLIA.toString()))
            .andExpect(jsonPath("$.infoContentType").value(DEFAULT_INFO_CONTENT_TYPE))
            .andExpect(jsonPath("$.info").value(Base64Utils.encodeToString(DEFAULT_INFO)))
            .andExpect(jsonPath("$.leaveDate").value(DEFAULT_LEAVE_DATE.toString()))
            .andExpect(jsonPath("$.isShowingHome").value(DEFAULT_IS_SHOWING_HOME.booleanValue()))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)));
    }


    @Test
    @Transactional
    public void getTeachersByIdFiltering() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        Long id = teacher.getId();

        defaultTeacherShouldBeFound("id.equals=" + id);
        defaultTeacherShouldNotBeFound("id.notEquals=" + id);

        defaultTeacherShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTeacherShouldNotBeFound("id.greaterThan=" + id);

        defaultTeacherShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTeacherShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllTeachersByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where firstName equals to DEFAULT_FIRST_NAME
        defaultTeacherShouldBeFound("firstName.equals=" + DEFAULT_FIRST_NAME);

        // Get all the teacherList where firstName equals to UPDATED_FIRST_NAME
        defaultTeacherShouldNotBeFound("firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllTeachersByFirstNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where firstName not equals to DEFAULT_FIRST_NAME
        defaultTeacherShouldNotBeFound("firstName.notEquals=" + DEFAULT_FIRST_NAME);

        // Get all the teacherList where firstName not equals to UPDATED_FIRST_NAME
        defaultTeacherShouldBeFound("firstName.notEquals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllTeachersByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where firstName in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultTeacherShouldBeFound("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME);

        // Get all the teacherList where firstName equals to UPDATED_FIRST_NAME
        defaultTeacherShouldNotBeFound("firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllTeachersByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where firstName is not null
        defaultTeacherShouldBeFound("firstName.specified=true");

        // Get all the teacherList where firstName is null
        defaultTeacherShouldNotBeFound("firstName.specified=false");
    }
                @Test
    @Transactional
    public void getAllTeachersByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where firstName contains DEFAULT_FIRST_NAME
        defaultTeacherShouldBeFound("firstName.contains=" + DEFAULT_FIRST_NAME);

        // Get all the teacherList where firstName contains UPDATED_FIRST_NAME
        defaultTeacherShouldNotBeFound("firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllTeachersByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where firstName does not contain DEFAULT_FIRST_NAME
        defaultTeacherShouldNotBeFound("firstName.doesNotContain=" + DEFAULT_FIRST_NAME);

        // Get all the teacherList where firstName does not contain UPDATED_FIRST_NAME
        defaultTeacherShouldBeFound("firstName.doesNotContain=" + UPDATED_FIRST_NAME);
    }


    @Test
    @Transactional
    public void getAllTeachersByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where lastName equals to DEFAULT_LAST_NAME
        defaultTeacherShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the teacherList where lastName equals to UPDATED_LAST_NAME
        defaultTeacherShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllTeachersByLastNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where lastName not equals to DEFAULT_LAST_NAME
        defaultTeacherShouldNotBeFound("lastName.notEquals=" + DEFAULT_LAST_NAME);

        // Get all the teacherList where lastName not equals to UPDATED_LAST_NAME
        defaultTeacherShouldBeFound("lastName.notEquals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllTeachersByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultTeacherShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the teacherList where lastName equals to UPDATED_LAST_NAME
        defaultTeacherShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllTeachersByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where lastName is not null
        defaultTeacherShouldBeFound("lastName.specified=true");

        // Get all the teacherList where lastName is null
        defaultTeacherShouldNotBeFound("lastName.specified=false");
    }
                @Test
    @Transactional
    public void getAllTeachersByLastNameContainsSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where lastName contains DEFAULT_LAST_NAME
        defaultTeacherShouldBeFound("lastName.contains=" + DEFAULT_LAST_NAME);

        // Get all the teacherList where lastName contains UPDATED_LAST_NAME
        defaultTeacherShouldNotBeFound("lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllTeachersByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where lastName does not contain DEFAULT_LAST_NAME
        defaultTeacherShouldNotBeFound("lastName.doesNotContain=" + DEFAULT_LAST_NAME);

        // Get all the teacherList where lastName does not contain UPDATED_LAST_NAME
        defaultTeacherShouldBeFound("lastName.doesNotContain=" + UPDATED_LAST_NAME);
    }


    @Test
    @Transactional
    public void getAllTeachersByMiddleNameIsEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where middleName equals to DEFAULT_MIDDLE_NAME
        defaultTeacherShouldBeFound("middleName.equals=" + DEFAULT_MIDDLE_NAME);

        // Get all the teacherList where middleName equals to UPDATED_MIDDLE_NAME
        defaultTeacherShouldNotBeFound("middleName.equals=" + UPDATED_MIDDLE_NAME);
    }

    @Test
    @Transactional
    public void getAllTeachersByMiddleNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where middleName not equals to DEFAULT_MIDDLE_NAME
        defaultTeacherShouldNotBeFound("middleName.notEquals=" + DEFAULT_MIDDLE_NAME);

        // Get all the teacherList where middleName not equals to UPDATED_MIDDLE_NAME
        defaultTeacherShouldBeFound("middleName.notEquals=" + UPDATED_MIDDLE_NAME);
    }

    @Test
    @Transactional
    public void getAllTeachersByMiddleNameIsInShouldWork() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where middleName in DEFAULT_MIDDLE_NAME or UPDATED_MIDDLE_NAME
        defaultTeacherShouldBeFound("middleName.in=" + DEFAULT_MIDDLE_NAME + "," + UPDATED_MIDDLE_NAME);

        // Get all the teacherList where middleName equals to UPDATED_MIDDLE_NAME
        defaultTeacherShouldNotBeFound("middleName.in=" + UPDATED_MIDDLE_NAME);
    }

    @Test
    @Transactional
    public void getAllTeachersByMiddleNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where middleName is not null
        defaultTeacherShouldBeFound("middleName.specified=true");

        // Get all the teacherList where middleName is null
        defaultTeacherShouldNotBeFound("middleName.specified=false");
    }
                @Test
    @Transactional
    public void getAllTeachersByMiddleNameContainsSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where middleName contains DEFAULT_MIDDLE_NAME
        defaultTeacherShouldBeFound("middleName.contains=" + DEFAULT_MIDDLE_NAME);

        // Get all the teacherList where middleName contains UPDATED_MIDDLE_NAME
        defaultTeacherShouldNotBeFound("middleName.contains=" + UPDATED_MIDDLE_NAME);
    }

    @Test
    @Transactional
    public void getAllTeachersByMiddleNameNotContainsSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where middleName does not contain DEFAULT_MIDDLE_NAME
        defaultTeacherShouldNotBeFound("middleName.doesNotContain=" + DEFAULT_MIDDLE_NAME);

        // Get all the teacherList where middleName does not contain UPDATED_MIDDLE_NAME
        defaultTeacherShouldBeFound("middleName.doesNotContain=" + UPDATED_MIDDLE_NAME);
    }


    @Test
    @Transactional
    public void getAllTeachersByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where email equals to DEFAULT_EMAIL
        defaultTeacherShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the teacherList where email equals to UPDATED_EMAIL
        defaultTeacherShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllTeachersByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where email not equals to DEFAULT_EMAIL
        defaultTeacherShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the teacherList where email not equals to UPDATED_EMAIL
        defaultTeacherShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllTeachersByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultTeacherShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the teacherList where email equals to UPDATED_EMAIL
        defaultTeacherShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllTeachersByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where email is not null
        defaultTeacherShouldBeFound("email.specified=true");

        // Get all the teacherList where email is null
        defaultTeacherShouldNotBeFound("email.specified=false");
    }
                @Test
    @Transactional
    public void getAllTeachersByEmailContainsSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where email contains DEFAULT_EMAIL
        defaultTeacherShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the teacherList where email contains UPDATED_EMAIL
        defaultTeacherShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllTeachersByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where email does not contain DEFAULT_EMAIL
        defaultTeacherShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the teacherList where email does not contain UPDATED_EMAIL
        defaultTeacherShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }


    @Test
    @Transactional
    public void getAllTeachersByDateOfBirthIsEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where dateOfBirth equals to DEFAULT_DATE_OF_BIRTH
        defaultTeacherShouldBeFound("dateOfBirth.equals=" + DEFAULT_DATE_OF_BIRTH);

        // Get all the teacherList where dateOfBirth equals to UPDATED_DATE_OF_BIRTH
        defaultTeacherShouldNotBeFound("dateOfBirth.equals=" + UPDATED_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    public void getAllTeachersByDateOfBirthIsNotEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where dateOfBirth not equals to DEFAULT_DATE_OF_BIRTH
        defaultTeacherShouldNotBeFound("dateOfBirth.notEquals=" + DEFAULT_DATE_OF_BIRTH);

        // Get all the teacherList where dateOfBirth not equals to UPDATED_DATE_OF_BIRTH
        defaultTeacherShouldBeFound("dateOfBirth.notEquals=" + UPDATED_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    public void getAllTeachersByDateOfBirthIsInShouldWork() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where dateOfBirth in DEFAULT_DATE_OF_BIRTH or UPDATED_DATE_OF_BIRTH
        defaultTeacherShouldBeFound("dateOfBirth.in=" + DEFAULT_DATE_OF_BIRTH + "," + UPDATED_DATE_OF_BIRTH);

        // Get all the teacherList where dateOfBirth equals to UPDATED_DATE_OF_BIRTH
        defaultTeacherShouldNotBeFound("dateOfBirth.in=" + UPDATED_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    public void getAllTeachersByDateOfBirthIsNullOrNotNull() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where dateOfBirth is not null
        defaultTeacherShouldBeFound("dateOfBirth.specified=true");

        // Get all the teacherList where dateOfBirth is null
        defaultTeacherShouldNotBeFound("dateOfBirth.specified=false");
    }

    @Test
    @Transactional
    public void getAllTeachersByDateOfBirthIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where dateOfBirth is greater than or equal to DEFAULT_DATE_OF_BIRTH
        defaultTeacherShouldBeFound("dateOfBirth.greaterThanOrEqual=" + DEFAULT_DATE_OF_BIRTH);

        // Get all the teacherList where dateOfBirth is greater than or equal to UPDATED_DATE_OF_BIRTH
        defaultTeacherShouldNotBeFound("dateOfBirth.greaterThanOrEqual=" + UPDATED_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    public void getAllTeachersByDateOfBirthIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where dateOfBirth is less than or equal to DEFAULT_DATE_OF_BIRTH
        defaultTeacherShouldBeFound("dateOfBirth.lessThanOrEqual=" + DEFAULT_DATE_OF_BIRTH);

        // Get all the teacherList where dateOfBirth is less than or equal to SMALLER_DATE_OF_BIRTH
        defaultTeacherShouldNotBeFound("dateOfBirth.lessThanOrEqual=" + SMALLER_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    public void getAllTeachersByDateOfBirthIsLessThanSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where dateOfBirth is less than DEFAULT_DATE_OF_BIRTH
        defaultTeacherShouldNotBeFound("dateOfBirth.lessThan=" + DEFAULT_DATE_OF_BIRTH);

        // Get all the teacherList where dateOfBirth is less than UPDATED_DATE_OF_BIRTH
        defaultTeacherShouldBeFound("dateOfBirth.lessThan=" + UPDATED_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    public void getAllTeachersByDateOfBirthIsGreaterThanSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where dateOfBirth is greater than DEFAULT_DATE_OF_BIRTH
        defaultTeacherShouldNotBeFound("dateOfBirth.greaterThan=" + DEFAULT_DATE_OF_BIRTH);

        // Get all the teacherList where dateOfBirth is greater than SMALLER_DATE_OF_BIRTH
        defaultTeacherShouldBeFound("dateOfBirth.greaterThan=" + SMALLER_DATE_OF_BIRTH);
    }


    @Test
    @Transactional
    public void getAllTeachersByGenderIsEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where gender equals to DEFAULT_GENDER
        defaultTeacherShouldBeFound("gender.equals=" + DEFAULT_GENDER);

        // Get all the teacherList where gender equals to UPDATED_GENDER
        defaultTeacherShouldNotBeFound("gender.equals=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    public void getAllTeachersByGenderIsNotEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where gender not equals to DEFAULT_GENDER
        defaultTeacherShouldNotBeFound("gender.notEquals=" + DEFAULT_GENDER);

        // Get all the teacherList where gender not equals to UPDATED_GENDER
        defaultTeacherShouldBeFound("gender.notEquals=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    public void getAllTeachersByGenderIsInShouldWork() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where gender in DEFAULT_GENDER or UPDATED_GENDER
        defaultTeacherShouldBeFound("gender.in=" + DEFAULT_GENDER + "," + UPDATED_GENDER);

        // Get all the teacherList where gender equals to UPDATED_GENDER
        defaultTeacherShouldNotBeFound("gender.in=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    public void getAllTeachersByGenderIsNullOrNotNull() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where gender is not null
        defaultTeacherShouldBeFound("gender.specified=true");

        // Get all the teacherList where gender is null
        defaultTeacherShouldNotBeFound("gender.specified=false");
    }

    @Test
    @Transactional
    public void getAllTeachersByRegisterationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where registerationDate equals to DEFAULT_REGISTERATION_DATE
        defaultTeacherShouldBeFound("registerationDate.equals=" + DEFAULT_REGISTERATION_DATE);

        // Get all the teacherList where registerationDate equals to UPDATED_REGISTERATION_DATE
        defaultTeacherShouldNotBeFound("registerationDate.equals=" + UPDATED_REGISTERATION_DATE);
    }

    @Test
    @Transactional
    public void getAllTeachersByRegisterationDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where registerationDate not equals to DEFAULT_REGISTERATION_DATE
        defaultTeacherShouldNotBeFound("registerationDate.notEquals=" + DEFAULT_REGISTERATION_DATE);

        // Get all the teacherList where registerationDate not equals to UPDATED_REGISTERATION_DATE
        defaultTeacherShouldBeFound("registerationDate.notEquals=" + UPDATED_REGISTERATION_DATE);
    }

    @Test
    @Transactional
    public void getAllTeachersByRegisterationDateIsInShouldWork() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where registerationDate in DEFAULT_REGISTERATION_DATE or UPDATED_REGISTERATION_DATE
        defaultTeacherShouldBeFound("registerationDate.in=" + DEFAULT_REGISTERATION_DATE + "," + UPDATED_REGISTERATION_DATE);

        // Get all the teacherList where registerationDate equals to UPDATED_REGISTERATION_DATE
        defaultTeacherShouldNotBeFound("registerationDate.in=" + UPDATED_REGISTERATION_DATE);
    }

    @Test
    @Transactional
    public void getAllTeachersByRegisterationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where registerationDate is not null
        defaultTeacherShouldBeFound("registerationDate.specified=true");

        // Get all the teacherList where registerationDate is null
        defaultTeacherShouldNotBeFound("registerationDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllTeachersByRegisterationDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where registerationDate is greater than or equal to DEFAULT_REGISTERATION_DATE
        defaultTeacherShouldBeFound("registerationDate.greaterThanOrEqual=" + DEFAULT_REGISTERATION_DATE);

        // Get all the teacherList where registerationDate is greater than or equal to UPDATED_REGISTERATION_DATE
        defaultTeacherShouldNotBeFound("registerationDate.greaterThanOrEqual=" + UPDATED_REGISTERATION_DATE);
    }

    @Test
    @Transactional
    public void getAllTeachersByRegisterationDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where registerationDate is less than or equal to DEFAULT_REGISTERATION_DATE
        defaultTeacherShouldBeFound("registerationDate.lessThanOrEqual=" + DEFAULT_REGISTERATION_DATE);

        // Get all the teacherList where registerationDate is less than or equal to SMALLER_REGISTERATION_DATE
        defaultTeacherShouldNotBeFound("registerationDate.lessThanOrEqual=" + SMALLER_REGISTERATION_DATE);
    }

    @Test
    @Transactional
    public void getAllTeachersByRegisterationDateIsLessThanSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where registerationDate is less than DEFAULT_REGISTERATION_DATE
        defaultTeacherShouldNotBeFound("registerationDate.lessThan=" + DEFAULT_REGISTERATION_DATE);

        // Get all the teacherList where registerationDate is less than UPDATED_REGISTERATION_DATE
        defaultTeacherShouldBeFound("registerationDate.lessThan=" + UPDATED_REGISTERATION_DATE);
    }

    @Test
    @Transactional
    public void getAllTeachersByRegisterationDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where registerationDate is greater than DEFAULT_REGISTERATION_DATE
        defaultTeacherShouldNotBeFound("registerationDate.greaterThan=" + DEFAULT_REGISTERATION_DATE);

        // Get all the teacherList where registerationDate is greater than SMALLER_REGISTERATION_DATE
        defaultTeacherShouldBeFound("registerationDate.greaterThan=" + SMALLER_REGISTERATION_DATE);
    }


    @Test
    @Transactional
    public void getAllTeachersByTelephoneIsEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where telephone equals to DEFAULT_TELEPHONE
        defaultTeacherShouldBeFound("telephone.equals=" + DEFAULT_TELEPHONE);

        // Get all the teacherList where telephone equals to UPDATED_TELEPHONE
        defaultTeacherShouldNotBeFound("telephone.equals=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    public void getAllTeachersByTelephoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where telephone not equals to DEFAULT_TELEPHONE
        defaultTeacherShouldNotBeFound("telephone.notEquals=" + DEFAULT_TELEPHONE);

        // Get all the teacherList where telephone not equals to UPDATED_TELEPHONE
        defaultTeacherShouldBeFound("telephone.notEquals=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    public void getAllTeachersByTelephoneIsInShouldWork() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where telephone in DEFAULT_TELEPHONE or UPDATED_TELEPHONE
        defaultTeacherShouldBeFound("telephone.in=" + DEFAULT_TELEPHONE + "," + UPDATED_TELEPHONE);

        // Get all the teacherList where telephone equals to UPDATED_TELEPHONE
        defaultTeacherShouldNotBeFound("telephone.in=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    public void getAllTeachersByTelephoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where telephone is not null
        defaultTeacherShouldBeFound("telephone.specified=true");

        // Get all the teacherList where telephone is null
        defaultTeacherShouldNotBeFound("telephone.specified=false");
    }
                @Test
    @Transactional
    public void getAllTeachersByTelephoneContainsSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where telephone contains DEFAULT_TELEPHONE
        defaultTeacherShouldBeFound("telephone.contains=" + DEFAULT_TELEPHONE);

        // Get all the teacherList where telephone contains UPDATED_TELEPHONE
        defaultTeacherShouldNotBeFound("telephone.contains=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    public void getAllTeachersByTelephoneNotContainsSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where telephone does not contain DEFAULT_TELEPHONE
        defaultTeacherShouldNotBeFound("telephone.doesNotContain=" + DEFAULT_TELEPHONE);

        // Get all the teacherList where telephone does not contain UPDATED_TELEPHONE
        defaultTeacherShouldBeFound("telephone.doesNotContain=" + UPDATED_TELEPHONE);
    }


    @Test
    @Transactional
    public void getAllTeachersByMobileIsEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where mobile equals to DEFAULT_MOBILE
        defaultTeacherShouldBeFound("mobile.equals=" + DEFAULT_MOBILE);

        // Get all the teacherList where mobile equals to UPDATED_MOBILE
        defaultTeacherShouldNotBeFound("mobile.equals=" + UPDATED_MOBILE);
    }

    @Test
    @Transactional
    public void getAllTeachersByMobileIsNotEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where mobile not equals to DEFAULT_MOBILE
        defaultTeacherShouldNotBeFound("mobile.notEquals=" + DEFAULT_MOBILE);

        // Get all the teacherList where mobile not equals to UPDATED_MOBILE
        defaultTeacherShouldBeFound("mobile.notEquals=" + UPDATED_MOBILE);
    }

    @Test
    @Transactional
    public void getAllTeachersByMobileIsInShouldWork() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where mobile in DEFAULT_MOBILE or UPDATED_MOBILE
        defaultTeacherShouldBeFound("mobile.in=" + DEFAULT_MOBILE + "," + UPDATED_MOBILE);

        // Get all the teacherList where mobile equals to UPDATED_MOBILE
        defaultTeacherShouldNotBeFound("mobile.in=" + UPDATED_MOBILE);
    }

    @Test
    @Transactional
    public void getAllTeachersByMobileIsNullOrNotNull() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where mobile is not null
        defaultTeacherShouldBeFound("mobile.specified=true");

        // Get all the teacherList where mobile is null
        defaultTeacherShouldNotBeFound("mobile.specified=false");
    }
                @Test
    @Transactional
    public void getAllTeachersByMobileContainsSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where mobile contains DEFAULT_MOBILE
        defaultTeacherShouldBeFound("mobile.contains=" + DEFAULT_MOBILE);

        // Get all the teacherList where mobile contains UPDATED_MOBILE
        defaultTeacherShouldNotBeFound("mobile.contains=" + UPDATED_MOBILE);
    }

    @Test
    @Transactional
    public void getAllTeachersByMobileNotContainsSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where mobile does not contain DEFAULT_MOBILE
        defaultTeacherShouldNotBeFound("mobile.doesNotContain=" + DEFAULT_MOBILE);

        // Get all the teacherList where mobile does not contain UPDATED_MOBILE
        defaultTeacherShouldBeFound("mobile.doesNotContain=" + UPDATED_MOBILE);
    }


    @Test
    @Transactional
    public void getAllTeachersByFullPhotoUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where fullPhotoUrl equals to DEFAULT_FULL_PHOTO_URL
        defaultTeacherShouldBeFound("fullPhotoUrl.equals=" + DEFAULT_FULL_PHOTO_URL);

        // Get all the teacherList where fullPhotoUrl equals to UPDATED_FULL_PHOTO_URL
        defaultTeacherShouldNotBeFound("fullPhotoUrl.equals=" + UPDATED_FULL_PHOTO_URL);
    }

    @Test
    @Transactional
    public void getAllTeachersByFullPhotoUrlIsNotEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where fullPhotoUrl not equals to DEFAULT_FULL_PHOTO_URL
        defaultTeacherShouldNotBeFound("fullPhotoUrl.notEquals=" + DEFAULT_FULL_PHOTO_URL);

        // Get all the teacherList where fullPhotoUrl not equals to UPDATED_FULL_PHOTO_URL
        defaultTeacherShouldBeFound("fullPhotoUrl.notEquals=" + UPDATED_FULL_PHOTO_URL);
    }

    @Test
    @Transactional
    public void getAllTeachersByFullPhotoUrlIsInShouldWork() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where fullPhotoUrl in DEFAULT_FULL_PHOTO_URL or UPDATED_FULL_PHOTO_URL
        defaultTeacherShouldBeFound("fullPhotoUrl.in=" + DEFAULT_FULL_PHOTO_URL + "," + UPDATED_FULL_PHOTO_URL);

        // Get all the teacherList where fullPhotoUrl equals to UPDATED_FULL_PHOTO_URL
        defaultTeacherShouldNotBeFound("fullPhotoUrl.in=" + UPDATED_FULL_PHOTO_URL);
    }

    @Test
    @Transactional
    public void getAllTeachersByFullPhotoUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where fullPhotoUrl is not null
        defaultTeacherShouldBeFound("fullPhotoUrl.specified=true");

        // Get all the teacherList where fullPhotoUrl is null
        defaultTeacherShouldNotBeFound("fullPhotoUrl.specified=false");
    }
                @Test
    @Transactional
    public void getAllTeachersByFullPhotoUrlContainsSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where fullPhotoUrl contains DEFAULT_FULL_PHOTO_URL
        defaultTeacherShouldBeFound("fullPhotoUrl.contains=" + DEFAULT_FULL_PHOTO_URL);

        // Get all the teacherList where fullPhotoUrl contains UPDATED_FULL_PHOTO_URL
        defaultTeacherShouldNotBeFound("fullPhotoUrl.contains=" + UPDATED_FULL_PHOTO_URL);
    }

    @Test
    @Transactional
    public void getAllTeachersByFullPhotoUrlNotContainsSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where fullPhotoUrl does not contain DEFAULT_FULL_PHOTO_URL
        defaultTeacherShouldNotBeFound("fullPhotoUrl.doesNotContain=" + DEFAULT_FULL_PHOTO_URL);

        // Get all the teacherList where fullPhotoUrl does not contain UPDATED_FULL_PHOTO_URL
        defaultTeacherShouldBeFound("fullPhotoUrl.doesNotContain=" + UPDATED_FULL_PHOTO_URL);
    }


    @Test
    @Transactional
    public void getAllTeachersByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where active equals to DEFAULT_ACTIVE
        defaultTeacherShouldBeFound("active.equals=" + DEFAULT_ACTIVE);

        // Get all the teacherList where active equals to UPDATED_ACTIVE
        defaultTeacherShouldNotBeFound("active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllTeachersByActiveIsNotEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where active not equals to DEFAULT_ACTIVE
        defaultTeacherShouldNotBeFound("active.notEquals=" + DEFAULT_ACTIVE);

        // Get all the teacherList where active not equals to UPDATED_ACTIVE
        defaultTeacherShouldBeFound("active.notEquals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllTeachersByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where active in DEFAULT_ACTIVE or UPDATED_ACTIVE
        defaultTeacherShouldBeFound("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE);

        // Get all the teacherList where active equals to UPDATED_ACTIVE
        defaultTeacherShouldNotBeFound("active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllTeachersByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where active is not null
        defaultTeacherShouldBeFound("active.specified=true");

        // Get all the teacherList where active is null
        defaultTeacherShouldNotBeFound("active.specified=false");
    }

    @Test
    @Transactional
    public void getAllTeachersByKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where key equals to DEFAULT_KEY
        defaultTeacherShouldBeFound("key.equals=" + DEFAULT_KEY);

        // Get all the teacherList where key equals to UPDATED_KEY
        defaultTeacherShouldNotBeFound("key.equals=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    public void getAllTeachersByKeyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where key not equals to DEFAULT_KEY
        defaultTeacherShouldNotBeFound("key.notEquals=" + DEFAULT_KEY);

        // Get all the teacherList where key not equals to UPDATED_KEY
        defaultTeacherShouldBeFound("key.notEquals=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    public void getAllTeachersByKeyIsInShouldWork() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where key in DEFAULT_KEY or UPDATED_KEY
        defaultTeacherShouldBeFound("key.in=" + DEFAULT_KEY + "," + UPDATED_KEY);

        // Get all the teacherList where key equals to UPDATED_KEY
        defaultTeacherShouldNotBeFound("key.in=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    public void getAllTeachersByKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where key is not null
        defaultTeacherShouldBeFound("key.specified=true");

        // Get all the teacherList where key is null
        defaultTeacherShouldNotBeFound("key.specified=false");
    }
                @Test
    @Transactional
    public void getAllTeachersByKeyContainsSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where key contains DEFAULT_KEY
        defaultTeacherShouldBeFound("key.contains=" + DEFAULT_KEY);

        // Get all the teacherList where key contains UPDATED_KEY
        defaultTeacherShouldNotBeFound("key.contains=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    public void getAllTeachersByKeyNotContainsSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where key does not contain DEFAULT_KEY
        defaultTeacherShouldNotBeFound("key.doesNotContain=" + DEFAULT_KEY);

        // Get all the teacherList where key does not contain UPDATED_KEY
        defaultTeacherShouldBeFound("key.doesNotContain=" + UPDATED_KEY);
    }


    @Test
    @Transactional
    public void getAllTeachersByLeaveDateIsEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where leaveDate equals to DEFAULT_LEAVE_DATE
        defaultTeacherShouldBeFound("leaveDate.equals=" + DEFAULT_LEAVE_DATE);

        // Get all the teacherList where leaveDate equals to UPDATED_LEAVE_DATE
        defaultTeacherShouldNotBeFound("leaveDate.equals=" + UPDATED_LEAVE_DATE);
    }

    @Test
    @Transactional
    public void getAllTeachersByLeaveDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where leaveDate not equals to DEFAULT_LEAVE_DATE
        defaultTeacherShouldNotBeFound("leaveDate.notEquals=" + DEFAULT_LEAVE_DATE);

        // Get all the teacherList where leaveDate not equals to UPDATED_LEAVE_DATE
        defaultTeacherShouldBeFound("leaveDate.notEquals=" + UPDATED_LEAVE_DATE);
    }

    @Test
    @Transactional
    public void getAllTeachersByLeaveDateIsInShouldWork() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where leaveDate in DEFAULT_LEAVE_DATE or UPDATED_LEAVE_DATE
        defaultTeacherShouldBeFound("leaveDate.in=" + DEFAULT_LEAVE_DATE + "," + UPDATED_LEAVE_DATE);

        // Get all the teacherList where leaveDate equals to UPDATED_LEAVE_DATE
        defaultTeacherShouldNotBeFound("leaveDate.in=" + UPDATED_LEAVE_DATE);
    }

    @Test
    @Transactional
    public void getAllTeachersByLeaveDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where leaveDate is not null
        defaultTeacherShouldBeFound("leaveDate.specified=true");

        // Get all the teacherList where leaveDate is null
        defaultTeacherShouldNotBeFound("leaveDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllTeachersByLeaveDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where leaveDate is greater than or equal to DEFAULT_LEAVE_DATE
        defaultTeacherShouldBeFound("leaveDate.greaterThanOrEqual=" + DEFAULT_LEAVE_DATE);

        // Get all the teacherList where leaveDate is greater than or equal to UPDATED_LEAVE_DATE
        defaultTeacherShouldNotBeFound("leaveDate.greaterThanOrEqual=" + UPDATED_LEAVE_DATE);
    }

    @Test
    @Transactional
    public void getAllTeachersByLeaveDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where leaveDate is less than or equal to DEFAULT_LEAVE_DATE
        defaultTeacherShouldBeFound("leaveDate.lessThanOrEqual=" + DEFAULT_LEAVE_DATE);

        // Get all the teacherList where leaveDate is less than or equal to SMALLER_LEAVE_DATE
        defaultTeacherShouldNotBeFound("leaveDate.lessThanOrEqual=" + SMALLER_LEAVE_DATE);
    }

    @Test
    @Transactional
    public void getAllTeachersByLeaveDateIsLessThanSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where leaveDate is less than DEFAULT_LEAVE_DATE
        defaultTeacherShouldNotBeFound("leaveDate.lessThan=" + DEFAULT_LEAVE_DATE);

        // Get all the teacherList where leaveDate is less than UPDATED_LEAVE_DATE
        defaultTeacherShouldBeFound("leaveDate.lessThan=" + UPDATED_LEAVE_DATE);
    }

    @Test
    @Transactional
    public void getAllTeachersByLeaveDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where leaveDate is greater than DEFAULT_LEAVE_DATE
        defaultTeacherShouldNotBeFound("leaveDate.greaterThan=" + DEFAULT_LEAVE_DATE);

        // Get all the teacherList where leaveDate is greater than SMALLER_LEAVE_DATE
        defaultTeacherShouldBeFound("leaveDate.greaterThan=" + SMALLER_LEAVE_DATE);
    }


    @Test
    @Transactional
    public void getAllTeachersByIsShowingHomeIsEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where isShowingHome equals to DEFAULT_IS_SHOWING_HOME
        defaultTeacherShouldBeFound("isShowingHome.equals=" + DEFAULT_IS_SHOWING_HOME);

        // Get all the teacherList where isShowingHome equals to UPDATED_IS_SHOWING_HOME
        defaultTeacherShouldNotBeFound("isShowingHome.equals=" + UPDATED_IS_SHOWING_HOME);
    }

    @Test
    @Transactional
    public void getAllTeachersByIsShowingHomeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where isShowingHome not equals to DEFAULT_IS_SHOWING_HOME
        defaultTeacherShouldNotBeFound("isShowingHome.notEquals=" + DEFAULT_IS_SHOWING_HOME);

        // Get all the teacherList where isShowingHome not equals to UPDATED_IS_SHOWING_HOME
        defaultTeacherShouldBeFound("isShowingHome.notEquals=" + UPDATED_IS_SHOWING_HOME);
    }

    @Test
    @Transactional
    public void getAllTeachersByIsShowingHomeIsInShouldWork() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where isShowingHome in DEFAULT_IS_SHOWING_HOME or UPDATED_IS_SHOWING_HOME
        defaultTeacherShouldBeFound("isShowingHome.in=" + DEFAULT_IS_SHOWING_HOME + "," + UPDATED_IS_SHOWING_HOME);

        // Get all the teacherList where isShowingHome equals to UPDATED_IS_SHOWING_HOME
        defaultTeacherShouldNotBeFound("isShowingHome.in=" + UPDATED_IS_SHOWING_HOME);
    }

    @Test
    @Transactional
    public void getAllTeachersByIsShowingHomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where isShowingHome is not null
        defaultTeacherShouldBeFound("isShowingHome.specified=true");

        // Get all the teacherList where isShowingHome is null
        defaultTeacherShouldNotBeFound("isShowingHome.specified=false");
    }

    @Test
    @Transactional
    public void getAllTeachersByModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);
        User modifiedBy = UserResourceIT.createEntity(em);
        em.persist(modifiedBy);
        em.flush();
        teacher.setModifiedBy(modifiedBy);
        teacherRepository.saveAndFlush(teacher);
        Long modifiedById = modifiedBy.getId();

        // Get all the teacherList where modifiedBy equals to modifiedById
        defaultTeacherShouldBeFound("modifiedById.equals=" + modifiedById);

        // Get all the teacherList where modifiedBy equals to modifiedById + 1
        defaultTeacherShouldNotBeFound("modifiedById.equals=" + (modifiedById + 1));
    }


    @Test
    @Transactional
    public void getAllTeachersByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        teacher.setUser(user);
        teacherRepository.saveAndFlush(teacher);
        Long userId = user.getId();

        // Get all the teacherList where user equals to userId
        defaultTeacherShouldBeFound("userId.equals=" + userId);

        // Get all the teacherList where user equals to userId + 1
        defaultTeacherShouldNotBeFound("userId.equals=" + (userId + 1));
    }


    @Test
    @Transactional
    public void getAllTeachersBySkillsIsEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);
        Skill skills = SkillResourceIT.createEntity(em);
        em.persist(skills);
        em.flush();
        teacher.addSkills(skills);
        teacherRepository.saveAndFlush(teacher);
        Long skillsId = skills.getId();

        // Get all the teacherList where skills equals to skillsId
        defaultTeacherShouldBeFound("skillsId.equals=" + skillsId);

        // Get all the teacherList where skills equals to skillsId + 1
        defaultTeacherShouldNotBeFound("skillsId.equals=" + (skillsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTeacherShouldBeFound(String filter) throws Exception {
        restTeacherMockMvc.perform(get("/api/teachers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(teacher.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].middleName").value(hasItem(DEFAULT_MIDDLE_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].dateOfBirth").value(hasItem(DEFAULT_DATE_OF_BIRTH.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].registerationDate").value(hasItem(DEFAULT_REGISTERATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)))
            .andExpect(jsonPath("$.[*].mobile").value(hasItem(DEFAULT_MOBILE)))
            .andExpect(jsonPath("$.[*].fullPhotoUrl").value(hasItem(DEFAULT_FULL_PHOTO_URL)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY)))
            .andExpect(jsonPath("$.[*].about").value(hasItem(DEFAULT_ABOUT.toString())))
            .andExpect(jsonPath("$.[*].portfolia").value(hasItem(DEFAULT_PORTFOLIA.toString())))
            .andExpect(jsonPath("$.[*].infoContentType").value(hasItem(DEFAULT_INFO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].info").value(hasItem(Base64Utils.encodeToString(DEFAULT_INFO))))
            .andExpect(jsonPath("$.[*].leaveDate").value(hasItem(DEFAULT_LEAVE_DATE.toString())))
            .andExpect(jsonPath("$.[*].isShowingHome").value(hasItem(DEFAULT_IS_SHOWING_HOME.booleanValue())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));

        // Check, that the count call also returns 1
        restTeacherMockMvc.perform(get("/api/teachers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTeacherShouldNotBeFound(String filter) throws Exception {
        restTeacherMockMvc.perform(get("/api/teachers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTeacherMockMvc.perform(get("/api/teachers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingTeacher() throws Exception {
        // Get the teacher
        restTeacherMockMvc.perform(get("/api/teachers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTeacher() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        int databaseSizeBeforeUpdate = teacherRepository.findAll().size();

        // Update the teacher
        Teacher updatedTeacher = teacherRepository.findById(teacher.getId()).get();
        // Disconnect from session so that the updates on updatedTeacher are not directly saved in db
        em.detach(updatedTeacher);
        updatedTeacher
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .middleName(UPDATED_MIDDLE_NAME)
            .email(UPDATED_EMAIL)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .gender(UPDATED_GENDER)
            .registerationDate(UPDATED_REGISTERATION_DATE)
            .telephone(UPDATED_TELEPHONE)
            .mobile(UPDATED_MOBILE)
            .fullPhotoUrl(UPDATED_FULL_PHOTO_URL)
            .active(UPDATED_ACTIVE)
            .key(UPDATED_KEY)
            .about(UPDATED_ABOUT)
            .portfolia(UPDATED_PORTFOLIA)
            .info(UPDATED_INFO)
            .infoContentType(UPDATED_INFO_CONTENT_TYPE)
            .leaveDate(UPDATED_LEAVE_DATE)
            .isShowingHome(UPDATED_IS_SHOWING_HOME)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);
        TeacherDTO teacherDTO = teacherMapper.toDto(updatedTeacher);

        restTeacherMockMvc.perform(put("/api/teachers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(teacherDTO)))
            .andExpect(status().isOk());

        // Validate the Teacher in the database
        List<Teacher> teacherList = teacherRepository.findAll();
        assertThat(teacherList).hasSize(databaseSizeBeforeUpdate);
        Teacher testTeacher = teacherList.get(teacherList.size() - 1);
        assertThat(testTeacher.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testTeacher.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testTeacher.getMiddleName()).isEqualTo(UPDATED_MIDDLE_NAME);
        assertThat(testTeacher.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testTeacher.getDateOfBirth()).isEqualTo(UPDATED_DATE_OF_BIRTH);
        assertThat(testTeacher.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testTeacher.getRegisterationDate()).isEqualTo(UPDATED_REGISTERATION_DATE);
        assertThat(testTeacher.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testTeacher.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testTeacher.getFullPhotoUrl()).isEqualTo(UPDATED_FULL_PHOTO_URL);
        assertThat(testTeacher.isActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testTeacher.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testTeacher.getAbout()).isEqualTo(UPDATED_ABOUT);
        assertThat(testTeacher.getPortfolia()).isEqualTo(UPDATED_PORTFOLIA);
        assertThat(testTeacher.getInfo()).isEqualTo(UPDATED_INFO);
        assertThat(testTeacher.getInfoContentType()).isEqualTo(UPDATED_INFO_CONTENT_TYPE);
        assertThat(testTeacher.getLeaveDate()).isEqualTo(UPDATED_LEAVE_DATE);
        assertThat(testTeacher.isIsShowingHome()).isEqualTo(UPDATED_IS_SHOWING_HOME);
        assertThat(testTeacher.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testTeacher.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingTeacher() throws Exception {
        int databaseSizeBeforeUpdate = teacherRepository.findAll().size();

        // Create the Teacher
        TeacherDTO teacherDTO = teacherMapper.toDto(teacher);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTeacherMockMvc.perform(put("/api/teachers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(teacherDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Teacher in the database
        List<Teacher> teacherList = teacherRepository.findAll();
        assertThat(teacherList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTeacher() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        int databaseSizeBeforeDelete = teacherRepository.findAll().size();

        // Delete the teacher
        restTeacherMockMvc.perform(delete("/api/teachers/{id}", teacher.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Teacher> teacherList = teacherRepository.findAll();
        assertThat(teacherList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
