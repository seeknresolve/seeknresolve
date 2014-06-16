package pl.edu.pw.ii.pik01.seeknresolve.envers.fieldMapper;

public class StringMapper extends AbstractFieldMapper<String>{
    @Override
    protected String doGetDescription(String before, String after, String fieldName) {
        return getFormattedDesc(before, after, fieldName);
    }
}
