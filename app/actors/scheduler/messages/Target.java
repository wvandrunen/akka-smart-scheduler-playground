package actors.scheduler.messages;

public class Target {

    public final Long linkId;
    public final String structureId;
    public final ActionType actionType;

    public Target(Long linkId, String structureId, ActionType actionType) {
        this.linkId = linkId;
        this.structureId = structureId;
        this.actionType = actionType;
    }

    @Override
    public String toString() {
        return "linkId=" + linkId +
                "_structureId=" + structureId +
                "_actionType=" + actionType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Target target = (Target) o;

        if (!linkId.equals(target.linkId)) return false;
        if (!structureId.equals(target.structureId)) return false;
        return actionType == target.actionType;

    }

    @Override
    public int hashCode() {
        int result = linkId.hashCode();
        result = 31 * result + structureId.hashCode();
        result = 31 * result + actionType.hashCode();
        return result;
    }

}
