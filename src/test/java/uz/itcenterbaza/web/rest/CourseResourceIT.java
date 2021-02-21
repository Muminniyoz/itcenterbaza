package uz.itcenterbaza.web.rest;

import uz.itcenterbaza.ItcenterbazaApp;
import uz.itcenterbaza.domain.Course;
import uz.itcenterbaza.domain.Registered;
import uz.itcenterbaza.domain.Teacher;
import uz.itcenterbaza.domain.Center;
import uz.itcenterbaza.domain.Skill;
import uz.itcenterbaza.repository.CourseRepository;
import uz.itcenterbaza.service.CourseService;
import uz.itcenterbaza.service.dto.CourseDTO;
import uz.itcenterbaza.service.mapper.CourseMapper;
import uz.itcenterbaza.service.dto.CourseCriteria;
import uz.itcenterbaza.service.CourseQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import uz.itcenterbaza.domain.enumeration.CourseStatus;
/**
 * Integration tests for the {@link CourseResource} REST controller.
 */
@SpringBootTest(classes = ItcenterbazaApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class CourseResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final Float DEFAULT_PRICE = 1F;
    private static final Float UPDATED_PRICE = 2F;
    private static final Float SMALLER_PRICE = 1F - 1F;

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_START_DATE = LocalDate.ofEpochDay(-1L);

    private static final CourseStatus DEFAULT_STATUS = CourseStatus.ACTIVE;
    private static final CourseStatus UPDATED_STATUS = CourseStatus.PLANNING;

    private static final Integer DEFAULT_DURATION = 1;
    private static final Integer UPDATED_DURATION = 2;
    private static final Integer SMALLER_DURATION = 1 - 1;

    private static final byte[] DEFAULT_PLAN_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PLAN_FILE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PLAN_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PLAN_FILE_CONTENT_TYPE = "image/png";

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseQueryService courseQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCourseMockMvc;

    private Course course;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Course createEntity(EntityManager em) {
        Course course = new Course()
            .title(DEFAULT_TITLE)
            .price(DEFAULT_PRICE)
            .startDate(DEFAULT_START_DATE)
            .status(DEFAULT_STATUS)
            .duration(DEFAULT_DURATION)
            .planFile(DEFAULT_PLAN_FILE)
            .planFileContentType(DEFAULT_PLAN_FILE_CONTENT_TYPE);
        return course;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Course createUpdatedEntity(EntityManager em) {
        Course course = new Course()
            .title(UPDATED_TITLE)
            .price(UPDATED_PRICE)
            .startDate(UPDATED_START_DATE)
            .status(UPDATED_STATUS)
            .duration(UPDATED_DURATION)
            .planFile(UPDATED_PLAN_FILE)
            .planFileContentType(UPDATED_PLAN_FILE_CONTENT_TYPE);
        return course;
    }

    @BeforeEach
    public void initTest() {
        course = createEntity(em);
    }

    @Test
    @Transactional
    public void createCourse() throws Exception {
        int databaseSizeBeforeCreate = courseRepository.findAll().size();
        // Create the Course
        CourseDTO courseDTO = courseMapper.toDto(course);
        restCourseMockMvc.perform(post("/api/courses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(courseDTO)))
            .andExpect(status().isCreated());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeCreate + 1);
        Course testCourse = courseList.get(courseList.size() - 1);
        assertThat(testCourse.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testCourse.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testCourse.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testCourse.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testCourse.getDuration()).isEqualTo(DEFAULT_DURATION);
        assertThat(testCourse.getPlanFile()).isEqualTo(DEFAULT_PLAN_FILE);
        assertThat(testCourse.getPlanFileContentType()).isEqualTo(DEFAULT_PLAN_FILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createCourseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = courseRepository.findAll().size();

        // Create the Course with an existing ID
        course.setId(1L);
        CourseDTO courseDTO = courseMapper.toDto(course);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCourseMockMvc.perform(post("/api/courses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(courseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllCourses() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList
        restCourseMockMvc.perform(get("/api/courses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(course.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION)))
            .andExpect(jsonPath("$.[*].planFileContentType").value(hasItem(DEFAULT_PLAN_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].planFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_PLAN_FILE))));
    }
    
    @Test
    @Transactional
    public void getCourse() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get the course
        restCourseMockMvc.perform(get("/api/courses/{id}", course.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(course.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION))
            .andExpect(jsonPath("$.planFileContentType").value(DEFAULT_PLAN_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.planFile").value(Base64Utils.encodeToString(DEFAULT_PLAN_FILE)));
    }


    @Test
    @Transactional
    public void getCoursesByIdFiltering() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        Long id = course.getId();

        defaultCourseShouldBeFound("id.equals=" + id);
        defaultCourseShouldNotBeFound("id.notEquals=" + id);

        defaultCourseShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCourseShouldNotBeFound("id.greaterThan=" + id);

        defaultCourseShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCourseShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllCoursesByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where title equals to DEFAULT_TITLE
        defaultCourseShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the courseList where title equals to UPDATED_TITLE
        defaultCourseShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllCoursesByTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where title not equals to DEFAULT_TITLE
        defaultCourseShouldNotBeFound("title.notEquals=" + DEFAULT_TITLE);

        // Get all the courseList where title not equals to UPDATED_TITLE
        defaultCourseShouldBeFound("title.notEquals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllCoursesByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultCourseShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the courseList where title equals to UPDATED_TITLE
        defaultCourseShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllCoursesByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where title is not null
        defaultCourseShouldBeFound("title.specified=true");

        // Get all the courseList where title is null
        defaultCourseShouldNotBeFound("title.specified=false");
    }
                @Test
    @Transactional
    public void getAllCoursesByTitleContainsSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where title contains DEFAULT_TITLE
        defaultCourseShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the courseList where title contains UPDATED_TITLE
        defaultCourseShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllCoursesByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where title does not contain DEFAULT_TITLE
        defaultCourseShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the courseList where title does not contain UPDATED_TITLE
        defaultCourseShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }


    @Test
    @Transactional
    public void getAllCoursesByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where price equals to DEFAULT_PRICE
        defaultCourseShouldBeFound("price.equals=" + DEFAULT_PRICE);

        // Get all the courseList where price equals to UPDATED_PRICE
        defaultCourseShouldNotBeFound("price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllCoursesByPriceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where price not equals to DEFAULT_PRICE
        defaultCourseShouldNotBeFound("price.notEquals=" + DEFAULT_PRICE);

        // Get all the courseList where price not equals to UPDATED_PRICE
        defaultCourseShouldBeFound("price.notEquals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllCoursesByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where price in DEFAULT_PRICE or UPDATED_PRICE
        defaultCourseShouldBeFound("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE);

        // Get all the courseList where price equals to UPDATED_PRICE
        defaultCourseShouldNotBeFound("price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllCoursesByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where price is not null
        defaultCourseShouldBeFound("price.specified=true");

        // Get all the courseList where price is null
        defaultCourseShouldNotBeFound("price.specified=false");
    }

    @Test
    @Transactional
    public void getAllCoursesByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where price is greater than or equal to DEFAULT_PRICE
        defaultCourseShouldBeFound("price.greaterThanOrEqual=" + DEFAULT_PRICE);

        // Get all the courseList where price is greater than or equal to UPDATED_PRICE
        defaultCourseShouldNotBeFound("price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllCoursesByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where price is less than or equal to DEFAULT_PRICE
        defaultCourseShouldBeFound("price.lessThanOrEqual=" + DEFAULT_PRICE);

        // Get all the courseList where price is less than or equal to SMALLER_PRICE
        defaultCourseShouldNotBeFound("price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    public void getAllCoursesByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where price is less than DEFAULT_PRICE
        defaultCourseShouldNotBeFound("price.lessThan=" + DEFAULT_PRICE);

        // Get all the courseList where price is less than UPDATED_PRICE
        defaultCourseShouldBeFound("price.lessThan=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllCoursesByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where price is greater than DEFAULT_PRICE
        defaultCourseShouldNotBeFound("price.greaterThan=" + DEFAULT_PRICE);

        // Get all the courseList where price is greater than SMALLER_PRICE
        defaultCourseShouldBeFound("price.greaterThan=" + SMALLER_PRICE);
    }


    @Test
    @Transactional
    public void getAllCoursesByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where startDate equals to DEFAULT_START_DATE
        defaultCourseShouldBeFound("startDate.equals=" + DEFAULT_START_DATE);

        // Get all the courseList where startDate equals to UPDATED_START_DATE
        defaultCourseShouldNotBeFound("startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllCoursesByStartDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where startDate not equals to DEFAULT_START_DATE
        defaultCourseShouldNotBeFound("startDate.notEquals=" + DEFAULT_START_DATE);

        // Get all the courseList where startDate not equals to UPDATED_START_DATE
        defaultCourseShouldBeFound("startDate.notEquals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllCoursesByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where startDate in DEFAULT_START_DATE or UPDATED_START_DATE
        defaultCourseShouldBeFound("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE);

        // Get all the courseList where startDate equals to UPDATED_START_DATE
        defaultCourseShouldNotBeFound("startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllCoursesByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where startDate is not null
        defaultCourseShouldBeFound("startDate.specified=true");

        // Get all the courseList where startDate is null
        defaultCourseShouldNotBeFound("startDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllCoursesByStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where startDate is greater than or equal to DEFAULT_START_DATE
        defaultCourseShouldBeFound("startDate.greaterThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the courseList where startDate is greater than or equal to UPDATED_START_DATE
        defaultCourseShouldNotBeFound("startDate.greaterThanOrEqual=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllCoursesByStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where startDate is less than or equal to DEFAULT_START_DATE
        defaultCourseShouldBeFound("startDate.lessThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the courseList where startDate is less than or equal to SMALLER_START_DATE
        defaultCourseShouldNotBeFound("startDate.lessThanOrEqual=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    public void getAllCoursesByStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where startDate is less than DEFAULT_START_DATE
        defaultCourseShouldNotBeFound("startDate.lessThan=" + DEFAULT_START_DATE);

        // Get all the courseList where startDate is less than UPDATED_START_DATE
        defaultCourseShouldBeFound("startDate.lessThan=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllCoursesByStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where startDate is greater than DEFAULT_START_DATE
        defaultCourseShouldNotBeFound("startDate.greaterThan=" + DEFAULT_START_DATE);

        // Get all the courseList where startDate is greater than SMALLER_START_DATE
        defaultCourseShouldBeFound("startDate.greaterThan=" + SMALLER_START_DATE);
    }


    @Test
    @Transactional
    public void getAllCoursesByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where status equals to DEFAULT_STATUS
        defaultCourseShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the courseList where status equals to UPDATED_STATUS
        defaultCourseShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllCoursesByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where status not equals to DEFAULT_STATUS
        defaultCourseShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the courseList where status not equals to UPDATED_STATUS
        defaultCourseShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllCoursesByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultCourseShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the courseList where status equals to UPDATED_STATUS
        defaultCourseShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllCoursesByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where status is not null
        defaultCourseShouldBeFound("status.specified=true");

        // Get all the courseList where status is null
        defaultCourseShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    public void getAllCoursesByDurationIsEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where duration equals to DEFAULT_DURATION
        defaultCourseShouldBeFound("duration.equals=" + DEFAULT_DURATION);

        // Get all the courseList where duration equals to UPDATED_DURATION
        defaultCourseShouldNotBeFound("duration.equals=" + UPDATED_DURATION);
    }

    @Test
    @Transactional
    public void getAllCoursesByDurationIsNotEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where duration not equals to DEFAULT_DURATION
        defaultCourseShouldNotBeFound("duration.notEquals=" + DEFAULT_DURATION);

        // Get all the courseList where duration not equals to UPDATED_DURATION
        defaultCourseShouldBeFound("duration.notEquals=" + UPDATED_DURATION);
    }

    @Test
    @Transactional
    public void getAllCoursesByDurationIsInShouldWork() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where duration in DEFAULT_DURATION or UPDATED_DURATION
        defaultCourseShouldBeFound("duration.in=" + DEFAULT_DURATION + "," + UPDATED_DURATION);

        // Get all the courseList where duration equals to UPDATED_DURATION
        defaultCourseShouldNotBeFound("duration.in=" + UPDATED_DURATION);
    }

    @Test
    @Transactional
    public void getAllCoursesByDurationIsNullOrNotNull() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where duration is not null
        defaultCourseShouldBeFound("duration.specified=true");

        // Get all the courseList where duration is null
        defaultCourseShouldNotBeFound("duration.specified=false");
    }

    @Test
    @Transactional
    public void getAllCoursesByDurationIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where duration is greater than or equal to DEFAULT_DURATION
        defaultCourseShouldBeFound("duration.greaterThanOrEqual=" + DEFAULT_DURATION);

        // Get all the courseList where duration is greater than or equal to UPDATED_DURATION
        defaultCourseShouldNotBeFound("duration.greaterThanOrEqual=" + UPDATED_DURATION);
    }

    @Test
    @Transactional
    public void getAllCoursesByDurationIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where duration is less than or equal to DEFAULT_DURATION
        defaultCourseShouldBeFound("duration.lessThanOrEqual=" + DEFAULT_DURATION);

        // Get all the courseList where duration is less than or equal to SMALLER_DURATION
        defaultCourseShouldNotBeFound("duration.lessThanOrEqual=" + SMALLER_DURATION);
    }

    @Test
    @Transactional
    public void getAllCoursesByDurationIsLessThanSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where duration is less than DEFAULT_DURATION
        defaultCourseShouldNotBeFound("duration.lessThan=" + DEFAULT_DURATION);

        // Get all the courseList where duration is less than UPDATED_DURATION
        defaultCourseShouldBeFound("duration.lessThan=" + UPDATED_DURATION);
    }

    @Test
    @Transactional
    public void getAllCoursesByDurationIsGreaterThanSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where duration is greater than DEFAULT_DURATION
        defaultCourseShouldNotBeFound("duration.greaterThan=" + DEFAULT_DURATION);

        // Get all the courseList where duration is greater than SMALLER_DURATION
        defaultCourseShouldBeFound("duration.greaterThan=" + SMALLER_DURATION);
    }


    @Test
    @Transactional
    public void getAllCoursesByRegisteredIsEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);
        Registered registered = RegisteredResourceIT.createEntity(em);
        em.persist(registered);
        em.flush();
        course.addRegistered(registered);
        courseRepository.saveAndFlush(course);
        Long registeredId = registered.getId();

        // Get all the courseList where registered equals to registeredId
        defaultCourseShouldBeFound("registeredId.equals=" + registeredId);

        // Get all the courseList where registered equals to registeredId + 1
        defaultCourseShouldNotBeFound("registeredId.equals=" + (registeredId + 1));
    }


    @Test
    @Transactional
    public void getAllCoursesByTeacherIsEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);
        Teacher teacher = TeacherResourceIT.createEntity(em);
        em.persist(teacher);
        em.flush();
        course.setTeacher(teacher);
        courseRepository.saveAndFlush(course);
        Long teacherId = teacher.getId();

        // Get all the courseList where teacher equals to teacherId
        defaultCourseShouldBeFound("teacherId.equals=" + teacherId);

        // Get all the courseList where teacher equals to teacherId + 1
        defaultCourseShouldNotBeFound("teacherId.equals=" + (teacherId + 1));
    }


    @Test
    @Transactional
    public void getAllCoursesByCenterIsEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);
        Center center = CenterResourceIT.createEntity(em);
        em.persist(center);
        em.flush();
        course.setCenter(center);
        courseRepository.saveAndFlush(course);
        Long centerId = center.getId();

        // Get all the courseList where center equals to centerId
        defaultCourseShouldBeFound("centerId.equals=" + centerId);

        // Get all the courseList where center equals to centerId + 1
        defaultCourseShouldNotBeFound("centerId.equals=" + (centerId + 1));
    }


    @Test
    @Transactional
    public void getAllCoursesBySkillIsEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);
        Skill skill = SkillResourceIT.createEntity(em);
        em.persist(skill);
        em.flush();
        course.setSkill(skill);
        courseRepository.saveAndFlush(course);
        Long skillId = skill.getId();

        // Get all the courseList where skill equals to skillId
        defaultCourseShouldBeFound("skillId.equals=" + skillId);

        // Get all the courseList where skill equals to skillId + 1
        defaultCourseShouldNotBeFound("skillId.equals=" + (skillId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCourseShouldBeFound(String filter) throws Exception {
        restCourseMockMvc.perform(get("/api/courses?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(course.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION)))
            .andExpect(jsonPath("$.[*].planFileContentType").value(hasItem(DEFAULT_PLAN_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].planFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_PLAN_FILE))));

        // Check, that the count call also returns 1
        restCourseMockMvc.perform(get("/api/courses/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCourseShouldNotBeFound(String filter) throws Exception {
        restCourseMockMvc.perform(get("/api/courses?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCourseMockMvc.perform(get("/api/courses/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingCourse() throws Exception {
        // Get the course
        restCourseMockMvc.perform(get("/api/courses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCourse() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        int databaseSizeBeforeUpdate = courseRepository.findAll().size();

        // Update the course
        Course updatedCourse = courseRepository.findById(course.getId()).get();
        // Disconnect from session so that the updates on updatedCourse are not directly saved in db
        em.detach(updatedCourse);
        updatedCourse
            .title(UPDATED_TITLE)
            .price(UPDATED_PRICE)
            .startDate(UPDATED_START_DATE)
            .status(UPDATED_STATUS)
            .duration(UPDATED_DURATION)
            .planFile(UPDATED_PLAN_FILE)
            .planFileContentType(UPDATED_PLAN_FILE_CONTENT_TYPE);
        CourseDTO courseDTO = courseMapper.toDto(updatedCourse);

        restCourseMockMvc.perform(put("/api/courses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(courseDTO)))
            .andExpect(status().isOk());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
        Course testCourse = courseList.get(courseList.size() - 1);
        assertThat(testCourse.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testCourse.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testCourse.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testCourse.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testCourse.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testCourse.getPlanFile()).isEqualTo(UPDATED_PLAN_FILE);
        assertThat(testCourse.getPlanFileContentType()).isEqualTo(UPDATED_PLAN_FILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingCourse() throws Exception {
        int databaseSizeBeforeUpdate = courseRepository.findAll().size();

        // Create the Course
        CourseDTO courseDTO = courseMapper.toDto(course);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCourseMockMvc.perform(put("/api/courses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(courseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCourse() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        int databaseSizeBeforeDelete = courseRepository.findAll().size();

        // Delete the course
        restCourseMockMvc.perform(delete("/api/courses/{id}", course.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
