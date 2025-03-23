package br.com.gustavoz.model;

import java.util.Collection;
import java.util.List;

public class Board {

    private final List<List<Space>> spaceList;

    public Board(List<List<Space>> spaceList) {
        this.spaceList = spaceList;
    }

    public List<List<Space>> getSpaceList() {
        return spaceList;
    }

    public GameStatusEnum getGameStatus() {
        if (spaceList.stream().flatMap(Collection::stream).noneMatch(space -> !space.isFixed() && space.getActual() != null)) {
            return GameStatusEnum.NOT_STARTED;
        }
        return spaceList.stream().flatMap(Collection::stream).anyMatch(space -> space.getActual() == null) ?
                GameStatusEnum.INCOMPLETE : GameStatusEnum.COMPLETE;
    }

    public boolean hasErrors() {
        if (getGameStatus() == GameStatusEnum.NOT_STARTED) {
            return false;
        }
        return spaceList.stream().flatMap(Collection::stream)
                .anyMatch(space -> space.getActual() != null && !space.getActual().equals(space.getExpectedValue()));
    }

    public boolean changeValue(int row, int column, int value) {
        Space space = spaceList.get(row).get(column);
        if (space.isFixed()) {
            return false;
        }
        space.setActual(value);
        return true;
    }

    public boolean clearSpace(int row, int column) {
        Space space = spaceList.get(row).get(column);
        if (space.isFixed()) {
            return false;
        }
        space.clearSpace();
        return true;
    }

    public void reset() {
        spaceList.forEach(row -> row.forEach(Space::clearSpace));
    }

    public boolean gameIsComplete() {
        return hasErrors() && getGameStatus() == GameStatusEnum.COMPLETE;
    }
}
