package uz.itcenterbaza.web.rest;

import uz.itcenterbaza.ItcenterbazaApp;
import uz.itcenterbaza.domain.Payment;
import uz.itcenterbaza.domain.User;
import uz.itcenterbaza.domain.Participant;
import uz.itcenterbaza.domain.PaymentMethod;
import uz.itcenterbaza.repository.PaymentRepository;
import uz.itcenterbaza.service.PaymentService;
import uz.itcenterbaza.service.dto.PaymentDTO;
import uz.itcenterbaza.service.mapper.PaymentMapper;
import uz.itcenterbaza.service.dto.PaymentCriteria;
import uz.itcenterbaza.service.PaymentQueryService;

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
import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static uz.itcenterbaza.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import uz.itcenterbaza.domain.enumeration.PaymentStatus;
/**
 * Integration tests for the {@link PaymentResource} REST controller.
 */
@SpringBootTest(classes = ItcenterbazaApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class PaymentResourceIT {

    private static final ZonedDateTime DEFAULT_PAYMENT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_PAYMENT_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_PAYMENT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String DEFAULT_PAYMENT_PROVIDER = "AAAAAAAAAA";
    private static final String UPDATED_PAYMENT_PROVIDER = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_AMOUNT = new BigDecimal(1 - 1);

    private static final PaymentStatus DEFAULT_PAYMENT_STATUS = PaymentStatus.PENDING;
    private static final PaymentStatus UPDATED_PAYMENT_STATUS = PaymentStatus.PAID;

    private static final String DEFAULT_CURENCY = "AAAAAAAAAA";
    private static final String UPDATED_CURENCY = "BBBBBBBBBB";

    private static final String DEFAULT_CUSTOMER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ENOUGH = false;
    private static final Boolean UPDATED_IS_ENOUGH = true;

    private static final Boolean DEFAULT_IS_CONFIRMED = false;
    private static final Boolean UPDATED_IS_CONFIRMED = true;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentMapper paymentMapper;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PaymentQueryService paymentQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaymentMockMvc;

    private Payment payment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Payment createEntity(EntityManager em) {
        Payment payment = new Payment()
            .paymentDate(DEFAULT_PAYMENT_DATE)
            .paymentProvider(DEFAULT_PAYMENT_PROVIDER)
            .amount(DEFAULT_AMOUNT)
            .paymentStatus(DEFAULT_PAYMENT_STATUS)
            .curency(DEFAULT_CURENCY)
            .customerName(DEFAULT_CUSTOMER_NAME)
            .isEnough(DEFAULT_IS_ENOUGH)
            .isConfirmed(DEFAULT_IS_CONFIRMED);
        return payment;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Payment createUpdatedEntity(EntityManager em) {
        Payment payment = new Payment()
            .paymentDate(UPDATED_PAYMENT_DATE)
            .paymentProvider(UPDATED_PAYMENT_PROVIDER)
            .amount(UPDATED_AMOUNT)
            .paymentStatus(UPDATED_PAYMENT_STATUS)
            .curency(UPDATED_CURENCY)
            .customerName(UPDATED_CUSTOMER_NAME)
            .isEnough(UPDATED_IS_ENOUGH)
            .isConfirmed(UPDATED_IS_CONFIRMED);
        return payment;
    }

    @BeforeEach
    public void initTest() {
        payment = createEntity(em);
    }

    @Test
    @Transactional
    public void createPayment() throws Exception {
        int databaseSizeBeforeCreate = paymentRepository.findAll().size();
        // Create the Payment
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);
        restPaymentMockMvc.perform(post("/api/payments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(paymentDTO)))
            .andExpect(status().isCreated());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeCreate + 1);
        Payment testPayment = paymentList.get(paymentList.size() - 1);
        assertThat(testPayment.getPaymentDate()).isEqualTo(DEFAULT_PAYMENT_DATE);
        assertThat(testPayment.getPaymentProvider()).isEqualTo(DEFAULT_PAYMENT_PROVIDER);
        assertThat(testPayment.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testPayment.getPaymentStatus()).isEqualTo(DEFAULT_PAYMENT_STATUS);
        assertThat(testPayment.getCurency()).isEqualTo(DEFAULT_CURENCY);
        assertThat(testPayment.getCustomerName()).isEqualTo(DEFAULT_CUSTOMER_NAME);
        assertThat(testPayment.isIsEnough()).isEqualTo(DEFAULT_IS_ENOUGH);
        assertThat(testPayment.isIsConfirmed()).isEqualTo(DEFAULT_IS_CONFIRMED);
    }

    @Test
    @Transactional
    public void createPaymentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = paymentRepository.findAll().size();

        // Create the Payment with an existing ID
        payment.setId(1L);
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaymentMockMvc.perform(post("/api/payments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(paymentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllPayments() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList
        restPaymentMockMvc.perform(get("/api/payments?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(payment.getId().intValue())))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(sameInstant(DEFAULT_PAYMENT_DATE))))
            .andExpect(jsonPath("$.[*].paymentProvider").value(hasItem(DEFAULT_PAYMENT_PROVIDER)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].paymentStatus").value(hasItem(DEFAULT_PAYMENT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].curency").value(hasItem(DEFAULT_CURENCY)))
            .andExpect(jsonPath("$.[*].customerName").value(hasItem(DEFAULT_CUSTOMER_NAME)))
            .andExpect(jsonPath("$.[*].isEnough").value(hasItem(DEFAULT_IS_ENOUGH.booleanValue())))
            .andExpect(jsonPath("$.[*].isConfirmed").value(hasItem(DEFAULT_IS_CONFIRMED.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getPayment() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get the payment
        restPaymentMockMvc.perform(get("/api/payments/{id}", payment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(payment.getId().intValue()))
            .andExpect(jsonPath("$.paymentDate").value(sameInstant(DEFAULT_PAYMENT_DATE)))
            .andExpect(jsonPath("$.paymentProvider").value(DEFAULT_PAYMENT_PROVIDER))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.intValue()))
            .andExpect(jsonPath("$.paymentStatus").value(DEFAULT_PAYMENT_STATUS.toString()))
            .andExpect(jsonPath("$.curency").value(DEFAULT_CURENCY))
            .andExpect(jsonPath("$.customerName").value(DEFAULT_CUSTOMER_NAME))
            .andExpect(jsonPath("$.isEnough").value(DEFAULT_IS_ENOUGH.booleanValue()))
            .andExpect(jsonPath("$.isConfirmed").value(DEFAULT_IS_CONFIRMED.booleanValue()));
    }


    @Test
    @Transactional
    public void getPaymentsByIdFiltering() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        Long id = payment.getId();

        defaultPaymentShouldBeFound("id.equals=" + id);
        defaultPaymentShouldNotBeFound("id.notEquals=" + id);

        defaultPaymentShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPaymentShouldNotBeFound("id.greaterThan=" + id);

        defaultPaymentShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPaymentShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllPaymentsByPaymentDateIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentDate equals to DEFAULT_PAYMENT_DATE
        defaultPaymentShouldBeFound("paymentDate.equals=" + DEFAULT_PAYMENT_DATE);

        // Get all the paymentList where paymentDate equals to UPDATED_PAYMENT_DATE
        defaultPaymentShouldNotBeFound("paymentDate.equals=" + UPDATED_PAYMENT_DATE);
    }

    @Test
    @Transactional
    public void getAllPaymentsByPaymentDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentDate not equals to DEFAULT_PAYMENT_DATE
        defaultPaymentShouldNotBeFound("paymentDate.notEquals=" + DEFAULT_PAYMENT_DATE);

        // Get all the paymentList where paymentDate not equals to UPDATED_PAYMENT_DATE
        defaultPaymentShouldBeFound("paymentDate.notEquals=" + UPDATED_PAYMENT_DATE);
    }

    @Test
    @Transactional
    public void getAllPaymentsByPaymentDateIsInShouldWork() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentDate in DEFAULT_PAYMENT_DATE or UPDATED_PAYMENT_DATE
        defaultPaymentShouldBeFound("paymentDate.in=" + DEFAULT_PAYMENT_DATE + "," + UPDATED_PAYMENT_DATE);

        // Get all the paymentList where paymentDate equals to UPDATED_PAYMENT_DATE
        defaultPaymentShouldNotBeFound("paymentDate.in=" + UPDATED_PAYMENT_DATE);
    }

    @Test
    @Transactional
    public void getAllPaymentsByPaymentDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentDate is not null
        defaultPaymentShouldBeFound("paymentDate.specified=true");

        // Get all the paymentList where paymentDate is null
        defaultPaymentShouldNotBeFound("paymentDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllPaymentsByPaymentDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentDate is greater than or equal to DEFAULT_PAYMENT_DATE
        defaultPaymentShouldBeFound("paymentDate.greaterThanOrEqual=" + DEFAULT_PAYMENT_DATE);

        // Get all the paymentList where paymentDate is greater than or equal to UPDATED_PAYMENT_DATE
        defaultPaymentShouldNotBeFound("paymentDate.greaterThanOrEqual=" + UPDATED_PAYMENT_DATE);
    }

    @Test
    @Transactional
    public void getAllPaymentsByPaymentDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentDate is less than or equal to DEFAULT_PAYMENT_DATE
        defaultPaymentShouldBeFound("paymentDate.lessThanOrEqual=" + DEFAULT_PAYMENT_DATE);

        // Get all the paymentList where paymentDate is less than or equal to SMALLER_PAYMENT_DATE
        defaultPaymentShouldNotBeFound("paymentDate.lessThanOrEqual=" + SMALLER_PAYMENT_DATE);
    }

    @Test
    @Transactional
    public void getAllPaymentsByPaymentDateIsLessThanSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentDate is less than DEFAULT_PAYMENT_DATE
        defaultPaymentShouldNotBeFound("paymentDate.lessThan=" + DEFAULT_PAYMENT_DATE);

        // Get all the paymentList where paymentDate is less than UPDATED_PAYMENT_DATE
        defaultPaymentShouldBeFound("paymentDate.lessThan=" + UPDATED_PAYMENT_DATE);
    }

    @Test
    @Transactional
    public void getAllPaymentsByPaymentDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentDate is greater than DEFAULT_PAYMENT_DATE
        defaultPaymentShouldNotBeFound("paymentDate.greaterThan=" + DEFAULT_PAYMENT_DATE);

        // Get all the paymentList where paymentDate is greater than SMALLER_PAYMENT_DATE
        defaultPaymentShouldBeFound("paymentDate.greaterThan=" + SMALLER_PAYMENT_DATE);
    }


    @Test
    @Transactional
    public void getAllPaymentsByPaymentProviderIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentProvider equals to DEFAULT_PAYMENT_PROVIDER
        defaultPaymentShouldBeFound("paymentProvider.equals=" + DEFAULT_PAYMENT_PROVIDER);

        // Get all the paymentList where paymentProvider equals to UPDATED_PAYMENT_PROVIDER
        defaultPaymentShouldNotBeFound("paymentProvider.equals=" + UPDATED_PAYMENT_PROVIDER);
    }

    @Test
    @Transactional
    public void getAllPaymentsByPaymentProviderIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentProvider not equals to DEFAULT_PAYMENT_PROVIDER
        defaultPaymentShouldNotBeFound("paymentProvider.notEquals=" + DEFAULT_PAYMENT_PROVIDER);

        // Get all the paymentList where paymentProvider not equals to UPDATED_PAYMENT_PROVIDER
        defaultPaymentShouldBeFound("paymentProvider.notEquals=" + UPDATED_PAYMENT_PROVIDER);
    }

    @Test
    @Transactional
    public void getAllPaymentsByPaymentProviderIsInShouldWork() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentProvider in DEFAULT_PAYMENT_PROVIDER or UPDATED_PAYMENT_PROVIDER
        defaultPaymentShouldBeFound("paymentProvider.in=" + DEFAULT_PAYMENT_PROVIDER + "," + UPDATED_PAYMENT_PROVIDER);

        // Get all the paymentList where paymentProvider equals to UPDATED_PAYMENT_PROVIDER
        defaultPaymentShouldNotBeFound("paymentProvider.in=" + UPDATED_PAYMENT_PROVIDER);
    }

    @Test
    @Transactional
    public void getAllPaymentsByPaymentProviderIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentProvider is not null
        defaultPaymentShouldBeFound("paymentProvider.specified=true");

        // Get all the paymentList where paymentProvider is null
        defaultPaymentShouldNotBeFound("paymentProvider.specified=false");
    }
                @Test
    @Transactional
    public void getAllPaymentsByPaymentProviderContainsSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentProvider contains DEFAULT_PAYMENT_PROVIDER
        defaultPaymentShouldBeFound("paymentProvider.contains=" + DEFAULT_PAYMENT_PROVIDER);

        // Get all the paymentList where paymentProvider contains UPDATED_PAYMENT_PROVIDER
        defaultPaymentShouldNotBeFound("paymentProvider.contains=" + UPDATED_PAYMENT_PROVIDER);
    }

    @Test
    @Transactional
    public void getAllPaymentsByPaymentProviderNotContainsSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentProvider does not contain DEFAULT_PAYMENT_PROVIDER
        defaultPaymentShouldNotBeFound("paymentProvider.doesNotContain=" + DEFAULT_PAYMENT_PROVIDER);

        // Get all the paymentList where paymentProvider does not contain UPDATED_PAYMENT_PROVIDER
        defaultPaymentShouldBeFound("paymentProvider.doesNotContain=" + UPDATED_PAYMENT_PROVIDER);
    }


    @Test
    @Transactional
    public void getAllPaymentsByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where amount equals to DEFAULT_AMOUNT
        defaultPaymentShouldBeFound("amount.equals=" + DEFAULT_AMOUNT);

        // Get all the paymentList where amount equals to UPDATED_AMOUNT
        defaultPaymentShouldNotBeFound("amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllPaymentsByAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where amount not equals to DEFAULT_AMOUNT
        defaultPaymentShouldNotBeFound("amount.notEquals=" + DEFAULT_AMOUNT);

        // Get all the paymentList where amount not equals to UPDATED_AMOUNT
        defaultPaymentShouldBeFound("amount.notEquals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllPaymentsByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where amount in DEFAULT_AMOUNT or UPDATED_AMOUNT
        defaultPaymentShouldBeFound("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT);

        // Get all the paymentList where amount equals to UPDATED_AMOUNT
        defaultPaymentShouldNotBeFound("amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllPaymentsByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where amount is not null
        defaultPaymentShouldBeFound("amount.specified=true");

        // Get all the paymentList where amount is null
        defaultPaymentShouldNotBeFound("amount.specified=false");
    }

    @Test
    @Transactional
    public void getAllPaymentsByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where amount is greater than or equal to DEFAULT_AMOUNT
        defaultPaymentShouldBeFound("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT);

        // Get all the paymentList where amount is greater than or equal to UPDATED_AMOUNT
        defaultPaymentShouldNotBeFound("amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllPaymentsByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where amount is less than or equal to DEFAULT_AMOUNT
        defaultPaymentShouldBeFound("amount.lessThanOrEqual=" + DEFAULT_AMOUNT);

        // Get all the paymentList where amount is less than or equal to SMALLER_AMOUNT
        defaultPaymentShouldNotBeFound("amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllPaymentsByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where amount is less than DEFAULT_AMOUNT
        defaultPaymentShouldNotBeFound("amount.lessThan=" + DEFAULT_AMOUNT);

        // Get all the paymentList where amount is less than UPDATED_AMOUNT
        defaultPaymentShouldBeFound("amount.lessThan=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllPaymentsByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where amount is greater than DEFAULT_AMOUNT
        defaultPaymentShouldNotBeFound("amount.greaterThan=" + DEFAULT_AMOUNT);

        // Get all the paymentList where amount is greater than SMALLER_AMOUNT
        defaultPaymentShouldBeFound("amount.greaterThan=" + SMALLER_AMOUNT);
    }


    @Test
    @Transactional
    public void getAllPaymentsByPaymentStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentStatus equals to DEFAULT_PAYMENT_STATUS
        defaultPaymentShouldBeFound("paymentStatus.equals=" + DEFAULT_PAYMENT_STATUS);

        // Get all the paymentList where paymentStatus equals to UPDATED_PAYMENT_STATUS
        defaultPaymentShouldNotBeFound("paymentStatus.equals=" + UPDATED_PAYMENT_STATUS);
    }

    @Test
    @Transactional
    public void getAllPaymentsByPaymentStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentStatus not equals to DEFAULT_PAYMENT_STATUS
        defaultPaymentShouldNotBeFound("paymentStatus.notEquals=" + DEFAULT_PAYMENT_STATUS);

        // Get all the paymentList where paymentStatus not equals to UPDATED_PAYMENT_STATUS
        defaultPaymentShouldBeFound("paymentStatus.notEquals=" + UPDATED_PAYMENT_STATUS);
    }

    @Test
    @Transactional
    public void getAllPaymentsByPaymentStatusIsInShouldWork() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentStatus in DEFAULT_PAYMENT_STATUS or UPDATED_PAYMENT_STATUS
        defaultPaymentShouldBeFound("paymentStatus.in=" + DEFAULT_PAYMENT_STATUS + "," + UPDATED_PAYMENT_STATUS);

        // Get all the paymentList where paymentStatus equals to UPDATED_PAYMENT_STATUS
        defaultPaymentShouldNotBeFound("paymentStatus.in=" + UPDATED_PAYMENT_STATUS);
    }

    @Test
    @Transactional
    public void getAllPaymentsByPaymentStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentStatus is not null
        defaultPaymentShouldBeFound("paymentStatus.specified=true");

        // Get all the paymentList where paymentStatus is null
        defaultPaymentShouldNotBeFound("paymentStatus.specified=false");
    }

    @Test
    @Transactional
    public void getAllPaymentsByCurencyIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where curency equals to DEFAULT_CURENCY
        defaultPaymentShouldBeFound("curency.equals=" + DEFAULT_CURENCY);

        // Get all the paymentList where curency equals to UPDATED_CURENCY
        defaultPaymentShouldNotBeFound("curency.equals=" + UPDATED_CURENCY);
    }

    @Test
    @Transactional
    public void getAllPaymentsByCurencyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where curency not equals to DEFAULT_CURENCY
        defaultPaymentShouldNotBeFound("curency.notEquals=" + DEFAULT_CURENCY);

        // Get all the paymentList where curency not equals to UPDATED_CURENCY
        defaultPaymentShouldBeFound("curency.notEquals=" + UPDATED_CURENCY);
    }

    @Test
    @Transactional
    public void getAllPaymentsByCurencyIsInShouldWork() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where curency in DEFAULT_CURENCY or UPDATED_CURENCY
        defaultPaymentShouldBeFound("curency.in=" + DEFAULT_CURENCY + "," + UPDATED_CURENCY);

        // Get all the paymentList where curency equals to UPDATED_CURENCY
        defaultPaymentShouldNotBeFound("curency.in=" + UPDATED_CURENCY);
    }

    @Test
    @Transactional
    public void getAllPaymentsByCurencyIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where curency is not null
        defaultPaymentShouldBeFound("curency.specified=true");

        // Get all the paymentList where curency is null
        defaultPaymentShouldNotBeFound("curency.specified=false");
    }
                @Test
    @Transactional
    public void getAllPaymentsByCurencyContainsSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where curency contains DEFAULT_CURENCY
        defaultPaymentShouldBeFound("curency.contains=" + DEFAULT_CURENCY);

        // Get all the paymentList where curency contains UPDATED_CURENCY
        defaultPaymentShouldNotBeFound("curency.contains=" + UPDATED_CURENCY);
    }

    @Test
    @Transactional
    public void getAllPaymentsByCurencyNotContainsSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where curency does not contain DEFAULT_CURENCY
        defaultPaymentShouldNotBeFound("curency.doesNotContain=" + DEFAULT_CURENCY);

        // Get all the paymentList where curency does not contain UPDATED_CURENCY
        defaultPaymentShouldBeFound("curency.doesNotContain=" + UPDATED_CURENCY);
    }


    @Test
    @Transactional
    public void getAllPaymentsByCustomerNameIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where customerName equals to DEFAULT_CUSTOMER_NAME
        defaultPaymentShouldBeFound("customerName.equals=" + DEFAULT_CUSTOMER_NAME);

        // Get all the paymentList where customerName equals to UPDATED_CUSTOMER_NAME
        defaultPaymentShouldNotBeFound("customerName.equals=" + UPDATED_CUSTOMER_NAME);
    }

    @Test
    @Transactional
    public void getAllPaymentsByCustomerNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where customerName not equals to DEFAULT_CUSTOMER_NAME
        defaultPaymentShouldNotBeFound("customerName.notEquals=" + DEFAULT_CUSTOMER_NAME);

        // Get all the paymentList where customerName not equals to UPDATED_CUSTOMER_NAME
        defaultPaymentShouldBeFound("customerName.notEquals=" + UPDATED_CUSTOMER_NAME);
    }

    @Test
    @Transactional
    public void getAllPaymentsByCustomerNameIsInShouldWork() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where customerName in DEFAULT_CUSTOMER_NAME or UPDATED_CUSTOMER_NAME
        defaultPaymentShouldBeFound("customerName.in=" + DEFAULT_CUSTOMER_NAME + "," + UPDATED_CUSTOMER_NAME);

        // Get all the paymentList where customerName equals to UPDATED_CUSTOMER_NAME
        defaultPaymentShouldNotBeFound("customerName.in=" + UPDATED_CUSTOMER_NAME);
    }

    @Test
    @Transactional
    public void getAllPaymentsByCustomerNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where customerName is not null
        defaultPaymentShouldBeFound("customerName.specified=true");

        // Get all the paymentList where customerName is null
        defaultPaymentShouldNotBeFound("customerName.specified=false");
    }
                @Test
    @Transactional
    public void getAllPaymentsByCustomerNameContainsSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where customerName contains DEFAULT_CUSTOMER_NAME
        defaultPaymentShouldBeFound("customerName.contains=" + DEFAULT_CUSTOMER_NAME);

        // Get all the paymentList where customerName contains UPDATED_CUSTOMER_NAME
        defaultPaymentShouldNotBeFound("customerName.contains=" + UPDATED_CUSTOMER_NAME);
    }

    @Test
    @Transactional
    public void getAllPaymentsByCustomerNameNotContainsSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where customerName does not contain DEFAULT_CUSTOMER_NAME
        defaultPaymentShouldNotBeFound("customerName.doesNotContain=" + DEFAULT_CUSTOMER_NAME);

        // Get all the paymentList where customerName does not contain UPDATED_CUSTOMER_NAME
        defaultPaymentShouldBeFound("customerName.doesNotContain=" + UPDATED_CUSTOMER_NAME);
    }


    @Test
    @Transactional
    public void getAllPaymentsByIsEnoughIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where isEnough equals to DEFAULT_IS_ENOUGH
        defaultPaymentShouldBeFound("isEnough.equals=" + DEFAULT_IS_ENOUGH);

        // Get all the paymentList where isEnough equals to UPDATED_IS_ENOUGH
        defaultPaymentShouldNotBeFound("isEnough.equals=" + UPDATED_IS_ENOUGH);
    }

    @Test
    @Transactional
    public void getAllPaymentsByIsEnoughIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where isEnough not equals to DEFAULT_IS_ENOUGH
        defaultPaymentShouldNotBeFound("isEnough.notEquals=" + DEFAULT_IS_ENOUGH);

        // Get all the paymentList where isEnough not equals to UPDATED_IS_ENOUGH
        defaultPaymentShouldBeFound("isEnough.notEquals=" + UPDATED_IS_ENOUGH);
    }

    @Test
    @Transactional
    public void getAllPaymentsByIsEnoughIsInShouldWork() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where isEnough in DEFAULT_IS_ENOUGH or UPDATED_IS_ENOUGH
        defaultPaymentShouldBeFound("isEnough.in=" + DEFAULT_IS_ENOUGH + "," + UPDATED_IS_ENOUGH);

        // Get all the paymentList where isEnough equals to UPDATED_IS_ENOUGH
        defaultPaymentShouldNotBeFound("isEnough.in=" + UPDATED_IS_ENOUGH);
    }

    @Test
    @Transactional
    public void getAllPaymentsByIsEnoughIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where isEnough is not null
        defaultPaymentShouldBeFound("isEnough.specified=true");

        // Get all the paymentList where isEnough is null
        defaultPaymentShouldNotBeFound("isEnough.specified=false");
    }

    @Test
    @Transactional
    public void getAllPaymentsByIsConfirmedIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where isConfirmed equals to DEFAULT_IS_CONFIRMED
        defaultPaymentShouldBeFound("isConfirmed.equals=" + DEFAULT_IS_CONFIRMED);

        // Get all the paymentList where isConfirmed equals to UPDATED_IS_CONFIRMED
        defaultPaymentShouldNotBeFound("isConfirmed.equals=" + UPDATED_IS_CONFIRMED);
    }

    @Test
    @Transactional
    public void getAllPaymentsByIsConfirmedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where isConfirmed not equals to DEFAULT_IS_CONFIRMED
        defaultPaymentShouldNotBeFound("isConfirmed.notEquals=" + DEFAULT_IS_CONFIRMED);

        // Get all the paymentList where isConfirmed not equals to UPDATED_IS_CONFIRMED
        defaultPaymentShouldBeFound("isConfirmed.notEquals=" + UPDATED_IS_CONFIRMED);
    }

    @Test
    @Transactional
    public void getAllPaymentsByIsConfirmedIsInShouldWork() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where isConfirmed in DEFAULT_IS_CONFIRMED or UPDATED_IS_CONFIRMED
        defaultPaymentShouldBeFound("isConfirmed.in=" + DEFAULT_IS_CONFIRMED + "," + UPDATED_IS_CONFIRMED);

        // Get all the paymentList where isConfirmed equals to UPDATED_IS_CONFIRMED
        defaultPaymentShouldNotBeFound("isConfirmed.in=" + UPDATED_IS_CONFIRMED);
    }

    @Test
    @Transactional
    public void getAllPaymentsByIsConfirmedIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where isConfirmed is not null
        defaultPaymentShouldBeFound("isConfirmed.specified=true");

        // Get all the paymentList where isConfirmed is null
        defaultPaymentShouldNotBeFound("isConfirmed.specified=false");
    }

    @Test
    @Transactional
    public void getAllPaymentsByModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);
        User modifiedBy = UserResourceIT.createEntity(em);
        em.persist(modifiedBy);
        em.flush();
        payment.setModifiedBy(modifiedBy);
        paymentRepository.saveAndFlush(payment);
        Long modifiedById = modifiedBy.getId();

        // Get all the paymentList where modifiedBy equals to modifiedById
        defaultPaymentShouldBeFound("modifiedById.equals=" + modifiedById);

        // Get all the paymentList where modifiedBy equals to modifiedById + 1
        defaultPaymentShouldNotBeFound("modifiedById.equals=" + (modifiedById + 1));
    }


    @Test
    @Transactional
    public void getAllPaymentsByStudentIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);
        Participant student = ParticipantResourceIT.createEntity(em);
        em.persist(student);
        em.flush();
        payment.setStudent(student);
        paymentRepository.saveAndFlush(payment);
        Long studentId = student.getId();

        // Get all the paymentList where student equals to studentId
        defaultPaymentShouldBeFound("studentId.equals=" + studentId);

        // Get all the paymentList where student equals to studentId + 1
        defaultPaymentShouldNotBeFound("studentId.equals=" + (studentId + 1));
    }


    @Test
    @Transactional
    public void getAllPaymentsByMethodIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);
        PaymentMethod method = PaymentMethodResourceIT.createEntity(em);
        em.persist(method);
        em.flush();
        payment.setMethod(method);
        paymentRepository.saveAndFlush(payment);
        Long methodId = method.getId();

        // Get all the paymentList where method equals to methodId
        defaultPaymentShouldBeFound("methodId.equals=" + methodId);

        // Get all the paymentList where method equals to methodId + 1
        defaultPaymentShouldNotBeFound("methodId.equals=" + (methodId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPaymentShouldBeFound(String filter) throws Exception {
        restPaymentMockMvc.perform(get("/api/payments?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(payment.getId().intValue())))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(sameInstant(DEFAULT_PAYMENT_DATE))))
            .andExpect(jsonPath("$.[*].paymentProvider").value(hasItem(DEFAULT_PAYMENT_PROVIDER)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].paymentStatus").value(hasItem(DEFAULT_PAYMENT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].curency").value(hasItem(DEFAULT_CURENCY)))
            .andExpect(jsonPath("$.[*].customerName").value(hasItem(DEFAULT_CUSTOMER_NAME)))
            .andExpect(jsonPath("$.[*].isEnough").value(hasItem(DEFAULT_IS_ENOUGH.booleanValue())))
            .andExpect(jsonPath("$.[*].isConfirmed").value(hasItem(DEFAULT_IS_CONFIRMED.booleanValue())));

        // Check, that the count call also returns 1
        restPaymentMockMvc.perform(get("/api/payments/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPaymentShouldNotBeFound(String filter) throws Exception {
        restPaymentMockMvc.perform(get("/api/payments?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPaymentMockMvc.perform(get("/api/payments/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingPayment() throws Exception {
        // Get the payment
        restPaymentMockMvc.perform(get("/api/payments/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePayment() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        int databaseSizeBeforeUpdate = paymentRepository.findAll().size();

        // Update the payment
        Payment updatedPayment = paymentRepository.findById(payment.getId()).get();
        // Disconnect from session so that the updates on updatedPayment are not directly saved in db
        em.detach(updatedPayment);
        updatedPayment
            .paymentDate(UPDATED_PAYMENT_DATE)
            .paymentProvider(UPDATED_PAYMENT_PROVIDER)
            .amount(UPDATED_AMOUNT)
            .paymentStatus(UPDATED_PAYMENT_STATUS)
            .curency(UPDATED_CURENCY)
            .customerName(UPDATED_CUSTOMER_NAME)
            .isEnough(UPDATED_IS_ENOUGH)
            .isConfirmed(UPDATED_IS_CONFIRMED);
        PaymentDTO paymentDTO = paymentMapper.toDto(updatedPayment);

        restPaymentMockMvc.perform(put("/api/payments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(paymentDTO)))
            .andExpect(status().isOk());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
        Payment testPayment = paymentList.get(paymentList.size() - 1);
        assertThat(testPayment.getPaymentDate()).isEqualTo(UPDATED_PAYMENT_DATE);
        assertThat(testPayment.getPaymentProvider()).isEqualTo(UPDATED_PAYMENT_PROVIDER);
        assertThat(testPayment.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testPayment.getPaymentStatus()).isEqualTo(UPDATED_PAYMENT_STATUS);
        assertThat(testPayment.getCurency()).isEqualTo(UPDATED_CURENCY);
        assertThat(testPayment.getCustomerName()).isEqualTo(UPDATED_CUSTOMER_NAME);
        assertThat(testPayment.isIsEnough()).isEqualTo(UPDATED_IS_ENOUGH);
        assertThat(testPayment.isIsConfirmed()).isEqualTo(UPDATED_IS_CONFIRMED);
    }

    @Test
    @Transactional
    public void updateNonExistingPayment() throws Exception {
        int databaseSizeBeforeUpdate = paymentRepository.findAll().size();

        // Create the Payment
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentMockMvc.perform(put("/api/payments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(paymentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePayment() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        int databaseSizeBeforeDelete = paymentRepository.findAll().size();

        // Delete the payment
        restPaymentMockMvc.perform(delete("/api/payments/{id}", payment.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
