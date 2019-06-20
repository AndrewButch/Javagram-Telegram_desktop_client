package Model.FakeData;

import org.javagram.response.object.UserContact;
import org.telegram.api.TLAbsUserProfilePhoto;
import org.telegram.api.TLAbsUserStatus;
import org.telegram.api.TLUserSelf;

public class FakeSelfUser extends UserContact {
    public FakeSelfUser(int id) {
        super(new TLUserSelf(id, "Андрей", "Бутц", "+79533749628", null, null, false));
    }

    public FakeSelfUser(int id, String firsName, String lastName, String phone, TLAbsUserProfilePhoto photo, TLAbsUserStatus status, boolean inactive) {
        super(new TLUserSelf(id, firsName, lastName, phone, photo, status, inactive));
    }
}
