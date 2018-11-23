package App.model;

import java.time.LocalDateTime;

public abstract class EntityDTO {

    protected long __id;
    protected LocalDateTime __modified;

    public long getId() {
        return __id;
    }

    public LocalDateTime getModified() {
        return __modified;
    }
}
