package uz.itcenterbaza.service.dto;

import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import uz.itcenterbaza.domain.enumeration.EventType;

/**
 * A DTO for the {@link uz.itcenterbaza.domain.EventHistory} entity.
 */
public class EventHistoryDTO implements Serializable {
    
    private Long id;

    private EventType type;

    private String text;

    private ZonedDateTime time;


    private Long centerId;

    private Long userId;
    private Set<UserDTO> openedUsers = new HashSet<>();
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ZonedDateTime getTime() {
        return time;
    }

    public void setTime(ZonedDateTime time) {
        this.time = time;
    }

    public Long getCenterId() {
        return centerId;
    }

    public void setCenterId(Long centerId) {
        this.centerId = centerId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Set<UserDTO> getOpenedUsers() {
        return openedUsers;
    }

    public void setOpenedUsers(Set<UserDTO> users) {
        this.openedUsers = users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventHistoryDTO)) {
            return false;
        }

        return id != null && id.equals(((EventHistoryDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventHistoryDTO{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", text='" + getText() + "'" +
            ", time='" + getTime() + "'" +
            ", centerId=" + getCenterId() +
            ", userId=" + getUserId() +
            ", openedUsers='" + getOpenedUsers() + "'" +
            "}";
    }
}
