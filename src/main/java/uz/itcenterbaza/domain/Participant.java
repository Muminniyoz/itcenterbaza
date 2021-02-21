package uz.itcenterbaza.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;

import uz.itcenterbaza.domain.enumeration.ParticipantStatus;

/**
 * A Participant.
 */
@Entity
@Table(name = "participant")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Participant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "starting_date")
    private LocalDate startingDate;

    @Column(name = "active")
    private Boolean active;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ParticipantStatus status;

    @Column(name = "contract_number")
    private String contractNumber;

    @ManyToOne
    @JsonIgnoreProperties(value = "participants", allowSetters = true)
    private Student student;

    @ManyToOne
    @JsonIgnoreProperties(value = "participants", allowSetters = true)
    private Course course;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStartingDate() {
        return startingDate;
    }

    public Participant startingDate(LocalDate startingDate) {
        this.startingDate = startingDate;
        return this;
    }

    public void setStartingDate(LocalDate startingDate) {
        this.startingDate = startingDate;
    }

    public Boolean isActive() {
        return active;
    }

    public Participant active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public ParticipantStatus getStatus() {
        return status;
    }

    public Participant status(ParticipantStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(ParticipantStatus status) {
        this.status = status;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public Participant contractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
        return this;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public Student getStudent() {
        return student;
    }

    public Participant student(Student student) {
        this.student = student;
        return this;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public Participant course(Course course) {
        this.course = course;
        return this;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Participant)) {
            return false;
        }
        return id != null && id.equals(((Participant) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Participant{" +
            "id=" + getId() +
            ", startingDate='" + getStartingDate() + "'" +
            ", active='" + isActive() + "'" +
            ", status='" + getStatus() + "'" +
            ", contractNumber='" + getContractNumber() + "'" +
            "}";
    }
}
