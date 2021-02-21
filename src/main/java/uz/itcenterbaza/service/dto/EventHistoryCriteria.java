package uz.itcenterbaza.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import uz.itcenterbaza.domain.enumeration.EventType;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link uz.itcenterbaza.domain.EventHistory} entity. This class is used
 * in {@link uz.itcenterbaza.web.rest.EventHistoryResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /event-histories?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class EventHistoryCriteria implements Serializable, Criteria {
    /**
     * Class for filtering EventType
     */
    public static class EventTypeFilter extends Filter<EventType> {

        public EventTypeFilter() {
        }

        public EventTypeFilter(EventTypeFilter filter) {
            super(filter);
        }

        @Override
        public EventTypeFilter copy() {
            return new EventTypeFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private EventTypeFilter type;

    private StringFilter text;

    private ZonedDateTimeFilter time;

    private LongFilter centerId;

    private LongFilter userId;

    private LongFilter openedUserId;

    public EventHistoryCriteria() {
    }

    public EventHistoryCriteria(EventHistoryCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.text = other.text == null ? null : other.text.copy();
        this.time = other.time == null ? null : other.time.copy();
        this.centerId = other.centerId == null ? null : other.centerId.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.openedUserId = other.openedUserId == null ? null : other.openedUserId.copy();
    }

    @Override
    public EventHistoryCriteria copy() {
        return new EventHistoryCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public EventTypeFilter getType() {
        return type;
    }

    public void setType(EventTypeFilter type) {
        this.type = type;
    }

    public StringFilter getText() {
        return text;
    }

    public void setText(StringFilter text) {
        this.text = text;
    }

    public ZonedDateTimeFilter getTime() {
        return time;
    }

    public void setTime(ZonedDateTimeFilter time) {
        this.time = time;
    }

    public LongFilter getCenterId() {
        return centerId;
    }

    public void setCenterId(LongFilter centerId) {
        this.centerId = centerId;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getOpenedUserId() {
        return openedUserId;
    }

    public void setOpenedUserId(LongFilter openedUserId) {
        this.openedUserId = openedUserId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final EventHistoryCriteria that = (EventHistoryCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(type, that.type) &&
            Objects.equals(text, that.text) &&
            Objects.equals(time, that.time) &&
            Objects.equals(centerId, that.centerId) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(openedUserId, that.openedUserId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        type,
        text,
        time,
        centerId,
        userId,
        openedUserId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventHistoryCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (type != null ? "type=" + type + ", " : "") +
                (text != null ? "text=" + text + ", " : "") +
                (time != null ? "time=" + time + ", " : "") +
                (centerId != null ? "centerId=" + centerId + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
                (openedUserId != null ? "openedUserId=" + openedUserId + ", " : "") +
            "}";
    }

}
