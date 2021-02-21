package uz.itcenterbaza.web.rest;

import uz.itcenterbaza.ItcenterbazaApp;
import uz.itcenterbaza.domain.Skill;
import uz.itcenterbaza.domain.Teacher;
import uz.itcenterbaza.repository.SkillRepository;
import uz.itcenterbaza.service.SkillService;
import uz.itcenterbaza.service.dto.SkillDTO;
import uz.itcenterbaza.service.mapper.SkillMapper;
import uz.itcenterbaza.service.dto.SkillCriteria;
import uz.itcenterbaza.service.SkillQueryService;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link SkillResource} REST controller.
 */
@SpringBootTest(classes = ItcenterbazaApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class SkillResourceIT {

    private static final String DEFAULT_TITLE_UZ = "AAAAAAAAAA";
    private static final String UPDATED_TITLE_UZ = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE_RU = "AAAAAAAAAA";
    private static final String UPDATED_TITLE_RU = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE_EN = "AAAAAAAAAA";
    private static final String UPDATED_TITLE_EN = "BBBBBBBBBB";

    private static final String DEFAULT_ABOUT = "AAAAAAAAAA";
    private static final String UPDATED_ABOUT = "BBBBBBBBBB";

    private static final byte[] DEFAULT_PLAN_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PLAN_FILE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PLAN_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PLAN_FILE_CONTENT_TYPE = "image/png";

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private SkillMapper skillMapper;

    @Autowired
    private SkillService skillService;

    @Autowired
    private SkillQueryService skillQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSkillMockMvc;

    private Skill skill;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Skill createEntity(EntityManager em) {
        Skill skill = new Skill()
            .titleUz(DEFAULT_TITLE_UZ)
            .titleRu(DEFAULT_TITLE_RU)
            .titleEn(DEFAULT_TITLE_EN)
            .about(DEFAULT_ABOUT)
            .planFile(DEFAULT_PLAN_FILE)
            .planFileContentType(DEFAULT_PLAN_FILE_CONTENT_TYPE);
        return skill;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Skill createUpdatedEntity(EntityManager em) {
        Skill skill = new Skill()
            .titleUz(UPDATED_TITLE_UZ)
            .titleRu(UPDATED_TITLE_RU)
            .titleEn(UPDATED_TITLE_EN)
            .about(UPDATED_ABOUT)
            .planFile(UPDATED_PLAN_FILE)
            .planFileContentType(UPDATED_PLAN_FILE_CONTENT_TYPE);
        return skill;
    }

    @BeforeEach
    public void initTest() {
        skill = createEntity(em);
    }

    @Test
    @Transactional
    public void createSkill() throws Exception {
        int databaseSizeBeforeCreate = skillRepository.findAll().size();
        // Create the Skill
        SkillDTO skillDTO = skillMapper.toDto(skill);
        restSkillMockMvc.perform(post("/api/skills")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(skillDTO)))
            .andExpect(status().isCreated());

        // Validate the Skill in the database
        List<Skill> skillList = skillRepository.findAll();
        assertThat(skillList).hasSize(databaseSizeBeforeCreate + 1);
        Skill testSkill = skillList.get(skillList.size() - 1);
        assertThat(testSkill.getTitleUz()).isEqualTo(DEFAULT_TITLE_UZ);
        assertThat(testSkill.getTitleRu()).isEqualTo(DEFAULT_TITLE_RU);
        assertThat(testSkill.getTitleEn()).isEqualTo(DEFAULT_TITLE_EN);
        assertThat(testSkill.getAbout()).isEqualTo(DEFAULT_ABOUT);
        assertThat(testSkill.getPlanFile()).isEqualTo(DEFAULT_PLAN_FILE);
        assertThat(testSkill.getPlanFileContentType()).isEqualTo(DEFAULT_PLAN_FILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createSkillWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = skillRepository.findAll().size();

        // Create the Skill with an existing ID
        skill.setId(1L);
        SkillDTO skillDTO = skillMapper.toDto(skill);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSkillMockMvc.perform(post("/api/skills")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(skillDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Skill in the database
        List<Skill> skillList = skillRepository.findAll();
        assertThat(skillList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkTitleUzIsRequired() throws Exception {
        int databaseSizeBeforeTest = skillRepository.findAll().size();
        // set the field null
        skill.setTitleUz(null);

        // Create the Skill, which fails.
        SkillDTO skillDTO = skillMapper.toDto(skill);


        restSkillMockMvc.perform(post("/api/skills")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(skillDTO)))
            .andExpect(status().isBadRequest());

        List<Skill> skillList = skillRepository.findAll();
        assertThat(skillList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSkills() throws Exception {
        // Initialize the database
        skillRepository.saveAndFlush(skill);

        // Get all the skillList
        restSkillMockMvc.perform(get("/api/skills?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(skill.getId().intValue())))
            .andExpect(jsonPath("$.[*].titleUz").value(hasItem(DEFAULT_TITLE_UZ)))
            .andExpect(jsonPath("$.[*].titleRu").value(hasItem(DEFAULT_TITLE_RU)))
            .andExpect(jsonPath("$.[*].titleEn").value(hasItem(DEFAULT_TITLE_EN)))
            .andExpect(jsonPath("$.[*].about").value(hasItem(DEFAULT_ABOUT.toString())))
            .andExpect(jsonPath("$.[*].planFileContentType").value(hasItem(DEFAULT_PLAN_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].planFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_PLAN_FILE))));
    }
    
    @Test
    @Transactional
    public void getSkill() throws Exception {
        // Initialize the database
        skillRepository.saveAndFlush(skill);

        // Get the skill
        restSkillMockMvc.perform(get("/api/skills/{id}", skill.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(skill.getId().intValue()))
            .andExpect(jsonPath("$.titleUz").value(DEFAULT_TITLE_UZ))
            .andExpect(jsonPath("$.titleRu").value(DEFAULT_TITLE_RU))
            .andExpect(jsonPath("$.titleEn").value(DEFAULT_TITLE_EN))
            .andExpect(jsonPath("$.about").value(DEFAULT_ABOUT.toString()))
            .andExpect(jsonPath("$.planFileContentType").value(DEFAULT_PLAN_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.planFile").value(Base64Utils.encodeToString(DEFAULT_PLAN_FILE)));
    }


    @Test
    @Transactional
    public void getSkillsByIdFiltering() throws Exception {
        // Initialize the database
        skillRepository.saveAndFlush(skill);

        Long id = skill.getId();

        defaultSkillShouldBeFound("id.equals=" + id);
        defaultSkillShouldNotBeFound("id.notEquals=" + id);

        defaultSkillShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSkillShouldNotBeFound("id.greaterThan=" + id);

        defaultSkillShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSkillShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllSkillsByTitleUzIsEqualToSomething() throws Exception {
        // Initialize the database
        skillRepository.saveAndFlush(skill);

        // Get all the skillList where titleUz equals to DEFAULT_TITLE_UZ
        defaultSkillShouldBeFound("titleUz.equals=" + DEFAULT_TITLE_UZ);

        // Get all the skillList where titleUz equals to UPDATED_TITLE_UZ
        defaultSkillShouldNotBeFound("titleUz.equals=" + UPDATED_TITLE_UZ);
    }

    @Test
    @Transactional
    public void getAllSkillsByTitleUzIsNotEqualToSomething() throws Exception {
        // Initialize the database
        skillRepository.saveAndFlush(skill);

        // Get all the skillList where titleUz not equals to DEFAULT_TITLE_UZ
        defaultSkillShouldNotBeFound("titleUz.notEquals=" + DEFAULT_TITLE_UZ);

        // Get all the skillList where titleUz not equals to UPDATED_TITLE_UZ
        defaultSkillShouldBeFound("titleUz.notEquals=" + UPDATED_TITLE_UZ);
    }

    @Test
    @Transactional
    public void getAllSkillsByTitleUzIsInShouldWork() throws Exception {
        // Initialize the database
        skillRepository.saveAndFlush(skill);

        // Get all the skillList where titleUz in DEFAULT_TITLE_UZ or UPDATED_TITLE_UZ
        defaultSkillShouldBeFound("titleUz.in=" + DEFAULT_TITLE_UZ + "," + UPDATED_TITLE_UZ);

        // Get all the skillList where titleUz equals to UPDATED_TITLE_UZ
        defaultSkillShouldNotBeFound("titleUz.in=" + UPDATED_TITLE_UZ);
    }

    @Test
    @Transactional
    public void getAllSkillsByTitleUzIsNullOrNotNull() throws Exception {
        // Initialize the database
        skillRepository.saveAndFlush(skill);

        // Get all the skillList where titleUz is not null
        defaultSkillShouldBeFound("titleUz.specified=true");

        // Get all the skillList where titleUz is null
        defaultSkillShouldNotBeFound("titleUz.specified=false");
    }
                @Test
    @Transactional
    public void getAllSkillsByTitleUzContainsSomething() throws Exception {
        // Initialize the database
        skillRepository.saveAndFlush(skill);

        // Get all the skillList where titleUz contains DEFAULT_TITLE_UZ
        defaultSkillShouldBeFound("titleUz.contains=" + DEFAULT_TITLE_UZ);

        // Get all the skillList where titleUz contains UPDATED_TITLE_UZ
        defaultSkillShouldNotBeFound("titleUz.contains=" + UPDATED_TITLE_UZ);
    }

    @Test
    @Transactional
    public void getAllSkillsByTitleUzNotContainsSomething() throws Exception {
        // Initialize the database
        skillRepository.saveAndFlush(skill);

        // Get all the skillList where titleUz does not contain DEFAULT_TITLE_UZ
        defaultSkillShouldNotBeFound("titleUz.doesNotContain=" + DEFAULT_TITLE_UZ);

        // Get all the skillList where titleUz does not contain UPDATED_TITLE_UZ
        defaultSkillShouldBeFound("titleUz.doesNotContain=" + UPDATED_TITLE_UZ);
    }


    @Test
    @Transactional
    public void getAllSkillsByTitleRuIsEqualToSomething() throws Exception {
        // Initialize the database
        skillRepository.saveAndFlush(skill);

        // Get all the skillList where titleRu equals to DEFAULT_TITLE_RU
        defaultSkillShouldBeFound("titleRu.equals=" + DEFAULT_TITLE_RU);

        // Get all the skillList where titleRu equals to UPDATED_TITLE_RU
        defaultSkillShouldNotBeFound("titleRu.equals=" + UPDATED_TITLE_RU);
    }

    @Test
    @Transactional
    public void getAllSkillsByTitleRuIsNotEqualToSomething() throws Exception {
        // Initialize the database
        skillRepository.saveAndFlush(skill);

        // Get all the skillList where titleRu not equals to DEFAULT_TITLE_RU
        defaultSkillShouldNotBeFound("titleRu.notEquals=" + DEFAULT_TITLE_RU);

        // Get all the skillList where titleRu not equals to UPDATED_TITLE_RU
        defaultSkillShouldBeFound("titleRu.notEquals=" + UPDATED_TITLE_RU);
    }

    @Test
    @Transactional
    public void getAllSkillsByTitleRuIsInShouldWork() throws Exception {
        // Initialize the database
        skillRepository.saveAndFlush(skill);

        // Get all the skillList where titleRu in DEFAULT_TITLE_RU or UPDATED_TITLE_RU
        defaultSkillShouldBeFound("titleRu.in=" + DEFAULT_TITLE_RU + "," + UPDATED_TITLE_RU);

        // Get all the skillList where titleRu equals to UPDATED_TITLE_RU
        defaultSkillShouldNotBeFound("titleRu.in=" + UPDATED_TITLE_RU);
    }

    @Test
    @Transactional
    public void getAllSkillsByTitleRuIsNullOrNotNull() throws Exception {
        // Initialize the database
        skillRepository.saveAndFlush(skill);

        // Get all the skillList where titleRu is not null
        defaultSkillShouldBeFound("titleRu.specified=true");

        // Get all the skillList where titleRu is null
        defaultSkillShouldNotBeFound("titleRu.specified=false");
    }
                @Test
    @Transactional
    public void getAllSkillsByTitleRuContainsSomething() throws Exception {
        // Initialize the database
        skillRepository.saveAndFlush(skill);

        // Get all the skillList where titleRu contains DEFAULT_TITLE_RU
        defaultSkillShouldBeFound("titleRu.contains=" + DEFAULT_TITLE_RU);

        // Get all the skillList where titleRu contains UPDATED_TITLE_RU
        defaultSkillShouldNotBeFound("titleRu.contains=" + UPDATED_TITLE_RU);
    }

    @Test
    @Transactional
    public void getAllSkillsByTitleRuNotContainsSomething() throws Exception {
        // Initialize the database
        skillRepository.saveAndFlush(skill);

        // Get all the skillList where titleRu does not contain DEFAULT_TITLE_RU
        defaultSkillShouldNotBeFound("titleRu.doesNotContain=" + DEFAULT_TITLE_RU);

        // Get all the skillList where titleRu does not contain UPDATED_TITLE_RU
        defaultSkillShouldBeFound("titleRu.doesNotContain=" + UPDATED_TITLE_RU);
    }


    @Test
    @Transactional
    public void getAllSkillsByTitleEnIsEqualToSomething() throws Exception {
        // Initialize the database
        skillRepository.saveAndFlush(skill);

        // Get all the skillList where titleEn equals to DEFAULT_TITLE_EN
        defaultSkillShouldBeFound("titleEn.equals=" + DEFAULT_TITLE_EN);

        // Get all the skillList where titleEn equals to UPDATED_TITLE_EN
        defaultSkillShouldNotBeFound("titleEn.equals=" + UPDATED_TITLE_EN);
    }

    @Test
    @Transactional
    public void getAllSkillsByTitleEnIsNotEqualToSomething() throws Exception {
        // Initialize the database
        skillRepository.saveAndFlush(skill);

        // Get all the skillList where titleEn not equals to DEFAULT_TITLE_EN
        defaultSkillShouldNotBeFound("titleEn.notEquals=" + DEFAULT_TITLE_EN);

        // Get all the skillList where titleEn not equals to UPDATED_TITLE_EN
        defaultSkillShouldBeFound("titleEn.notEquals=" + UPDATED_TITLE_EN);
    }

    @Test
    @Transactional
    public void getAllSkillsByTitleEnIsInShouldWork() throws Exception {
        // Initialize the database
        skillRepository.saveAndFlush(skill);

        // Get all the skillList where titleEn in DEFAULT_TITLE_EN or UPDATED_TITLE_EN
        defaultSkillShouldBeFound("titleEn.in=" + DEFAULT_TITLE_EN + "," + UPDATED_TITLE_EN);

        // Get all the skillList where titleEn equals to UPDATED_TITLE_EN
        defaultSkillShouldNotBeFound("titleEn.in=" + UPDATED_TITLE_EN);
    }

    @Test
    @Transactional
    public void getAllSkillsByTitleEnIsNullOrNotNull() throws Exception {
        // Initialize the database
        skillRepository.saveAndFlush(skill);

        // Get all the skillList where titleEn is not null
        defaultSkillShouldBeFound("titleEn.specified=true");

        // Get all the skillList where titleEn is null
        defaultSkillShouldNotBeFound("titleEn.specified=false");
    }
                @Test
    @Transactional
    public void getAllSkillsByTitleEnContainsSomething() throws Exception {
        // Initialize the database
        skillRepository.saveAndFlush(skill);

        // Get all the skillList where titleEn contains DEFAULT_TITLE_EN
        defaultSkillShouldBeFound("titleEn.contains=" + DEFAULT_TITLE_EN);

        // Get all the skillList where titleEn contains UPDATED_TITLE_EN
        defaultSkillShouldNotBeFound("titleEn.contains=" + UPDATED_TITLE_EN);
    }

    @Test
    @Transactional
    public void getAllSkillsByTitleEnNotContainsSomething() throws Exception {
        // Initialize the database
        skillRepository.saveAndFlush(skill);

        // Get all the skillList where titleEn does not contain DEFAULT_TITLE_EN
        defaultSkillShouldNotBeFound("titleEn.doesNotContain=" + DEFAULT_TITLE_EN);

        // Get all the skillList where titleEn does not contain UPDATED_TITLE_EN
        defaultSkillShouldBeFound("titleEn.doesNotContain=" + UPDATED_TITLE_EN);
    }


    @Test
    @Transactional
    public void getAllSkillsByTeacherIsEqualToSomething() throws Exception {
        // Initialize the database
        skillRepository.saveAndFlush(skill);
        Teacher teacher = TeacherResourceIT.createEntity(em);
        em.persist(teacher);
        em.flush();
        skill.addTeacher(teacher);
        skillRepository.saveAndFlush(skill);
        Long teacherId = teacher.getId();

        // Get all the skillList where teacher equals to teacherId
        defaultSkillShouldBeFound("teacherId.equals=" + teacherId);

        // Get all the skillList where teacher equals to teacherId + 1
        defaultSkillShouldNotBeFound("teacherId.equals=" + (teacherId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSkillShouldBeFound(String filter) throws Exception {
        restSkillMockMvc.perform(get("/api/skills?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(skill.getId().intValue())))
            .andExpect(jsonPath("$.[*].titleUz").value(hasItem(DEFAULT_TITLE_UZ)))
            .andExpect(jsonPath("$.[*].titleRu").value(hasItem(DEFAULT_TITLE_RU)))
            .andExpect(jsonPath("$.[*].titleEn").value(hasItem(DEFAULT_TITLE_EN)))
            .andExpect(jsonPath("$.[*].about").value(hasItem(DEFAULT_ABOUT.toString())))
            .andExpect(jsonPath("$.[*].planFileContentType").value(hasItem(DEFAULT_PLAN_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].planFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_PLAN_FILE))));

        // Check, that the count call also returns 1
        restSkillMockMvc.perform(get("/api/skills/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSkillShouldNotBeFound(String filter) throws Exception {
        restSkillMockMvc.perform(get("/api/skills?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSkillMockMvc.perform(get("/api/skills/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingSkill() throws Exception {
        // Get the skill
        restSkillMockMvc.perform(get("/api/skills/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSkill() throws Exception {
        // Initialize the database
        skillRepository.saveAndFlush(skill);

        int databaseSizeBeforeUpdate = skillRepository.findAll().size();

        // Update the skill
        Skill updatedSkill = skillRepository.findById(skill.getId()).get();
        // Disconnect from session so that the updates on updatedSkill are not directly saved in db
        em.detach(updatedSkill);
        updatedSkill
            .titleUz(UPDATED_TITLE_UZ)
            .titleRu(UPDATED_TITLE_RU)
            .titleEn(UPDATED_TITLE_EN)
            .about(UPDATED_ABOUT)
            .planFile(UPDATED_PLAN_FILE)
            .planFileContentType(UPDATED_PLAN_FILE_CONTENT_TYPE);
        SkillDTO skillDTO = skillMapper.toDto(updatedSkill);

        restSkillMockMvc.perform(put("/api/skills")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(skillDTO)))
            .andExpect(status().isOk());

        // Validate the Skill in the database
        List<Skill> skillList = skillRepository.findAll();
        assertThat(skillList).hasSize(databaseSizeBeforeUpdate);
        Skill testSkill = skillList.get(skillList.size() - 1);
        assertThat(testSkill.getTitleUz()).isEqualTo(UPDATED_TITLE_UZ);
        assertThat(testSkill.getTitleRu()).isEqualTo(UPDATED_TITLE_RU);
        assertThat(testSkill.getTitleEn()).isEqualTo(UPDATED_TITLE_EN);
        assertThat(testSkill.getAbout()).isEqualTo(UPDATED_ABOUT);
        assertThat(testSkill.getPlanFile()).isEqualTo(UPDATED_PLAN_FILE);
        assertThat(testSkill.getPlanFileContentType()).isEqualTo(UPDATED_PLAN_FILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingSkill() throws Exception {
        int databaseSizeBeforeUpdate = skillRepository.findAll().size();

        // Create the Skill
        SkillDTO skillDTO = skillMapper.toDto(skill);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSkillMockMvc.perform(put("/api/skills")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(skillDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Skill in the database
        List<Skill> skillList = skillRepository.findAll();
        assertThat(skillList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSkill() throws Exception {
        // Initialize the database
        skillRepository.saveAndFlush(skill);

        int databaseSizeBeforeDelete = skillRepository.findAll().size();

        // Delete the skill
        restSkillMockMvc.perform(delete("/api/skills/{id}", skill.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Skill> skillList = skillRepository.findAll();
        assertThat(skillList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
