package EntityPack;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class Software {

    @Getter
    @Setter
    private String class_name;
    @Getter
    @Setter
    private String publisher_name;
    @Getter
    @Setter
    private String title;
    @Getter
    @Setter
    private String release_date;
    @Getter
    @Setter
    private char esrb;
    @Getter
    @Setter
    private String actual_price;
}
