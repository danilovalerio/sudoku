package model;

import java.util.Collection;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class Board {

    private final List<List<Space>> spaces;

    public Board(List<List<Space>> spaces) {
        this.spaces = spaces;
    }

    public List<List<Space>> getSpaces() {
        return spaces;
    }

    public GameStatusEnum getStatus() {
        //noneMatch verifica se não tem nenhum item na lista
        if (spaces.stream().flatMap(Collection::stream).noneMatch(space -> !space.isFixed() && nonNull(space.getActual()))) {
            return GameStatusEnum.NO_STARTED;
        }
        //anyMatch verifica se tem algum item na lista
        return spaces.stream().flatMap(Collection::stream).anyMatch(space -> isNull(space.getActual())) ? GameStatusEnum.INCOMPLETE : GameStatusEnum.COMPLETED;
    }

    public boolean hasErrors() {
        if (getStatus() == GameStatusEnum.NO_STARTED){
            return false;
        }
        //flatMap para verificar a lista interna
        return spaces.stream().flatMap(Collection::stream)
                .anyMatch(space -> nonNull(space.getActual()) && !space.getActual().equals(space.getExpected()));
    }

    public boolean changeValue(final int coluna, final int linha, final Integer value){
        var space = spaces.get(coluna).get(linha);
        if (space.isFixed()) return false;
        space.setActual(value);
        return true;
    }

    public boolean clearValue(final int coluna, final int linha){
        var space = spaces.get(coluna).get(linha);
        if (space.isFixed()) return false;
        space.clearSpace();
        return true;
    }

    public void reset() {
        spaces.forEach(coluna -> coluna.forEach(Space::clearSpace));
    }
}
