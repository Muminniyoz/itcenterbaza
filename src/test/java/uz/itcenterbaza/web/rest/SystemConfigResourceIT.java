package uz.itcenterbaza.web.rest;

import uz.itcenterbaza.ItcenterbazaApp;
import uz.itcenterbaza.domain.SystemConfig;
import uz.itcenterbaza.repository.SystemConfigRepository;
import uz.itcenterbaza.service.SystemConfigService;
import uz.itcenterbaza.service.dto.SystemConfigDTO;
import uz.itcenterbaza.service.mapper.SystemConfigMapper;
import uz.itcenterbaza.service.dto.SystemConfigCriteria;
import uz.itcenterbaza.service.SystemConfigQueryService;

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
 * Integration tests for the {@link SystemConfigResource} REST controller.
 */
@SpringBootTest(classes = ItcenterbazaApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class SystemConfigResourceIT {

    private static final String DEFAULT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ENABLED = false;
    private static final Boolean UPDATED_ENABLED = true;

    @Autowired
    private SystemConfigRepository systemConfigRepository;

    @Autowired
    private SystemConfigMapper systemConfigMapper;

    @Autowired
    private SystemConfigService systemConfigService;

    @Autowired
    private SystemConfigQueryService systemConfigQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSystemConfigMockMvc;

    private SystemConfig systemConfig;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SystemConfig createEntity(EntityManager em) {
        SystemConfig systemConfig = new SystemConfig()
            .key(DEFAULT_KEY)
            .value(DEFAULT_VALUE)
            .note(DEFAULT_NOTE)
            .enabled(DEFAULT_ENABLED);
        return systemConfig;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SystemConfig createUpdatedEntity(EntityManager em) {
        SystemConfig systemConfig = new SystemConfig()
            .key(UPDATED_KEY)
            .value(UPDATED_VALUE)
            .note(UPDATED_NOTE)
            .enabled(UPDATED_ENABLED);
        return systemConfig;
    }

    @BeforeEach
    public void initTest() {
        systemConfig = createEntity(em);
    }

    @Test
    @Transactional
    public void createSystemConfig() throws Exception {
        int databaseSizeBeforeCreate = systemConfigRepository.findAll().size();
        // Create the SystemConfig
        SystemConfigDTO systemConfigDTO = systemConfigMapper.toDto(systemConfig);
        restSystemConfigMockMvc.perform(post("/api/system-configs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(systemConfigDTO)))
            .andExpect(status().isCreated());

        // Validate the SystemConfig in the database
        List<SystemConfig> systemConfigList = systemConfigRepository.findAll();
        assertThat(systemConfigList).hasSize(databaseSizeBeforeCreate + 1);
        SystemConfig testSystemConfig = systemConfigList.get(systemConfigList.size() - 1);
        assertThat(testSystemConfig.getKey()).isEqualTo(DEFAULT_KEY);
        assertThat(testSystemConfig.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testSystemConfig.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(testSystemConfig.isEnabled()).isEqualTo(DEFAULT_ENABLED);
    }

    @Test
    @Transactional
    public void createSystemConfigWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = systemConfigRepository.findAll().size();

        // Create the SystemConfig with an existing ID
        systemConfig.setId(1L);
        SystemConfigDTO systemConfigDTO = systemConfigMapper.toDto(systemConfig);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSystemConfigMockMvc.perform(post("/api/system-configs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(systemConfigDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SystemConfig in the database
        List<SystemConfig> systemConfigList = systemConfigRepository.findAll();
        assertThat(systemConfigList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllSystemConfigs() throws Exception {
        // Initialize the database
        systemConfigRepository.saveAndFlush(systemConfig);

        // Get all the systemConfigList
        restSystemConfigMockMvc.perform(get("/api/system-configs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(systemConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getSystemConfig() throws Exception {
        // Initialize the database
        systemConfigRepository.saveAndFlush(systemConfig);

        // Get the systemConfig
        restSystemConfigMockMvc.perform(get("/api/system-configs/{id}", systemConfig.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(systemConfig.getId().intValue()))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE))
            .andExpect(jsonPath("$.enabled").value(DEFAULT_ENABLED.booleanValue()));
    }


    @Test
    @Transactional
    public void getSystemConfigsByIdFiltering() throws Exception {
        // Initialize the database
        systemConfigRepository.saveAndFlush(systemConfig);

        Long id = systemConfig.getId();

        defaultSystemConfigShouldBeFound("id.equals=" + id);
        defaultSystemConfigShouldNotBeFound("id.notEquals=" + id);

        defaultSystemConfigShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSystemConfigShouldNotBeFound("id.greaterThan=" + id);

        defaultSystemConfigShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSystemConfigShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllSystemConfigsByKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        systemConfigRepository.saveAndFlush(systemConfig);

        // Get all the systemConfigList where key equals to DEFAULT_KEY
        defaultSystemConfigShouldBeFound("key.equals=" + DEFAULT_KEY);

        // Get all the systemConfigList where key equals to UPDATED_KEY
        defaultSystemConfigShouldNotBeFound("key.equals=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    public void getAllSystemConfigsByKeyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        systemConfigRepository.saveAndFlush(systemConfig);

        // Get all the systemConfigList where key not equals to DEFAULT_KEY
        defaultSystemConfigShouldNotBeFound("key.notEquals=" + DEFAULT_KEY);

        // Get all the systemConfigList where key not equals to UPDATED_KEY
        defaultSystemConfigShouldBeFound("key.notEquals=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    public void getAllSystemConfigsByKeyIsInShouldWork() throws Exception {
        // Initialize the database
        systemConfigRepository.saveAndFlush(systemConfig);

        // Get all the systemConfigList where key in DEFAULT_KEY or UPDATED_KEY
        defaultSystemConfigShouldBeFound("key.in=" + DEFAULT_KEY + "," + UPDATED_KEY);

        // Get all the systemConfigList where key equals to UPDATED_KEY
        defaultSystemConfigShouldNotBeFound("key.in=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    public void getAllSystemConfigsByKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        systemConfigRepository.saveAndFlush(systemConfig);

        // Get all the systemConfigList where key is not null
        defaultSystemConfigShouldBeFound("key.specified=true");

        // Get all the systemConfigList where key is null
        defaultSystemConfigShouldNotBeFound("key.specified=false");
    }
                @Test
    @Transactional
    public void getAllSystemConfigsByKeyContainsSomething() throws Exception {
        // Initialize the database
        systemConfigRepository.saveAndFlush(systemConfig);

        // Get all the systemConfigList where key contains DEFAULT_KEY
        defaultSystemConfigShouldBeFound("key.contains=" + DEFAULT_KEY);

        // Get all the systemConfigList where key contains UPDATED_KEY
        defaultSystemConfigShouldNotBeFound("key.contains=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    public void getAllSystemConfigsByKeyNotContainsSomething() throws Exception {
        // Initialize the database
        systemConfigRepository.saveAndFlush(systemConfig);

        // Get all the systemConfigList where key does not contain DEFAULT_KEY
        defaultSystemConfigShouldNotBeFound("key.doesNotContain=" + DEFAULT_KEY);

        // Get all the systemConfigList where key does not contain UPDATED_KEY
        defaultSystemConfigShouldBeFound("key.doesNotContain=" + UPDATED_KEY);
    }


    @Test
    @Transactional
    public void getAllSystemConfigsByValueIsEqualToSomething() throws Exception {
        // Initialize the database
        systemConfigRepository.saveAndFlush(systemConfig);

        // Get all the systemConfigList where value equals to DEFAULT_VALUE
        defaultSystemConfigShouldBeFound("value.equals=" + DEFAULT_VALUE);

        // Get all the systemConfigList where value equals to UPDATED_VALUE
        defaultSystemConfigShouldNotBeFound("value.equals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void getAllSystemConfigsByValueIsNotEqualToSomething() throws Exception {
        // Initialize the database
        systemConfigRepository.saveAndFlush(systemConfig);

        // Get all the systemConfigList where value not equals to DEFAULT_VALUE
        defaultSystemConfigShouldNotBeFound("value.notEquals=" + DEFAULT_VALUE);

        // Get all the systemConfigList where value not equals to UPDATED_VALUE
        defaultSystemConfigShouldBeFound("value.notEquals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void getAllSystemConfigsByValueIsInShouldWork() throws Exception {
        // Initialize the database
        systemConfigRepository.saveAndFlush(systemConfig);

        // Get all the systemConfigList where value in DEFAULT_VALUE or UPDATED_VALUE
        defaultSystemConfigShouldBeFound("value.in=" + DEFAULT_VALUE + "," + UPDATED_VALUE);

        // Get all the systemConfigList where value equals to UPDATED_VALUE
        defaultSystemConfigShouldNotBeFound("value.in=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void getAllSystemConfigsByValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        systemConfigRepository.saveAndFlush(systemConfig);

        // Get all the systemConfigList where value is not null
        defaultSystemConfigShouldBeFound("value.specified=true");

        // Get all the systemConfigList where value is null
        defaultSystemConfigShouldNotBeFound("value.specified=false");
    }
                @Test
    @Transactional
    public void getAllSystemConfigsByValueContainsSomething() throws Exception {
        // Initialize the database
        systemConfigRepository.saveAndFlush(systemConfig);

        // Get all the systemConfigList where value contains DEFAULT_VALUE
        defaultSystemConfigShouldBeFound("value.contains=" + DEFAULT_VALUE);

        // Get all the systemConfigList where value contains UPDATED_VALUE
        defaultSystemConfigShouldNotBeFound("value.contains=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void getAllSystemConfigsByValueNotContainsSomething() throws Exception {
        // Initialize the database
        systemConfigRepository.saveAndFlush(systemConfig);

        // Get all the systemConfigList where value does not contain DEFAULT_VALUE
        defaultSystemConfigShouldNotBeFound("value.doesNotContain=" + DEFAULT_VALUE);

        // Get all the systemConfigList where value does not contain UPDATED_VALUE
        defaultSystemConfigShouldBeFound("value.doesNotContain=" + UPDATED_VALUE);
    }


    @Test
    @Transactional
    public void getAllSystemConfigsByNoteIsEqualToSomething() throws Exception {
        // Initialize the database
        systemConfigRepository.saveAndFlush(systemConfig);

        // Get all the systemConfigList where note equals to DEFAULT_NOTE
        defaultSystemConfigShouldBeFound("note.equals=" + DEFAULT_NOTE);

        // Get all the systemConfigList where note equals to UPDATED_NOTE
        defaultSystemConfigShouldNotBeFound("note.equals=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void getAllSystemConfigsByNoteIsNotEqualToSomething() throws Exception {
        // Initialize the database
        systemConfigRepository.saveAndFlush(systemConfig);

        // Get all the systemConfigList where note not equals to DEFAULT_NOTE
        defaultSystemConfigShouldNotBeFound("note.notEquals=" + DEFAULT_NOTE);

        // Get all the systemConfigList where note not equals to UPDATED_NOTE
        defaultSystemConfigShouldBeFound("note.notEquals=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void getAllSystemConfigsByNoteIsInShouldWork() throws Exception {
        // Initialize the database
        systemConfigRepository.saveAndFlush(systemConfig);

        // Get all the systemConfigList where note in DEFAULT_NOTE or UPDATED_NOTE
        defaultSystemConfigShouldBeFound("note.in=" + DEFAULT_NOTE + "," + UPDATED_NOTE);

        // Get all the systemConfigList where note equals to UPDATED_NOTE
        defaultSystemConfigShouldNotBeFound("note.in=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void getAllSystemConfigsByNoteIsNullOrNotNull() throws Exception {
        // Initialize the database
        systemConfigRepository.saveAndFlush(systemConfig);

        // Get all the systemConfigList where note is not null
        defaultSystemConfigShouldBeFound("note.specified=true");

        // Get all the systemConfigList where note is null
        defaultSystemConfigShouldNotBeFound("note.specified=false");
    }
                @Test
    @Transactional
    public void getAllSystemConfigsByNoteContainsSomething() throws Exception {
        // Initialize the database
        systemConfigRepository.saveAndFlush(systemConfig);

        // Get all the systemConfigList where note contains DEFAULT_NOTE
        defaultSystemConfigShouldBeFound("note.contains=" + DEFAULT_NOTE);

        // Get all the systemConfigList where note contains UPDATED_NOTE
        defaultSystemConfigShouldNotBeFound("note.contains=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void getAllSystemConfigsByNoteNotContainsSomething() throws Exception {
        // Initialize the database
        systemConfigRepository.saveAndFlush(systemConfig);

        // Get all the systemConfigList where note does not contain DEFAULT_NOTE
        defaultSystemConfigShouldNotBeFound("note.doesNotContain=" + DEFAULT_NOTE);

        // Get all the systemConfigList where note does not contain UPDATED_NOTE
        defaultSystemConfigShouldBeFound("note.doesNotContain=" + UPDATED_NOTE);
    }


    @Test
    @Transactional
    public void getAllSystemConfigsByEnabledIsEqualToSomething() throws Exception {
        // Initialize the database
        systemConfigRepository.saveAndFlush(systemConfig);

        // Get all the systemConfigList where enabled equals to DEFAULT_ENABLED
        defaultSystemConfigShouldBeFound("enabled.equals=" + DEFAULT_ENABLED);

        // Get all the systemConfigList where enabled equals to UPDATED_ENABLED
        defaultSystemConfigShouldNotBeFound("enabled.equals=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    public void getAllSystemConfigsByEnabledIsNotEqualToSomething() throws Exception {
        // Initialize the database
        systemConfigRepository.saveAndFlush(systemConfig);

        // Get all the systemConfigList where enabled not equals to DEFAULT_ENABLED
        defaultSystemConfigShouldNotBeFound("enabled.notEquals=" + DEFAULT_ENABLED);

        // Get all the systemConfigList where enabled not equals to UPDATED_ENABLED
        defaultSystemConfigShouldBeFound("enabled.notEquals=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    public void getAllSystemConfigsByEnabledIsInShouldWork() throws Exception {
        // Initialize the database
        systemConfigRepository.saveAndFlush(systemConfig);

        // Get all the systemConfigList where enabled in DEFAULT_ENABLED or UPDATED_ENABLED
        defaultSystemConfigShouldBeFound("enabled.in=" + DEFAULT_ENABLED + "," + UPDATED_ENABLED);

        // Get all the systemConfigList where enabled equals to UPDATED_ENABLED
        defaultSystemConfigShouldNotBeFound("enabled.in=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    public void getAllSystemConfigsByEnabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        systemConfigRepository.saveAndFlush(systemConfig);

        // Get all the systemConfigList where enabled is not null
        defaultSystemConfigShouldBeFound("enabled.specified=true");

        // Get all the systemConfigList where enabled is null
        defaultSystemConfigShouldNotBeFound("enabled.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSystemConfigShouldBeFound(String filter) throws Exception {
        restSystemConfigMockMvc.perform(get("/api/system-configs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(systemConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())));

        // Check, that the count call also returns 1
        restSystemConfigMockMvc.perform(get("/api/system-configs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSystemConfigShouldNotBeFound(String filter) throws Exception {
        restSystemConfigMockMvc.perform(get("/api/system-configs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSystemConfigMockMvc.perform(get("/api/system-configs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingSystemConfig() throws Exception {
        // Get the systemConfig
        restSystemConfigMockMvc.perform(get("/api/system-configs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSystemConfig() throws Exception {
        // Initialize the database
        systemConfigRepository.saveAndFlush(systemConfig);

        int databaseSizeBeforeUpdate = systemConfigRepository.findAll().size();

        // Update the systemConfig
        SystemConfig updatedSystemConfig = systemConfigRepository.findById(systemConfig.getId()).get();
        // Disconnect from session so that the updates on updatedSystemConfig are not directly saved in db
        em.detach(updatedSystemConfig);
        updatedSystemConfig
            .key(UPDATED_KEY)
            .value(UPDATED_VALUE)
            .note(UPDATED_NOTE)
            .enabled(UPDATED_ENABLED);
        SystemConfigDTO systemConfigDTO = systemConfigMapper.toDto(updatedSystemConfig);

        restSystemConfigMockMvc.perform(put("/api/system-configs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(systemConfigDTO)))
            .andExpect(status().isOk());

        // Validate the SystemConfig in the database
        List<SystemConfig> systemConfigList = systemConfigRepository.findAll();
        assertThat(systemConfigList).hasSize(databaseSizeBeforeUpdate);
        SystemConfig testSystemConfig = systemConfigList.get(systemConfigList.size() - 1);
        assertThat(testSystemConfig.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testSystemConfig.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testSystemConfig.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testSystemConfig.isEnabled()).isEqualTo(UPDATED_ENABLED);
    }

    @Test
    @Transactional
    public void updateNonExistingSystemConfig() throws Exception {
        int databaseSizeBeforeUpdate = systemConfigRepository.findAll().size();

        // Create the SystemConfig
        SystemConfigDTO systemConfigDTO = systemConfigMapper.toDto(systemConfig);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSystemConfigMockMvc.perform(put("/api/system-configs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(systemConfigDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SystemConfig in the database
        List<SystemConfig> systemConfigList = systemConfigRepository.findAll();
        assertThat(systemConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSystemConfig() throws Exception {
        // Initialize the database
        systemConfigRepository.saveAndFlush(systemConfig);

        int databaseSizeBeforeDelete = systemConfigRepository.findAll().size();

        // Delete the systemConfig
        restSystemConfigMockMvc.perform(delete("/api/system-configs/{id}", systemConfig.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SystemConfig> systemConfigList = systemConfigRepository.findAll();
        assertThat(systemConfigList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
