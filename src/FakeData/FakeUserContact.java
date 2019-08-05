package FakeData;

import org.javagram.response.object.UserContact;
import org.telegram.api.TLUserContact;

public class FakeUserContact extends UserContact {
    public FakeUserContact(int id, String phone) {
        super(new TLUserContact(id, "User " + id, "Fake",
                1234, phone,
                null, null));
    }

}
