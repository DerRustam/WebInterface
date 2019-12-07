package EntityPack;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class Digital_Copy {
   @Getter @Setter private String pay_code;
   @Getter @Setter private String product_key;
   @Getter @Setter private String title;
   @Getter @Setter private String class_name;
}
