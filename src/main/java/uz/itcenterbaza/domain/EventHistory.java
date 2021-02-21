package uz.itcenterbaza.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

import uz.itcenterbaza.domain.enumeration.EventType;

/**
 * A EventHistory.
 */
@Entity
@Table(name = "event_history")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class EventHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private EventType type;

    @Column(name = "text")
    private String text;

    @Column(name = "time")
    private ZonedDateTime time;

    @ManyToOne
    @JsonIgnoreProperties(value = "eventHistories", allowSetters = true)
    private Center center;

    @ManyToOne
    @JsonIgnoreProperties(value = "eventHistories", allowSetters = true)
    private User user;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(name = "event_history_opened_user",
               joinColumns = @JoinColumn(name = "event_history_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "opened_user_id", referencedColumnName = "id"))
    private Set<User> openedUsers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EventType getType() {
        return type;
    }

    public EventHistory type(EventType type) {
        this.type = type;
        return this;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public EventHistory text(String text) {
        this.text = text;
        return this;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ZonedDateTime getTime() {
        return time;
    }

    public EventHistory time(ZonedDateTime time) {
        this.time = time;
        return this;
    }

    public void setTime(ZonedDateTime time) {
        this.time = time;
    }

    public Center getCenter() {
        return center;
    }

    public EventHistory center(Center center) {
        this.center = center;
        return this;
    }

    public void setCenter(Center center) {
        this.center = center;
    }

    public User getUser() {
        return user;
    }

    public EventHistory user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<User> getOpenedUsers() {
        return openedUsers;
    }

    public EventHistory openedUsers(Set<User> users) {
        this.openedUsers = users;
        return this;
    }

    public EventHistory addOpenedUser(User user) {
        this.openedUsers.add(user);
        return this;
    }

    public EventHistory removeOpenedUser(User user) {
        this.openedUsers.remove(user);
        return this;
    }

    public void setOpenedUsers(Set<User> users) {
        this.openedUsers = users;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventHistory)) {
            return false;
        }
        return id != null && id.equals(((EventHistory) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventHistory{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", text='" + getText() + "'" +
            ", time='" + getTime() + "'" +
            "}";
    }
}
