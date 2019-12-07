package EntityPack;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class Account {
   @Getter @Setter private String owner_name;
   @Getter @Setter private String date_of_birth;
   @Getter @Setter private char gender;
   @Getter @Setter private String e_mail;
   @Getter @Setter private String password;
}
