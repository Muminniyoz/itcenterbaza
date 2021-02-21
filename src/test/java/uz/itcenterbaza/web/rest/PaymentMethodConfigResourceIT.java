package uz.itcenterbaza.web.rest;

import uz.itcenterbaza.ItcenterbazaApp;
import uz.itcenterbaza.domain.PaymentMethodConfig;
import uz.itcenterbaza.domain.PaymentMethod;
import uz.itcenterbaza.repository.PaymentMethodConfigRepository;
import uz.itcenterbaza.service.PaymentMethodConfigService;
import uz.itcenterbaza.service.dto.PaymentMethodConfigDTO;
import uz.itcenterbaza.service.mapper.PaymentMethodConfigMapper;
import uz.itcenterbaza.service.dto.PaymentMethodConfigCriteria;
import uz.itcenterbaza.service.PaymentMethodConfigQueryService;

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
 * Integration tests for the {@link PaymentMethodConfigResource} REST controller.
 */
@SpringBootTest(classes = ItcenterbazaApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class PaymentMethodConfigResourceIT {

    private static final String DEFAULT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ENABLED = false;
    private static final Boolean UPDATED_ENABLED = true;

    @Autowired
    private PaymentMethodConfigRepository paymentMethodConfigRepository;

    @Autowired
    private PaymentMethodConfigMapper paymentMethodConfigMapper;

    @Autowired
    private PaymentMethodConfigService paymentMethodConfigService;

    @Autowired
    private PaymentMethodConfigQueryService paymentMethodConfigQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaymentMethodConfigMockMvc;

    private PaymentMethodConfig paymentMethodConfig;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentMethodConfig createEntity(EntityManager em) {
        PaymentMethodConfig paymentMethodConfig = new PaymentMethodConfig()
            .key(DEFAULT_KEY)
            .value(DEFAULT_VALUE)
            .note(DEFAULT_NOTE)
            .enabled(DEFAULT_ENABLED);
        return paymentMethodConfig;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentMethodConfig createUpdatedEntity(EntityManager em) {
        PaymentMethodConfig paymentMethodConfig = new PaymentMethodConfig()
            .key(UPDATED_KEY)
            .value(UPDATED_VALUE)
            .note(UPDATED_NOTE)
            .enabled(UPDATED_ENABLED);
        return paymentMethodConfig;
    }

    @BeforeEach
    public void initTest() {
        paymentMethodConfig = createEntity(em);
    }

    @Test
    @Transactional
    public void createPaymentMethodConfig() throws Exception {
        int databaseSizeBeforeCreate = paymentMethodConfigRepository.findAll().size();
        // Create the PaymentMethodConfig
        PaymentMethodConfigDTO paymentMethodConfigDTO = paymentMethodConfigMapper.toDto(paymentMethodConfig);
        restPaymentMethodConfigMockMvc.perform(post("/api/payment-method-configs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(paymentMethodConfigDTO)))
            .andExpect(status().isCreated());

        // Validate the PaymentMethodConfig in the database
        List<PaymentMethodConfig> paymentMethodConfigList = paymentMethodConfigRepository.findAll();
        assertThat(paymentMethodConfigList).hasSize(databaseSizeBeforeCreate + 1);
        PaymentMethodConfig testPaymentMethodConfig = paymentMethodConfigList.get(paymentMethodConfigList.size() - 1);
        assertThat(testPaymentMethodConfig.getKey()).isEqualTo(DEFAULT_KEY);
        assertThat(testPaymentMethodConfig.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testPaymentMethodConfig.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(testPaymentMethodConfig.isEnabled()).isEqualTo(DEFAULT_ENABLED);
    }

    @Test
    @Transactional
    public void createPaymentMethodConfigWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = paymentMethodConfigRepository.findAll().size();

        // Create the PaymentMethodConfig with an existing ID
        paymentMethodConfig.setId(1L);
        PaymentMethodConfigDTO paymentMethodConfigDTO = paymentMethodConfigMapper.toDto(paymentMethodConfig);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaymentMethodConfigMockMvc.perform(post("/api/payment-method-configs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(paymentMethodConfigDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PaymentMethodConfig in the database
        List<PaymentMethodConfig> paymentMethodConfigList = paymentMethodConfigRepository.findAll();
        assertThat(paymentMethodConfigList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllPaymentMethodConfigs() throws Exception {
        // Initialize the database
        paymentMethodConfigRepository.saveAndFlush(paymentMethodConfig);

        // Get all the paymentMethodConfigList
        restPaymentMethodConfigMockMvc.perform(get("/api/payment-method-configs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentMethodConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getPaymentMethodConfig() throws Exception {
        // Initialize the database
        paymentMethodConfigRepository.saveAndFlush(paymentMethodConfig);

        // Get the paymentMethodConfig
        restPaymentMethodConfigMockMvc.perform(get("/api/payment-method-configs/{id}", paymentMethodConfig.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(paymentMethodConfig.getId().intValue()))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE))
            .andExpect(jsonPath("$.enabled").value(DEFAULT_ENABLED.booleanValue()));
    }


    @Test
    @Transactional
    public void getPaymentMethodConfigsByIdFiltering() throws Exception {
        // Initialize the database
        paymentMethodConfigRepository.saveAndFlush(paymentMethodConfig);

        Long id = paymentMethodConfig.getId();

        defaultPaymentMethodConfigShouldBeFound("id.equals=" + id);
        defaultPaymentMethodConfigShouldNotBeFound("id.notEquals=" + id);

        defaultPaymentMethodConfigShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPaymentMethodConfigShouldNotBeFound("id.greaterThan=" + id);

        defaultPaymentMethodConfigShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPaymentMethodConfigShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllPaymentMethodConfigsByKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentMethodConfigRepository.saveAndFlush(paymentMethodConfig);

        // Get all the paymentMethodConfigList where key equals to DEFAULT_KEY
        defaultPaymentMethodConfigShouldBeFound("key.equals=" + DEFAULT_KEY);

        // Get all the paymentMethodConfigList where key equals to UPDATED_KEY
        defaultPaymentMethodConfigShouldNotBeFound("key.equals=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    public void getAllPaymentMethodConfigsByKeyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentMethodConfigRepository.saveAndFlush(paymentMethodConfig);

        // Get all the paymentMethodConfigList where key not equals to DEFAULT_KEY
        defaultPaymentMethodConfigShouldNotBeFound("key.notEquals=" + DEFAULT_KEY);

        // Get all the paymentMethodConfigList where key not equals to UPDATED_KEY
        defaultPaymentMethodConfigShouldBeFound("key.notEquals=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    public void getAllPaymentMethodConfigsByKeyIsInShouldWork() throws Exception {
        // Initialize the database
        paymentMethodConfigRepository.saveAndFlush(paymentMethodConfig);

        // Get all the paymentMethodConfigList where key in DEFAULT_KEY or UPDATED_KEY
        defaultPaymentMethodConfigShouldBeFound("key.in=" + DEFAULT_KEY + "," + UPDATED_KEY);

        // Get all the paymentMethodConfigList where key equals to UPDATED_KEY
        defaultPaymentMethodConfigShouldNotBeFound("key.in=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    public void getAllPaymentMethodConfigsByKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentMethodConfigRepository.saveAndFlush(paymentMethodConfig);

        // Get all the paymentMethodConfigList where key is not null
        defaultPaymentMethodConfigShouldBeFound("key.specified=true");

        // Get all the paymentMethodConfigList where key is null
        defaultPaymentMethodConfigShouldNotBeFound("key.specified=false");
    }
                @Test
    @Transactional
    public void getAllPaymentMethodConfigsByKeyContainsSomething() throws Exception {
        // Initialize the database
        paymentMethodConfigRepository.saveAndFlush(paymentMethodConfig);

        // Get all the paymentMethodConfigList where key contains DEFAULT_KEY
        defaultPaymentMethodConfigShouldBeFound("key.contains=" + DEFAULT_KEY);

        // Get all the paymentMethodConfigList where key contains UPDATED_KEY
        defaultPaymentMethodConfigShouldNotBeFound("key.contains=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    public void getAllPaymentMethodConfigsByKeyNotContainsSomething() throws Exception {
        // Initialize the database
        paymentMethodConfigRepository.saveAndFlush(paymentMethodConfig);

        // Get all the paymentMethodConfigList where key does not contain DEFAULT_KEY
        defaultPaymentMethodConfigShouldNotBeFound("key.doesNotContain=" + DEFAULT_KEY);

        // Get all the paymentMethodConfigList where key does not contain UPDATED_KEY
        defaultPaymentMethodConfigShouldBeFound("key.doesNotContain=" + UPDATED_KEY);
    }


    @Test
    @Transactional
    public void getAllPaymentMethodConfigsByValueIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentMethodConfigRepository.saveAndFlush(paymentMethodConfig);

        // Get all the paymentMethodConfigList where value equals to DEFAULT_VALUE
        defaultPaymentMethodConfigShouldBeFound("value.equals=" + DEFAULT_VALUE);

        // Get all the paymentMethodConfigList where value equals to UPDATED_VALUE
        defaultPaymentMethodConfigShouldNotBeFound("value.equals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void getAllPaymentMethodConfigsByValueIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentMethodConfigRepository.saveAndFlush(paymentMethodConfig);

        // Get all the paymentMethodConfigList where value not equals to DEFAULT_VALUE
        defaultPaymentMethodConfigShouldNotBeFound("value.notEquals=" + DEFAULT_VALUE);

        // Get all the paymentMethodConfigList where value not equals to UPDATED_VALUE
        defaultPaymentMethodConfigShouldBeFound("value.notEquals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void getAllPaymentMethodConfigsByValueIsInShouldWork() throws Exception {
        // Initialize the database
        paymentMethodConfigRepository.saveAndFlush(paymentMethodConfig);

        // Get all the paymentMethodConfigList where value in DEFAULT_VALUE or UPDATED_VALUE
        defaultPaymentMethodConfigShouldBeFound("value.in=" + DEFAULT_VALUE + "," + UPDATED_VALUE);

        // Get all the paymentMethodConfigList where value equals to UPDATED_VALUE
        defaultPaymentMethodConfigShouldNotBeFound("value.in=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void getAllPaymentMethodConfigsByValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentMethodConfigRepository.saveAndFlush(paymentMethodConfig);

        // Get all the paymentMethodConfigList where value is not null
        defaultPaymentMethodConfigShouldBeFound("value.specified=true");

        // Get all the paymentMethodConfigList where value is null
        defaultPaymentMethodConfigShouldNotBeFound("value.specified=false");
    }
                @Test
    @Transactional
    public void getAllPaymentMethodConfigsByValueContainsSomething() throws Exception {
        // Initialize the database
        paymentMethodConfigRepository.saveAndFlush(paymentMethodConfig);

        // Get all the paymentMethodConfigList where value contains DEFAULT_VALUE
        defaultPaymentMethodConfigShouldBeFound("value.contains=" + DEFAULT_VALUE);

        // Get all the paymentMethodConfigList where value contains UPDATED_VALUE
        defaultPaymentMethodConfigShouldNotBeFound("value.contains=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void getAllPaymentMethodConfigsByValueNotContainsSomething() throws Exception {
        // Initialize the database
        paymentMethodConfigRepository.saveAndFlush(paymentMethodConfig);

        // Get all the paymentMethodConfigList where value does not contain DEFAULT_VALUE
        defaultPaymentMethodConfigShouldNotBeFound("value.doesNotContain=" + DEFAULT_VALUE);

        // Get all the paymentMethodConfigList where value does not contain UPDATED_VALUE
        defaultPaymentMethodConfigShouldBeFound("value.doesNotContain=" + UPDATED_VALUE);
    }


    @Test
    @Transactional
    public void getAllPaymentMethodConfigsByNoteIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentMethodConfigRepository.saveAndFlush(paymentMethodConfig);

        // Get all the paymentMethodConfigList where note equals to DEFAULT_NOTE
        defaultPaymentMethodConfigShouldBeFound("note.equals=" + DEFAULT_NOTE);

        // Get all the paymentMethodConfigList where note equals to UPDATED_NOTE
        defaultPaymentMethodConfigShouldNotBeFound("note.equals=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void getAllPaymentMethodConfigsByNoteIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentMethodConfigRepository.saveAndFlush(paymentMethodConfig);

        // Get all the paymentMethodConfigList where note not equals to DEFAULT_NOTE
        defaultPaymentMethodConfigShouldNotBeFound("note.notEquals=" + DEFAULT_NOTE);

        // Get all the paymentMethodConfigList where note not equals to UPDATED_NOTE
        defaultPaymentMethodConfigShouldBeFound("note.notEquals=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void getAllPaymentMethodConfigsByNoteIsInShouldWork() throws Exception {
        // Initialize the database
        paymentMethodConfigRepository.saveAndFlush(paymentMethodConfig);

        // Get all the paymentMethodConfigList where note in DEFAULT_NOTE or UPDATED_NOTE
        defaultPaymentMethodConfigShouldBeFound("note.in=" + DEFAULT_NOTE + "," + UPDATED_NOTE);

        // Get all the paymentMethodConfigList where note equals to UPDATED_NOTE
        defaultPaymentMethodConfigShouldNotBeFound("note.in=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void getAllPaymentMethodConfigsByNoteIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentMethodConfigRepository.saveAndFlush(paymentMethodConfig);

        // Get all the paymentMethodConfigList where note is not null
        defaultPaymentMethodConfigShouldBeFound("note.specified=true");

        // Get all the paymentMethodConfigList where note is null
        defaultPaymentMethodConfigShouldNotBeFound("note.specified=false");
    }
                @Test
    @Transactional
    public void getAllPaymentMethodConfigsByNoteContainsSomething() throws Exception {
        // Initialize the database
        paymentMethodConfigRepository.saveAndFlush(paymentMethodConfig);

        // Get all the paymentMethodConfigList where note contains DEFAULT_NOTE
        defaultPaymentMethodConfigShouldBeFound("note.contains=" + DEFAULT_NOTE);

        // Get all the paymentMethodConfigList where note contains UPDATED_NOTE
        defaultPaymentMethodConfigShouldNotBeFound("note.contains=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void getAllPaymentMethodConfigsByNoteNotContainsSomething() throws Exception {
        // Initialize the database
        paymentMethodConfigRepository.saveAndFlush(paymentMethodConfig);

        // Get all the paymentMethodConfigList where note does not contain DEFAULT_NOTE
        defaultPaymentMethodConfigShouldNotBeFound("note.doesNotContain=" + DEFAULT_NOTE);

        // Get all the paymentMethodConfigList where note does not contain UPDATED_NOTE
        defaultPaymentMethodConfigShouldBeFound("note.doesNotContain=" + UPDATED_NOTE);
    }


    @Test
    @Transactional
    public void getAllPaymentMethodConfigsByEnabledIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentMethodConfigRepository.saveAndFlush(paymentMethodConfig);

        // Get all the paymentMethodConfigList where enabled equals to DEFAULT_ENABLED
        defaultPaymentMethodConfigShouldBeFound("enabled.equals=" + DEFAULT_ENABLED);

        // Get all the paymentMethodConfigList where enabled equals to UPDATED_ENABLED
        defaultPaymentMethodConfigShouldNotBeFound("enabled.equals=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    public void getAllPaymentMethodConfigsByEnabledIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentMethodConfigRepository.saveAndFlush(paymentMethodConfig);

        // Get all the paymentMethodConfigList where enabled not equals to DEFAULT_ENABLED
        defaultPaymentMethodConfigShouldNotBeFound("enabled.notEquals=" + DEFAULT_ENABLED);

        // Get all the paymentMethodConfigList where enabled not equals to UPDATED_ENABLED
        defaultPaymentMethodConfigShouldBeFound("enabled.notEquals=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    public void getAllPaymentMethodConfigsByEnabledIsInShouldWork() throws Exception {
        // Initialize the database
        paymentMethodConfigRepository.saveAndFlush(paymentMethodConfig);

        // Get all the paymentMethodConfigList where enabled in DEFAULT_ENABLED or UPDATED_ENABLED
        defaultPaymentMethodConfigShouldBeFound("enabled.in=" + DEFAULT_ENABLED + "," + UPDATED_ENABLED);

        // Get all the paymentMethodConfigList where enabled equals to UPDATED_ENABLED
        defaultPaymentMethodConfigShouldNotBeFound("enabled.in=" + UPDATED_ENABLED);
    }

    @Test
    @Transactional
    public void getAllPaymentMethodConfigsByEnabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentMethodConfigRepository.saveAndFlush(paymentMethodConfig);

        // Get all the paymentMethodConfigList where enabled is not null
        defaultPaymentMethodConfigShouldBeFound("enabled.specified=true");

        // Get all the paymentMethodConfigList where enabled is null
        defaultPaymentMethodConfigShouldNotBeFound("enabled.specified=false");
    }

    @Test
    @Transactional
    public void getAllPaymentMethodConfigsByMethodIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentMethodConfigRepository.saveAndFlush(paymentMethodConfig);
        PaymentMethod method = PaymentMethodResourceIT.createEntity(em);
        em.persist(method);
        em.flush();
        paymentMethodConfig.setMethod(method);
        paymentMethodConfigRepository.saveAndFlush(paymentMethodConfig);
        Long methodId = method.getId();

        // Get all the paymentMethodConfigList where method equals to methodId
        defaultPaymentMethodConfigShouldBeFound("methodId.equals=" + methodId);

        // Get all the paymentMethodConfigList where method equals to methodId + 1
        defaultPaymentMethodConfigShouldNotBeFound("methodId.equals=" + (methodId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPaymentMethodConfigShouldBeFound(String filter) throws Exception {
        restPaymentMethodConfigMockMvc.perform(get("/api/payment-method-configs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentMethodConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())));

        // Check, that the count call also returns 1
        restPaymentMethodConfigMockMvc.perform(get("/api/payment-method-configs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPaymentMethodConfigShouldNotBeFound(String filter) throws Exception {
        restPaymentMethodConfigMockMvc.perform(get("/api/payment-method-configs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPaymentMethodConfigMockMvc.perform(get("/api/payment-method-configs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingPaymentMethodConfig() throws Exception {
        // Get the paymentMethodConfig
        restPaymentMethodConfigMockMvc.perform(get("/api/payment-method-configs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePaymentMethodConfig() throws Exception {
        // Initialize the database
        paymentMethodConfigRepository.saveAndFlush(paymentMethodConfig);

        int databaseSizeBeforeUpdate = paymentMethodConfigRepository.findAll().size();

        // Update the paymentMethodConfig
        PaymentMethodConfig updatedPaymentMethodConfig = paymentMethodConfigRepository.findById(paymentMethodConfig.getId()).get();
        // Disconnect from session so that the updates on updatedPaymentMethodConfig are not directly saved in db
        em.detach(updatedPaymentMethodConfig);
        updatedPaymentMethodConfig
            .key(UPDATED_KEY)
            .value(UPDATED_VALUE)
            .note(UPDATED_NOTE)
            .enabled(UPDATED_ENABLED);
        PaymentMethodConfigDTO paymentMethodConfigDTO = paymentMethodConfigMapper.toDto(updatedPaymentMethodConfig);

        restPaymentMethodConfigMockMvc.perform(put("/api/payment-method-configs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(paymentMethodConfigDTO)))
            .andExpect(status().isOk());

        // Validate the PaymentMethodConfig in the database
        List<PaymentMethodConfig> paymentMethodConfigList = paymentMethodConfigRepository.findAll();
        assertThat(paymentMethodConfigList).hasSize(databaseSizeBeforeUpdate);
        PaymentMethodConfig testPaymentMethodConfig = paymentMethodConfigList.get(paymentMethodConfigList.size() - 1);
        assertThat(testPaymentMethodConfig.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testPaymentMethodConfig.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testPaymentMethodConfig.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testPaymentMethodConfig.isEnabled()).isEqualTo(UPDATED_ENABLED);
    }

    @Test
    @Transactional
    public void updateNonExistingPaymentMethodConfig() throws Exception {
        int databaseSizeBeforeUpdate = paymentMethodConfigRepository.findAll().size();

        // Create the PaymentMethodConfig
        PaymentMethodConfigDTO paymentMethodConfigDTO = paymentMethodConfigMapper.toDto(paymentMethodConfig);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentMethodConfigMockMvc.perform(put("/api/payment-method-configs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(paymentMethodConfigDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PaymentMethodConfig in the database
        List<PaymentMethodConfig> paymentMethodConfigList = paymentMethodConfigRepository.findAll();
        assertThat(paymentMethodConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePaymentMethodConfig() throws Exception {
        // Initialize the database
        paymentMethodConfigRepository.saveAndFlush(paymentMethodConfig);

        int databaseSizeBeforeDelete = paymentMethodConfigRepository.findAll().size();

        // Delete the paymentMethodConfig
        restPaymentMethodConfigMockMvc.perform(delete("/api/payment-method-configs/{id}", paymentMethodConfig.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PaymentMethodConfig> paymentMethodConfigList = paymentMethodConfigRepository.findAll();
        assertThat(paymentMethodConfigList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
