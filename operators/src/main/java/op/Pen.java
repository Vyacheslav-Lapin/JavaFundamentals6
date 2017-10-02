package op;

import java.util.Objects;

public class Pen {

    private int price;
    private String producerName = "";
    //...

    @Override
    public boolean equals(Object obj) {
        Pen pen;
        return this == obj
                || null != obj
                && getClass() == obj.getClass()
                && price == (pen = (Pen) obj).price
                && producerName.equals(pen.producerName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price, producerName);
    }
}
