package EntityPack;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class Product_Feedback {

    @Getter
    @Setter
    private String e_mail;
    @Getter
    @Setter
    private String title;
    @Getter
    @Setter
    private String class_name;
    @Getter
    @Setter
    private String message;
}
