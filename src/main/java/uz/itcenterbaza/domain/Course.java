package uz.itcenterbaza.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import uz.itcenterbaza.domain.enumeration.CourseStatus;

/**
 * A Course.
 */
@Entity
@Table(name = "course")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Course implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "price")
    private Float price;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private CourseStatus status;

    @Column(name = "duration")
    private Integer duration;

    @Lob
    @Column(name = "plan_file")
    private byte[] planFile;

    @Column(name = "plan_file_content_type")
    private String planFileContentType;

    @OneToMany(mappedBy = "course")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Registered> registereds = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = "courses", allowSetters = true)
    private Teacher teacher;

    @ManyToOne
    @JsonIgnoreProperties(value = "courses", allowSetters = true)
    private Center center;

    @ManyToOne
    @JsonIgnoreProperties(value = "courses", allowSetters = true)
    private Skill skill;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Course title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Float getPrice() {
        return price;
    }

    public Course price(Float price) {
        this.price = price;
        return this;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public Course startDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public CourseStatus getStatus() {
        return status;
    }

    public Course status(CourseStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(CourseStatus status) {
        this.status = status;
    }

    public Integer getDuration() {
        return duration;
    }

    public Course duration(Integer duration) {
        this.duration = duration;
        return this;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public byte[] getPlanFile() {
        return planFile;
    }

    public Course planFile(byte[] planFile) {
        this.planFile = planFile;
        return this;
    }

    public void setPlanFile(byte[] planFile) {
        this.planFile = planFile;
    }

    public String getPlanFileContentType() {
        return planFileContentType;
    }

    public Course planFileContentType(String planFileContentType) {
        this.planFileContentType = planFileContentType;
        return this;
    }

    public void setPlanFileContentType(String planFileContentType) {
        this.planFileContentType = planFileContentType;
    }

    public Set<Registered> getRegistereds() {
        return registereds;
    }

    public Course registereds(Set<Registered> registereds) {
        this.registereds = registereds;
        return this;
    }

    public Course addRegistered(Registered registered) {
        this.registereds.add(registered);
        registered.setCourse(this);
        return this;
    }

    public Course removeRegistered(Registered registered) {
        this.registereds.remove(registered);
        registered.setCourse(null);
        return this;
    }

    public void setRegistereds(Set<Registered> registereds) {
        this.registereds = registereds;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public Course teacher(Teacher teacher) {
        this.teacher = teacher;
        return this;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Center getCenter() {
        return center;
    }

    public Course center(Center center) {
        this.center = center;
        return this;
    }

    public void setCenter(Center center) {
        this.center = center;
    }

    public Skill getSkill() {
        return skill;
    }

    public Course skill(Skill skill) {
        this.skill = skill;
        return this;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Course)) {
            return false;
        }
        return id != null && id.equals(((Course) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Course{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", price=" + getPrice() +
            ", startDate='" + getStartDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", duration=" + getDuration() +
            ", planFile='" + getPlanFile() + "'" +
            ", planFileContentType='" + getPlanFileContentType() + "'" +
            "}";
    }
}
