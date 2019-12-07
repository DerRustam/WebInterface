package EntityPack;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class Purchase {

    @Setter
    @Getter
    private String pay_code;
    @Setter
    @Getter
    private String title;
    @Setter
    @Getter
    private String class_name;
    @Setter
    @Getter
    private short count;
    @Setter
    @Getter
    private String price_single;
}
