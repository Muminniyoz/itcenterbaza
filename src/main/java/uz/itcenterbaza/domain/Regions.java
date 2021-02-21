package uz.itcenterbaza.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * Entities START
 */
@Entity
@Table(name = "regions")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Regions implements Serializable {

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

    @Column(name = "google_url")
    private String googleUrl;

    @ManyToOne
    @JsonIgnoreProperties(value = "regions", allowSetters = true)
    private User director;

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

    public Regions title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInfo() {
        return info;
    }

    public Regions info(String info) {
        this.info = info;
        return this;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getGoogleUrl() {
        return googleUrl;
    }

    public Regions googleUrl(String googleUrl) {
        this.googleUrl = googleUrl;
        return this;
    }

    public void setGoogleUrl(String googleUrl) {
        this.googleUrl = googleUrl;
    }

    public User getDirector() {
        return director;
    }

    public Regions director(User user) {
        this.director = user;
        return this;
    }

    public void setDirector(User user) {
        this.director = user;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Regions)) {
            return false;
        }
        return id != null && id.equals(((Regions) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Regions{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", info='" + getInfo() + "'" +
            ", googleUrl='" + getGoogleUrl() + "'" +
            "}";
    }
}
