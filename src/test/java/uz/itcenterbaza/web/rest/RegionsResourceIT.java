package uz.itcenterbaza.web.rest;

import uz.itcenterbaza.ItcenterbazaApp;
import uz.itcenterbaza.domain.Regions;
import uz.itcenterbaza.domain.User;
import uz.itcenterbaza.repository.RegionsRepository;
import uz.itcenterbaza.service.RegionsService;
import uz.itcenterbaza.service.dto.RegionsDTO;
import uz.itcenterbaza.service.mapper.RegionsMapper;
import uz.itcenterbaza.service.dto.RegionsCriteria;
import uz.itcenterbaza.service.RegionsQueryService;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link RegionsResource} REST controller.
 */
@SpringBootTest(classes = ItcenterbazaApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class RegionsResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_INFO = "AAAAAAAAAA";
    private static final String UPDATED_INFO = "BBBBBBBBBB";

    private static final String DEFAULT_GOOGLE_URL = "AAAAAAAAAA";
    private static final String UPDATED_GOOGLE_URL = "BBBBBBBBBB";

    @Autowired
    private RegionsRepository regionsRepository;

    @Autowired
    private RegionsMapper regionsMapper;

    @Autowired
    private RegionsService regionsService;

    @Autowired
    private RegionsQueryService regionsQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRegionsMockMvc;

    private Regions regions;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Regions createEntity(EntityManager em) {
        Regions regions = new Regions()
            .title(DEFAULT_TITLE)
            .info(DEFAULT_INFO)
            .googleUrl(DEFAULT_GOOGLE_URL);
        return regions;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Regions createUpdatedEntity(EntityManager em) {
        Regions regions = new Regions()
            .title(UPDATED_TITLE)
            .info(UPDATED_INFO)
            .googleUrl(UPDATED_GOOGLE_URL);
        return regions;
    }

    @BeforeEach
    public void initTest() {
        regions = createEntity(em);
    }

    @Test
    @Transactional
    public void createRegions() throws Exception {
        int databaseSizeBeforeCreate = regionsRepository.findAll().size();
        // Create the Regions
        RegionsDTO regionsDTO = regionsMapper.toDto(regions);
        restRegionsMockMvc.perform(post("/api/regions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(regionsDTO)))
            .andExpect(status().isCreated());

        // Validate the Regions in the database
        List<Regions> regionsList = regionsRepository.findAll();
        assertThat(regionsList).hasSize(databaseSizeBeforeCreate + 1);
        Regions testRegions = regionsList.get(regionsList.size() - 1);
        assertThat(testRegions.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testRegions.getInfo()).isEqualTo(DEFAULT_INFO);
        assertThat(testRegions.getGoogleUrl()).isEqualTo(DEFAULT_GOOGLE_URL);
    }

    @Test
    @Transactional
    public void createRegionsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = regionsRepository.findAll().size();

        // Create the Regions with an existing ID
        regions.setId(1L);
        RegionsDTO regionsDTO = regionsMapper.toDto(regions);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRegionsMockMvc.perform(post("/api/regions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(regionsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Regions in the database
        List<Regions> regionsList = regionsRepository.findAll();
        assertThat(regionsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = regionsRepository.findAll().size();
        // set the field null
        regions.setTitle(null);

        // Create the Regions, which fails.
        RegionsDTO regionsDTO = regionsMapper.toDto(regions);


        restRegionsMockMvc.perform(post("/api/regions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(regionsDTO)))
            .andExpect(status().isBadRequest());

        List<Regions> regionsList = regionsRepository.findAll();
        assertThat(regionsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRegions() throws Exception {
        // Initialize the database
        regionsRepository.saveAndFlush(regions);

        // Get all the regionsList
        restRegionsMockMvc.perform(get("/api/regions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(regions.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].info").value(hasItem(DEFAULT_INFO)))
            .andExpect(jsonPath("$.[*].googleUrl").value(hasItem(DEFAULT_GOOGLE_URL)));
    }
    
    @Test
    @Transactional
    public void getRegions() throws Exception {
        // Initialize the database
        regionsRepository.saveAndFlush(regions);

        // Get the regions
        restRegionsMockMvc.perform(get("/api/regions/{id}", regions.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(regions.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.info").value(DEFAULT_INFO))
            .andExpect(jsonPath("$.googleUrl").value(DEFAULT_GOOGLE_URL));
    }


    @Test
    @Transactional
    public void getRegionsByIdFiltering() throws Exception {
        // Initialize the database
        regionsRepository.saveAndFlush(regions);

        Long id = regions.getId();

        defaultRegionsShouldBeFound("id.equals=" + id);
        defaultRegionsShouldNotBeFound("id.notEquals=" + id);

        defaultRegionsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRegionsShouldNotBeFound("id.greaterThan=" + id);

        defaultRegionsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRegionsShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllRegionsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        regionsRepository.saveAndFlush(regions);

        // Get all the regionsList where title equals to DEFAULT_TITLE
        defaultRegionsShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the regionsList where title equals to UPDATED_TITLE
        defaultRegionsShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllRegionsByTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        regionsRepository.saveAndFlush(regions);

        // Get all the regionsList where title not equals to DEFAULT_TITLE
        defaultRegionsShouldNotBeFound("title.notEquals=" + DEFAULT_TITLE);

        // Get all the regionsList where title not equals to UPDATED_TITLE
        defaultRegionsShouldBeFound("title.notEquals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllRegionsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        regionsRepository.saveAndFlush(regions);

        // Get all the regionsList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultRegionsShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the regionsList where title equals to UPDATED_TITLE
        defaultRegionsShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllRegionsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        regionsRepository.saveAndFlush(regions);

        // Get all the regionsList where title is not null
        defaultRegionsShouldBeFound("title.specified=true");

        // Get all the regionsList where title is null
        defaultRegionsShouldNotBeFound("title.specified=false");
    }
                @Test
    @Transactional
    public void getAllRegionsByTitleContainsSomething() throws Exception {
        // Initialize the database
        regionsRepository.saveAndFlush(regions);

        // Get all the regionsList where title contains DEFAULT_TITLE
        defaultRegionsShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the regionsList where title contains UPDATED_TITLE
        defaultRegionsShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllRegionsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        regionsRepository.saveAndFlush(regions);

        // Get all the regionsList where title does not contain DEFAULT_TITLE
        defaultRegionsShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the regionsList where title does not contain UPDATED_TITLE
        defaultRegionsShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }


    @Test
    @Transactional
    public void getAllRegionsByInfoIsEqualToSomething() throws Exception {
        // Initialize the database
        regionsRepository.saveAndFlush(regions);

        // Get all the regionsList where info equals to DEFAULT_INFO
        defaultRegionsShouldBeFound("info.equals=" + DEFAULT_INFO);

        // Get all the regionsList where info equals to UPDATED_INFO
        defaultRegionsShouldNotBeFound("info.equals=" + UPDATED_INFO);
    }

    @Test
    @Transactional
    public void getAllRegionsByInfoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        regionsRepository.saveAndFlush(regions);

        // Get all the regionsList where info not equals to DEFAULT_INFO
        defaultRegionsShouldNotBeFound("info.notEquals=" + DEFAULT_INFO);

        // Get all the regionsList where info not equals to UPDATED_INFO
        defaultRegionsShouldBeFound("info.notEquals=" + UPDATED_INFO);
    }

    @Test
    @Transactional
    public void getAllRegionsByInfoIsInShouldWork() throws Exception {
        // Initialize the database
        regionsRepository.saveAndFlush(regions);

        // Get all the regionsList where info in DEFAULT_INFO or UPDATED_INFO
        defaultRegionsShouldBeFound("info.in=" + DEFAULT_INFO + "," + UPDATED_INFO);

        // Get all the regionsList where info equals to UPDATED_INFO
        defaultRegionsShouldNotBeFound("info.in=" + UPDATED_INFO);
    }

    @Test
    @Transactional
    public void getAllRegionsByInfoIsNullOrNotNull() throws Exception {
        // Initialize the database
        regionsRepository.saveAndFlush(regions);

        // Get all the regionsList where info is not null
        defaultRegionsShouldBeFound("info.specified=true");

        // Get all the regionsList where info is null
        defaultRegionsShouldNotBeFound("info.specified=false");
    }
                @Test
    @Transactional
    public void getAllRegionsByInfoContainsSomething() throws Exception {
        // Initialize the database
        regionsRepository.saveAndFlush(regions);

        // Get all the regionsList where info contains DEFAULT_INFO
        defaultRegionsShouldBeFound("info.contains=" + DEFAULT_INFO);

        // Get all the regionsList where info contains UPDATED_INFO
        defaultRegionsShouldNotBeFound("info.contains=" + UPDATED_INFO);
    }

    @Test
    @Transactional
    public void getAllRegionsByInfoNotContainsSomething() throws Exception {
        // Initialize the database
        regionsRepository.saveAndFlush(regions);

        // Get all the regionsList where info does not contain DEFAULT_INFO
        defaultRegionsShouldNotBeFound("info.doesNotContain=" + DEFAULT_INFO);

        // Get all the regionsList where info does not contain UPDATED_INFO
        defaultRegionsShouldBeFound("info.doesNotContain=" + UPDATED_INFO);
    }


    @Test
    @Transactional
    public void getAllRegionsByGoogleUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        regionsRepository.saveAndFlush(regions);

        // Get all the regionsList where googleUrl equals to DEFAULT_GOOGLE_URL
        defaultRegionsShouldBeFound("googleUrl.equals=" + DEFAULT_GOOGLE_URL);

        // Get all the regionsList where googleUrl equals to UPDATED_GOOGLE_URL
        defaultRegionsShouldNotBeFound("googleUrl.equals=" + UPDATED_GOOGLE_URL);
    }

    @Test
    @Transactional
    public void getAllRegionsByGoogleUrlIsNotEqualToSomething() throws Exception {
        // Initialize the database
        regionsRepository.saveAndFlush(regions);

        // Get all the regionsList where googleUrl not equals to DEFAULT_GOOGLE_URL
        defaultRegionsShouldNotBeFound("googleUrl.notEquals=" + DEFAULT_GOOGLE_URL);

        // Get all the regionsList where googleUrl not equals to UPDATED_GOOGLE_URL
        defaultRegionsShouldBeFound("googleUrl.notEquals=" + UPDATED_GOOGLE_URL);
    }

    @Test
    @Transactional
    public void getAllRegionsByGoogleUrlIsInShouldWork() throws Exception {
        // Initialize the database
        regionsRepository.saveAndFlush(regions);

        // Get all the regionsList where googleUrl in DEFAULT_GOOGLE_URL or UPDATED_GOOGLE_URL
        defaultRegionsShouldBeFound("googleUrl.in=" + DEFAULT_GOOGLE_URL + "," + UPDATED_GOOGLE_URL);

        // Get all the regionsList where googleUrl equals to UPDATED_GOOGLE_URL
        defaultRegionsShouldNotBeFound("googleUrl.in=" + UPDATED_GOOGLE_URL);
    }

    @Test
    @Transactional
    public void getAllRegionsByGoogleUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        regionsRepository.saveAndFlush(regions);

        // Get all the regionsList where googleUrl is not null
        defaultRegionsShouldBeFound("googleUrl.specified=true");

        // Get all the regionsList where googleUrl is null
        defaultRegionsShouldNotBeFound("googleUrl.specified=false");
    }
                @Test
    @Transactional
    public void getAllRegionsByGoogleUrlContainsSomething() throws Exception {
        // Initialize the database
        regionsRepository.saveAndFlush(regions);

        // Get all the regionsList where googleUrl contains DEFAULT_GOOGLE_URL
        defaultRegionsShouldBeFound("googleUrl.contains=" + DEFAULT_GOOGLE_URL);

        // Get all the regionsList where googleUrl contains UPDATED_GOOGLE_URL
        defaultRegionsShouldNotBeFound("googleUrl.contains=" + UPDATED_GOOGLE_URL);
    }

    @Test
    @Transactional
    public void getAllRegionsByGoogleUrlNotContainsSomething() throws Exception {
        // Initialize the database
        regionsRepository.saveAndFlush(regions);

        // Get all the regionsList where googleUrl does not contain DEFAULT_GOOGLE_URL
        defaultRegionsShouldNotBeFound("googleUrl.doesNotContain=" + DEFAULT_GOOGLE_URL);

        // Get all the regionsList where googleUrl does not contain UPDATED_GOOGLE_URL
        defaultRegionsShouldBeFound("googleUrl.doesNotContain=" + UPDATED_GOOGLE_URL);
    }


    @Test
    @Transactional
    public void getAllRegionsByDirectorIsEqualToSomething() throws Exception {
        // Initialize the database
        regionsRepository.saveAndFlush(regions);
        User director = UserResourceIT.createEntity(em);
        em.persist(director);
        em.flush();
        regions.setDirector(director);
        regionsRepository.saveAndFlush(regions);
        Long directorId = director.getId();

        // Get all the regionsList where director equals to directorId
        defaultRegionsShouldBeFound("directorId.equals=" + directorId);

        // Get all the regionsList where director equals to directorId + 1
        defaultRegionsShouldNotBeFound("directorId.equals=" + (directorId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRegionsShouldBeFound(String filter) throws Exception {
        restRegionsMockMvc.perform(get("/api/regions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(regions.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].info").value(hasItem(DEFAULT_INFO)))
            .andExpect(jsonPath("$.[*].googleUrl").value(hasItem(DEFAULT_GOOGLE_URL)));

        // Check, that the count call also returns 1
        restRegionsMockMvc.perform(get("/api/regions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRegionsShouldNotBeFound(String filter) throws Exception {
        restRegionsMockMvc.perform(get("/api/regions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRegionsMockMvc.perform(get("/api/regions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingRegions() throws Exception {
        // Get the regions
        restRegionsMockMvc.perform(get("/api/regions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRegions() throws Exception {
        // Initialize the database
        regionsRepository.saveAndFlush(regions);

        int databaseSizeBeforeUpdate = regionsRepository.findAll().size();

        // Update the regions
        Regions updatedRegions = regionsRepository.findById(regions.getId()).get();
        // Disconnect from session so that the updates on updatedRegions are not directly saved in db
        em.detach(updatedRegions);
        updatedRegions
            .title(UPDATED_TITLE)
            .info(UPDATED_INFO)
            .googleUrl(UPDATED_GOOGLE_URL);
        RegionsDTO regionsDTO = regionsMapper.toDto(updatedRegions);

        restRegionsMockMvc.perform(put("/api/regions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(regionsDTO)))
            .andExpect(status().isOk());

        // Validate the Regions in the database
        List<Regions> regionsList = regionsRepository.findAll();
        assertThat(regionsList).hasSize(databaseSizeBeforeUpdate);
        Regions testRegions = regionsList.get(regionsList.size() - 1);
        assertThat(testRegions.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testRegions.getInfo()).isEqualTo(UPDATED_INFO);
        assertThat(testRegions.getGoogleUrl()).isEqualTo(UPDATED_GOOGLE_URL);
    }

    @Test
    @Transactional
    public void updateNonExistingRegions() throws Exception {
        int databaseSizeBeforeUpdate = regionsRepository.findAll().size();

        // Create the Regions
        RegionsDTO regionsDTO = regionsMapper.toDto(regions);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRegionsMockMvc.perform(put("/api/regions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(regionsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Regions in the database
        List<Regions> regionsList = regionsRepository.findAll();
        assertThat(regionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRegions() throws Exception {
        // Initialize the database
        regionsRepository.saveAndFlush(regions);

        int databaseSizeBeforeDelete = regionsRepository.findAll().size();

        // Delete the regions
        restRegionsMockMvc.perform(delete("/api/regions/{id}", regions.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Regions> regionsList = regionsRepository.findAll();
        assertThat(regionsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
