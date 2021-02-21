package uz.itcenterbaza.web.rest;

import uz.itcenterbaza.ItcenterbazaApp;
import uz.itcenterbaza.domain.Center;
import uz.itcenterbaza.domain.User;
import uz.itcenterbaza.domain.Regions;
import uz.itcenterbaza.repository.CenterRepository;
import uz.itcenterbaza.service.CenterService;
import uz.itcenterbaza.service.dto.CenterDTO;
import uz.itcenterbaza.service.mapper.CenterMapper;
import uz.itcenterbaza.service.dto.CenterCriteria;
import uz.itcenterbaza.service.CenterQueryService;

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

/**
 * Integration tests for the {@link CenterResource} REST controller.
 */
@SpringBootTest(classes = ItcenterbazaApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class CenterResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_INFO = "AAAAAAAAAA";
    private static final String UPDATED_INFO = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_START_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_GOOGLE_MAP_URL = "AAAAAAAAAA";
    private static final String UPDATED_GOOGLE_MAP_URL = "BBBBBBBBBB";

    @Autowired
    private CenterRepository centerRepository;

    @Autowired
    private CenterMapper centerMapper;

    @Autowired
    private CenterService centerService;

    @Autowired
    private CenterQueryService centerQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCenterMockMvc;

    private Center center;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Center createEntity(EntityManager em) {
        Center center = new Center()
            .title(DEFAULT_TITLE)
            .info(DEFAULT_INFO)
            .startDate(DEFAULT_START_DATE)
            .googleMapUrl(DEFAULT_GOOGLE_MAP_URL);
        return center;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Center createUpdatedEntity(EntityManager em) {
        Center center = new Center()
            .title(UPDATED_TITLE)
            .info(UPDATED_INFO)
            .startDate(UPDATED_START_DATE)
            .googleMapUrl(UPDATED_GOOGLE_MAP_URL);
        return center;
    }

    @BeforeEach
    public void initTest() {
        center = createEntity(em);
    }

    @Test
    @Transactional
    public void createCenter() throws Exception {
        int databaseSizeBeforeCreate = centerRepository.findAll().size();
        // Create the Center
        CenterDTO centerDTO = centerMapper.toDto(center);
        restCenterMockMvc.perform(post("/api/centers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(centerDTO)))
            .andExpect(status().isCreated());

        // Validate the Center in the database
        List<Center> centerList = centerRepository.findAll();
        assertThat(centerList).hasSize(databaseSizeBeforeCreate + 1);
        Center testCenter = centerList.get(centerList.size() - 1);
        assertThat(testCenter.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testCenter.getInfo()).isEqualTo(DEFAULT_INFO);
        assertThat(testCenter.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testCenter.getGoogleMapUrl()).isEqualTo(DEFAULT_GOOGLE_MAP_URL);
    }

    @Test
    @Transactional
    public void createCenterWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = centerRepository.findAll().size();

        // Create the Center with an existing ID
        center.setId(1L);
        CenterDTO centerDTO = centerMapper.toDto(center);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCenterMockMvc.perform(post("/api/centers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(centerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Center in the database
        List<Center> centerList = centerRepository.findAll();
        assertThat(centerList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = centerRepository.findAll().size();
        // set the field null
        center.setTitle(null);

        // Create the Center, which fails.
        CenterDTO centerDTO = centerMapper.toDto(center);


        restCenterMockMvc.perform(post("/api/centers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(centerDTO)))
            .andExpect(status().isBadRequest());

        List<Center> centerList = centerRepository.findAll();
        assertThat(centerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCenters() throws Exception {
        // Initialize the database
        centerRepository.saveAndFlush(center);

        // Get all the centerList
        restCenterMockMvc.perform(get("/api/centers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(center.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].info").value(hasItem(DEFAULT_INFO)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].googleMapUrl").value(hasItem(DEFAULT_GOOGLE_MAP_URL)));
    }
    
    @Test
    @Transactional
    public void getCenter() throws Exception {
        // Initialize the database
        centerRepository.saveAndFlush(center);

        // Get the center
        restCenterMockMvc.perform(get("/api/centers/{id}", center.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(center.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.info").value(DEFAULT_INFO))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.googleMapUrl").value(DEFAULT_GOOGLE_MAP_URL));
    }


    @Test
    @Transactional
    public void getCentersByIdFiltering() throws Exception {
        // Initialize the database
        centerRepository.saveAndFlush(center);

        Long id = center.getId();

        defaultCenterShouldBeFound("id.equals=" + id);
        defaultCenterShouldNotBeFound("id.notEquals=" + id);

        defaultCenterShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCenterShouldNotBeFound("id.greaterThan=" + id);

        defaultCenterShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCenterShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllCentersByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        centerRepository.saveAndFlush(center);

        // Get all the centerList where title equals to DEFAULT_TITLE
        defaultCenterShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the centerList where title equals to UPDATED_TITLE
        defaultCenterShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllCentersByTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        centerRepository.saveAndFlush(center);

        // Get all the centerList where title not equals to DEFAULT_TITLE
        defaultCenterShouldNotBeFound("title.notEquals=" + DEFAULT_TITLE);

        // Get all the centerList where title not equals to UPDATED_TITLE
        defaultCenterShouldBeFound("title.notEquals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllCentersByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        centerRepository.saveAndFlush(center);

        // Get all the centerList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultCenterShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the centerList where title equals to UPDATED_TITLE
        defaultCenterShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllCentersByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        centerRepository.saveAndFlush(center);

        // Get all the centerList where title is not null
        defaultCenterShouldBeFound("title.specified=true");

        // Get all the centerList where title is null
        defaultCenterShouldNotBeFound("title.specified=false");
    }
                @Test
    @Transactional
    public void getAllCentersByTitleContainsSomething() throws Exception {
        // Initialize the database
        centerRepository.saveAndFlush(center);

        // Get all the centerList where title contains DEFAULT_TITLE
        defaultCenterShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the centerList where title contains UPDATED_TITLE
        defaultCenterShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllCentersByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        centerRepository.saveAndFlush(center);

        // Get all the centerList where title does not contain DEFAULT_TITLE
        defaultCenterShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the centerList where title does not contain UPDATED_TITLE
        defaultCenterShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }


    @Test
    @Transactional
    public void getAllCentersByInfoIsEqualToSomething() throws Exception {
        // Initialize the database
        centerRepository.saveAndFlush(center);

        // Get all the centerList where info equals to DEFAULT_INFO
        defaultCenterShouldBeFound("info.equals=" + DEFAULT_INFO);

        // Get all the centerList where info equals to UPDATED_INFO
        defaultCenterShouldNotBeFound("info.equals=" + UPDATED_INFO);
    }

    @Test
    @Transactional
    public void getAllCentersByInfoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        centerRepository.saveAndFlush(center);

        // Get all the centerList where info not equals to DEFAULT_INFO
        defaultCenterShouldNotBeFound("info.notEquals=" + DEFAULT_INFO);

        // Get all the centerList where info not equals to UPDATED_INFO
        defaultCenterShouldBeFound("info.notEquals=" + UPDATED_INFO);
    }

    @Test
    @Transactional
    public void getAllCentersByInfoIsInShouldWork() throws Exception {
        // Initialize the database
        centerRepository.saveAndFlush(center);

        // Get all the centerList where info in DEFAULT_INFO or UPDATED_INFO
        defaultCenterShouldBeFound("info.in=" + DEFAULT_INFO + "," + UPDATED_INFO);

        // Get all the centerList where info equals to UPDATED_INFO
        defaultCenterShouldNotBeFound("info.in=" + UPDATED_INFO);
    }

    @Test
    @Transactional
    public void getAllCentersByInfoIsNullOrNotNull() throws Exception {
        // Initialize the database
        centerRepository.saveAndFlush(center);

        // Get all the centerList where info is not null
        defaultCenterShouldBeFound("info.specified=true");

        // Get all the centerList where info is null
        defaultCenterShouldNotBeFound("info.specified=false");
    }
                @Test
    @Transactional
    public void getAllCentersByInfoContainsSomething() throws Exception {
        // Initialize the database
        centerRepository.saveAndFlush(center);

        // Get all the centerList where info contains DEFAULT_INFO
        defaultCenterShouldBeFound("info.contains=" + DEFAULT_INFO);

        // Get all the centerList where info contains UPDATED_INFO
        defaultCenterShouldNotBeFound("info.contains=" + UPDATED_INFO);
    }

    @Test
    @Transactional
    public void getAllCentersByInfoNotContainsSomething() throws Exception {
        // Initialize the database
        centerRepository.saveAndFlush(center);

        // Get all the centerList where info does not contain DEFAULT_INFO
        defaultCenterShouldNotBeFound("info.doesNotContain=" + DEFAULT_INFO);

        // Get all the centerList where info does not contain UPDATED_INFO
        defaultCenterShouldBeFound("info.doesNotContain=" + UPDATED_INFO);
    }


    @Test
    @Transactional
    public void getAllCentersByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        centerRepository.saveAndFlush(center);

        // Get all the centerList where startDate equals to DEFAULT_START_DATE
        defaultCenterShouldBeFound("startDate.equals=" + DEFAULT_START_DATE);

        // Get all the centerList where startDate equals to UPDATED_START_DATE
        defaultCenterShouldNotBeFound("startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllCentersByStartDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        centerRepository.saveAndFlush(center);

        // Get all the centerList where startDate not equals to DEFAULT_START_DATE
        defaultCenterShouldNotBeFound("startDate.notEquals=" + DEFAULT_START_DATE);

        // Get all the centerList where startDate not equals to UPDATED_START_DATE
        defaultCenterShouldBeFound("startDate.notEquals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllCentersByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        centerRepository.saveAndFlush(center);

        // Get all the centerList where startDate in DEFAULT_START_DATE or UPDATED_START_DATE
        defaultCenterShouldBeFound("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE);

        // Get all the centerList where startDate equals to UPDATED_START_DATE
        defaultCenterShouldNotBeFound("startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllCentersByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        centerRepository.saveAndFlush(center);

        // Get all the centerList where startDate is not null
        defaultCenterShouldBeFound("startDate.specified=true");

        // Get all the centerList where startDate is null
        defaultCenterShouldNotBeFound("startDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllCentersByStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        centerRepository.saveAndFlush(center);

        // Get all the centerList where startDate is greater than or equal to DEFAULT_START_DATE
        defaultCenterShouldBeFound("startDate.greaterThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the centerList where startDate is greater than or equal to UPDATED_START_DATE
        defaultCenterShouldNotBeFound("startDate.greaterThanOrEqual=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllCentersByStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        centerRepository.saveAndFlush(center);

        // Get all the centerList where startDate is less than or equal to DEFAULT_START_DATE
        defaultCenterShouldBeFound("startDate.lessThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the centerList where startDate is less than or equal to SMALLER_START_DATE
        defaultCenterShouldNotBeFound("startDate.lessThanOrEqual=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    public void getAllCentersByStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        centerRepository.saveAndFlush(center);

        // Get all the centerList where startDate is less than DEFAULT_START_DATE
        defaultCenterShouldNotBeFound("startDate.lessThan=" + DEFAULT_START_DATE);

        // Get all the centerList where startDate is less than UPDATED_START_DATE
        defaultCenterShouldBeFound("startDate.lessThan=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllCentersByStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        centerRepository.saveAndFlush(center);

        // Get all the centerList where startDate is greater than DEFAULT_START_DATE
        defaultCenterShouldNotBeFound("startDate.greaterThan=" + DEFAULT_START_DATE);

        // Get all the centerList where startDate is greater than SMALLER_START_DATE
        defaultCenterShouldBeFound("startDate.greaterThan=" + SMALLER_START_DATE);
    }


    @Test
    @Transactional
    public void getAllCentersByGoogleMapUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        centerRepository.saveAndFlush(center);

        // Get all the centerList where googleMapUrl equals to DEFAULT_GOOGLE_MAP_URL
        defaultCenterShouldBeFound("googleMapUrl.equals=" + DEFAULT_GOOGLE_MAP_URL);

        // Get all the centerList where googleMapUrl equals to UPDATED_GOOGLE_MAP_URL
        defaultCenterShouldNotBeFound("googleMapUrl.equals=" + UPDATED_GOOGLE_MAP_URL);
    }

    @Test
    @Transactional
    public void getAllCentersByGoogleMapUrlIsNotEqualToSomething() throws Exception {
        // Initialize the database
        centerRepository.saveAndFlush(center);

        // Get all the centerList where googleMapUrl not equals to DEFAULT_GOOGLE_MAP_URL
        defaultCenterShouldNotBeFound("googleMapUrl.notEquals=" + DEFAULT_GOOGLE_MAP_URL);

        // Get all the centerList where googleMapUrl not equals to UPDATED_GOOGLE_MAP_URL
        defaultCenterShouldBeFound("googleMapUrl.notEquals=" + UPDATED_GOOGLE_MAP_URL);
    }

    @Test
    @Transactional
    public void getAllCentersByGoogleMapUrlIsInShouldWork() throws Exception {
        // Initialize the database
        centerRepository.saveAndFlush(center);

        // Get all the centerList where googleMapUrl in DEFAULT_GOOGLE_MAP_URL or UPDATED_GOOGLE_MAP_URL
        defaultCenterShouldBeFound("googleMapUrl.in=" + DEFAULT_GOOGLE_MAP_URL + "," + UPDATED_GOOGLE_MAP_URL);

        // Get all the centerList where googleMapUrl equals to UPDATED_GOOGLE_MAP_URL
        defaultCenterShouldNotBeFound("googleMapUrl.in=" + UPDATED_GOOGLE_MAP_URL);
    }

    @Test
    @Transactional
    public void getAllCentersByGoogleMapUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        centerRepository.saveAndFlush(center);

        // Get all the centerList where googleMapUrl is not null
        defaultCenterShouldBeFound("googleMapUrl.specified=true");

        // Get all the centerList where googleMapUrl is null
        defaultCenterShouldNotBeFound("googleMapUrl.specified=false");
    }
                @Test
    @Transactional
    public void getAllCentersByGoogleMapUrlContainsSomething() throws Exception {
        // Initialize the database
        centerRepository.saveAndFlush(center);

        // Get all the centerList where googleMapUrl contains DEFAULT_GOOGLE_MAP_URL
        defaultCenterShouldBeFound("googleMapUrl.contains=" + DEFAULT_GOOGLE_MAP_URL);

        // Get all the centerList where googleMapUrl contains UPDATED_GOOGLE_MAP_URL
        defaultCenterShouldNotBeFound("googleMapUrl.contains=" + UPDATED_GOOGLE_MAP_URL);
    }

    @Test
    @Transactional
    public void getAllCentersByGoogleMapUrlNotContainsSomething() throws Exception {
        // Initialize the database
        centerRepository.saveAndFlush(center);

        // Get all the centerList where googleMapUrl does not contain DEFAULT_GOOGLE_MAP_URL
        defaultCenterShouldNotBeFound("googleMapUrl.doesNotContain=" + DEFAULT_GOOGLE_MAP_URL);

        // Get all the centerList where googleMapUrl does not contain UPDATED_GOOGLE_MAP_URL
        defaultCenterShouldBeFound("googleMapUrl.doesNotContain=" + UPDATED_GOOGLE_MAP_URL);
    }


    @Test
    @Transactional
    public void getAllCentersByModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        centerRepository.saveAndFlush(center);
        User modifiedBy = UserResourceIT.createEntity(em);
        em.persist(modifiedBy);
        em.flush();
        center.setModifiedBy(modifiedBy);
        centerRepository.saveAndFlush(center);
        Long modifiedById = modifiedBy.getId();

        // Get all the centerList where modifiedBy equals to modifiedById
        defaultCenterShouldBeFound("modifiedById.equals=" + modifiedById);

        // Get all the centerList where modifiedBy equals to modifiedById + 1
        defaultCenterShouldNotBeFound("modifiedById.equals=" + (modifiedById + 1));
    }


    @Test
    @Transactional
    public void getAllCentersByRegionsIsEqualToSomething() throws Exception {
        // Initialize the database
        centerRepository.saveAndFlush(center);
        Regions regions = RegionsResourceIT.createEntity(em);
        em.persist(regions);
        em.flush();
        center.setRegions(regions);
        centerRepository.saveAndFlush(center);
        Long regionsId = regions.getId();

        // Get all the centerList where regions equals to regionsId
        defaultCenterShouldBeFound("regionsId.equals=" + regionsId);

        // Get all the centerList where regions equals to regionsId + 1
        defaultCenterShouldNotBeFound("regionsId.equals=" + (regionsId + 1));
    }


    @Test
    @Transactional
    public void getAllCentersByManagerIsEqualToSomething() throws Exception {
        // Initialize the database
        centerRepository.saveAndFlush(center);
        User manager = UserResourceIT.createEntity(em);
        em.persist(manager);
        em.flush();
        center.setManager(manager);
        centerRepository.saveAndFlush(center);
        Long managerId = manager.getId();

        // Get all the centerList where manager equals to managerId
        defaultCenterShouldBeFound("managerId.equals=" + managerId);

        // Get all the centerList where manager equals to managerId + 1
        defaultCenterShouldNotBeFound("managerId.equals=" + (managerId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCenterShouldBeFound(String filter) throws Exception {
        restCenterMockMvc.perform(get("/api/centers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(center.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].info").value(hasItem(DEFAULT_INFO)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].googleMapUrl").value(hasItem(DEFAULT_GOOGLE_MAP_URL)));

        // Check, that the count call also returns 1
        restCenterMockMvc.perform(get("/api/centers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCenterShouldNotBeFound(String filter) throws Exception {
        restCenterMockMvc.perform(get("/api/centers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCenterMockMvc.perform(get("/api/centers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingCenter() throws Exception {
        // Get the center
        restCenterMockMvc.perform(get("/api/centers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCenter() throws Exception {
        // Initialize the database
        centerRepository.saveAndFlush(center);

        int databaseSizeBeforeUpdate = centerRepository.findAll().size();

        // Update the center
        Center updatedCenter = centerRepository.findById(center.getId()).get();
        // Disconnect from session so that the updates on updatedCenter are not directly saved in db
        em.detach(updatedCenter);
        updatedCenter
            .title(UPDATED_TITLE)
            .info(UPDATED_INFO)
            .startDate(UPDATED_START_DATE)
            .googleMapUrl(UPDATED_GOOGLE_MAP_URL);
        CenterDTO centerDTO = centerMapper.toDto(updatedCenter);

        restCenterMockMvc.perform(put("/api/centers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(centerDTO)))
            .andExpect(status().isOk());

        // Validate the Center in the database
        List<Center> centerList = centerRepository.findAll();
        assertThat(centerList).hasSize(databaseSizeBeforeUpdate);
        Center testCenter = centerList.get(centerList.size() - 1);
        assertThat(testCenter.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testCenter.getInfo()).isEqualTo(UPDATED_INFO);
        assertThat(testCenter.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testCenter.getGoogleMapUrl()).isEqualTo(UPDATED_GOOGLE_MAP_URL);
    }

    @Test
    @Transactional
    public void updateNonExistingCenter() throws Exception {
        int databaseSizeBeforeUpdate = centerRepository.findAll().size();

        // Create the Center
        CenterDTO centerDTO = centerMapper.toDto(center);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCenterMockMvc.perform(put("/api/centers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(centerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Center in the database
        List<Center> centerList = centerRepository.findAll();
        assertThat(centerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCenter() throws Exception {
        // Initialize the database
        centerRepository.saveAndFlush(center);

        int databaseSizeBeforeDelete = centerRepository.findAll().size();

        // Delete the center
        restCenterMockMvc.perform(delete("/api/centers/{id}", center.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Center> centerList = centerRepository.findAll();
        assertThat(centerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
