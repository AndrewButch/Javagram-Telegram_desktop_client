package FakeData;

import org.javagram.response.object.UserContact;
import org.telegram.api.TLUserContact;

public class FakeUserContact extends UserContact {
    public FakeUserContact(int id) {
        super(new TLUserContact(id, "User " + id, " Fake",
                1234, "+791112343" + id,
                null, null));
    }
}
