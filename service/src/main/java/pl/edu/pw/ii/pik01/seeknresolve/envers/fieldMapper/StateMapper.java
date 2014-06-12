package pl.edu.pw.ii.pik01.seeknresolve.envers.fieldMapper;

import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.Bug;

public class StateMapper extends AbstractFieldMapper<Bug.State>{
    @Override
    protected String doGetDescription(Bug.State before, Bug.State after, String fieldName) {
        return getFormattedDesc(before.toString(), after.toString(), fieldName);
    }
}
