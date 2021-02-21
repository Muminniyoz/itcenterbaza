package uz.itcenterbaza.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * A Center.
 */
@Entity
@Table(name = "center")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Center implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "info")
    private String info;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "google_map_url")
    private String googleMapUrl;

    @ManyToOne
    @JsonIgnoreProperties(value = "centers", allowSetters = true)
    private User modifiedBy;

    @ManyToOne
    @JsonIgnoreProperties(value = "centers", allowSetters = true)
    private Regions regions;

    @ManyToOne
    @JsonIgnoreProperties(value = "centers", allowSetters = true)
    private User manager;

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

    public Center title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInfo() {
        return info;
    }

    public Center info(String info) {
        this.info = info;
        return this;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public Center startDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public String getGoogleMapUrl() {
        return googleMapUrl;
    }

    public Center googleMapUrl(String googleMapUrl) {
        this.googleMapUrl = googleMapUrl;
        return this;
    }

    public void setGoogleMapUrl(String googleMapUrl) {
        this.googleMapUrl = googleMapUrl;
    }

    public User getModifiedBy() {
        return modifiedBy;
    }

    public Center modifiedBy(User user) {
        this.modifiedBy = user;
        return this;
    }

    public void setModifiedBy(User user) {
        this.modifiedBy = user;
    }

    public Regions getRegions() {
        return regions;
    }

    public Center regions(Regions regions) {
        this.regions = regions;
        return this;
    }

    public void setRegions(Regions regions) {
        this.regions = regions;
    }

    public User getManager() {
        return manager;
    }

    public Center manager(User user) {
        this.manager = user;
        return this;
    }

    public void setManager(User user) {
        this.manager = user;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Center)) {
            return false;
        }
        return id != null && id.equals(((Center) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Center{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", info='" + getInfo() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", googleMapUrl='" + getGoogleMapUrl() + "'" +
            "}";
    }
}
