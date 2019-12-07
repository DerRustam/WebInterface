package EntityPack;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class Order {
    @Getter @Setter private String e_mail;
    @Getter @Setter private String order_datetime;
    @Getter @Setter private String summary_price;
    @Getter @Setter private String pay_code;
}
