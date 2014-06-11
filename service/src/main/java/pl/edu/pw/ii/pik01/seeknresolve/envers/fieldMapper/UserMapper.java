package pl.edu.pw.ii.pik01.seeknresolve.envers.fieldMapper;

import pl.edu.pw.ii.pik01.seeknresolve.domain.entity.User;

public class UserMapper extends AbstractFieldMapper<User> implements FieldMapper<User> {

    @Override
    public String doGetDescription(User before, User after, String fieldName) {
        return getFormattedDesc(userToDesc(before), userToDesc(after), fieldName);
    }

    private String userToDesc(User user) {
        return user == null ? ""
                :new StringBuilder(user.getFirstName())
                .append(" ")
                .append(user.getLastName())
                .append(" (")
                .append(user.getLogin())
                .append(" )")
                .toString();
    }
}
